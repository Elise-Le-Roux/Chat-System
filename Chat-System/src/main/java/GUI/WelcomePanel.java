package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import controller.specificUser;

public class WelcomePanel extends JPanel {
	
	private JTextField pseudo;
    private JLabel error;
	
	public WelcomePanel() {
    	super(new BorderLayout());
        
        //Add "Choose your pseudo" label.
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("Please, Enter a pseudo");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(new Font("Roboto Mono", Font.BOLD, 30));
        label.setBounds(500,500,120,120);
        add(Box.createRigidArea(new Dimension(0,400))); //Add space between the top of the window and the labels
        add(label);
        
        JButton enter = new JButton("Enter");
        EnterListener enterListener = new EnterListener(enter);
        enter.setActionCommand("Enter");
        enter.addActionListener(enterListener);
        enter.setEnabled(false);
        enter.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        //Add a textfield to enter the pseudo
        pseudo = new JTextField(10);
        
        pseudo.addActionListener(enterListener);
        pseudo.getDocument().addDocumentListener(enterListener);
        pseudo.setMaximumSize(new Dimension(200,30));
        pseudo.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(pseudo);
        
        //Add an enter button
        add(enter);
        
        
        error = new JLabel("Your pseudo already exists, please enter a new one.");
        error.setVisible(false);
        error.setAlignmentX(Component.CENTER_ALIGNMENT);
        error.setForeground(Color.red);
        add(error);
       
    }
	
	 //This listener is shared by the text field and the enter button.
		class EnterListener implements ActionListener, DocumentListener {
			private boolean alreadyEnabled = false;
			private JButton enterButton;
			
	        public EnterListener(JButton button) {
	            this.enterButton = button;
	        }
	        
			public void actionPerformed(ActionEvent e) {

				boolean pseudo_ok = specificUser.chooseUsername(pseudo.getText());
				
				if(!pseudo_ok) {
					 error.setVisible(true);
				}
				else {
					Window.change_view();
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
	            	enterButton.setEnabled(true);
	            }
	        }

	        private boolean handleEmptyTextField(DocumentEvent e) {
	            if (e.getDocument().getLength() <= 0) {
	            	enterButton.setEnabled(false);
	                alreadyEnabled = false;
	                return true;
	            }
	            return false;
		}
		}

}
