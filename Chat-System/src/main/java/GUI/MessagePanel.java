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

import controller.Users;
import controller.Controller;
import controller.TCPMessage;
import controller.specificUser;
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
		list_msg = DB.select_conv(Controller.get_address(),Controller.get_host_address(pseudo)); 
		textArea.selectAll();
		textArea.replaceSelection("");
		for(TCPMessage msg : list_msg) {
			textArea.append(msg.afficherMsg());
		}
	}
}
