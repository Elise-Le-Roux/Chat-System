package controller;

import java.util.ArrayList;

public class ConnectedUsers {
	
	static ArrayList<User> ConnectedUsers = new ArrayList<User>();
	
	public static void addUser(String pseudo, String hostname) {
		ConnectedUsers.add(new User(pseudo,hostname));
	}
	
	public static void removeUser(String pseudo) {
		for (User u : ConnectedUsers) {
			if (u.getPseudo().equals(pseudo)) {
				ConnectedUsers.remove(u);
			}
		}
	}
	
	public static ArrayList<User> getConnectedUsers() {
		return ConnectedUsers;
	}
	
}
