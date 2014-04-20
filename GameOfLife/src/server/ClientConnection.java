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
	private volatile boolean isPlaying = false;
	public boolean getIsPlaying() {
		return isPlaying;
	}
	
	public Grid getGrid() {
		return g;
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
			//e.printStackTrace();
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
						System.out.println("Received Grid Size: " + g.getNumRows() + " rows");
						updateDisplay();
					}
					else if(rcvObj instanceof Cell) {
						Cell c = (Cell) rcvObj;
						g.setCell(c);
						System.out.println("Received Cell " + c + " " + ((c.getCellState() == 1) ? "alive":"dead"));
						updateCell(c);
					}
					else if(rcvObj instanceof AtomicReference[]) {
						AtomicReference[] list = (AtomicReference[]) rcvObj;
						System.out.println("Received partialComponent of size: " + list.length);
						
						ClientIterationCalculator cic = null;
						try {
							cic = new ClientIterationCalculator(list, g, this);
						}
						catch(Exception e) {
							e.printStackTrace();//TODO: Remove
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
								System.out.println("Received PLAY");
								isPlaying = true;
								break;
							case CALCULATION_COMPLETE:
								System.out.println("Received CALCULATION_COMPLETE");
								isPlaying = false;
								break;
							case PAUSE:
								System.out.println("Received PAUSE");
								isPlaying = false;
								break;
							case CLEAR://probably won't be used
								break;
							default:
								break;
						}
						updatePausePlay(nm);
					}
					if(g != null) g.printGrid();//TODO: remove
				}
				else {
					doLoop = false;
				}
			}
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch(IOException e) {
			setStatus("Server Closed", "red");
			//e.printStackTrace();//EOFException,SocketException
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
	
	
	
	/*TODO: remove
	public void startLife(int x, int y){
        //courdinates of the cell that the user clicked to be the first cell
		Cell c = null;
		try {
			c = new Cell(x,y);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		if(c != null) {
			c.setCellState(1);
		}
		send(c);
    }
	
	public void stopLife(int x, int y) {
		Cell c = null;
		try {
			c = new Cell(x,y);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		send(c);
	}
	*/
	
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
		System.out.println("Sent cell " + c + " " + ((state == 1) ? "alive":"dead") + " to server");
		//g.getCell(c.y, c.x).setCellState(c.getCellState());
		g.setCell(c);
		g.printGrid();
		send(c);
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
	
	/*TODO: remove
	//For resizes, or clearing of the Grid
	public void newGame(Grid g) {
		send(g);
	}
	*/
	
	/**Callback method for when ClientIterationCalculator finishes*/
	public void sendPartialComponent(ArrayList<Cell> component) {
		System.out.println("Sending component of size: " + component.size());
		send(component);
	}

	
	public static void main(String[] args) {
		final String serverIP = "127.0.0.1";
		ClientConnection self;
		try {
			Socket clientSocket = new Socket(serverIP, Server.port);
			self = new ClientConnection(clientSocket);
			self.start();
			Thread.sleep(5000);
			self.send(NetworkMessage.PLAY);
			System.out.println("Client socket accepted");
			System.out.println("Created I/O streams");
		}
		catch(IOException ex) {
			System.out.println("Failed to create I/O streams");
			System.exit(1);
		} catch(InterruptedException ex){
            System.out.println("Failed to create connection");
        }
	}
    
}
