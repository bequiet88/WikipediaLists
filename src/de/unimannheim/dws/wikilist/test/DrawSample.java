package de.unimannheim.dws.wikilist.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.tudarmstadt.ukp.wikipedia.api.DatabaseConfiguration;
import de.tudarmstadt.ukp.wikipedia.api.Page;
import de.tudarmstadt.ukp.wikipedia.api.PageQuery;
import de.tudarmstadt.ukp.wikipedia.api.WikiConstants.Language;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.unimannheim.dws.wikilist.util.SSHConnection;

public class DrawSample {
	public static void main(String[] args) throws Exception {
		// setup
		SSHConnection ssh = new SSHConnection();
        Connection conn = ssh.getMySQLConnection();
        
        Statement stmt = conn.createStatement();
        ResultSet RS = stmt.executeQuery("SELECT name FROM jwpl.Page WHERE name like 'List_of_%' ORDER BY RAND() LIMIT 500");
        while(RS.next())
        	System.out.println(RS.getString(1));
 
		// shutdown
		ssh.close();
	}
}
