package networkTest;

import java.net.InetAddress;

import network.UDPSocket;

public class UdpTest {

	public static void main(String[] args) {
		InetAddress addr = UDPSocket.getBroadcastAddress();
		System.out.println("Broadcast address = " + addr.toString());
	}
}