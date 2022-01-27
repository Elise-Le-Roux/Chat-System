package controller;

import model.DBManager;
import network.TcpServerSocket;
import network.UDPSocket;
import view.Window;

public class Principal {

	public static void main(String[] args) {
		DBManager DB = new DBManager();
		DB.connect();
		DB.drop_historique();
		DB.drop_users();
		DB.init();
		final Window window = new Window();
		Window.createAndShowGUI();
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
		
		
		new Controller(window, Socket, UserA, DB);
		
	}

}
