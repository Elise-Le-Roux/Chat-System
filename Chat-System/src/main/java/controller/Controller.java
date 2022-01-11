package controller;

import java.net.InetAddress;
import java.util.ArrayList;

import GUI.Window;
import network.TcpServerSocket;
import network.UDPSocket;

public class Controller {
	
	static Window window;
	static TcpServerSocket tcpSocket;
	static UDPSocket udpSocket;
	static specificUser specUser;
	static ConnectedUsers connected_users;
	
	public Controller(Window w, TcpServerSocket tcpS, UDPSocket udpS) {
		window = w;
		tcpSocket = tcpS;
		udpSocket = udpS;
	}
	
	static public void add_connected_user(String pseudo, String address) {
		connected_users.addUser(pseudo,address);
		window.refresh_list();
	}
	
	static public void remove_connected_user(String pseudo, String address) {
		if (!address.equals(specUser.get_address())) {
			connected_users.removeUser(pseudo);
			window.refresh_list();
		}
	}

	static public void send_connected(String pseudo, String address, InetAddress addr) {
		if (!address.equals(specUser.get_address()) && specUser.get_connected()) {
			udpSocket.send_connected(specUser.get_pseudo(), pseudo, addr);
		}
	}
	
	static public void change_pseudo_connected_user(String old_pseudo, String new_pseudo) {
		connected_users.changePseudo(old_pseudo, new_pseudo);
		window.refresh_list();
	}
	
	static public boolean choose_username(String username) {
		return specUser.chooseUsername(username);
	}
	
	static public void send_username_chosen(String username) {
		udpSocket.send_chosen_pseudo(username);
	}
	
	static public boolean change_username(String username) {
		return specUser.changeUsername(username);
	}
	static public void send_username_changed(String username) {
		udpSocket.send_username_changed(specUser.get_pseudo(), username);
	}
	
	static public void send_disconnected() {
		udpSocket.send_disconnected(specUser.get_pseudo());
	}
	
	static public void get_connected_users() {
		udpSocket.get_connected_users();
	}
	
	static public void init() {
		specUser = new specificUser(UDPSocket.getLocalAddr().getHostAddress());
		connected_users = new ConnectedUsers();
	}
	
	static public ArrayList<User> get_list_connected_users() {
		return connected_users.getConnectedUsers();
	}
	
	static public String get_address() {
		return specUser.get_address();
	}
	
	static public String get_host_address(String pseudo) {
		String result = null;
		try {
			result = connected_users.getHostAddress(pseudo);
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
