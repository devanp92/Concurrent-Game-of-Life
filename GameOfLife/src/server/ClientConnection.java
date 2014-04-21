package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import backend.Cell;
import backend.ClientIterationCalculator;
import backend.Grid;

public class ClientConnection extends Thread {
	private Socket s = null;
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	private volatile Grid g = null;
	public Grid getGrid() {
		return g;
	}
	
	private volatile boolean isPlaying = false;
	public boolean getIsPlaying() {
		return isPlaying;
	}
	
	private ArrayList<UICallback> subscribedUI = new ArrayList<UICallback>();
	public void subscribe(UICallback e) {
		subscribedUI.add(e);
	}
	public void unsubscribe(UICallback e) {
		subscribedUI.remove(e);
	}

	public ClientConnection(Socket s) {
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
			setStatus("Can't send to Server", "red");
		}
	}
	
	@Override
	public void run() {
		boolean doLoop = true;
		try {
			Object rcvObj;
			while(doLoop) {
				rcvObj = ois.readObject();
				if(rcvObj != null) {
					if(rcvObj instanceof Grid) {
						g = (Grid) rcvObj;
						//System.out.println("Client: received Grid Size: " + g.getNumRows() + " rows");
						updateDisplay();
					}
					else if(rcvObj instanceof Cell) {
						Cell c = (Cell) rcvObj;
						g.setCell(c);
						//System.out.println("Client: received Cell " + c + " " + ((c.getCellState() == 1) ? "alive":"dead"));
						updateCell(c);
					}
					else if(rcvObj instanceof AtomicReference[]) {
						AtomicReference[] list = (AtomicReference[]) rcvObj;
						//System.out.println("Client: received partialComponent of size: " + list.length);
						
						ClientIterationCalculator cic = null;
						try {
							cic = new ClientIterationCalculator(list, g, this);
						}
						catch(Exception e) {
							e.printStackTrace();
						}
						cic.start();//This makes the callback when done
					}
					else if(rcvObj instanceof IterationDelayPeriod) {
						IterationDelayPeriod idp = (IterationDelayPeriod) rcvObj;
						updateIterationDelay(idp.getDelayVal());
					}
					else if(rcvObj instanceof NetworkMessage) {
						NetworkMessage nm = (NetworkMessage) rcvObj;
						switch(nm) {
							case PLAY:
								//System.out.println("Client: received PLAY");
								isPlaying = true;
								break;
							case CALCULATION_COMPLETE:
								//System.out.println("Client: received CALCULATION_COMPLETE");
								isPlaying = false;
								break;
							case PAUSE:
								//System.out.println("Client: received PAUSE");
								isPlaying = false;
								break;
							default:
								break;
						}
						updatePausePlay(nm);
					}
				}
				else {
					doLoop = false;
				}
			}
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch(IOException e) {//EOFException,SocketException
			setStatus("Server Closed", "red");
		}
		finally {
			if(ois != null) {
				try {
					ois.close();
				}
				catch(IOException e) {e.printStackTrace();}
			}
			if(oos != null) {
				try {
					oos.close();
				}
				catch(IOException e) {e.printStackTrace();}
			}
			if(s != null) {
				try {
					s.close();
				}
				catch(IOException e) {e.printStackTrace();}
			}
		}
	}

	/**UICallback methods*/
	private void updateDisplay() {
		for(UICallback uic : subscribedUI) {
			uic.updateGame();
		}
	}
	private void updateCell(Cell c) {
		for(UICallback uic : subscribedUI) {
			uic.updateCell(c);
		}
	}
	private void updatePausePlay(NetworkMessage nm) {
		for(UICallback uic : subscribedUI) {
			uic.updatePausePlay(nm);
		}
	}
	private void updateIterationDelay(int delay) {
		for(UICallback uic : subscribedUI) {
			uic.updateIterationDelay(delay);
		}
	}
	private void setStatus(String newStatus, String txtFill) {
		for(UICallback uic : subscribedUI) {
			uic.setStatus(newStatus, txtFill);
		}
	}
	
	
	/**Send back to server methods*/
	public void changeCellState(int x, int y, int state) {
		Cell c = null;
		try {
			c = new Cell(x,y);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		if(c != null) {
			c.setCellState(state);
		}
		g.setCell(c);
		send(c);
		//System.out.println("Client: sent cell " + c + " " + ((state == 1) ? "alive":"dead") + " to server");
	}
	
	public void play() {
		//DON'T set isPlaying (let the receive set it)
		send(NetworkMessage.PLAY);
	}
	
	public void pause() {
		//DON'T set isPlaying (let the receive set it)
		send(NetworkMessage.PAUSE);
	}
	public void updateDelayValue(int delay) {
		send(new IterationDelayPeriod(delay));
	}
	
	public void resizeGrid(int numRows) throws Exception {
        g = new Grid(numRows);
        send(g);
    }
	
	/**Callback method for when ClientIterationCalculator finishes*/
	public void sendPartialComponent(ArrayList<Cell> component) {
		send(component);
		//System.out.println("Client: Sending component of size: " + component.size());
	}

	//in case you want to run a Client without a GUI
	public static void main(String[] args) {
		final String serverIP = "127.0.0.1";
		ClientConnection self;
		try {
			Socket clientSocket = new Socket(serverIP, Server.port);
			self = new ClientConnection(clientSocket);
			self.start();
			System.out.println("Client socket accepted");
			System.out.println("Created I/O streams");
		}
		catch(IOException ex) {
			System.out.println("Failed to create I/O streams");
			System.exit(1);
		}
	}
    
}
