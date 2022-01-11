package controller;

import java.util.ArrayList;

import network.UDPSocket;

public class specificUser {
	
	static String pseudo;
	static String address;

	public specificUser(String addr) {
		address = addr;
	}

	static public boolean chooseUsername(String pseudo) {
		boolean result = true;
		System.out.println(pseudo);
		ArrayList<User> listUsers = ConnectedUsers.getConnectedUsers();
		if (!listUsers.isEmpty()) {
			for( User u : listUsers) {
				System.out.println(u.getHostAddress());
				System.out.println(u.getPseudo());
				if(u.getPseudo().equals(pseudo)) {
					result = false;
				}
			}
		}
		if (result) {
			pseudo = pseudo;
			UDPSocket.send_chosen_pseudo(pseudo);
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
}
