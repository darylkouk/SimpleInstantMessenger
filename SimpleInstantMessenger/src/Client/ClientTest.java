package Client;

import javax.swing.JFrame;

public class ClientTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Client testClient;
		
		testClient = new Client("127.0.0.1");
		testClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		testClient.startRunning();
	}

}
