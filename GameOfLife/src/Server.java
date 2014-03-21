import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server implements Runnable {
	private static final int port = 44445;

	private ServerSocket serverSocket = null;
	private volatile ArrayList<Connection> clients = new ArrayList<Connection>();
	boolean listening = true;

	private Object game = new Object();//TODO: create an explicit object for this

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
						//TODO: use instanceof if necessary to distinguish returned object types
						

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
