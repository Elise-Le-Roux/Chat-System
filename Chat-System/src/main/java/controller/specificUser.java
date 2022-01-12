package controller;

import java.util.ArrayList;

import network.UDPSocket;

public class specificUser {
	
	String pseudo;
	String address;
	boolean connected;

	public specificUser(String addr) {
		this.address = addr;
		this.connected = false;
	}

	 public boolean chooseUsername(String username) {
		boolean result = true;
		ArrayList<User> listUsers = Controller.get_list_connected_users();
		if (!listUsers.isEmpty()) {
			for( User u : listUsers) {
				if(u.getPseudo().equals(username)) {
					result = false;
				}
			}
		}
		if (result) {
			this.pseudo = username;
			Controller.send_username_chosen(username);
		}
		return result;
	}

	public boolean changeUsername(String new_pseudo) {
		boolean result = true;
		String old_pseudo = this.pseudo;
		for( User u : Controller.get_list_connected_users()) {
			if(u.getPseudo().equals(new_pseudo)) {
				result = false;
			}
		}
		if (result) {
			this.pseudo = new_pseudo;
			Controller.send_username_changed(old_pseudo, new_pseudo);
		}
		return result;
	}
	
	public String get_pseudo() {
		return this.pseudo;
	}
	
	public String get_address() {
		return this.address;
	}
	
	public void set_address(String addr) {
		this.address = addr;
	}
	
    public boolean get_connected() {
		return this.connected;
	}
	
	public void set_connected(boolean con) {
		this.connected = con;
	}
}
