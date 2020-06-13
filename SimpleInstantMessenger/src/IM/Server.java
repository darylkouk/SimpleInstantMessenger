package IM;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame{
	
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	
	public Server() {
		super("Simple Instant Messenger");
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
		add(new JScrollPane(chatWindow));
		setSize(300,150);
		setVisible(true);
		
	}
	
	//set up and run the server
	public void startRunning() {
		try {
			server = new ServerSocket(6789, 100);
			while(true) {
				try {
					waitForConnection();
					setUpStreams();
					whileChatting();
				}
				catch(EOFException eof) {
					showMessage("\n Server ended the connection! ");
				}
				finally {
					closeAll();
				}
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//connection
	private void waitForConnection() throws IOException{
		showMessage("Waiting for someone to connect......\n");
		connection = server.accept();
		showMessage("Now connected to " + connection.getInetAddress().getHostName());
	}
	
	//get streams to send and receive data
	private void setUpStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Streams are now steup! \n");
	}
	
	//Chatting
	private void whileChatting() throws IOException{
		String message = "You are now Connected!";
		sendMessage(message);
		ableToType(true);
		do {
			try {
				message = (String) input.readObject();
				showMessage("\n" + message);
			}
			catch(ClassNotFoundException cnf) {
				showMessage("\n cannot interpret the recieving message");
			}
		}while(!message.equals("CLIENT - END"));
	}
	
	//Close streams and socket
	private void closeAll() {
		showMessage("\n Closing connection... \n");
		ableToType(false);
		try {
			output.close();
			input.close();
			connection.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	//send a message to client
	private void sendMessage(String message) {
		try {
			output.writeObject("SERVER - " + message);
			output.flush();
			showMessage("\nSERVER - " +message);
		}
		catch(IOException e) {
			chatWindow.append("\n ERROR");
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
