package network;

import java.io.IOException;
import java.net.*;
import java.sql.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import controller.Controller;
import network.TCPMessage.TypeNextMessage;

public class TcpServer extends Thread{
	
	static ServerSocket listener;
	static Hashtable<String, TcpSocket> connections = new Hashtable<String, TcpSocket>(50); // host address <-> socket
	static int port;
	
	// to stop the thread
	static volatile boolean exit = false;
	
	// CONSTRUCTOR
	public TcpServer(int socket_port) {
		try {
			listener = new ServerSocket(socket_port);
			port = socket_port;
			start();
		} catch (IOException e) {
			System.out.println("Server exception 1: " + e.getMessage());
		}
	}
	
	public void run() {
		while (!exit) {
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
		close();
	}

	// GETTER
	public static Hashtable<String, TcpSocket> getConnections() {
		return connections;
	}
	
	// for stopping the thread
	public void kill() {
		exit = true;
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
				TcpServer.getConnections().get(hostAddress).send_msg(msg);
			}
			else {
				TcpServer.connect(hostAddress, port).send_msg(msg);

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
	
	// Closes the sockets
	public static void close() {
		Enumeration<TcpSocket> e = connections.elements();
		TcpSocket sock = null;
		while (e.hasMoreElements()) {
			sock = e.nextElement();
			sock.kill();
		}
		try {
			listener.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}


