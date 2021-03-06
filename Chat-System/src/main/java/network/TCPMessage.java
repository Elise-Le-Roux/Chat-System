package network;

import java.io.Serializable;
import java.net.InetAddress;
import java.sql.Date;
import java.text.SimpleDateFormat;

import controller.Controller;

public class TCPMessage implements Serializable{
	
	private static final long serialVersionUID = 4120062234428174237L;
	
	// ATTRIBUTES
	InetAddress from; 
	InetAddress to;
	String content;
	Date time;
	public static enum TypeNextMessage {FILE, TEXT};
	TypeNextMessage typeNextMessage;
	
	public TCPMessage(InetAddress from, InetAddress to, String content, Date time, TypeNextMessage type) {
		this.from = from;
		this.to = to;
		this.content = content;
		this.time = time;
		this.typeNextMessage = type;
	}
	
	// GETTERS
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
	
	// METHODS
	public String afficherMsg() {
		String from = Controller.get_user_pseudo(this.getSender().getHostAddress());
		String to = Controller.get_user_pseudo(this.getReceiver().getHostAddress());
		String from1 = this.getSender().getHostAddress();
		String to1 = this.getReceiver().getHostAddress();
		String content1 = this.getContent();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String time1 = formatter.format(this.getTime());
		if(this.getTypeNextMessage().equals(TypeNextMessage.TEXT)) {
			return ("From: " + from + ", To: " + to + ", Time: " + time1 + "\nContent: " + content1 + "\n\n");
		}
		else { // Type file
			if( from1.equals(controller.Controller.get_ip_address()) & !from1.equals(to1)) {
				return ("From: " + from + ", To: " + to + ", Time: " + time1 + "\nFile sent: " + content1 + "\n\n");
			}
			else {
				return ("From: " + from + ", To: " + to + ", Time: " + time1 + "\nFile saved at: " + content1 + "\n\n");
			}
		}
	}
}
