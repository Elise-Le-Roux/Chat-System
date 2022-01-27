package model;

public class User {
	
	String pseudo;
	String hostAddress;
	boolean connected;
	boolean unread_msg;
	
	public User (String pseudo, String address, boolean connected, boolean unread_msg) {
		this.pseudo = pseudo;
		this.hostAddress = address;
		this.connected = connected;
		this.unread_msg = unread_msg;
	}
	
	// GETTERS
	
	public String getPseudo () {
		return this.pseudo;
	}
	
	public String getHostAddress () {
		return this.hostAddress;
	}
	
	public boolean getStatus () {
		return this.connected;
	}
	
	public boolean getUnreadMsg () {
		return this.unread_msg;
	}
	
	// SETTERS
	public void setPseudo (String pseudo) {
		this.pseudo = pseudo;
	}
	
	public void setStatus(boolean conn) {
		this.connected = conn;
	}
	
	public void setUnreadMsg(boolean unread_msg) {
		this.unread_msg = unread_msg;
	}
}
