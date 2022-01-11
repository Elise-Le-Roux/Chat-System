package controller;

import java.io.Serializable;
import java.sql.Date;

public class UDPMessage implements Serializable{
	String from; 
	String to;
	String new_pseudo;
	public static enum typeMessage {GET_CONNECTED_USER, CONNECTED, DISCONNECTED, PSEUDOCHANGED, PSEUDOCHOSEN};
	typeMessage type;

	public UDPMessage(String from, String to, String new_pseudo, typeMessage type) {
		this.from = from;
		this.to = to;
		this.new_pseudo = new_pseudo;
		this.type = type;
	}
	
	public typeMessage getType () {
		return this.type;
	}
	
	public String getSender() {
		return this.from;
	}
	
	public String getNewPseudo() {
		return this.new_pseudo;
	}
}
