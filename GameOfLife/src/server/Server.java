package server;


import backend.Cell;
import backend.Grid;
import backend.IterationCalculator;
import backend.QuadTreeElement;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

public class Server implements Runnable {
	public static final int port = 44445;

	private ServerSocket serverSocket = null;
	private volatile ArrayList<Connection> clients = new ArrayList<Connection>();
	boolean listening = true;
	
	/**Barrier to have merging thread await until all calculating clients have sent back a partialComponent*/
	volatile CyclicBarrier barrier;

	private Grid game;
	private Thread playThread = new Thread();
	
	//Note: the next two are separated because it more easily separates a Connection from its list,
	//making it easier to reassign a component to another Connection if a Connection drops before returning
	//its assignment
	/**A mapping between a connection and what component it is currently calculating*/
	private volatile HashMap<Connection, Integer> connectionCalculating = new HashMap<Connection, Integer>();
	/**A mapping between a component of the Grid to be calculated and the nextIteration*/
	private volatile HashMap<Integer, AtomicReference[]> partialComponents = new HashMap<Integer, AtomicReference[]>();
	
    public Server(int numRows) throws Exception {
        game = new Grid(numRows);
        System.out.println(game.getNumRows());
    }

    @Override
	public void run() {
		try {
			InetAddress ip = InetAddress.getLocalHost();
			System.out.println("Server IP address: " + ip.getHostAddress());
		}
		catch(UnknownHostException e) {
			e.printStackTrace();
		}

		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Listening on port " + port);
		}
		catch(IOException e) {
			System.err.println("Could not listen on port: " + port + ".");
			System.exit(-1);
		}

		try {
			while(!Thread.currentThread().isInterrupted()) {
				Connection c = new Connection(serverSocket.accept());
				synchronized(clients) {
					clients.add(c);
				}
				c.start();
				c.send(game);
				System.out.println("Client Connected");
			}
		}
		catch(IOException e) {
			//e.printStackTrace();
		}

