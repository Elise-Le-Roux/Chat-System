package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;
import controller.ConnectedUsers;
import controller.Controller;
import controller.User;
import controller.specificUser;
import database.DBManager;
import network.TcpServerSocket;
import network.UDPSocket;

public class Window extends JPanel {

	//Specify the look and feel to use.  Valid values:
	//null (use the default), "Metal", "System", "Motif", "GTK+"
	final static String LOOKANDFEEL = "GTK+";
	public static MessagePanel messages;
	static JFrame frame;
	static JPanel welcome;
	static String adressee;
	static ListConnectedUsers list;

	private static void initLookAndFeel() {

		// Swing allows you to specify which look and feel your program uses--Java,
		// GTK+, Windows, and so on as shown below.
		String lookAndFeel = null;

		if (LOOKANDFEEL != null) {
			if (LOOKANDFEEL.equals("Metal")) {
				lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
			} else if (LOOKANDFEEL.equals("System")) {
				lookAndFeel = UIManager.getSystemLookAndFeelClassName();
			} else if (LOOKANDFEEL.equals("Motif")) {
				lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
			} else if (LOOKANDFEEL.equals("GTK+")) { //new in 1.4.2
				lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
			} else {
				System.err.println("Unexpected value of LOOKANDFEEL specified: "
						+ LOOKANDFEEL);
				lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
			}

			try {
				UIManager.setLookAndFeel(lookAndFeel);
			} catch (ClassNotFoundException e) {
				System.err.println("Couldn't find class for specified look and feel:"
						+ lookAndFeel);
				System.err.println("Did you include the L&F library in the class path?");
				System.err.println("Using the default look and feel.");
			} catch (UnsupportedLookAndFeelException e) {
				System.err.println("Can't use the specified look and feel ("
						+ lookAndFeel
						+ ") on this platform.");
				System.err.println("Using the default look and feel.");
			} catch (Exception e) {
				System.err.println("Couldn't get specified look and feel ("
						+ lookAndFeel
						+ "), for some reason.");
				System.err.println("Using the default look and feel.");
				e.printStackTrace();
			}
		}
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
		list = new ListConnectedUsers();
		list.setOpaque(true); //content panes must be opaque
		frame.add(list, BorderLayout.WEST);
		
		Controller.get_connected_users();

		//Create and set up the content pane.
		/* JComponent newContentPane = new WelcomePanel();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane); */
		welcome = new WelcomePanel();
		frame.add(welcome, BorderLayout.CENTER);


		//Display the window.
		frame.pack();
		frame.setSize(1000,1000);
		frame.setVisible(true);
	}

	static public void exitProcedure() {
		Controller.send_disconnected();
	    frame.dispose();
	    System.exit(0);
	}

	// When a valid pseudo is entered the window's view change
	public static void change_view() {
		frame.remove(welcome);
		
		messages = new MessagePanel();
		JPanel chatBox = new ChatBoxPanel();

		//Create a split pane with the two scroll panes in it.
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				messages, chatBox);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(700);

		frame.add(splitPane, BorderLayout.CENTER);
		frame.validate();
	}
	
	public void refresh_list() {
		list.refresh(); // ajouter if si la liste n'a pas encore été créée 
	}
	
	
	/*public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	} */
}
