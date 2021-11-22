import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

	private ArrayList<ConnectionHandler> connections;
	private ServerSocket server;
	private boolean serverFull;
	private ExecutorService threadPool;

	public Server() {
		connections = new ArrayList<>();
		serverFull = false;
	}

	public void broadcastMessage(String message) {
		for (ConnectionHandler connection : connections) {
			connection.sendMessage(message);
		}
	}

	public void shutdown() {
		try {
			threadPool.shutdown();
			server.close();
			for (ConnectionHandler connection : connections) {
				connection.shutdown();
			}
		} catch (IOException e) {
			// TODO Handle exception
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			server = new ServerSocket(9999);
			threadPool = Executors.newCachedThreadPool();
			while (!serverFull) {
				Socket client = server.accept();
				ConnectionHandler handler = new ConnectionHandler(client);
				connections.add(handler);
				threadPool.execute(handler);
			}
		} catch (IOException e) {
			// TODO Handle exception
			e.printStackTrace();
			shutdown();
		}
	}

	class ConnectionHandler implements Runnable {
		private Socket client;
		private BufferedReader in;
		private PrintWriter out;
		private String nickname;

		public ConnectionHandler(Socket client) {
			this.client = client;
		}

		public void shutdown() {
			try {
				in.close();
				out.close();
				client.close();
			} catch (IOException e) {
				// TODO Handle exception
				e.printStackTrace();
			}
		}

		public void sendMessage(String message) {
			out.println(message);
		}

		@Override
		public void run() {
			try {
				out = new PrintWriter(client.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				out.println("Enter a nickname: ");
				do {
					nickname = in.readLine();
				} while (nickname.isBlank());
				System.out.println(nickname + " is connected");
				broadcastMessage(nickname + " joined chatroom");
				String message;

				while ((message = in.readLine()) != null) {
					if (message.startsWith("/quit")) {
						shutdown();
						// TODO: handle commands
					} else {
						broadcastMessage(nickname+": "+ message);
					}
				}
			} catch (IOException e) {
				// TODO: handle exception
			}
		}

	}

	
	public static void main(String[] args) {
		System.out.println("server");
		Server server = new Server();
		server.run();
	}
}
