package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import backend.Grid;

public class ClientConnection extends Thread {
	private Socket s = null;
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	private volatile Grid g;
	public Grid getGrid() {
		return g;
	}

    public void initializeGrid(int numRows) throws Exception {
        this.g = new Grid(numRows);
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
	
	public void send(Object o) {
		try {
			oos.writeObject(o);
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
						updateDisplay();
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

	public void updateDisplay() {
		//TODO
	}
	

	
	public static void main(String[] args) {
		final String serverIP = "127.0.0.1";
		ClientConnection self;
		try {
			Socket clientSocket = new Socket(serverIP, Server.port);
			self = new ClientConnection(clientSocket);
			self.start();
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
