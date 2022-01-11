package controller;

import java.io.Serializable;
import java.net.InetAddress;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class TCPMessage implements Serializable{
	InetAddress from; 
	InetAddress to;
	String content;
	Date time;
	boolean connected;
	
	public TCPMessage(InetAddress from, InetAddress to, String content, Date time, Boolean connected) {
		this.from = from;
		this.to = to;
		this.content = content;
		this.time = time;
		this.connected = connected;
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
	public Boolean getConnected() {
		return this.connected;
	}
	public void setConnected(boolean status) {
		this.connected = status;
	}
	public String afficherMsg() {
		String from1 = this.getSender().getHostAddress();
		String to1 = this.getReceiver().getHostAddress();
		String content1 = this.getContent();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String time1 = formatter.format(this.getTime());
		return ("From : " + from1 + ", To : " + to1 + ", Time : " + time1 + "\nContent : " + content1 + "\n\n");
	}
}
