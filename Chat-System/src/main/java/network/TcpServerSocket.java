package network;

import java.io.IOException;
import java.net.*;
import java.util.Hashtable;

public class TcpServerSocket extends Thread{
	
	ServerSocket listener;
	Hashtable<String, TcpSocket> connections = new Hashtable<String, TcpSocket>(50); // hostname <-> socket

	public TcpServerSocket(int port) {
		try {
			this.listener = new ServerSocket(port);
			start();
		} catch (IOException e) {
			System.out.println("Server exception 1: " + e.getMessage());
		}
	}

	public void run() {
		while (true) {
			// Accept client connection requests
			// Get new Socket and new thread for the rest of the connection.
			Socket socketOfServer;
			try {
				socketOfServer = listener.accept();
				System.out.println("Connected");
				TcpSocket thread = new TcpSocket(socketOfServer);
				connections.put(socketOfServer.getInetAddress().getHostName(), thread);
			} catch (IOException e) {
				System.out.println("Server exception 2: " + e.getMessage());
			}
		}
	}
	
	// Demand of connection to a specific host (and on a specific port)
	public void connect(String host, int port) {
		Socket clientSocket;
		try {
			clientSocket = new Socket(host, port);
			TcpSocket thread = new TcpSocket(clientSocket);
			connections.put(host, thread);
		} catch (UnknownHostException e) {
			System.out.println("Server not found: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("I/O error: " + e.getMessage());
		}
	}
	
	public Hashtable<String, TcpSocket> getConnections() {
		return connections;
	}
	
}


