package model;

import java.util.ArrayList;

public class Users {

	ArrayList<User> listUsers = new ArrayList<User>();

	// CONSTRUCTOR

	public Users(ArrayList<User> users) {
		this.listUsers = users;
	}

	// GETTERS

	public synchronized ArrayList<User> getUsers() {
		return listUsers;
	}
	// Retourne l'ensemble des pseudos des utilisateurs
	public synchronized ArrayList<String> getPseudos() {
		ArrayList<String> pseudos = new ArrayList<String>();
		for(User u : listUsers) {
			pseudos.add(u.getPseudo());
		}
		return pseudos;
	}

	// Retourne l'adresse IP d'un utilisateur connaissant son pseudo
	public synchronized String getHostAddress(String pseudo) throws Exception {
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

	// Retourne le pseudo d'un utilisateur connaissant son addresse IP
	public synchronized String getHostPseudo(String hostAddress) throws Exception {
		String result = "";
		boolean exists = false;
		for (User u : listUsers) {
			if (u.getHostAddress().equals(hostAddress)) {
				result = u.getPseudo();
				exists = true;
			}
		}
		if(!exists) {throw new Exception("Utilisateur non présent dans la liste des utilisateurs connectés");}
		return result;
	}

	// Retourne l'état de connexion d'un utilisateur
	public synchronized boolean isConnected(String pseudo) {
		boolean result = false;
		for (User u : listUsers) {
			if (u.getPseudo().equals(pseudo)) {
				result = u.getStatus();
			}
		}
		return result;
	}

	// Retourne le booléen indiquant si un message est non lu avec un utilisateur
	public synchronized boolean getUnread(String pseudo) {
		boolean result = false;
		for (User u : listUsers) {
			if (u.getPseudo().equals(pseudo)) {
				result = u.getUnreadMsg();
			}
		}
		return result;
	}

	// SETTERS

	// Ajout d'un utilisateur connecté dans la liste
	public synchronized void addConnectedUser(String pseudo, String hostAddress) {
		boolean exists = false;
		User usr = null;

		if (listUsers != null && !listUsers.isEmpty()) {
			for (User u : listUsers) {
				if (u.getHostAddress().equals(hostAddress)) {
					exists = true;
					usr = u;
				}
			}
		}
		if (exists) {
			usr.setStatus(true);
			usr.setPseudo(pseudo);
		}
		else {
			listUsers.add(new User(pseudo,hostAddress,true,false));
		}
	}

	// Changement de pseudo d'un utilisateur de la liste
	public synchronized void changePseudo(String hostAddress, String new_pseudo) {
		for (User u : listUsers) {
			if (u.getHostAddress().equals(hostAddress)) {
				u.setPseudo(new_pseudo);
			}
		}
	}

	// Changement du statut de connexion d'un utilisateur de la liste
	public synchronized void changeStatus(String hostAddress, boolean status) {
		for (User u : listUsers) {
			if (u.getHostAddress().equals(hostAddress)) {
				u.setStatus(status);
			}
		}
	}

	// Changement du booléen indiquant si un message est non lu avec un utilisateur
	public synchronized void changeUnread(String hostAddress, boolean unread) {
		for (User u : listUsers) {
			if (u.getHostAddress().equals(hostAddress)) {
				u.setUnreadMsg(unread);
			}
		}
	}


}
