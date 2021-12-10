package controller;

import java.util.ArrayList;

public class ConnectedUsers {
	
	static ArrayList<User> ConnectedUsers = new ArrayList<User>();
	
	public static void addUser(String pseudo, String hostname) {
		ConnectedUsers.add(new User(pseudo,hostname));
	}
	
	public static void removeUser(String pseudo) {
		for (User u : ConnectedUsers) {
			if (pseudo == u.getPseudo()) {
				ConnectedUsers.remove(u);
			}
		}
	}
	
	
}
