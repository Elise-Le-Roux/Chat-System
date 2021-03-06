package model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import network.TCPMessage;
import network.TCPMessage.TypeNextMessage;

public class DBManager {

	static String url = "jdbc:sqlite:test.db";
	static Connection conn = null;

	// Connexion à la base de donnée
	public void connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection(url);
		} catch (ClassNotFoundException e) {
			System.out.println("Class exception " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("SQL exception 1 " + e.getMessage());
		}
	}

	// Insertion d'un message dans la table historique
	public void insert(String from, String to, String content, Date time, String type) {
		try {
			String query = "INSERT INTO historique values (?,?,?,?,?)";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1,from);
			pstmt.setString(2,to);
			pstmt.setString(3,content);
			pstmt.setDate(4,time);
			pstmt.setString(5,type);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL INSERT exception" + e.getMessage());
		}
	}

	// Insertion d'un utilisateur dans la table users
	public void add_new_user(String pseudo, String address) {
		try {
			String query = "REPLACE INTO users values (?,?,?)";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1,pseudo);
			pstmt.setString(2,address);
			pstmt.setBoolean(3,false);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL INSERT exception 2" + e.getMessage());
		}
	}

	// Changement du pseudo d'un utilisateur dans la table users
	public void change_pseudo(String pseudo, String address) {
		try {
			String query = "UPDATE users"
					+ "	SET pseudo = ?"
					+ "	WHERE address = ?" ;
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1,pseudo);
			pstmt.setString(2,address);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL INSERT exception 3" + e.getMessage());
		}
	}

	// Changement du booléen indiquant si un message est non lu dans la table users
	public void change_unread(String address, Boolean unread) { 
		try {
			String query = "UPDATE users"
					+ "	SET unread = ?"
					+ "	WHERE address = ?" ;
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setBoolean(1, unread);
			pstmt.setString(2,address);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL INSERT exception 3" + e.getMessage());
		}
	}

	// Sélection de tous les utilisateurs enregistrés
	public ArrayList<User> select_users() {
		try {
			ArrayList<User> users = new ArrayList<User>();
			String query = "SELECT * FROM users";
			Statement stmt;
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				users.add(new User(rs.getString(1), rs.getString(2), false, rs.getBoolean(3)));
			}
			return users;
		} catch (SQLException e) {
			System.out.println("SQL exception select 2" + e.getMessage());
			return null;
		}

	}

	// Sélection d'une conversation
	public ArrayList<TCPMessage> select_conv(String from, String to) {
		try {
			ArrayList<TCPMessage> messages = new ArrayList<TCPMessage>();
			String query = "SELECT * FROM historique WHERE (de=\"" + from + "\" AND a=\"" + to + "\")"
					+ " OR (de=\"" + to + "\"" + " AND a=\"" + from + "\")";
			Statement stmt;
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				InetAddress addFrom = InetAddress.getByName(rs.getString(1));
				InetAddress addTo = InetAddress.getByName(rs.getString(2));
				String content = rs.getString(3);
				Date   time = rs.getDate(4);
				TypeNextMessage type;
				if (rs.getString(5).equals("TEXT")) {
					type = TypeNextMessage.TEXT;
				}
				else {
					type = TypeNextMessage.FILE;
				}
				messages.add(new TCPMessage(addFrom, addTo, content, time, type));
			}
			return messages;
		} catch (SQLException e) {
			System.out.println("SQL exception select 1" + e.getMessage());
			return null;
		} catch (UnknownHostException e) {
			System.out.println("SQL exception inetaddress conversion" + e.getMessage());
			return null;
		}
	}

	// Affichage de la base de donnée
	static public void afficher_BDD () {
		try {
			String query = "SELECT * FROM historique";
			Statement stmt;
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				System.out.print("from = " + rs.getString(1));
				System.out.print(", to = " + rs.getString(2));
				System.out.print(", content = " + rs.getString(3));
				System.out.print(", time = ");
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				System.out.print(formatter.format(rs.getDate(4)));
				System.out.println(", type = " + rs.getString(5));
			}
		} catch (SQLException e) {
			System.out.println("SQL exception select 2" + e.getMessage());
		}

	}

	//initialisation de la base de donnée
	public void init(){
		String query1 = "CREATE TABLE historique ("
				+ "de VARCHAR(100), " // adresse ip
				+ "a VARCHAR(100), " // adresse ip
				+ "content TEXT, "
				+ "time DATETIME, "
				+ "type VARCHAR(10))";

		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(query1);
		} catch (SQLException e) {
			// Do nothing if database already exists
		} finally {

			String query2 = "CREATE TABLE users ("
					+ "pseudo VARCHAR(100) NOT NULL UNIQUE,"
					+ "address VARCHAR(100) NOT NULL PRIMARY KEY," // adresse ip
					+ "unread BOOLEAN)";
			try {
				Statement stmt2 = conn.createStatement();
				stmt2.executeUpdate(query2);
			} catch (SQLException e) {
				// Do nothing if database already exists
			}
		}
	}

	public void drop_historique() {
		try {
			String query = "DROP TABLE historique";
			Statement stmt;
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void drop_users() {
		try {
			String query = "DROP TABLE users";
			Statement stmt;
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
