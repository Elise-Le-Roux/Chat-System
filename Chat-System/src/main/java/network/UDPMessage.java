package network;

import java.io.Serializable;

public class UDPMessage implements Serializable{
	
	private static final long serialVersionUID = 8163688917457157270L;
	
	// ATTRIBUTES
	public static enum typeMessage {GET_CONNECTED_USERS, CONNECTED, DISCONNECTED, PSEUDOCHANGED, PSEUDOCHOSEN};
	typeMessage type;
	String pseudo;
	
	// CONSTRUCTORS
	public UDPMessage(typeMessage type) {
		this.type = type;
	}
	
	public UDPMessage(typeMessage type, String pseudo) {
		this.type = type;
		this.pseudo = pseudo;
	}
	
	// GETTERS
	public typeMessage getType () {
		return this.type;
	}
	
	public String getPseudo() {
		return this.pseudo;
	}
}
