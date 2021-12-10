package network;

import java.io.*;
import java.net.*;

public class TcpSocket extends Thread {
	
	Socket socketOfServer;
	
	public TcpSocket(Socket socketOfServer) {
		this.socketOfServer = socketOfServer;
		start();
	}
	
	public void run() {
		try {
			String msg;
			BufferedReader in;
			in = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));

			msg = in.readLine();
			
			while (msg != null){
			System.out.println("Message received : " + msg + "\n"); // Read the messages
			msg = in.readLine();
			}
			
			socketOfServer.close();
			in.close();

		} catch (IOException e) {
			System.out.println("Input exception: " + e.getMessage());
		}
	}
	
	public void send_msg(String msg) {
		try {
			PrintWriter out = new PrintWriter(socketOfServer.getOutputStream());
			out.println(msg);
			out.flush();
		} catch (IOException e) {
			System.out.println("Output exception: " + e.getMessage());
		}
	}
}
