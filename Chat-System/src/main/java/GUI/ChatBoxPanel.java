package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import controller.Controller;

//Chatbox at the bottom to send messages
public class ChatBoxPanel extends JPanel {

	private static final long serialVersionUID = 7937661154453022047L;
	
	JTextField message;
	JFileChooser fc;
	JButton send;
	JButton fileButton;
	JButton changeUsername;

	public ChatBoxPanel() {
		super(new BorderLayout());
		
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		// Change Username button
		changeUsername = new JButton("Change username");
		changeUsername.setActionCommand("Change username");
		ChangeUsernameListener usernameListener = new ChangeUsernameListener(changeUsername);
		changeUsername.addActionListener(usernameListener);
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

		send = new JButton(new ImageIcon(((new ImageIcon(
				img).getImage()
				.getScaledInstance(30, 30,
						java.awt.Image.SCALE_SMOOTH)))));

		SendListener sendListener = new SendListener(send);
		send.setActionCommand("Send");
		send.addActionListener(sendListener);
		send.setMaximumSize(new Dimension(50,30));

		// to remote the spacing between the image and button's borders
		send.setMargin(new Insets(0, 0, 0, 0));
		// to remove the border
		send.setBorder(null);

		send.setEnabled(false);

		message.addActionListener(sendListener);
		message.setEnabled(false);

		// send files button
		//Create a file chooser
		fc = new JFileChooser();

		//Create the open button.  We use the image from the JLF
		//Graphics Repository (but we extracted it from the jar).
		fileButton = new JButton("Send file");
		fileButton.setActionCommand("Send file");
		FileListener fileListener = new FileListener(fileButton);
		fileButton.addActionListener(fileListener);
		fileButton.setMaximumSize(new Dimension(130,30));
		fileButton.setEnabled(false);

		//Organize the layout of the buttons and the text field inside the panel
		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addComponent(changeUsername)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(message)
						.addComponent(send)
						.addComponent(fileButton)));
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(changeUsername)
						.addComponent(fileButton))
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
				String to = Controller.get_host_address(Window.getAdressee());
				Controller.send_msg(to, message.getText());
				/*Date date = new Date(System.currentTimeMillis());
				if (TcpServerSocket.getConnections().containsKey(to)) {
					TcpServerSocket.getConnections().get(to).send_msg(new TCPMessage(InetAddress.getByName(Controller.get_ip_address()), InetAddress.getByName(to), message.getText(), date, TypeNextMessage.TEXT));
				}
				else {
					TcpServerSocket.connect(to, 3000).send_msg(new TCPMessage(InetAddress.getByName(Controller.get_ip_address()), InetAddress.getByName(to), message.getText(), date, TypeNextMessage.TEXT)); //Port

				} */
				if (!InetAddress.getByName(Controller.get_ip_address()).equals(InetAddress.getByName(to))) {
					Window.messages.setContent(Window.getAdressee());
				}
			} catch (Exception e1) {
				System.out.println("Exception actionPerformed SendListener: " + e1.getMessage());
			} 
			message.setText("");
		}

		//Required by DocumentListener.
		public void insertUpdate(DocumentEvent e) {
			enableButton();
		}

		//Required by DocumentListener.SendListener
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

	//This listener is shared by the text field and the enter button.
	class ChangeUsernameListener implements ActionListener, DocumentListener {
		private boolean alreadyEnabled = false;
		private JButton changeUsernameButton;

		public ChangeUsernameListener(JButton button) {
			this.changeUsernameButton = button;
		}

		public void actionPerformed(ActionEvent e) {
			ChangeUsername changed = new ChangeUsername("Enter your new pseudo please. ");
			String new_pseudo = changed.get_new_username();
			if(new_pseudo != null && !new_pseudo.equals("")) {
				boolean pseudo_ok = Controller.changeUsername(new_pseudo);
				while(!pseudo_ok) {
					changed = new ChangeUsername("This pseudo already exists, please enter a new one.");
					if(new_pseudo != null && !new_pseudo.equals("")) {
						new_pseudo = changed.get_new_username();
						pseudo_ok = Controller.changeUsername(new_pseudo);
					}
				}
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
				changeUsernameButton.setEnabled(true);
			}
		}

		private boolean handleEmptyTextField(DocumentEvent e) {
			if (e.getDocument().getLength() <= 0) {
				changeUsernameButton.setEnabled(false);
				alreadyEnabled = false;
				return true;
			}
			return false;
		}
	}

	//This listener is shared by the text field and the enter button.
	class FileListener implements ActionListener, DocumentListener {
		private boolean alreadyEnabled = false;
		private JButton fileButton;

		public FileListener(JButton button) {
			this.fileButton = button;
		}

		public void actionPerformed(ActionEvent e) {
			try {
				int returnVal = fc.showOpenDialog(ChatBoxPanel.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					System.out.println(file.getAbsolutePath());
					String to = Controller.get_host_address(Window.getAdressee());
					Controller.send_file(to, file.getAbsolutePath(), file.getName());

					/*Date date = new Date(System.currentTimeMillis());
					if (TcpServerSocket.getConnections().containsKey(to)) {
						TcpServerSocket.getConnections().get(to).sendFile(new TCPMessage(InetAddress.getByName(Controller.get_ip_address()), InetAddress.getByName(to), file.getName(), date, TypeNextMessage.FILE), file.getAbsolutePath());
					}
					else {
						TcpServerSocket.connect(to, 3000).sendFile(new TCPMessage(InetAddress.getByName(Controller.get_ip_address()), InetAddress.getByName(to), file.getName(), date, TypeNextMessage.FILE), file.getAbsolutePath());
					} */
					
					if (!InetAddress.getByName(Controller.get_ip_address()).equals(InetAddress.getByName(to))) {
						Window.messages.setContent(Window.getAdressee());
					}
				}
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
				fileButton.setEnabled(true);
			}
		}

		private boolean handleEmptyTextField(DocumentEvent e) {
			if (e.getDocument().getLength() <= 0) {
				fileButton.setEnabled(false);
				alreadyEnabled = false;
				return true;
			}
			return false;
		}
	}
	
	public void setButton(String pseudo) {
		if (Controller.isConnected(pseudo)) {
			fileButton.setEnabled(true);
			send.setEnabled(true);
			message.setEnabled(true);
		}
		else {
			fileButton.setEnabled(false);
			send.setEnabled(false);
			message.setEnabled(false);
		}
	}
	
	public void lockButton() {
		fileButton.setEnabled(false);
		send.setEnabled(false);
		message.setEnabled(false);
	}
	
}
