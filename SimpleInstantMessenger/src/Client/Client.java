package Client;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame{
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String serverIP;
	private Socket connection;
	
	public Client(String host) {
		super("Client");
		serverIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
				event -> {
					sendMessage(event.getActionCommand());
					userText.setText("");
				}
		);
		
		//GUI
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(600,300);
		setVisible(true);
	}
	
	//connection
	public void startRunning() {
		try {
			connectToServer();
			setUpStreams();
			whileChatting();
		}
		catch(EOFException eof) {
			showMessage("\n Client terminated connection");
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		finally {
			closeAll();
		}
	}
	
	//connect to server
	private void connectToServer() throws IOException{
		showMessage("Attempting connection....\n");
		connection = new Socket(InetAddress.getByName(serverIP), 6789);
		showMessage("Connected to: " + connection.getInetAddress().getHostName());
	}
	
	//set up streams to send and recieve messages
	private void setUpStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n streams are now setup");
	}
	
	//chatting
	private void whileChatting() throws IOException{
		ableToType(true);
		do {
			try {
				message = (String) input.readObject();
				showMessage("\n " + message);
			}
			catch(ClassNotFoundException cnf) {
				showMessage("\n cannot interpret the recieving message");
			}
		}while(!message.equals("SERVER - END"));
	}
	
	//close the streams and sockets
	private void closeAll() {
		showMessage("\n Closing connection....");
		ableToType(false);
		try {
			output.close();
			input.close();
			connection.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	//send messages to server
	private void sendMessage(String message) {
		try {
			output.writeObject("CLIENT - " + message);
			output.flush();
			showMessage("\nCLIENT - " + message);
		}
		catch(IOException e) {
			chatWindow.append("\n ERROR:");
		}
	}
	
	//updates chatwindow
	private void showMessage(final String text) {
		SwingUtilities.invokeLater(
				() -> chatWindow.append(text)
		);
	}
	
	private void ableToType(final Boolean flag) {
		SwingUtilities.invokeLater(
				() -> userText.setEditable(flag)
		);
	}
	
}
