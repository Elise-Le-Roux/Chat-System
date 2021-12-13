package controller;

import java.util.ArrayList;

public class ConnectedUsers {
	
	static ArrayList<User> ConnectedUsers = new ArrayList<User>();
	
	public static void addUser(String pseudo, String hostname) {
		boolean exists = false;
		for (User u : ConnectedUsers) {
			if (u.getPseudo().equals(pseudo)) {
				exists = true;
			}
		}
		if (!exists) {
			ConnectedUsers.add(new User(pseudo,hostname));
		}
	}
	
	public static void removeUser(String pseudo) {
		for (User u : ConnectedUsers) {
			if (u.getPseudo().equals(pseudo)) {
				ConnectedUsers.remove(u);
			}
		}
	}
	
	public static void changePseudo(String old_pseudo, String new_pseudo) {
		for (User u : ConnectedUsers) {
			if (u.getPseudo().equals(old_pseudo)) {
				u.setPseudo(new_pseudo);
			}
		}
	}
	
	public static ArrayList<User> getConnectedUsers() {
		return ConnectedUsers;
	}
	
}
