package network;

import java.io.*;
import java.net.*;

import controller.Controller;
import model.DBManager;
import network.TCPMessage.TypeNextMessage;
import view.Window;

public class TcpSocket extends Thread {
	
	Socket socketOfServer;
	
	 // to stop the thread
	 static volatile boolean exit = false;
    
    ObjectInputStream is;
    ObjectOutputStream os;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    TypeNextMessage msgType;

    public TcpSocket(Socket socketOfServer) {
    	this.socketOfServer = socketOfServer;
    	exit = false;
    	try {
    		this.os = new ObjectOutputStream(socketOfServer.getOutputStream());
    		this.is = new ObjectInputStream(socketOfServer.getInputStream());
    		this.dataInputStream = new DataInputStream(socketOfServer.getInputStream());
            this.dataOutputStream = new DataOutputStream(socketOfServer.getOutputStream());
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
			msgType = TypeNextMessage.TEXT;
			String fileName = null;
			//msg = (TCPMessage) is.readObject();

			while (!exit) {
				if (msgType.equals(TypeNextMessage.TEXT)) {
					msg = (TCPMessage) is.readObject();
					if(msg.getTypeNextMessage().equals(TypeNextMessage.TEXT)) {
						DB.insert(msg.getSender().getHostAddress(), msg.getReceiver().getHostAddress(), msg.getContent(), msg.getTime(), "TEXT");
					}
					msgType = msg.getTypeNextMessage();
					if (msgType.equals(TypeNextMessage.FILE)) {
						String path = System.getProperty("user.dir") + "/" + msg.getContent();
						DB.insert(msg.getSender().getHostAddress(), msg.getReceiver().getHostAddress(), path, msg.getTime(), "FILE");
						fileName = msg.getContent();
					}
					Window.messages.setContent(Window.getAdressee()); // a changer
					Controller.set_msg_unread(msg.getSender().getHostAddress());
				}
				else if (msgType.equals(TypeNextMessage.FILE)){
					int bytes = 0;
			        FileOutputStream fileOutputStream = new FileOutputStream(fileName); // A changer
			        long size = dataInputStream.readLong();     // read file size
			        byte[] buffer = new byte[4*1024];
			        while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
			            fileOutputStream.write(buffer,0,bytes);
			            size -= bytes;      // read up to file size
			        }
			        fileOutputStream.close();
					msgType = TypeNextMessage.TEXT;
				}
			}

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

		} catch (IOException e) { // raised when is.readObject is null
			try {
				socketOfServer.close(); 
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			System.out.println("class TCPMessage not found " + e.getMessage());
		}
	}

	public void send_msg(TCPMessage msg) {
		try {
			
			os.writeObject(msg);
			if(!msg.getReceiver().getHostAddress().equals(Controller.get_ip_address())) { // prevent from adding 2 times the same message in the database when we send a message to ourselves
				DBManager DB = new DBManager();
				DB.connect();
				DB.insert(msg.getSender().getHostAddress(), msg.getReceiver().getHostAddress(), msg.getContent(), msg.getTime(), msg.getTypeNextMessage().toString());
			}
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
	
	public void sendFile(TCPMessage msg, String path) throws Exception{
		//send_msg(msg); // with TypeNextMessage.FILE
		try {
			
			os.writeObject(msg);
			System.out.println(msg.getSender().getHostAddress() + "     " + Controller.get_ip_address());
			if(!msg.getReceiver().getHostAddress().equals(Controller.get_ip_address())) { // prevent from adding 2 times the same message in the database when we send a message to ourselves
				DBManager DB = new DBManager();
				DB.connect();
				DB.insert(msg.getSender().getHostAddress(), msg.getReceiver().getHostAddress(), path, msg.getTime(), msg.getTypeNextMessage().toString());
			}
		} catch (IOException e) {
			System.out.println("Output exception: " + e.getMessage());
		}
		
		
		
        int bytes = 0;
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);
        
        // send file size
        dataOutputStream.writeLong(file.length());  
        // break file into chunks
        byte[] buffer = new byte[4*1024];
        while ((bytes=fileInputStream.read(buffer))!=-1){
            dataOutputStream.write(buffer,0,bytes);
            dataOutputStream.flush();
        }
        fileInputStream.close();
    }
}
