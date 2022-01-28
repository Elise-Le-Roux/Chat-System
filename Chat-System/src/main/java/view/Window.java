package view;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme;

import controller.Controller;

public class Window extends JPanel {

	private static final long serialVersionUID = 2087030015107756824L;
	
	public static MessagePanel messages;
	static JFrame frame;
	static JPanel welcome;
	static String adressee;
	static ListUsers list;
	public static ChatBoxPanel chatBox;

	private static void initLookAndFeel() {
		FlatDarkPurpleIJTheme.setup();
	}

	static public void setAdressee(String adressee_1) {
		adressee = adressee_1;
	}

	static public String getAdressee() {
		return adressee;
	}

	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event-dispatching thread.
	 */
	public static void createAndShowGUI() {
		//Set the look and feel.
		initLookAndFeel();

		//Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);

		//Create and set up the window.
		frame = new JFrame("ChatSystem");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent event) {
		        exitProcedure();
		    }
		});
		
		//Create the list
		list = new ListUsers();
		list.setOpaque(true); //content panes must be opaque
		frame.add(list, BorderLayout.WEST);
		

		//Create and set up the content pane.
		welcome = new WelcomePanel();
		frame.add(welcome, BorderLayout.CENTER);


		//Display the window.
		frame.pack();
		frame.setSize(1000,1000);
		frame.setVisible(true);
	}

	static public void exitProcedure() {
		Controller.exit_procedure();
	    frame.dispose();
	    System.exit(0);
	}

	// When a valid pseudo is entered the window's view change
	public static void change_view() {
		frame.remove(welcome);
		messages = new MessagePanel();
		chatBox = new ChatBoxPanel();

		//Create a split pane with the two scroll panes in it.
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				messages, chatBox);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(700);

		frame.add(splitPane, BorderLayout.CENTER);
		frame.validate();
	}
	
	public static void refresh_list() {
		list.refresh();
	}
}
