package controller;

import java.io.Serializable;
import java.net.InetAddress;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class TCPMessage implements Serializable{
	InetAddress from; 
	InetAddress to;
	String content;
	Date time;// a enlever
	public static enum TypeNextMessage {FILE, TEXT};
	TypeNextMessage typeNextMessage;
	
	public TCPMessage(InetAddress from, InetAddress to, String content, Date time, TypeNextMessage type) {
		this.from = from;
		this.to = to;
		this.content = content;
		this.time = time;
		this.typeNextMessage = type;
	}
	
	public InetAddress getSender() {
		return this.from;
	}
	public InetAddress getReceiver() {
		return this.to;
	}
	public String getContent() {
		return this.content;
	}
	public Date getTime() {
		return this.time;
	}
	public TypeNextMessage getTypeNextMessage() {
		return this.typeNextMessage;
	}
	
	public String afficherMsg() {
		String from1 = this.getSender().getHostAddress();
		String to1 = this.getReceiver().getHostAddress();
		String content1 = this.getContent();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String time1 = formatter.format(this.getTime());
		if(this.getTypeNextMessage().equals(TypeNextMessage.TEXT)) {
			return ("From: " + from1 + ", To: " + to1 + ", Time: " + time1 + "\nContent: " + content1 + "\n\n");
		}
		else { // Type file
			if( from1.equals(Controller.get_address()) & !from1.equals(to1)) {
				return ("From: " + from1 + ", To: " + to1 + ", Time: " + time1 + "\nFile sent: " + content1 + "\n\n");
			}
			else {
				return ("From: " + from1 + ", To: " + to1 + ", Time: " + time1 + "\nFile saved at: " + content1 + "\n\n");
			}
		}
	}
}
