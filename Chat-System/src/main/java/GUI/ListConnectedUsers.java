package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


import controller.Controller;
import controller.User;

public class ListConnectedUsers extends JPanel implements ListSelectionListener {

	//For the list of connected users 
	JList<String> list;
	private DefaultListModel<String> listModel;

	public ListConnectedUsers(){
		super(new BorderLayout());
		listModel = new DefaultListModel<String>();
		
		
		/*for( User u : ConnectedUsers.getConnectedUsers()) {
			listModel.addElement(u.getPseudo());
		} */
		
		//Create the list and put it in a scroll pane.
        list = new JList<String>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //list.setSelectedIndex(0);      
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        list.setCellRenderer(new IconListCellRenderer());

		//list.setEnabled(false);
        JScrollPane listScrollPane = new JScrollPane(list);
        add(listScrollPane);
        
	} 
	
	//For the chatPanel
	public void valueChanged(ListSelectionEvent evt) { 
		if( evt.getValueIsAdjusting() & Window.messages != null) {
			Window.messages.setContent((String) list.getSelectedValue());
			Window.setAdressee((String) list.getSelectedValue());
			Window.chatBox.setButton((String) list.getSelectedValue());
		}
		else {
			//list.clearSelection();
		}
	} 
	
	public void refresh() {
		listModel = new DefaultListModel<String>();
		ArrayList<User> list_user = null;
		list_user = Controller.get_list_users();
		for( User u : list_user) {
			listModel.addElement(u.getPseudo());
		}
		this.list.setModel(listModel);
		
	}
	
	private class IconListCellRenderer extends DefaultListCellRenderer {

		public IconListCellRenderer() {
            super();
        }
  
        public Component getListCellRendererComponent( JList<?> list,
                Object value, int index, boolean isSelected,
                boolean cellHasFocus ) {
            Component c = super.getListCellRendererComponent( list,
                    value, index, isSelected, cellHasFocus );
  
            if( c instanceof JLabel ) {
                JLabel label = ( JLabel )c;
                Image img = null;
                if (Controller.isConnected((String)value)) {
                	try {
                		img = ImageIO.read(getClass().getResource("/controller/point_vert.png"));
                	} catch (IOException e) {
                		System.out.println("Icone not found");
                	}
                } else {
                	try {
                		img = ImageIO.read(getClass().getResource("/controller/point_rouge.png"));
                	} catch (IOException e) {
                		System.out.println("Icone not found");
                	}
                }
                ImageIcon ii = new ImageIcon(img);
                int scale = 30; // 2 times smaller
            	int width = ii.getIconWidth();
            	int newWidth = width / scale;
                label.setIcon(new ImageIcon(ii.getImage().getScaledInstance(newWidth, -1,
        	            java.awt.Image.SCALE_SMOOTH)));
            }
            return c;
        }
    }
}
