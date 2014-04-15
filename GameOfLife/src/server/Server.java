package server;


import backend.Cell;
import backend.Grid;
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
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Server implements Runnable {
	public static final int port = 44445;

	private ServerSocket serverSocket = null;
	private volatile ArrayList<Connection> clients = new ArrayList<Connection>();
	boolean listening = true;
	
	/**Barrier to have merging thread await until all calculating clients have sent back a partialComponent*/
	volatile CyclicBarrier barrier;

	private Grid game;
	private volatile boolean isPlaying = false;
	private Thread playThread = new Thread();
	
	/**A mapping between a connection and what component it is currently calculating*/
	private volatile HashMap<Connection, Integer> connectionCalculating = new HashMap<Connection, Integer>();
	
	/**A mapping between a component of the Grid to be calculated and the nextIteration*/
	private volatile HashMap<Integer, ArrayList<Cell>> partialComponents = new HashMap<Integer, ArrayList<Cell>>();
	
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

		while(listening) {
			try {
				Connection c = new Connection(serverSocket.accept());
				clients.add(c);
				c.start();
				c.send(game);
				System.out.println("Client Connected");
			}
			catch(IOException e) {
				e.printStackTrace();
			}
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
			
			public void run() {
				isPlaying = true;
				try {
					while(!Thread.interrupted()) {
						Collection<Connection> clientCopy;
						synchronized(clients) {
							clientCopy = Collections.unmodifiableCollection(clients);
						}
						//TODO: initialize partialComponents
						//TODO: initialize connectionCalculating
						barrier = new CyclicBarrier(connectionCalculating.size() + 1);
						
						for(Connection c : connectionCalculating.keySet()) {
							//c.send(partialComponent)
						}
						
						barrier.await();

						while(!connectionCalculating.isEmpty()) {
							resendRemainingPartialComponents();
						}
						System.out.println("After barrier.await(): " + barrier);
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
				isPlaying = false;
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
		for(Connection c : clients) {
			c.send(game);
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
						if(!isPlaying) {
							//pause();
							Cell c = (Cell) o;
							System.out.println("Received Cell " + c);
							game.getCell(c.y, c.x).setCellState(c.getCellState());
	
							/*if(c.isAlive == 1) {
									game.insert(c);
								}
								else if(c.isAlive == 0) {
									game.remove(c);
								}*/
							sendGameToAll();
						}
					}
					else if(o instanceof Grid) {
						if(!isPlaying) {
							//pause();
							game = (Grid) o;
							sendGameToAll();
						}
					}
					else if(o instanceof ArrayList<?>) {
						pause();
						ArrayList<Cell> partialComponent = (ArrayList<Cell>) o;//TODO: reflection again?
						Integer item = connectionCalculating.get(this);
						partialComponents.put(item, partialComponent);
						passBarrier();
					}
					else if(o instanceof QuadTreeElement) {//TODO: move this to instanceof Grid
						passBarrier();
						//QuadTree qTree = (QuadTree) o;
							//partialComponents.add(qTree);
					}
				}
			}
			catch(ClassNotFoundException e) {
				//e.printStackTrace();
			}
			catch(IOException e) {
				//e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
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
				clients.remove(this);
				//if the current thread should await on the barrier, pass it
				if(connectionCalculating.containsKey(this)) {
					passBarrier();
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
