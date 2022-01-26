package network;

import java.io.*;
import java.net.*;
import java.util.Enumeration;

import controller.Controller;
import network.UDPMessage.typeMessage;

public class UDPSocket extends Thread{
	
	static DatagramSocket dgramSocket;
	static private int port;
	byte[] bufferIN = new byte[1500]; // buffer for incoming data
	static byte[] bufferOUT = new byte[1500]; // buffer for outcoming data
	DatagramPacket inPacket = new DatagramPacket(bufferIN, bufferIN.length); // DatagramPacket object for the incoming datagram

	// to stop the thread
	static volatile boolean exit = false;

	public UDPSocket (int prt) {
		try {
			port = prt;
			dgramSocket = new DatagramSocket(port);
			start();
		} catch (SocketException e) {
			System.out.println("DatagramSocket exception: " + e.getMessage());
		}
	}

	// for stopping the thread
	public void kill() {
		exit = true;
	}

	public void run() {
		while (!exit) {
			try {
				dgramSocket.receive(inPacket);
				String hostAddress = inPacket.getAddress().getHostAddress();
				InetAddress addr = inPacket.getAddress();
				ByteArrayInputStream bais = new ByteArrayInputStream(bufferIN);
			    ObjectInputStream ois = new ObjectInputStream(bais);
			    
			    UDPMessage msg = (UDPMessage)ois.readObject();
			    
				String pseudo = msg.getPseudo();
				
				if (msg.getType() == typeMessage.CONNECTED) {
					Controller.add_connected_user(pseudo,hostAddress);
				}
				else if (msg.getType() == typeMessage.DISCONNECTED){
					Controller.set_disconnected_user(hostAddress);
				}
				else if (msg.getType() == typeMessage.GET_CONNECTED_USERS){
					if(Controller.get_pseudo() != null) { // si j'ai choisi mon pseudo
						send_connected(Controller.get_pseudo(), addr);
					}
				}
				else if (msg.getType() == typeMessage.PSEUDOCHANGED){
					Controller.change_pseudo_connected_user(hostAddress,pseudo);
				}
				else if (msg.getType() == typeMessage.PSEUDOCHOSEN){
					Controller.add_connected_user(pseudo,hostAddress);
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

	
	private static void send_msg(UDPMessage message, InetAddress addr) {
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
	public void get_connected_users() {
		UDPMessage message = new UDPMessage(typeMessage.GET_CONNECTED_USERS); 
		send_broadcast(message);
	}

	// Unicast in response to broadcast get_connected_users
	public void send_connected(String pseudo, InetAddress addr) { 
		UDPMessage message = new UDPMessage(typeMessage.CONNECTED, pseudo); 
		send_unicast(message, addr);
	}

	// Broadcast : Notify all users that we are disconnected
	public void send_disconnected() {
		UDPMessage message = new UDPMessage(typeMessage.DISCONNECTED); 
		send_broadcast(message);
	}

	// Broadcast : Notify all users that our pseudo has changed
	public void send_username_changed(String pseudo) {
		UDPMessage message = new UDPMessage(typeMessage.PSEUDOCHANGED, pseudo); 
		send_broadcast(message);
	}
	
	public void send_chosen_pseudo(String pseudo) {
		UDPMessage message = new UDPMessage(typeMessage.PSEUDOCHOSEN, pseudo); 
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
	
	public static InetAddress getLocalAddr() { 
		InetAddress localAddress = null;
		try {
			
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

			while(interfaces.hasMoreElements()) {
				
				NetworkInterface ni = interfaces.nextElement();
                 
                 Enumeration<InetAddress> a = ni.getInetAddresses();
                 for (; a.hasMoreElements();)
                 {
                	 InetAddress ia= (InetAddress) a.nextElement();
                     if (!ia.isLinkLocalAddress() 
                      && !ia.isLoopbackAddress()
                      && ia instanceof Inet4Address) {
                         return ia;
                     }
                 }
			}

		} catch (SocketException e) {
			System.out.println("getLocalAddr exception: " + e.getMessage());
		}
		return localAddress;
	}
	
	public void close() {
		dgramSocket.close();
	}
}
