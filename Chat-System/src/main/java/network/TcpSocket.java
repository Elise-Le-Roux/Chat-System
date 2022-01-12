package network;

import java.io.*;
import java.net.*;

import GUI.Window;
import controller.TCPMessage;
import controller.specificUser;
import database.DBManager;

public class TcpSocket extends Thread {
	
	Socket socketOfServer;
	
	 // to stop the thread
    private boolean exit;
    
    ObjectInputStream is;
    ObjectOutputStream os;

	public TcpSocket(Socket socketOfServer) {
		this.socketOfServer = socketOfServer;
		this.exit = false;
		try {
			this.os = new ObjectOutputStream(socketOfServer.getOutputStream());
			this.is = new ObjectInputStream(socketOfServer.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		start();
	}

	// for stopping the thread
	public void kill() {
		exit = true;
	}

	public void run() {
		try {
			DBManager DB = new DBManager();
			DB.connect();
			TCPMessage msg;
			msg = (TCPMessage) is.readObject();

			while (!exit & msg != null) {
				System.out.println("TEST");
				DB.insert(msg.getSender().getHostAddress(), msg.getReceiver().getHostAddress(), msg.getContent(), msg.getTime());
				Window.messages.setContent(Window.getAdressee()); // a changer
				msg = (TCPMessage) is.readObject();
			}


				  //msg = (TCPMessage) is.readObject();
			  System.out.println("close " + socketOfServer.toString());
			  socketOfServer.close();
			  //is.close();
			
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
			System.out.println("Input exception TcpSocket: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("class TCPMessage not found " + e.getMessage());
		}
	}

	public void send_msg(TCPMessage msg) {
		try {
			
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
