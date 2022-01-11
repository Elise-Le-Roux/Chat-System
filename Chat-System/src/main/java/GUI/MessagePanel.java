package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import controller.ConnectedUsers;
import controller.TCPMessage;
import database.DBManager;

//Messagebox to display the history messages with current user 
public class MessagePanel extends JPanel {
	
	private JTextArea textArea;
	
	public MessagePanel() {
    	super(new BorderLayout());
    	
    	textArea = new JTextArea(5, 20);
    	JScrollPane scrollPane = new JScrollPane(textArea); 
    	textArea.setEditable(false);
    	
    	add(scrollPane);

	}
	
	public void setContent(String pseudo) {
		DBManager DB = new DBManager();
		DB.connect();
		ArrayList<TCPMessage> list_msg = null;
		try {
			list_msg = DB.select_conv(InetAddress.getLocalHost().getHostAddress(),ConnectedUsers.getHostAddress(pseudo));
		} catch (UnknownHostException e) {
			System.out.println("Exception InetAddress.getLocalHost().getHostAddress() ");
		} catch (Exception e) {
			System.out.println("Exception ConnectedUsers.getHostname ");
		}
		textArea.selectAll();
		textArea.replaceSelection("");
		for(TCPMessage msg : list_msg) {
			textArea.append(msg.afficherMsg());
		}
	}
}
