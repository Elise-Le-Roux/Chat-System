package controller;

import GUI.ChangeUsername;
import GUI.Window;
import database.DBManager;
import network.TcpServerSocket;
import network.UDPSocket;
import network.TCPMessage.TypeNextMessage;

public class main {

	public static void main(String[] args) {
		DBManager DB = new DBManager();
		/*DB.connect();
		DB.drop_users();
		DB.init();*/
		final Window window = new Window();
		window.createAndShowGUI();
		/*javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				window.createAndShowGUI();
			}
		}); 
		DBManager DB = new DBManager();
		/*DB.connect();
		DB.drop_users();*/
		UDPSocket UserA = new UDPSocket(4956);
		TcpServerSocket Socket = new TcpServerSocket(3000);
		
		
		Controller control = new Controller(window, Socket, UserA, DB);
		
	}

}
