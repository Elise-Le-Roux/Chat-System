package controller;

import GUI.Window;
import network.TcpServerSocket;
import network.UDPSocket;

public class main {

	public static void main(String[] args) {
		UDPSocket UserA = new UDPSocket();
		TcpServerSocket Socket = new TcpServerSocket(3000);
		final Window window = new Window();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				window.createAndShowGUI();
			}
		});
		
		Controller control = new Controller(window, Socket, UserA);
		Controller.init();
	}

}
