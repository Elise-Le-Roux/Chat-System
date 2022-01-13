package GUI;

import java.awt.BorderLayout;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import controller.ConnectedUsers;
import controller.Controller;
import controller.User;
import database.DBManager;
import network.UDPSocket;

public class ListConnectedUsers extends JPanel implements ListSelectionListener {

	//For the list of connected users 
	private JList list;
	private DefaultListModel listModel;

	public ListConnectedUsers(){
		super(new BorderLayout());
		listModel = new DefaultListModel();
		
		
		/*for( User u : ConnectedUsers.getConnectedUsers()) {
			listModel.addElement(u.getPseudo());
		} */
		
		//Create the list and put it in a scroll pane.
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //list.setSelectedIndex(0);      
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);
        add(listScrollPane);
        
	} 
	
	//For the chatPanel
	public void valueChanged(ListSelectionEvent evt) { 
		if( evt.getValueIsAdjusting()) {
			System.out.println("shddddddddfgqsjdghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
			Window.messages.setContent((String) list.getSelectedValue());
			Window.setAdressee((String) list.getSelectedValue());
		}
	} 
	
	public void refresh() {
		listModel = new DefaultListModel();
		for( User u : Controller.get_list_connected_users()) {
			listModel.addElement(u.getPseudo());
		}
		list.setModel(listModel);
	}
}
