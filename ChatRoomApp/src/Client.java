import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {

	private Socket client;
	private BufferedReader in;
	private PrintWriter out;
	private UserInterface UI = new UserInterface();

	public void shutDown() {
		try {
			in.close();
			out.close();
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			client = new Socket("127.0.0.1", 9999);
			out = new PrintWriter(client.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));

			InputHandler handler = new InputHandler();
			Thread t = new Thread(handler);
			t.start();

			String inMessage;
			while ((inMessage = in.readLine()) != null) {
				UI.printMessage(inMessage);
			}
		} catch (IOException e) {
			// TODO: handle exception
			shutDown();
		}
	}

	class InputHandler implements Runnable {

		@Override
		public void run() {
			try {
				while (true) {
					BufferedReader inReader = new BufferedReader(new InputStreamReader(UI.in));
					String message;

					if ((message = inReader.readLine()) != null) {
						out.println(message);
						if (message.equals("/quit")) {
							inReader.close();
							shutDown();
							System.exit(0);
						}
					}

				}
			} catch (IOException e) {
				// TODO: handle exception
			}
		}

	}

	public static void main(String[] args) {
		System.out.println("client");
		// TODO Auto-generated method stub
		Client client = new Client();
		client.run();
	}

}
