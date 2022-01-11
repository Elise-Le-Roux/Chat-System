package controller;

import network.UDPSocket;

public class specificUser {
	
	static String pseudo;
	static String hostname;

	public specificUser(String hostname) {
		this.hostname = hostname;
	}

	static public boolean chooseUsername(String pseudo) {
		boolean result = true;
		for( User u : ConnectedUsers.getConnectedUsers()) {
			if(u.getPseudo().equals(pseudo)) {
				result = false;
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
}
