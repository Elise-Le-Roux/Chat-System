package GUI;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ChangeUsername extends JFrame{

	private static final long serialVersionUID = 5296525556558137121L;
	
	String new_username;
	
	public ChangeUsername(String text) {
		new_username = JOptionPane.showInputDialog(text);
	}
	
	public String get_new_username() {
		return this.new_username;
	}

}
