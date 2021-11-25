import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class UserInterface {
	JTextArea ta;
	String message = "";
	JTextField tf;
	InputStream in = new ByteArrayInputStream(message.getBytes());

	public UserInterface() {

		// Creating the Frame
		JFrame frame = new JFrame("Chat Frame");
		frame.setSize(400, 400);

		JPanel panel = new JPanel();
		tf = new JTextField(10);
		tf.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					sendMessage();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}});
		JButton send = new JButton("Send");

		JButton reset = new JButton("Reset");

		frame.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	            message="/quit";
	        	sendMessage();
	        }
	    });
		
		send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		panel.add(tf);
		panel.add(send);
		panel.add(reset);

		// Text Area at the Center
		ta = new JTextArea();
		ta.setEditable(false);
		

		// Adding Components to the frame.
		frame.getContentPane().add(BorderLayout.SOUTH, panel);
		frame.getContentPane().add(BorderLayout.CENTER, ta);
		frame.setVisible(true);
	}

	public void printMessage(String message) {
		ta.append(message + "\n");
	}

	private void sendMessage() {

		if (!tf.getText().equals(""))
			message = tf.getText();
		tf.setText("");
		in = new ByteArrayInputStream(message.getBytes());
	}


}
