package IM;

import javax.swing.JFrame;

public class ServerTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Server test = new Server();
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test.startRunning();
	}

}
