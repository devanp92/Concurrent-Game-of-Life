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
import java.util.concurrent.atomic.AtomicReference;

public class Server implements Runnable {
	public static final int port = 44445;

	private ServerSocket serverSocket = null;
	private ArrayList<Connection> clients = new ArrayList<Connection>();
	
	/**Barrier to have merging thread await until all calculating clients have sent back a partialComponent*/
	//volatile CyclicBarrier barrier;
	
	Object barrierLock = new Object();
	/**An integer representing the number of responded clients
	 * A client responds when it sends back partialComponent or it closes
	 * Note: responded does not mean they have sent back their expected partialComponent
	 */
	int numOfRespondedClients;

	private volatile Grid g;
	private Thread playThread = new Thread();
	private volatile IterationDelayPeriod iterationDelay = new IterationDelayPeriod(0);
	
	//Note: the next two are separated because it more easily separates a Connection from its list,
	//making it easier to reassign a component to another Connection if a Connection drops before returning
	//its assignment
	//Uses of any of these should be GuardedBy synchronizing on connectionCalculating
	/**A mapping between a connection and what component it is currently calculating*/
	private HashMap<Connection, Integer> connectionCalculating = new HashMap<Connection, Integer>();
	/**A mapping between a component of the Grid and the partial component to send*/
	private HashMap<Integer, AtomicReference[]> partialComponents = new HashMap<Integer, AtomicReference[]>();
	/**A mapping between a component of the Grid and the completed partial component*/
	private HashMap<Integer, ArrayList<Cell>> completePartialComponents = new HashMap<Integer, ArrayList<Cell>>();
	
    public Server(int numRows) throws Exception {
        g = new Grid(numRows);
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
			//System.exit(-1);
		}

		if(serverSocket != null) {
			try {
				while(!Thread.currentThread().isInterrupted()) {
					Connection c = new Connection(serverSocket.accept());
					synchronized(clients) {
						clients.add(c);
					}
					c.start();
					c.send((playThread.isAlive()) ? NetworkMessage.PLAY:NetworkMessage.PAUSE);
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
	}
    
	
	private void play() {
		final Runnable r = new Runnable() {
			/**run()*/
			public void run() {
				try {
					boolean gridChanged = true;
					while(!Thread.interrupted() && gridChanged) {
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
						
						g.printGrid();
						long start = System.currentTimeMillis();
						synchronized(connectionCalculating) {
							for(Connection c : connectionCalculating.keySet()) {
								c.send(partialComponents.get(connectionCalculating.get(c)));
							}
						}
						
						System.out.println("WAITING ON BARRIER");
						synchronized(barrierLock) {
							while(numOfRespondedClients != clientCopy.size()) barrierLock.wait();
							numOfRespondedClients = 0;
						}

						boolean isEmpty;
						int clientCount;
						synchronized(connectionCalculating) {
							isEmpty = connectionCalculating.isEmpty();
						}
						synchronized(clients) {
							clientCount = clients.size();
						}
						while(!isEmpty && clientCount > 0) {
							resendRemainingPartialComponents();
							synchronized(connectionCalculating) {
								isEmpty = connectionCalculating.isEmpty();
							}
							synchronized(clients) {
								clientCount = clients.size();
							}
						}
						
						System.out.println("After barrier WAIT");
						gridChanged = !mergeData();
						System.out.println("DONE Calculating New Iteration: sending merge to all Clients");
						g.printGrid();
						long stop = System.currentTimeMillis();
						Thread.sleep(Math.max(iterationDelay.getDelayVal() - (stop-start), 0));
						sendGameToAll();
					}
				}
				catch(InterruptedException e) {
					//interrupted via PAUSE, do nothing
				}
				finally {
					synchronized(clients) {
						for(Connection c : clients) {
							c.send(NetworkMessage.CALCULATION_COMPLETE);
						}
					}
					sendGameToAll();
				}
			}
			
			/**resendRemainingPartialComponents()*/
			private void resendRemainingPartialComponents() throws InterruptedException {
				ArrayList<Connection> clientCopy;
				synchronized(clients) {
					clientCopy = new ArrayList<Connection>(Collections.unmodifiableCollection(clients));
				}
				if(clientCopy.size() == 0) return;
				HashMap<Connection, Integer> newConnectionCalculating = new HashMap<Connection, Integer>();
				int i = 0;
				synchronized(connectionCalculating) {
					for(Connection c : connectionCalculating.keySet()) {
						newConnectionCalculating.put(clientCopy.get(i), connectionCalculating.get(c));
						i++;
					}
					connectionCalculating = newConnectionCalculating;
					
					for(Connection c : connectionCalculating.keySet()) {
						c.send(partialComponents.get(connectionCalculating.get(c)));
					}
					
					System.out.println("WAITING ON BARRIER");
					synchronized(barrierLock) {
						while(numOfRespondedClients != clientCopy.size()) barrierLock.wait();
						numOfRespondedClients = 0;
					}
				}
			}
		};
		synchronized(clients) {
			for(Connection c : clients) {
				c.send(NetworkMessage.PLAY);
			}
		}
		playThread = new Thread(r);
		playThread.setDaemon(true);
		playThread.start();
	}
	
	private void pause() {
		playThread.interrupt();
		synchronized(clients) {
			for(Connection c : clients) {
				c.send(NetworkMessage.PAUSE);
			}
		}
	}
	
	private void clear() throws Exception {
		g = new Grid(g.getNumRows());
	}
	
	/**Returns true if the new Grid is identical to the old Grid*/
	private boolean mergeData() {
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
		boolean isSameGrid = true;
		for(int i = 0;i<g.getNumRows();i++) {
			for(int j = 0;j<g.getNumRows();j++) {
				if(tempGrid.getCell(i, j).getCellState() != g.getCell(i,j).getCellState()) {
					isSameGrid = false;
				}
			}
		}
		g = tempGrid;
		return isSameGrid;
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
		
		private synchronized void send(Object o) {
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
							g.setCell(c);
							sendCellToAll(c, this);
						}
					}
					else if(rcvObj instanceof Grid) {
						if(!playThread.isAlive()) {
							g = (Grid) rcvObj;
							System.out.println("Received Grid: " + g.getNumRows());
							sendGameToAll();//TODO:sendGameToAll(this) test before using
						}
					}
					else if(rcvObj instanceof IterationDelayPeriod) {
						iterationDelay = (IterationDelayPeriod) rcvObj;
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
			synchronized(barrierLock) {
				numOfRespondedClients++;
				barrierLock.notifyAll();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		new Thread(new Server(10)).start();
	}
}
