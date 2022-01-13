package controller;

import GUI.ChangeUsername;
import GUI.Window;
import controller.TCPMessage.TypeNextMessage;
import database.DBManager;
import network.TcpServerSocket;
import network.UDPSocket;

public class main {

	public static void main(String[] args) {
		DBManager DB = new DBManager();
		DB.connect();
		//DB.drop();
		DB.init();
		System.out.println("********************************BDD**********************************");
		DBManager.afficher_BDD();
		System.out.println("******************************************************************");
		UDPSocket UserA = new UDPSocket();
		TcpServerSocket Socket = new TcpServerSocket(3000);
		final Window window = new Window();
		
		
		Controller control = new Controller(window, Socket, UserA);
		Controller.init();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				window.createAndShowGUI();
			}
		});
	}

}
