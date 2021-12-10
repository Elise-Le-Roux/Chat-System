package controller;

import java.io.Serializable;
import java.sql.Date;

public class Message implements Serializable{
	String from; 
	String to;
	String content;
	Date time;
	public static enum typeMessage {TCP, GET_CONNECTED_USER, CONNECTED, DISCONNECTED, USERNAMECHANGED};
	typeMessage type;

	public Message(String from, String to, String content, Date time, typeMessage type) {
		this.from = from;
		this.to = to;
		this.content = content;
		this.time = time;
		this.type = type;
	}
	
	public typeMessage getType () {
		return this.type;
	}
	
	public String getSender() {
		return this.from;
	}
}
