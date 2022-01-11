package controller;

public class User {
	
	String pseudo;
	String hostAddress;
	
	public User (String pseudo, String hostname) {
		this.pseudo = pseudo;
		this.hostAddress = hostname;
	}
	
	public String getPseudo () {
		return this.pseudo;
	}
	
	public String getHostAddress () {
		return this.hostAddress;
	}
	
	public void setPseudo (String pseudo) {
		this.pseudo = pseudo;
	}
}
