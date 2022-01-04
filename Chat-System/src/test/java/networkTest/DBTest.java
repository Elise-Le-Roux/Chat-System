package networkTest;

import java.net.InetAddress;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;

import controller.TCPMessage;
import database.DBManager;

public class DBTest {
	public static void main(String[] args) {
		DBManager DB = new DBManager();
		DB.connect();
		//DB.drop();
		DB.init();
		//DB.connect();
		/* Date date = new Date(System.currentTimeMillis());
		DB.insert("192.168.0.2","192.168.0.3", "helloword", date);
		Date date2 = new Date(System.currentTimeMillis());
		DB.insert("192.168.0.3","192.168.0.2", "hello", date2);
		Date date3 = new Date(System.currentTimeMillis());
		DB.insert("192.168.0.4", "192.168.0.2", "bonjour", date3); */
		ArrayList<TCPMessage> messages = DB.select_conv("192.168.0.2", "192.168.0.3");
		Iterator iter = messages.iterator();
	      while (iter.hasNext()) {
	    	  TCPMessage msg = (TCPMessage) iter.next();
	    	  msg.afficherMsg();
	      }
		DB.afficher_BDD();
	}
	

}
