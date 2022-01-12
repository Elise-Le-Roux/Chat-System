package network;

import java.io.IOException;
import java.net.*;
import java.util.Hashtable;

public class TcpServerSocket extends Thread{
	
	ServerSocket listener;
	static Hashtable<String, TcpSocket> connections = new Hashtable<String, TcpSocket>(50); // host address <-> socket

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
				TcpSocket thread = new TcpSocket(socketOfServer);
				connections.put(socketOfServer.getInetAddress().getHostAddress(), thread);
			} catch (IOException e) {
				System.out.println("Server exception 2: " + e.getMessage());
			}
		}
	}
	
	// Demand of connection to a specific host (and on a specific port)
	static public TcpSocket connect(String hostAddress, int port) {
		Socket clientSocket;
		TcpSocket thread = null;
		try {
			clientSocket = new Socket(hostAddress, port);
			thread = new TcpSocket(clientSocket);
			connections.put(hostAddress, thread);
		} catch (UnknownHostException e) {
			System.out.println("Server not found: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("I/O error: " + e.getMessage());
		}
		return thread;
	}
	
	public static Hashtable<String, TcpSocket> getConnections() {
		return connections;
	}
	
}