		try {
			serverSocket.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void play() {
		final Runnable r = new Runnable() {
			public void resendRemainingPartialComponents() {
				ArrayList<Connection> clientCopy;
				synchronized(clients) {
					clientCopy = new ArrayList<Connection>(Collections.unmodifiableCollection(clients));
				}
				
				HashMap<Connection, Integer> newConnectionCalculating = new HashMap<Connection, Integer>();
				int i = 0;
				synchronized(connectionCalculating) {
					for(Connection c : connectionCalculating.keySet()) {
						newConnectionCalculating.put(clientCopy.get(i), connectionCalculating.get(c));
						i++;
					}
					connectionCalculating = newConnectionCalculating;
					for(Connection c : connectionCalculating.keySet()) {
						c.send(connectionCalculating.get(c));
					}
					System.out.println(game.getNumRows());
					barrier = new CyclicBarrier(connectionCalculating.size() + 1);
				}
			}
			
			public void run() {
				try {
					while(!Thread.interrupted()) {
						ArrayList<Connection> clientCopy;
						synchronized(clients) {
							clientCopy = new ArrayList<Connection>(Collections.unmodifiableCollection(clients));
						}
						IterationCalculator ic = null;
						try {
							ic = new IterationCalculator(game);
							List<AtomicReference[]> cellList = ic.findSubSetsOfCellsForThread(clientCopy.size());
							synchronized(connectionCalculating) {
								for(int i = 0;i<clientCopy.size();i++) {
									connectionCalculating.put(clientCopy.get(i), new Integer(i));
									partialComponents.put(new Integer(i), cellList.get(i));
								}
							}
						}
						catch(Exception e) {
							//TODO: maybe?
						}
						
						synchronized(connectionCalculating) {
							barrier = new CyclicBarrier(connectionCalculating.size() + 1);

							for(Connection c : connectionCalculating.keySet()) {
								c.send(partialComponents.get(connectionCalculating.get(c)));
							}

							barrier.await();

							while(!connectionCalculating.isEmpty()) {
								resendRemainingPartialComponents();
							}
						}
						System.out.println("After barrier.await()");
						mergeData();
						sendGameToAll();
						Thread.sleep(1000);//TODO: use appropriate timeout if necessary 
					}
				}
				catch(InterruptedException e) {
					//handle why we exited
					//if PAUSE, do nothing;
				}
				catch(BrokenBarrierException e) {
					e.printStackTrace();
				}
			}
		};
		playThread = new Thread(r);
		playThread.setDaemon(true);
		playThread.start();
	}
	
	public void pause() {
		playThread.interrupt();
	}
	
	public void clear() throws Exception {
		game = new Grid(game.getNumRows());
	}
	
	public void mergeData() {
		//TODO
		partialComponents.clear();
	}
	
	public void sendGameToAll() {
		sendGameToAll(null);
	}
	private void sendGameToAll(Connection exception) {
		ArrayList<Connection> clientCopy;
		synchronized(clients) {
			clientCopy = new ArrayList<Connection>(Collections.unmodifiableCollection(clients));
		}
		for(Connection c : clientCopy) {
			if(c != exception) {
				c.send(game);
			}
		}
	}
	
	public void sendCellToAll(Cell cell) {
		sendCellToAll(cell, null);
	}
	private void sendCellToAll(Cell cell, Connection exception) {
		ArrayList<Connection> clientCopy;
		synchronized(clients) {
			clientCopy = new ArrayList<Connection>(Collections.unmodifiableCollection(clients));
		}
		for(Connection c : clientCopy) {
			if(c != exception) {
				c.send(cell);
			}
		}
	}

	class Connection extends Thread {
		private Socket s = null;
		private ObjectOutputStream oos = null;
		private ObjectInputStream ois = null;

		public Connection(Socket s) {
			this.s = s;
			try {
				oos = new ObjectOutputStream(s.getOutputStream());
				ois = new ObjectInputStream(s.getInputStream());
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		public void send(Object o) {
			try {
				oos.writeObject(o);
				oos.reset();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				while(true) {
					Object o = ois.readObject();
					System.out.println("Received Back a Part");
					System.out.println("Barrier: " + barrier);
					if(o instanceof NetworkMessage) {
						NetworkMessage nm = (NetworkMessage) o;
						switch(nm) {
							case PLAY:
								System.out.println("Received PLAY");
								play();
								break;
							case PAUSE:
								System.out.println("Received PAUSE");
								pause();
								break;
							case CLEAR:
								System.out.println("Received CLEAR");
								pause();
								clear();
								break;
						}
					}
					else if(o instanceof Cell) {
						if(!playThread.isAlive()) {
							//pause();
							Cell c = (Cell) o;
							System.out.println("Received Cell " + c + " " + ((c.getCellState() == 1) ? "alive":"dead"));
							game.getCell(c.y, c.x).setCellState(c.getCellState());
	
							sendCellToAll(c, this);
						}
					}
					else if(o instanceof Grid) {
						if(!playThread.isAlive()) {
							//pause();
							game = (Grid) o;
							sendGameToAll(this);
						}
					}
					else if(o instanceof AtomicReference[]) {
						pause();
						AtomicReference[] partialComponent = (AtomicReference[]) o;//TODO: reflection?
						synchronized(connectionCalculating) {
							Integer item = connectionCalculating.get(this);
							partialComponents.put(item, partialComponent);
						}
						passBarrier();
					}
					
					
				}
			}
			catch(ClassNotFoundException e) {
				//e.printStackTrace();
			}
			catch(IOException e) {
				//e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();//TODO: comment this out
			} finally {
				synchronized(clients) {
					clients.remove(this);
				}
				if(ois != null) {
					try {
						ois.close();
					}
					catch(IOException e) {
						e.printStackTrace();
					}
				}
				if(oos != null) {
					try {
						oos.close();
					}
					catch(IOException e) {
						e.printStackTrace();
					}
				}
				if(s != null) {
					try {
						s.close();
					}
					catch(IOException e) {
						e.printStackTrace();
					}
				}
				//if the current thread should await on the barrier, pass it
				synchronized(connectionCalculating) {
					if(connectionCalculating.containsKey(this)) {
						passBarrier();
					}
				}
			}
		}
		
		private void passBarrier() {
			if(barrier != null) {
				try {
					barrier.await(0, TimeUnit.MILLISECONDS);
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
				catch(BrokenBarrierException e) {
					//thrown when any awaiting thread is interrupted
					//OR
					//the barrier is reset
				}
				catch(TimeoutException e) {/*DON'T DO ANYTHING: this is expected*/}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		new Thread(new Server(10)).start();
	}
}
