package view;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import controller.Controller;
import network.TCPMessage;

//Messagebox to display the history messages with current user 
public class MessagePanel extends JPanel {
	
	private static final long serialVersionUID = 7880084217471745877L;
	
	private JTextArea textArea;
	
	public MessagePanel() {
    	super(new BorderLayout());
    	
    	textArea = new JTextArea(5, 20);
    	JScrollPane scrollPane = new JScrollPane(textArea); 
    	textArea.setEditable(false);

    	add(scrollPane);

	}

	public void setContent(String pseudo) {
		ArrayList<TCPMessage> list_msg = Controller.getConv(Controller.get_host_address(pseudo));
		textArea.selectAll();
		textArea.replaceSelection("");
		for(TCPMessage msg : list_msg) {
			textArea.append(msg.afficherMsg());
		}
	}
}
