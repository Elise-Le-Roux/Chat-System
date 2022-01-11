package networkTest;

import network.*;
import java.net.*;
import java.sql.Date;

import controller.TCPMessage;
import database.DBManager;

public class TcpTest {

	public static void main(String[] args) throws InterruptedException {
		TcpServerSocket UserA = new TcpServerSocket(5310); // User A
		TcpServerSocket UserB = new TcpServerSocket(6000); // User B
		String hostnameAB;
		try {
			DBManager DB = new DBManager();
			DB.connect();
			DB.drop();
			DB.init();
			hostnameAB = InetAddress.getLocalHost().getHostName(); // Same hostname because we test on the same computer
			InetAddress addressAB = InetAddress.getLocalHost();
			UserA.connect(hostnameAB, 6000); // Demand of connection from User A to User B
			Thread.sleep(1000);
			TcpSocket socketA = UserA.getConnections().get(hostnameAB); 
			TcpSocket socketB = UserB.getConnections().get("localhost");
			System.out.println("Connections A = " + UserA.getConnections());
			System.out.println("Connections B = " + UserB.getConnections());
			System.out.println("Hostname = " + hostnameAB);
			Date date = new Date(System.currentTimeMillis());
			socketA.send_msg(new TCPMessage(addressAB,addressAB,"hello",date,true));
			socketB.send_msg(new TCPMessage(addressAB,addressAB,"helloword",date,true));
			System.out.println("\n");
			DBManager.afficher_BDD();
			
		} catch (UnknownHostException e) {
			System.out.println("Hostname exception: " + e.getMessage());
		} 
		
		
	}

}