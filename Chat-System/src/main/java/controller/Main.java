package controller;

import model.DBManager;
import network.TcpServer;
import network.UDPSocket;
import view.Window;

public class Main {

	public static void main(String[] args) {
		DBManager DB = new DBManager();
		final Window window = new Window();
		UDPSocket UserA = new UDPSocket(4956);
		TcpServer Socket = new TcpServer(3000);
		new Controller(window, Socket, UserA, DB);
	}
}
