package controller;

import java.util.ArrayList;

public class ConnectedUsers {
	
	ArrayList<User> ConnectedUsers = new ArrayList<User>();
	
	public void addUser(String pseudo, String hostAddress) {
		boolean exists = false;
		if (!ConnectedUsers.isEmpty()) {
			for (User u : ConnectedUsers) {
				System.out.println(u.getHostAddress());
				System.out.println(u.getPseudo());
				if (u.getPseudo().equals(pseudo)) {
					exists = true;
				}
			}
		}
		if (!exists) {
			ConnectedUsers.add(new User(pseudo,hostAddress));
		}
	}
	
	public void removeUser(String pseudo) {
		User usr = null;
		for (User u : ConnectedUsers) {
			if (u.getPseudo().equals(pseudo)) {
				usr = u;
			}
		}
		ConnectedUsers.remove(usr);
	}
	
	public void changePseudo(String old_pseudo, String new_pseudo) {
		for (User u : ConnectedUsers) {
			if (u.getPseudo().equals(old_pseudo)) {
				u.setPseudo(new_pseudo);
			}
		}
	}
	
	
	public ArrayList<User> getConnectedUsers() {
		return ConnectedUsers;
	}
	
	public String getHostAddress(String pseudo) throws Exception {
		String result = "";
		boolean exists = false;
		for (User u : ConnectedUsers) {
			if (u.getPseudo().equals(pseudo)) {
				result = u.getHostAddress();
				exists = true;
			}
		}
		if(!exists) {throw new Exception("Utilisateur non présent dans la liste des utilisateurs connectés");}
		return result;
	}
	
}
