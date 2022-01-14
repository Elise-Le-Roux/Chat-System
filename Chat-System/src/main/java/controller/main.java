package controller;

import GUI.ChangeUsername;
import GUI.Window;
import controller.TCPMessage.TypeNextMessage;
import database.DBManager;
import network.TcpServerSocket;
import network.UDPSocket;

public class main {

	public static void main(String[] args) {
		final Window window = new Window();
		window.createAndShowGUI();
		/*javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				window.createAndShowGUI();
			}
		}); */
		DBManager DB = new DBManager();
		/*DB.connect();
		DB.drop_users();*/
		UDPSocket UserA = new UDPSocket();
		TcpServerSocket Socket = new TcpServerSocket(3000);
		
		
		Controller control = new Controller(window, Socket, UserA, DB);
		
	}

}
