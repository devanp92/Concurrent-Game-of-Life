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
import java.util.concurrent.locks.ReentrantLock;

public class Server extends Thread {
	public static final int port = 44445;
	public static final int defaultSize = 10;

	private volatile ServerSocket serverSocket = null;
	private ArrayList<Connection> clients = new ArrayList<Connection>();
	
	/**An object for the playThread to wait on while clients respond with components*/
	Object barrierLock = new Object();
	/**An integer representing the number of responded clients
	 * A client responds when it sends back partialComponent or it closes
	 * Note: responded does not mean they have sent back their expected partialComponent
	 */
	int numOfRespondedClients;

	private volatile Grid g;
	private volatile Thread playThread = new Thread();
	private volatile IterationDelayPeriod iterationDelay = new IterationDelayPeriod(0);
	
	//Note: separate mappings are used for Connection->Integer & Integer->Component instead of Connection->Component
	//making it easier to reassign a Component to another Connection if a Connection drops before returning
	//its assignment
	/**A lock for access to any of the next 3 HashMaps*/
	ReentrantLock connectionComponentLock = new ReentrantLock();
	/**A mapping between a connection and what component it is currently calculating*/
	private HashMap<Connection, Integer> connectionCalculating = new HashMap<Connection, Integer>();
	/**A mapping between a component of the Grid and the partial component to send*/
	private HashMap<Integer, AtomicReference[]> partialComponents = new HashMap<Integer, AtomicReference[]>();
	/**A mapping between a component of the Grid and the completed partial component*/
	private HashMap<Integer, ArrayList<Cell>> completePartialComponents = new HashMap<Integer, ArrayList<Cell>>();
	
	/**Expected use case is for this to be a daemon thread. If you don't want it to be, explicitly setDaemon(false) before starting*/
	public Server() throws Exception {
		this(defaultSize);
	}
    public Server(int numRows) throws Exception {
        g = new Grid(numRows);
        setDaemon(true);
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

		//Setup server
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Listening on port " + port);
		}
		catch(IOException e) {
			System.err.println("Could not listen on port: " + port + ".");
		}

