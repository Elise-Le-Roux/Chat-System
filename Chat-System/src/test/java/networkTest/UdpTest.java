package networkTest;

import java.net.InetAddress;

import controller.ConnectedUsers;
import network.TcpServerSocket;
import network.UDPSocket;

public class UdpTest {

	public static void main(String[] args) throws InterruptedException {
		// Test getBroadcastAddress()
		InetAddress addr = UDPSocket.getBroadcastAddress();
		System.out.println("Broadcast address = " + addr.toString());
		
		// Test envoi message broadcast connected/deconnected
		UDPSocket UserA = new UDPSocket(5000); // User A
		UDPSocket UserB = new UDPSocket(6000); // User B
		
		UserA.send_connected(6000, "User A");
		Thread.sleep(1000);
		System.out.println("ConnectedUsers = " + ConnectedUsers.getConnectedUsers());
		Thread.sleep(1000);
		UserA.send_disconnected(6000, "User A");
		Thread.sleep(1000);
		System.out.println("ConnectedUsers = " + ConnectedUsers.getConnectedUsers());
		
	}
}