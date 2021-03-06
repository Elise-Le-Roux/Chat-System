package modelTest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;

import model.DBManager;
import network.TCPMessage;

public class DBTest {
	public static void main(String[] args) {
		DBManager DB = new DBManager();
		DB.connect();
		DBManager DB2 = new DBManager();
		DB2.connect();
		DB.drop_historique();
		DB.drop_users();
		DB.init();
		DB.select_users();
		System.out.println("******************************************************");
		DB.add_new_user("joel", "192.168.0.1");
		DB.select_users();
		System.out.println("******************************************************");
		DB.add_new_user("michel", "192.168.0.1");
		DB.select_users();
		System.out.println("******************************************************");
		DB.add_new_user("blbla", "192.169.0.1");
		DB.select_users();
		System.out.println("******************************************************");
		DB.add_new_user("joel", "192.168.0.1");
		DB.select_users();
		System.out.println("******************************************************");
		DB.add_new_user("mimi", "192.169.0.1");
		DB.select_users();
		System.out.println("******************************************************"); 
		//DB.connect();
		String hostname1 = "";
		try {
			hostname1 = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/*Date date = new Date(System.currentTimeMillis());
		DB.insert(hostname1,"192.168.10.1", "helloword", date);
		Date date2 = new Date(System.currentTimeMillis());
		DB2.insert("192.168.10.1",hostname1, "hello", date2);
		Date date3 = new Date(System.currentTimeMillis());
		DB2.insert(hostname1, "192.168.10.1", "bonjour", date3);
		DB.insert(hostname1, hostname1, "TEST", date3);
		ArrayList<TCPMessage> messages = DB.select_conv("192.168.0.2", "192.168.0.3");
		Iterator iter = messages.iterator();
	      while (iter.hasNext()) {
	    	  TCPMessage msg = (TCPMessage) iter.next();
	    	  msg.afficherMsg();
	      } */
		Date date3 = new Date(System.currentTimeMillis());
		DB.insert(hostname1, hostname1, "TEST", date3, "TEXT");
		DBManager.afficher_BDD();
	}
	

}
