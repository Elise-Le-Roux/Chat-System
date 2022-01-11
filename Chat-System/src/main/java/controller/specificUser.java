package controller;

import java.util.ArrayList;

import network.UDPSocket;

public class specificUser {
	
	static String pseudo;
	static String address;
	static boolean connected = false;

	public specificUser(String addr) {
		address = addr;
	}

	static public boolean chooseUsername(String username) {
		boolean result = true;
		ArrayList<User> listUsers = ConnectedUsers.getConnectedUsers();
		if (!listUsers.isEmpty()) {
			for( User u : listUsers) {
				if(u.getPseudo().equals(username)) {
					result = false;
				}
			}
		}
		if (result) {
			pseudo = username;
			UDPSocket.send_chosen_pseudo(username);
		}
		return result;
	}

	static public boolean changeUsername(String new_pseudo) {
		boolean result = true;
		for( User u : ConnectedUsers.getConnectedUsers()) {
			if(u.getPseudo().equals(new_pseudo)) {
				result = false;
			}
		}
		if (result) {
			pseudo = new_pseudo;
			UDPSocket.send_username_changed(pseudo, new_pseudo);
		}
		return result;
	}
	
	static public String get_pseudo() {
		return pseudo;
	}
	
	static public String get_address() {
		return address;
	}
	
	static public void set_address(String addr) {
		address = addr;
	}
	
	static public boolean get_connected() {
		return connected;
	}
	
	static public void set_connected(boolean con) {
		connected = con;
	}
}
