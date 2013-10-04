package de.unimannheim.dws.wikilist.test;

import java.sql.Connection;

import de.tudarmstadt.ukp.wikipedia.api.DatabaseConfiguration;
import de.tudarmstadt.ukp.wikipedia.api.Page;
import de.tudarmstadt.ukp.wikipedia.api.WikiConstants.Language;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.unimannheim.dws.wikilist.util.SSHConnection;

public class HelloWorld {
	public static void main(String[] args) throws Exception {
		// setup
		SSHConnection ssh = new SSHConnection();
        DatabaseConfiguration dbConfig = new DatabaseConfiguration();
        dbConfig.setHost("127.0.0.1:1234");
        dbConfig.setDatabase("jwpl");
        dbConfig.setUser("wikilist");
        dbConfig.setPassword("likiwist");
        dbConfig.setLanguage(Language.english);
		
        System.out.println("A");
        
		// do something
        Wikipedia wiki = new Wikipedia(dbConfig);
        
        System.out.println("B");
       
        // Get the page with title "Hello world".
        // May throw an exception, if the page does not exist.
        Page page = wiki.getPage("List_of_Peers_1330-1339");
        System.out.println(page.getText());
		
		// shutdown
		ssh.close();
	}
}
