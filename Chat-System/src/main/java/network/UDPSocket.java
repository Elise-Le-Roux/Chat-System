package network;

import java.io.*;
import java.net.*;
import java.sql.Date;
import java.util.Arrays;
import java.util.Enumeration;

import controller.ConnectedUsers;
import controller.UDPMessage;
import controller.UDPMessage.typeMessage;

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
			    
			    UDPMessage msg = (UDPMessage)ois.readObject();
			    
				String pseudo = msg.getSender();
				
				if (msg.getType() == typeMessage.CONNECTED) {
					ConnectedUsers.addUser(pseudo,hostname);
				}
				else if (msg.getType() == typeMessage.DISCONNECTED){
					ConnectedUsers.removeUser(pseudo);
				}
				else if (msg.getType() == typeMessage.GET_CONNECTED_USER){
					ConnectedUsers.addUser(pseudo,hostname);
					send_connected("", pseudo, addr); // changer "" avec notre pseudo
				}
				else if (msg.getType() == typeMessage.PSEUDOCHANGED){
					ConnectedUsers.changePseudo(pseudo, msg.getNewPseudo());
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
	private void send_msg(UDPMessage message, InetAddress addr) {
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
	
	private void send_broadcast(UDPMessage message) {
		send_msg(message, UDPSocket.getBroadcastAddress());
	}
	
	private void send_unicast(UDPMessage message, InetAddress addr) {
		send_msg(message, addr);
	}

	// Broadcast
	public void get_connected_users(String pseudo) {
		UDPMessage message = new UDPMessage(pseudo, "broadcast", "", typeMessage.GET_CONNECTED_USER); 
		send_broadcast(message);
	}

	// Unicast in response to broadcast get_connected_users
	public void send_connected(String from, String to, InetAddress addr) { 
		UDPMessage message = new UDPMessage(from, to, "", typeMessage.CONNECTED); 
		send_unicast(message, addr);
	}

	// Broadcast : Notify all users that we are disconnected
	public void send_disconnected(int port, String pseudo) {
		UDPMessage message = new UDPMessage(pseudo, "broadcast", "", typeMessage.DISCONNECTED); 
		send_broadcast(message);
	}

	public void send_username_changed(String old_pseudo, String new_pseudo) {
		UDPMessage message = new UDPMessage(old_pseudo, "broadcast", new_pseudo, typeMessage.PSEUDOCHANGED); 
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
