package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import backend.Grid;

public class ClientConnection {
	private Socket socket = null;
	private ObjectOutputStream oos = null;
	private InputHandler ih = null;
	private volatile Grid g = new Grid(10);
	public Grid getGrid() {
		return g;
	}

	public ClientConnection(Socket s) {
		this.socket = s;
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
			boolean doLoop = true;
			try {
				Object recvObj;
				while(doLoop) {
					recvObj = ois.readObject();
					if(recvObj != null) {
						if(recvObj instanceof Grid) {
							System.out.println("Received Grid");
							g = (Grid) recvObj;
							//TODO: display on UI
						}
						else if(recvObj instanceof Integer) {
							System.out.println("Received Row to Calculate");
							Integer rowToCalculate = (Integer) recvObj;
							//calculate iteration on the row
							//send back next iteration
						}
						
						//send(recvObj);//TODO: perhaps make this a send on another thread
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
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		final String serverIP = "127.0.0.1";
		ClientConnection self;
		try {
			Socket clientSocket = new Socket(serverIP, Server.port);
			self = new ClientConnection(clientSocket);
			Thread.sleep(2000);
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
