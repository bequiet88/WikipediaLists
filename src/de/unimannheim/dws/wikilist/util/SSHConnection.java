package de.unimannheim.dws.wikilist.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SSHConnection {

	private Session session;

	public SSHConnection() {
		// setup ssh
		
		String user = "uwiki";
        String password = "pwiki";
        String host = "wifo5-37.informatik.uni-mannheim.de";
        int port=22;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);
            int lport = 1234;
            String rhost = "localhost";
            int rport = 3306;
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Establishing Connection...");
            session.connect();
            int assinged_port=session.setPortForwardingL(lport, rhost, rport);
            System.out.println("localhost:"+assinged_port+" -> "+rhost+":"+rport);
        } catch(Exception e){System.err.print(e);
        }
	}
	
	public static void main(String[] args) throws Exception {
		SSHConnection connection = new SSHConnection();
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection dbConn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:1234/jwpl?user=wikilist&password=likiwist");
		Statement stmt = dbConn.createStatement();
		ResultSet RS = stmt.executeQuery("SHOW TABLES");
		while(RS.next())
			System.out.println(RS.getString(1));
		dbConn.close();
		connection.close();
	}

    public void close() {
		session.disconnect();
	}

}
