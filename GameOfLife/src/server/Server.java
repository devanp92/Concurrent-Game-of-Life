package server;


import backend.Cell;
import backend.DistributiveIterationCalculator;
import backend.Grid;

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
	private ArrayList<Connection> clients = new ArrayList<Connection>();
	
	/**Barrier to have merging thread await until all calculating clients have sent back a partialComponent*/
	volatile CyclicBarrier barrier;

	private volatile Grid g;
	private Thread playThread = new Thread();
	
	//Note: the next two are separated because it more easily separates a Connection from its list,
	//making it easier to reassign a component to another Connection if a Connection drops before returning
	//its assignment
	/**A mapping between a connection and what component it is currently calculating*/
	private HashMap<Connection, Integer> connectionCalculating = new HashMap<Connection, Integer>();
	///**A mapping between a component of the Grid to be calculated and the calculator thread for the nextIteration*/
	//private HashMap<Integer, Thread> calculatorThreads = new HashMap<Integer, Thread>();
	/**A mapping between a component of the Grid and the partial components*/
	private HashMap<Integer, AtomicReference[]> partialComponents = new HashMap<Integer, AtomicReference[]>();
	private HashMap<Integer, ArrayList<Cell>> completePartialComponents = new HashMap<Integer, ArrayList<Cell>>();
	
    public Server(int numRows) throws Exception {
        g = new Grid(numRows);
        System.out.println(g.getNumRows());
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
				c.send(g);
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
    
	
	private void play() {
		final Runnable r = new Runnable() {
			/**run()*/
			public void run() {
				try {
					while(!Thread.interrupted()) {
						System.out.println("Calculating New Iteration");
						ArrayList<Connection> clientCopy;
						synchronized(clients) {
							clientCopy = new ArrayList<Connection>(Collections.unmodifiableCollection(clients));
						}
						DistributiveIterationCalculator dic = null;
						try {
							dic = new DistributiveIterationCalculator(g);
							List<AtomicReference[]> components = dic.findSubSetsOfCellsForClients(clientCopy.size());
							
							synchronized(connectionCalculating) {
								for(int i = 0;i<clientCopy.size();i++) {
									connectionCalculating.put(clientCopy.get(i), new Integer(i));
									partialComponents.put(new Integer(i), components.get(i));
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
						}
						System.out.println("WAITING ON BARRIER");		
						barrier.await();

						boolean isEmpty;
						synchronized(connectionCalculating) {
							isEmpty = connectionCalculating.isEmpty();
						}
						while(!isEmpty) {
							resendRemainingPartialComponents();
							synchronized(connectionCalculating) {
								isEmpty = connectionCalculating.isEmpty();
							}
						}
						
						System.out.println("After barrier.await()");
						mergeData();
						System.out.println("DONE Calculating New Iteration: sending merge to all Clients");
						sendGameToAll();
						Thread.sleep(1000);//TODO: use appropriate timeout if necessary, make this editable in the GUI 
					}
				}
				catch(InterruptedException e) {
					//interrupted via PAUSE, do nothing
				}
				catch(BrokenBarrierException e) {
					e.printStackTrace();
				}
			}
			
			/**resendRemainingPartialComponents()*/
			private void resendRemainingPartialComponents() throws InterruptedException, BrokenBarrierException {
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
					
					//initialize barrier before resending components
					barrier = new CyclicBarrier(connectionCalculating.size() + 1);
					for(Connection c : connectionCalculating.keySet()) {
						c.send(partialComponents.get(connectionCalculating.get(c)));
					}
					barrier.await();
				}
			}
		};
		playThread = new Thread(r);
		playThread.setDaemon(true);
		playThread.start();
	}
	
	private void pause() {
		playThread.interrupt();
	}
	
	private void clear() throws Exception {
		g = new Grid(g.getNumRows());
	}
	
	private void mergeData() {
		Grid tempGrid = null;
		try {
			tempGrid = new Grid(g.getNumRows());
		}
		catch(Exception e) {
			e.printStackTrace();//TODO: remove
		}
		
		Collection<ArrayList<Cell>> components = null;
		//Note: connectionCalculating acts as a lock on itself and partialComponents
		synchronized(connectionCalculating) {
			components = Collections.unmodifiableCollection(completePartialComponents.values());
			partialComponents.clear();
		}
		for(ArrayList<Cell> cells : components) {
			for(Cell c : cells) {
				tempGrid.setCell(c);
			}
		}
		g = tempGrid;
	}
	
	private void sendGameToAll() {
		sendGameToAll(null);
	}
	private void sendGameToAll(Connection exception) {
		ArrayList<Connection> clientCopy;
		synchronized(clients) {
			clientCopy = new ArrayList<Connection>(Collections.unmodifiableCollection(clients));
		}
		for(Connection c : clientCopy) {
			if(c != exception) {
				System.out.println(c);
				c.send(g);
			}
		}
	}
	
	private void sendCellToAll(Cell cell) {
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
					Object rcvObj = ois.readObject();
					if(rcvObj instanceof NetworkMessage) {
						NetworkMessage nm = (NetworkMessage) rcvObj;
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
					else if(rcvObj instanceof Cell) {
						if(!playThread.isAlive()) {
							//pause();
							Cell c = (Cell) rcvObj;
							System.out.println("Received Cell " + c + " " + ((c.getCellState() == 1) ? "alive":"dead"));
							g.getCell(c.y, c.x).setCellState(c.getCellState());
	
							sendCellToAll(c, this);
						}
					}
					else if(rcvObj instanceof Grid) {
						if(!playThread.isAlive()) {
							//pause();
							g = (Grid) rcvObj;
							System.out.println("Received Grid: " + g.getNumRows());
							sendGameToAll(this);
						}
					}
					else if(rcvObj instanceof ArrayList<?>) {
						if(playThread.isAlive()) {
							System.out.println("Received Completed PartialComponent");
							ArrayList<Cell> partialComponent = (ArrayList<Cell>) rcvObj;//TODO: reflection?
							synchronized(connectionCalculating) {
								Integer item = connectionCalculating.get(this);
								completePartialComponents.put(item, partialComponent);
								connectionCalculating.remove(this);
							}
							passBarrier();
						}
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
				//if the current thread should await on the barrier, pass it to not create deadlock
				synchronized(connectionCalculating) {
					if(connectionCalculating.containsKey(this)) {
						passBarrier();
						//Don't remove this from connectionCalculating
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
