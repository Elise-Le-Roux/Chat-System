package controller;

public class User {
	
	String pseudo;
	String hostAddress;
	boolean connected;
	
	public User (String pseudo, String address, boolean connected) {
		this.pseudo = pseudo;
		this.hostAddress = address;
		this.connected = connected;
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
	
	public void setStatus(boolean conn) {
		this.connected = conn;
	}
	
	public boolean getStatus () {
		return this.connected;
	}
}
