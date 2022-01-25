package network;

import java.io.IOException;
import java.net.*;
import java.sql.Date;
import java.util.Hashtable;

import controller.Controller;
import network.TCPMessage.TypeNextMessage;

public class TcpServerSocket extends Thread{
	
	ServerSocket listener;
	static Hashtable<String, TcpSocket> connections = new Hashtable<String, TcpSocket>(50); // host address <-> socket
	static int port;
	
	public TcpServerSocket(int socket_port) {
		try {
			this.listener = new ServerSocket(socket_port);
			port = socket_port;
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
	
	static public void send_msg(String hostAddress, String content) {
		Date date = new Date(System.currentTimeMillis());
		TCPMessage msg;
		try {
			msg = new TCPMessage(InetAddress.getByName(Controller.get_ip_address()), InetAddress.getByName(hostAddress), content, date, TypeNextMessage.TEXT);
			if (getConnections().containsKey(hostAddress)) {
				TcpServerSocket.getConnections().get(hostAddress).send_msg(msg);
			}
			else {
				TcpServerSocket.connect(hostAddress, port).send_msg(msg);

			}
		} catch (UnknownHostException e) {
			System.out.println("exception TcpServerSocket send_msg");
		}
		
	}
	
	static public void send_file(String hostAddress, String filename, String path) {
		Date date = new Date(System.currentTimeMillis());
		TCPMessage msg;
		try {
			msg = new TCPMessage(InetAddress.getByName(Controller.get_ip_address()), InetAddress.getByName(hostAddress), filename, date, TypeNextMessage.FILE);
			if (getConnections().containsKey(hostAddress)) {
				getConnections().get(hostAddress).sendFile(msg,path);
			}
			else {
				connect(hostAddress, port).sendFile(msg,path);
			}
		} catch (UnknownHostException e) {
			System.out.println("exception TcpServerSocket send_file");
		} catch (Exception e) {
			System.out.println("exception TcpServerSocket send_file");
		}
		
	}
	
	public static Hashtable<String, TcpSocket> getConnections() {
		return connections;
	}
	
}


