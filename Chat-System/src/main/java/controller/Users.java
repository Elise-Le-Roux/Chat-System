package controller;

import java.util.ArrayList;
import database.DBManager;

public class Users {
	
	ArrayList<User> listUsers = new ArrayList<User>();
	
	public Users(ArrayList<User> users) {
		this.listUsers = users;
	}
	
	public void addConnectedUser(String pseudo, String hostAddress) {
		boolean exists = false;
		
		if (listUsers != null && !listUsers.isEmpty()) {
			for (User u : listUsers) {
				if (u.getHostAddress().equals(hostAddress)) {
					exists = true;
				}
			}
		}
		
		if (exists) {
			this.changeStatus(hostAddress, true);
			this.changePseudo(hostAddress, pseudo);
		}
		else {
			listUsers.add(new User(pseudo,hostAddress,true));
			DBManager DB = new DBManager();
			DB.connect();
			DB.add_new_user(pseudo, hostAddress);
		}
	}
	
	public void removeUser(String pseudo) { // Pas besoin  ????
		User usr = null;
		for (User u : listUsers) {
			if (u.getPseudo().equals(pseudo)) {
				usr = u;
			}
		}
		listUsers.remove(usr);
	}
	
	public void changePseudo(String hostAddress, String new_pseudo) {
		for (User u : listUsers) {
			if (u.getHostAddress().equals(hostAddress)) {
				u.setPseudo(new_pseudo);
				DBManager DB = new DBManager();
				DB.connect();
				DB.change_pseudo(new_pseudo, hostAddress);
			}
		}
	}
	
	public void changeStatus(String hostAddress, boolean status) {
		for (User u : listUsers) {
			if (u.getHostAddress().equals(hostAddress)) {
				u.setStatus(status);
			}
		}
	}
	
	public ArrayList<User> getConnectedUsers() {
		ArrayList<User> connectedUsers = new ArrayList<User>();
		for(User u : listUsers) {
			if(u.getStatus()) {
				connectedUsers.add(u);
			};
		}
		return connectedUsers;
	}
	
	public ArrayList<User> getDisconnectedUsers() {
		ArrayList<User> disconnectedUsers = new ArrayList<User>();
		for(User u : listUsers) {
			if(!u.getStatus()) {
				disconnectedUsers.add(u);
			};
		}
		return disconnectedUsers;
	}
	
	public ArrayList<User> getUsers() {
		return listUsers;
	}
	
	public String getHostAddress(String pseudo) throws Exception {
		String result = "";
		boolean exists = false;
		for (User u : listUsers) {
			if (u.getPseudo().equals(pseudo)) {
				result = u.getHostAddress();
				exists = true;
			}
		}
		if(!exists) {throw new Exception("Utilisateur non présent dans la liste des utilisateurs connectés");}
		return result;
	}
	
	public boolean isConnected(String pseudo) {
		boolean result = false;
		for (User u : listUsers) {
			if (u.getPseudo().equals(pseudo)) {
				result = u.getStatus();
			}
		}
		return result;
	}
}
