package networkTest;

import java.net.InetAddress;

import database.Users;
import network.UDPSocket;

public class UdpTest {

	public static void main(String[] args) throws InterruptedException {
		// Test getBroadcastAddress()
		InetAddress addr = UDPSocket.getBroadcastAddress();
		System.out.println("Broadcast address = " + addr.toString());
		
		// Test envoi message broadcast connected/deconnected
		UDPSocket UserA = new UDPSocket(4698); // User A
		
		Thread.sleep(1000);
		
		UserA.get_connected_users();
		Thread.sleep(1000);
		//UserA.send_disconnected(5000, "User A");
		//Thread.sleep(1000);.
		//System.out.println("ConnectedUsers = " + ConnectedUsers.getConnectedUsers());
		
	}
}