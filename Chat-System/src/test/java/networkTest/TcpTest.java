package networkTest;

import network.*;
import java.net.*;

public class TcpTest {

	public static void main(String[] args) throws InterruptedException {
		TcpServerSocket UserA = new TcpServerSocket(5310); // User A
		TcpServerSocket UserB = new TcpServerSocket(6000); // User B
		String hostnameAB;
		try {
			hostnameAB = InetAddress.getLocalHost().getHostName(); // Same hostname because we test on the same computer
			UserA.connect(hostnameAB, 6000); // Demand of connection from User A to User B
			Thread.sleep(1000);
			TcpSocket socketA = UserA.getConnections().get(hostnameAB); 
			TcpSocket socketB = UserB.getConnections().get("localhost");
			System.out.println("Connections A = " + UserA.getConnections());
			System.out.println("Connections B = " + UserB.getConnections());
			System.out.println("Hostname = " + hostnameAB);
			socketA.send_msg("hello");
			socketB.send_msg("hello world");
		} catch (UnknownHostException e) {
			System.out.println("Hostname exception: " + e.getMessage());
		} 
		
		
	}

}
