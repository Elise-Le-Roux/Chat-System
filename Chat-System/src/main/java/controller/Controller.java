package controller;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import GUI.Window;
import database.DBManager;
import database.User;
import database.Users;
import network.TCPMessage;
import network.TcpServerSocket;
import network.TcpSocket;
import network.UDPSocket;

public class Controller {
	
	static Window window;
	static TcpServerSocket tcpSocket;
	static UDPSocket udpSocket;
	static Users users;
	static DBManager DB;
	private static Semaphore mutex = new Semaphore(1);
	
	static String pseudo;
	static String ip_address;
	
	
	// CONSTRUCTOR
	
	public Controller(Window w, TcpServerSocket tcpS, UDPSocket udpS, DBManager db) {
		window = w;
		tcpSocket = tcpS;
		udpSocket = udpS;
		ip_address = UDPSocket.getLocalAddr().getHostAddress();
		DB = db;
		DB.connect();
		DB.init();
		users = new Users(DB.select_users()); // the attributes "connected" are set to false
		if(DB.select_users() != null) {
			window.refresh_list();
		}
		udpSocket.get_connected_users();
	}
	
	
	// GETTERS
	
	static public String get_ip_address() {
		return ip_address;
	}
	
	static public String get_pseudo() {
		return pseudo;
	}
	
	// METHODS TO UPDATE THE MODEL AND THE VIEW
	
	static public void add_connected_user(String pseudo, String address) {
		users.addConnectedUser(pseudo,address);
		window.refresh_list();
		DB.add_new_user(pseudo, address);
	}
	
	static public void set_disconnected_user(String address) {
		if (!address.equals(ip_address)) {
			users.changeStatus(address, false);
			window.refresh_list();
		}
	
		// Fermeture de la connexion tcp
		TcpSocket sock = TcpServerSocket.getConnections().get(address);
		if(sock != null) {
			TcpServerSocket.getConnections().remove(address);
			sock.kill();
		}
	}
	
	static public void change_pseudo_connected_user(String hostAddress, String new_pseudo) {
		users.changePseudo(hostAddress, new_pseudo);
		window.refresh_list();
		DB.change_pseudo(new_pseudo, hostAddress);
	}
	
	static public void set_msg_unread(String hostAddress) {
		users.changeUnread(hostAddress, true);
		//window.refresh_list();
		DB.change_unread(hostAddress, true);
	}
	
	static public void set_msg_read(String hostAddress) {
		users.changeUnread(hostAddress, false);
		//window.refresh_list();
		DB.change_unread(hostAddress, false);
	}
	
	// METHODS TO 
	
	static public boolean chooseUsername(String username) {
		boolean result = true;
		ArrayList<User> listUsers = users.getUsers();
		if (!listUsers.isEmpty()) {
			for( User u : listUsers) {
				if(u.getPseudo().equals(username) & !u.getHostAddress().equals(ip_address)) { // ajouter une méthode dans users pour faire la vérif
					result = false;
				}
			}
		}
		if (result) {
			pseudo = username;
			send_username_chosen(username);
		}
		return result;
	}
	
	static public boolean changeUsername(String new_pseudo) {
		boolean result = true;
		for( User u : Controller.get_list_users()) {
			if(u.getPseudo().equals(new_pseudo) & !u.getHostAddress().equals(ip_address)) {
				result = false;
			}
		}
		if (result) {
			pseudo = new_pseudo;
			Controller.send_username_changed(new_pseudo);
			change_pseudo_connected_user(ip_address ,new_pseudo);
			window.refresh_list();
			Window.chatBox.lockButton();
		}
		return result;
	}
	
	
	// METHODS TO SEND UDP MESSAGES
	
	static public void send_username_chosen(String username) {
		udpSocket.send_chosen_pseudo(username);
	}
	
	static public void send_username_changed(String pseudo) {
		udpSocket.send_username_changed(pseudo);
	}
	
	static public void send_disconnected() {
		udpSocket.send_disconnected();
	}
	
	static public void get_connected_users() {
		udpSocket.get_connected_users();
	}
	
	static public void send_connected(String pseudo, String address, InetAddress addr) {
		//if (!address.equals(ip_address) && specUser.get_connected()) { 
			udpSocket.send_connected(pseudo, addr);
		//}
	}
	
	
	// METHODS TO SEND TCP MESSAGES
	
	static public void send_msg(String hostAddress, String content) {
		TcpServerSocket.send_msg(hostAddress,content);
	}
	
	static public void send_file(String hostAddress, String filename, String path) {
		TcpServerSocket.send_file(hostAddress, filename, path);
	}
	
	
	
	
	
	static public ArrayList<User> get_list_users() {
		return users.getUsers();
	}
	
	static public ArrayList<String> get_list_pseudos() {
		return users.getPseudos();
	}
	
	static public boolean isConnected(String pseudo) {
		return users.isConnected(pseudo);
	}
	
	static public boolean getUnread(String pseudo) {
		return users.getUnread(pseudo);
	}
	
	static public ArrayList<TCPMessage> getConv(String hostAddress) {
		ArrayList<TCPMessage> list_msg = null;
		list_msg = DB.select_conv(get_ip_address(),hostAddress); 
		return list_msg;
	}
	
	static public String get_host_address(String pseudo) {
		String result = null;
		try {
			result = users.getHostAddress(pseudo);
		} catch (Exception e) {
			System.out.println("Exception controller : pseudo not found " + pseudo);
		}
		return result;
	}
	
	static public String get_user_pseudo(String hostAddress) {
		String result = null;
		try {
			result = users.getHostPseudo(hostAddress);
		} catch (Exception e) {
			System.out.println("Exception controller : hostaddress not found ");
		}
		return result;
	}
}