		if(serverSocket != null) {
			try {
				//Accept Clients, setup Connection thread for handling inputs
				while(!Thread.currentThread().isInterrupted()) {
					Connection c = new Connection(serverSocket.accept());
					synchronized(clients) {
						clients.add(c);
					}
					c.start();
					c.send((playThread.isAlive()) ? NetworkMessage.PLAY:NetworkMessage.PAUSE);
					c.send(iterationDelay);
					c.send(g);

					System.out.println("Client Connected");
				}
			}
			catch(IOException e) {//SocketException
				//ignored: expected behavior for closing
				//Usually invoked with stopServer()
			}

			try {
				serverSocket.close();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
		//Close all client connections to finish
		synchronized(clients) {
			for(Connection c : clients) {
				c.stopConnection();
			}
		}
	}
    
    public void stopServer() {
    	try {
			serverSocket.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
    }
    
	
	private synchronized void play() {
		final Runnable r = new Runnable() {
			/**run()*/
			public void run() {
				try {
					boolean gridChanged = true;
					while(!Thread.interrupted() && gridChanged) {
						System.out.println("Server: Calculating New Iteration");
						//Get a consistent (unmodifiable) list of clients
						ArrayList<Connection> clientCopy;
						synchronized(clients) {
							clientCopy = new ArrayList<Connection>(Collections.unmodifiableCollection(clients));
						}
						
						//Generate partial components to send to clients, initialize connectionCalculating, partialComponents
						DistributiveIterationCalculator distributedIC = null;
						try {
							distributedIC = new DistributiveIterationCalculator(g);
							List<AtomicReference[]> components = distributedIC.findSubSetsOfCellsForClients(clientCopy.size());
							
							connectionComponentLock.lock();
							try {
								for(int i = 0;i<clientCopy.size();i++) {
									connectionCalculating.put(clientCopy.get(i), new Integer(i));
									partialComponents.put(new Integer(i), components.get(i));
								}
								completePartialComponents.clear();
							}
							finally {
								connectionComponentLock.unlock();
							}
						}
						catch(Exception e) {/*ignored*/}
						
						//Clear number of responded clients
						synchronized(barrierLock) {
							numOfRespondedClients = 0;
						}
						
						//Send partial components to clients
						long start = System.currentTimeMillis();
						connectionComponentLock.lock();
						try {
							for(Connection c : connectionCalculating.keySet()) {
								c.send(partialComponents.get(connectionCalculating.get(c)));
							}
						}
						finally {
							connectionComponentLock.unlock();
						}
						
						//wait until all clients have responded (not necessarily with results)
						waitOnBarrier(clientCopy.size());

						//handle if a client exited during calculation: 
						boolean isEmpty;
						int clientCount;
						connectionComponentLock.lock();
						try {
							isEmpty = connectionCalculating.isEmpty();
						}
						finally {
							connectionComponentLock.unlock();
						}
						synchronized(clients) {
							clientCount = clients.size();
						}
						while(!isEmpty && clientCount > 0) {
							resendRemainingPartialComponents();
							connectionComponentLock.lock();
							try {
								isEmpty = connectionCalculating.isEmpty();
							}
							finally {
								connectionComponentLock.unlock();
							}
							synchronized(clients) {
								clientCount = clients.size();
							}
						}
						
						
						//merge data
						System.out.println("Server: all clients responded");
						gridChanged = !mergeData();
						System.out.println("Server: DONE Calculating New Iteration: sending merge to all Clients");
						//g.printGrid();
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
					synchronized(barrierLock) {
						numOfRespondedClients = 0;
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
				
				connectionComponentLock.lock();
				try {
					for(Connection c : connectionCalculating.keySet()) {
						newConnectionCalculating.put(clientCopy.get(i), connectionCalculating.get(c));
						i++;
					}
					connectionCalculating = newConnectionCalculating;
					
					for(Connection c : connectionCalculating.keySet()) {
						c.send(partialComponents.get(connectionCalculating.get(c)));
					}
				}
				finally {
					connectionComponentLock.unlock();
				}	

				waitOnBarrier(clientCopy.size());
			}
			
			private void waitOnBarrier(int waitCount) throws InterruptedException {
				System.out.println("Server: waiting on barrier");
				synchronized(barrierLock) {
					while(numOfRespondedClients < waitCount) {
						barrierLock.wait();
					}
					numOfRespondedClients = 0;
				}
			}
		};
		if(!playThread.isAlive()) {
			synchronized(clients) {
				for(Connection c : clients) {
					c.send(NetworkMessage.PLAY);
				}
			}
			playThread = new Thread(r);
			playThread.setDaemon(true);
			playThread.start();
		}
	}
	
	private synchronized void pause() {
		playThread.interrupt();
		synchronized(clients) {
			for(Connection c : clients) {
				c.send(NetworkMessage.PAUSE);
			}
		}
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
		//Note: connectionCompnentLock acts as a lock on itself, partialComponents, and completePartialComponents
		connectionComponentLock.lock();
		try {
			components = Collections.unmodifiableCollection(completePartialComponents.values());
			partialComponents.clear();
		}
		finally {
			connectionComponentLock.unlock();
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
	
	private void sendDelayToAll() {
		sendDelayToAll(null);
	}
	private void sendDelayToAll(Connection exception) {
		ArrayList<Connection> clientCopy;
		synchronized(clients) {
			clientCopy = new ArrayList<Connection>(Collections.unmodifiableCollection(clients));
		}
		for(Connection c : clientCopy) {
			if(c != exception) {
				c.send(iterationDelay);
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
				setDaemon(true);
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
				//ignored, the socket was closed
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
								System.out.println("Server: received PLAY");
								play();
								break;
							case PAUSE:
								System.out.println("Server: received PAUSE");
								pause();
								break;
							default:
								break;
						}
					}
					else if(rcvObj instanceof Cell) {
						if(!playThread.isAlive()) {
							Cell c = (Cell) rcvObj;
							System.out.println("Server: received Cell " + c + " " + ((c.getCellState() == 1) ? "alive":"dead"));
							g.setCell(c);
							sendCellToAll(c, this);
						}
					}
					else if(rcvObj instanceof Grid) {
						if(!playThread.isAlive()) {
							g = (Grid) rcvObj;
							System.out.println("Server: received Grid: " + g.getNumRows());
							sendGameToAll(this);//TODO:sendGameToAll(this) test before using
						}
					}
					else if(rcvObj instanceof IterationDelayPeriod) {
						IterationDelayPeriod idp = (IterationDelayPeriod) rcvObj;
						boolean isSame = true;
						synchronized(iterationDelay) {
							if(idp.getDelayVal() != iterationDelay.getDelayVal()) {
								iterationDelay.setDelayVal(idp.getDelayVal());
								isSame = false;
							}
						}
						if(!isSame) {
							sendDelayToAll(this);
						}
					}
					else if(rcvObj instanceof ArrayList<?>) {
						if(playThread.isAlive()) {
							//System.out.println("Server: received Completed PartialComponent");
							ArrayList<Cell> partialComponent = (ArrayList<Cell>) rcvObj;//TODO: reflection?
							connectionComponentLock.lock();
							try {
								Integer item = connectionCalculating.get(this);
								completePartialComponents.put(item, partialComponent);
								connectionCalculating.remove(this);
							}
							finally {
								connectionComponentLock.unlock();
							}
							passBarrier();
						}
					}
					
					
				}
			}
			catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
			catch(IOException e) {
				e.printStackTrace();
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
				connectionComponentLock.lock();
				try {
					if(connectionCalculating.containsKey(this)) {
						passBarrier();
						//Don't remove this from connectionCalculating
					}
				}
				finally {
					connectionComponentLock.unlock();
				}
			}
		}
		
		private void passBarrier() {
			synchronized(barrierLock) {
				numOfRespondedClients++;
				System.out.println("Server: responded client count: " + numOfRespondedClients);
				barrierLock.notifyAll();
			}
		}
		
		public void stopConnection() {
			try {
				ois.close();
			}
			catch(IOException e) {
				e.printStackTrace();//TODO
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Server s = new Server(defaultSize);
		s.setDaemon(false);
		s.start();
	}
}
