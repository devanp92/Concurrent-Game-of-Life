package cis4930.gameoflife.server;

import cis4930.gameoflife.Cell;
import cis4930.gameoflife.Grid;
import cis4930.gameoflife.QuadTree;
import cis4930.gameoflife.QuadTreeElement;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Server implements Runnable {
	public static final int port = 44445;

	private ServerSocket serverSocket = null;
	private volatile ArrayList<Connection> clients = new ArrayList<Connection>();
	boolean listening = true;
	
	volatile CyclicBarrier barrier;

	private Grid game = new Grid(10);
	private volatile ArrayList<QuadTreeElement> partialComponents = new ArrayList<QuadTreeElement>();
	private volatile boolean isPlaying = false;
	
	private Thread playThread = new Thread();
	private volatile ArrayList<Object> connectionLock = new ArrayList<Object>();

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
			public void run() {
				isPlaying = true;
				try {
					while(!Thread.interrupted()) {
						Collection<Connection> clientCopy;
						synchronized(clients) {
							clientCopy = Collections.unmodifiableCollection(clients);
						}
						barrier = new CyclicBarrier(clientCopy.size() + 1);
						System.out.println("Barrier in play(): " + barrier);
						Thread.sleep(1000);
						for(Connection c : clientCopy) {
							c.send(game);
						}
						
						
						barrier.await();
						System.out.println("After barrier.await(): " + barrier);
					}
				}
				catch(InterruptedException e) {
					//handle why we exited
					//if PAUSE, do nothing;
					//if client connection lost, determine what needs recalculated
				}
				catch(BrokenBarrierException e) {
					// TODO Auto-generated catch block
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
	
	public void clear() {
		game = new Grid(game.getNumRows());
	}
	
	public void mergeData() {
		//TODO
		partialComponents.clear();
	}

	class Connection {
		private Socket s = null;
		private ObjectOutputStream oos = null;
		private InputHandler ih = null;

		public Connection(Socket s) {
			this.s = s;
			try {
				oos = new ObjectOutputStream(s.getOutputStream());
				ih = new InputHandler(s);
				ih.start();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		public void send(Object o) {
			try {
				oos.writeObject(o);
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}

		class InputHandler extends Thread {
			private ObjectInputStream ois = null;
			public InputHandler(Socket s) {
				try {
					ois = new ObjectInputStream(s.getInputStream());
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
						//TODO: use instanceof if necessary to distinguish returned object types
						boolean maybe = true;
						if(o instanceof NetworkMessage) {
							NetworkMessage nm = (NetworkMessage) o;
							switch(nm) {
								case PLAY:
									play();
									break;
								case PAUSE:
									pause();
									break;
								case CLEAR:
									pause();
									clear();
									break;
							}
						}
						else if(o instanceof Cell) {
							pause();
							Cell c = (Cell) o;
							game.getCell(c.y, c.x).setCellState(c.getCellState());
							
							/*if(c.isAlive == 1) {
								game.insert(c);
							}
							else if(c.isAlive == 0) {
								game.remove(c);
							}*/
						}
						else if(o instanceof Grid) {
							pause();
							Grid g = (Grid) o;
							game = g;
							//TODO: implement appropriate barrier synchronization
						}
						else if(o instanceof QuadTreeElement) {//TODO: move this to instanceof Grid
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
								catch(TimeoutException e) {
									//DON'T DO ANYTHING
								}
								//QuadTree qTree = (QuadTree) o;
								//partialComponents.add(qTree);
							}
						}

						//TODO: pass information received to another thread (MergerThread(?): would need to be created in Server())
						//Other thread does merging, game object modification, resending next iteration
					}
				}
				catch(ClassNotFoundException e) {
					//e.printStackTrace();
				}
				catch(IOException e) {
					//e.printStackTrace();
				}
				finally {
					if(ois != null) {
						try {
							ois.close();
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
				}
			}
		}
	}

	public static void main(String[] args) {
		new Thread(new Server()).start();
	}
}
