package network;

import java.io.*;
import java.net.*;
import java.sql.Date;
import java.util.Arrays;
import java.util.Enumeration;

import controller.ConnectedUsers;
import controller.Message;
import controller.Message.typeMessage;

public class UDPSocket extends Thread{
	
	DatagramSocket dgramSocket; 
	int port = 5000;
	byte[] bufferIN = new byte[1000]; // buffer for incoming data
	byte[] bufferOUT = new byte[1000]; // buffer for outcoming data
	DatagramPacket inPacket = new DatagramPacket(bufferIN, bufferIN.length); // DatagramPacket object for the incoming datagram
	
	public UDPSocket () {
		try {
			this.dgramSocket = new DatagramSocket(this.port);
			start();
		} catch (SocketException e) {
			System.out.println("DatagramSocket exception: " + e.getMessage());
		}
	}
	
	public void run() {
		while (true) {
			try {
				dgramSocket.receive(inPacket);
				String hostname = inPacket.getAddress().getHostName();
				InetAddress addr = inPacket.getAddress();
				ByteArrayInputStream bais = new ByteArrayInputStream(bufferIN);
			    ObjectInputStream ois = new ObjectInputStream(bais);
			    
			    Message msg = (Message)ois.readObject();
			    
				String pseudo = msg.getSender();
				
				if (msg.getType() == typeMessage.CONNECTED) {
					ConnectedUsers.addUser(pseudo,hostname);
				}
				else if (msg.getType() == typeMessage.DISCONNECTED){
					ConnectedUsers.removeUser(pseudo);
				}
				else if (msg.getType() == typeMessage.GET_CONNECTED_USER){
					ConnectedUsers.addUser(pseudo,hostname);
					send_connected("", pseudo, addr); // changer "" avec notre pseudo et le port
				}
				else {
					System.out.println("Message not recognized");
				}
				
			} catch (IOException e) {
				System.out.println("Input exception: " + e.getMessage());
			} catch (ClassNotFoundException e) {
				System.out.println("Message exception: " + e.getMessage());
			}
		}
	}

	// send broadcast msg to retrieve the list of connected users and notify users that we are now connected
	private void send_msg(Message message, InetAddress addr) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(message);
			oos.flush();
			
			//get the byte array of the object
			bufferOUT = baos.toByteArray();

			DatagramPacket outPacket = new DatagramPacket(bufferOUT, bufferOUT.length, addr, port);
			dgramSocket.send(outPacket);
		} catch (SocketException e) {
			System.out.println("setBroadcast exception: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("send exception: " + e.getMessage());
		}
	}
	
	private void send_broadcast(Message message) {
		send_msg(message, UDPSocket.getBroadcastAddress());
	}
	
	private void send_unicast(Message message, InetAddress addr) {
		send_msg(message, addr);
	}

	// Broadcast
	public void get_connected_users(String pseudo) {
		Date date = new Date(System.currentTimeMillis());
		Message message = new Message(pseudo, "broadcast", "", date, typeMessage.GET_CONNECTED_USER); 
		send_broadcast(message);
	}

	// Unicast in response to broadcast get_connected_users
	public void send_connected(String from, String to, InetAddress addr) { 
		Date date = new Date(System.currentTimeMillis());
		Message message = new Message(from, to, "", date, typeMessage.CONNECTED); 
		send_unicast(message, addr);
	}

	// Broadcast : Notify all users that we are disconnected
	public void send_disconnected(int port, String pseudo) {
		Date date = new Date(System.currentTimeMillis());
		Message message = new Message(pseudo, "broadcast", "", date, typeMessage.DISCONNECTED); 
		send_broadcast(message);
	}




	public static InetAddress getBroadcastAddress() { // exception a la place de retourner null !
		InetAddress broadcastAddress = null;
		try {
			
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

			while(interfaces.hasMoreElements()) {
				
				NetworkInterface ni = interfaces.nextElement();
				
				if ( !ni.isLoopback() || ni.isUp()) {
					for (InterfaceAddress ia : ni.getInterfaceAddresses()) {
						if (ia.getBroadcast() != null) {
							broadcastAddress = ia.getBroadcast();
							break;
						}
					}
				}
			}

		} catch (SocketException e) {
			System.out.println("getBroadcastAddress exception: " + e.getMessage());
		}
		return broadcastAddress;
	}
}
