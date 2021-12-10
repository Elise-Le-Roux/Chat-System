package controller;

public class User {
	
	String pseudo;
	String hostname;
	
	public User (String pseudo, String hostname) {
		this.pseudo = pseudo;
		this.hostname = hostname;
	}
	
	public String getPseudo () {
		return this.pseudo;
	}
	
	public String getHostname () {
		return this.hostname;
	}
	
	public void setPseudo (String pseudo) {
		this.pseudo = pseudo;
	}
}
