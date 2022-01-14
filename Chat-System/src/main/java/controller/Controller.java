package controller;

import java.net.InetAddress;
import java.util.ArrayList;

import GUI.Window;
import database.DBManager;
import network.TcpServerSocket;
import network.TcpSocket;
import network.UDPSocket;

public class Controller {
	
	static Window window;
	static TcpServerSocket tcpSocket;
	static UDPSocket udpSocket;
	static specificUser specUser;
	static Users users;
	static DBManager DB;
	
	public Controller(Window w, TcpServerSocket tcpS, UDPSocket udpS, DBManager db) {
		window = w;
		tcpSocket = tcpS;
		udpSocket = udpS;
		specUser = new specificUser(UDPSocket.getLocalAddr().getHostAddress());
		DB = db;
		DB.connect();
		DB.init();
		users = new Users(DB.select_users()); // the attributes "connected" are set to false
		udpSocket.get_connected_users();
	}
	
	/************** Methods to control the list of users **************/
	
	static public void add_connected_user(String pseudo, String address) {
		users.addConnectedUser(pseudo,address);
		window.refresh_list();
	}
	
	static public void remove_connected_user(String pseudo, String address) { // Pas besoin ????
		if (!address.equals(specUser.get_address())) {
			users.removeUser(pseudo);
			window.refresh_list();
		}
		TcpSocket sock = tcpSocket.getConnections().get(address);
		if(sock != null) {
			tcpSocket.getConnections().remove(address);
			sock.kill();
		}
	}
	
	static public void set_disconnected_user(String pseudo, String address) {
		if (!address.equals(specUser.get_address())) {
			users.changeStatus(pseudo, false);
			window.refresh_list();
		}
		TcpSocket sock = TcpServerSocket.getConnections().get(address);
		if(sock != null) {
			TcpServerSocket.getConnections().remove(address);
			sock.kill();
		}
	}

	static public void send_connected(String pseudo, String address, InetAddress addr) {
		if (!address.equals(specUser.get_address()) && specUser.get_connected()) { 
			udpSocket.send_connected(specUser.get_pseudo(), pseudo, addr);
		}
	}
	
	static public void change_pseudo_connected_user(String old_pseudo, String new_pseudo) {
		users.changePseudo(old_pseudo, new_pseudo);
		window.refresh_list();
	}
	
	static public boolean choose_username(String username) {
		return specUser.chooseUsername(username);
	}
	
	static public void send_username_chosen(String username) {
		udpSocket.send_chosen_pseudo(username);
	}
	
	static public boolean change_username(String username) {
		String old_pseudo = specUser.get_pseudo();
		boolean result = specUser.changeUsername(username);
		if(result) {
			change_pseudo_connected_user(old_pseudo ,username);
		}
		window.refresh_list();
		return result;
	}
	
	static public void send_username_changed(String old_pseudo, String new_pseudo) {
		udpSocket.send_username_changed(old_pseudo, new_pseudo);
	}
	
	static public void send_disconnected() {
		udpSocket.send_disconnected(specUser.get_pseudo());
	}
	
	static public void get_connected_users() {
		udpSocket.get_connected_users();
	}
	
	static public ArrayList<User> get_list_connected_users() {
		return users.getConnectedUsers();
	}
	
	static public ArrayList<User> get_list_disconnected_users() {
		return users.getDisconnectedUsers();
	}
	
	static public ArrayList<User> get_list_users() {
		return users.getUsers();
	}
	
	static public boolean isConnected(String pseudo) {
		return users.isConnected(pseudo);
	}
	
	
	static public String get_address() {
		return specUser.get_address();
	}
	
	static public String get_pseudo() {
		if(specUser != null) {
			return specUser.get_pseudo();
		}
		else {
			return null;
		}
	}
	
	static public String get_host_address(String pseudo) {
		String result = null;
		try {
			result = users.getHostAddress(pseudo);
		} catch (Exception e) {
			System.out.println("Exception controller : pseudo not found ");
		}
		return result;
	}
	
	static public void set_connected(boolean connected) {
		specUser.set_connected(connected);
	}
	
	
	/*static public void set_list_connected_users() {
		connected_users;
	}*/
	
}
