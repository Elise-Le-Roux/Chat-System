package controller;

import java.net.InetAddress;
import java.util.ArrayList;

import model.DBManager;
import model.User;
import model.Users;
import network.TCPMessage;
import network.TcpServer;
import network.TcpSocket;
import network.UDPSocket;
import view.Window;

public class Controller {

	static Window window;
	static TcpServer tcpSocket;
	static UDPSocket udpSocket;
	static Users users;
	static DBManager DB;

	static String pseudo;
	static String ip_address;


	// CONSTRUCTOR

	public Controller(Window w, TcpServer tcpS, UDPSocket udpS, DBManager db) {
		window = w;
		tcpSocket = tcpS;
		udpSocket = udpS;
		ip_address = UDPSocket.getLocalAddr().getHostAddress();
		DB = db;
		DB.connect();
		DB.init();
		users = new Users(DB.select_users()); // the attributes "connected" are set to false
		Window.createAndShowGUI();
		if(DB.select_users() != null) {
			Window.refresh_list();
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
		Window.refresh_list();
		DB.add_new_user(pseudo, address);
	}

	static public void set_disconnected_user(String address) {
		if (!address.equals(ip_address)) {
			users.changeStatus(address, false);
			Window.refresh_list();
		}

		// Fermeture de la connexion tcp
		TcpSocket sock = TcpServer.getConnections().get(address);
		if(sock != null) {
			TcpServer.getConnections().remove(address);
			sock.kill();
		}
	}

	static public void change_pseudo_connected_user(String hostAddress, String new_pseudo) {
		users.changePseudo(hostAddress, new_pseudo);
		Window.refresh_list();
		DB.change_pseudo(new_pseudo, hostAddress);
	}

	static public void set_msg_unread(String hostAddress) {
		users.changeUnread(hostAddress, true);
		Window.refresh_list();
		DB.change_unread(hostAddress, true);
	}

	static public void set_msg_read(String hostAddress) {
		users.changeUnread(hostAddress, false);
		//window.refresh_list();
		DB.change_unread(hostAddress, false);
	}

	
	// METHODS TO CHOOSE/CHANGE THE USERNAME

	static public boolean chooseUsername(String username) {
		boolean result = true;
		ArrayList<User> listUsers = users.getUsers();
		if (!listUsers.isEmpty()) {
			for( User u : listUsers) {
				if(u.getPseudo().equals(username) & !u.getHostAddress().equals(ip_address)) { 
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
			Window.refresh_list();
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
		udpSocket.send_connected(pseudo, addr);
	}


	// METHODS TO SEND TCP MESSAGES

	static public void send_msg(String hostAddress, String content) {
		TcpServer.send_msg(hostAddress,content);
	}

	static public void send_file(String hostAddress, String filename, String path) {
		TcpServer.send_file(hostAddress, filename, path);
	}


	// GETTERS OF THE LIST OF USERS

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


	// GETTERS OF THE DATABASE

	static public ArrayList<TCPMessage> getConv(String hostAddress) {
		ArrayList<TCPMessage> list_msg = null;
		list_msg = DB.select_conv(get_ip_address(),hostAddress); 
		return list_msg;
	}


	// EXIT PROCEDURE

	static public void exit_procedure() {
		send_disconnected();
		tcpSocket.kill();
		udpSocket.kill();
	}
}
