package GUI;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import controller.Controller;
import database.DBManager;
import network.TCPMessage;

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
		list_msg = DB.select_conv(Controller.get_ip_address(),Controller.get_host_address(pseudo)); 
		textArea.selectAll();
		textArea.replaceSelection("");
		for(TCPMessage msg : list_msg) {
			textArea.append(msg.afficherMsg());
		}
	}
}
