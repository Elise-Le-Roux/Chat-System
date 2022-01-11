package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.Date;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import GUI.WelcomePanel.EnterListener;
import controller.ConnectedUsers;
import controller.TCPMessage;
import controller.specificUser;
import network.TcpServerSocket;

//Chatbox at bottom to send message 
public class ChatBoxPanel extends JPanel {

	private JTextField message;

	public ChatBoxPanel() {
		super(new BorderLayout());
		//setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		// Change Username button
		JButton changeUsername = new JButton("Change username");
		changeUsername.setActionCommand("Change username");
		changeUsername.setMaximumSize(new Dimension(130,30));
		changeUsername.setEnabled(true);

		// Textfield
		message = new JTextField(10);
		message.setMinimumSize(new Dimension(20,100));

		// Send button
		Image img = null;
		try {
			img = ImageIO.read(getClass().getResource("/GUI/icon.png"));
		} catch (IOException e) {
			System.out.println("Icone not found");
		}

		//JButton send = new JButton();
		/*	ImageIcon ii = new ImageIcon(img);
    	int scale = 5; // 2 times smaller
    	int width = ii.getIconWidth();
    	int newWidth = width / scale;
    	send.setIcon(new ImageIcon(ii.getImage().getScaledInstance(newWidth, -1,
    	            java.awt.Image.SCALE_SMOOTH)));
		 */

		JButton send = new JButton(new ImageIcon(((new ImageIcon(
				img).getImage()
				.getScaledInstance(30, 30,
						java.awt.Image.SCALE_SMOOTH)))));

		SendListener sendListener = new SendListener(send);
		send.setActionCommand("Send");
		send.addActionListener(sendListener);
		send.setEnabled(false);
		send.setMaximumSize(new Dimension(50,30));

		// to remote the spacing between the image and button's borders
		send.setMargin(new Insets(0, 0, 0, 0));
		// to remove the border
		send.setBorder(null);

		//send.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		send.setEnabled(true);
		// add(send, BorderLayout.SOUTH);

		message.addActionListener(sendListener);


		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addComponent(changeUsername)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(message)
						.addComponent(send)));
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(changeUsername)
				.addComponent(message)
				.addComponent(send));

	}

	//This listener is shared by the text field and the enter button.
	class SendListener implements ActionListener, DocumentListener {
		private boolean alreadyEnabled = false;
		private JButton sendButton;

		public SendListener(JButton button) {
			this.sendButton = button;
		}

		public void actionPerformed(ActionEvent e) {
			try {
				Date date = new Date(System.currentTimeMillis());
				String to = ConnectedUsers.getHostAddress(Window.getAdressee());
				if (TcpServerSocket.getConnections().containsKey(to)) {
					TcpServerSocket.getConnections().get(to).send_msg(new TCPMessage(InetAddress.getByName(specificUser.get_address()), InetAddress.getByName(to), message.getText(), date, true));
				}
				else {
					TcpServerSocket.connect(to, 3000).send_msg(new TCPMessage(InetAddress.getByName(specificUser.get_address()), InetAddress.getByName(to), message.getText(), date, true)); //Port
					
				}
				Window.messages.setContent(Window.getAdressee());
			} catch (Exception e1) {
				System.out.println("Exception actionPerformed SendListener: " + e1.getMessage());
			} 
		}

		//Required by DocumentListener.
		public void insertUpdate(DocumentEvent e) {
			enableButton();
		}

		//Required by DocumentListener.
		public void removeUpdate(DocumentEvent e) {
			handleEmptyTextField(e);
		}

		//Required by DocumentListener.
		public void changedUpdate(DocumentEvent e) {
			if (!handleEmptyTextField(e)) {
				enableButton();
			}
		}

		private void enableButton() {
			if (!alreadyEnabled) {
				sendButton.setEnabled(true);
			}
		}

		private boolean handleEmptyTextField(DocumentEvent e) {
			if (e.getDocument().getLength() <= 0) {
				sendButton.setEnabled(false);
				alreadyEnabled = false;
				return true;
			}
			return false;
		}
	}
}
