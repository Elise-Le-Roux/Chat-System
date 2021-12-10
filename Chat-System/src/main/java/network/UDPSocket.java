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
	byte[] buffer = new byte[256]; // buffer for incoming data
	DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length); // DatagramPacket object for the incoming datagram
	
	public UDPSocket (int port) {
		try {
			this.dgramSocket = new DatagramSocket(port);
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
				
				ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
			    ObjectInputStream ois = new ObjectInputStream(bais);
			    Message msg = (Message)ois.readObject();
				
				String pseudo = msg.getSender();
				if (msg.getType() == typeMessage.CONNECTED) {
					ConnectedUsers.addUser(pseudo,hostname);
				}
				else if (msg.getType() == typeMessage.DISCONNECTED){
					ConnectedUsers.removeUser(pseudo);
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
	public void send_broadcast(int port, Message message) {
		try {
			dgramSocket.setBroadcast(true);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(message);
			oos.flush();

			// get the byte array of the object
			byte[] buf = baos.toByteArray();

			DatagramPacket outPacket = new DatagramPacket(buf, buf.length, UDPSocket.getBroadcastAddress(), port);
			dgramSocket.send(outPacket);
		} catch (SocketException e) {
			System.out.println("setBroadcast exception: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("send exception: " + e.getMessage());
		}
	}

	public void send_connected(int port, String pseudo) {
		Date date = new Date(System.currentTimeMillis());
		Message message = new Message(pseudo, "broadcast", pseudo, date, typeMessage.CONNECTED); 
		send_broadcast(port, message);
	}
	
	public void send_disconnected(int port, String pseudo) {
		Date date = new Date(System.currentTimeMillis());
		Message message = new Message(pseudo, "broadcast", pseudo, date, typeMessage.DISCONNECTED); 
		send_broadcast(port, message);
	}
	

	public static InetAddress getBroadcastAddress() { // exception a la place de null
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
