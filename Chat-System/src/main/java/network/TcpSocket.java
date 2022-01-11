package network;

import java.io.*;
import java.net.*;

import controller.TCPMessage;
import database.DBManager;

public class TcpSocket extends Thread {
	
	Socket socketOfServer;
	
	public TcpSocket(Socket socketOfServer) {
		this.socketOfServer = socketOfServer;
		start();
	}
	
	public void run() {
		try {
			  DBManager DB = new DBManager();
			  DB.connect();
			  ObjectInputStream is = new ObjectInputStream(socketOfServer.getInputStream());

			  TCPMessage msg = (TCPMessage) is.readObject();
		      
			  while (msg.getConnected()){
				  DB.insert(msg.getSender().getHostAddress(), msg.getReceiver().getHostAddress(), msg.getContent(), msg.getTime());
				  msg = (TCPMessage) is.readObject();
			  }
			  socketOfServer.close();
			  is.close();
			
			/* TCPMessage msg;
			BufferedReader in;
			in = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));

			msg = in.readLine();
			
			while (msg != null){
			System.out.println("Message received : " + msg + "\n"); // Read the messages
			msg = in.readLine();
			}
			
			socketOfServer.close();
			in.close(); */

		} catch (IOException e) {
			System.out.println("Input exception: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("class TCPMessage not found " + e.getMessage());
		}
	}

	public void send_msg(TCPMessage msg) {
		try {
			ObjectOutputStream os = new ObjectOutputStream(socketOfServer.getOutputStream());
			os.writeObject(msg);
			DBManager DB = new DBManager();
			DB.connect();
			DB.insert(msg.getSender().getHostAddress(), msg.getReceiver().getHostAddress(), msg.getContent(), msg.getTime());
		} catch (IOException e) {
			System.out.println("Output exception: " + e.getMessage());
		}
		/* try {
			PrintWriter out = new PrintWriter(socketOfServer.getOutputStream());
			out.println(msg);
			out.flush();
		} catch (IOException e) {
			System.out.println("Output exception: " + e.getMessage());
		} */
	}
}