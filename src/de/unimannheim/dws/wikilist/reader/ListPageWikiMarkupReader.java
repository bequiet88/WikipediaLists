package de.unimannheim.dws.wikilist.reader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.wikipedia.api.DatabaseConfiguration;
import de.tudarmstadt.ukp.wikipedia.api.Page;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.WikiConstants.Language;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiException;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiInitializationException;
import de.unimannheim.dws.wikilist.util.SSHConnection;

public class ListPageWikiMarkupReader implements IListPageReader<List<String>> {
	
	BufferedWriter writer = null;
	String wikiRes = null;
	ArrayList<String> output = null;
	
	SSHConnection ssh = null;
    DatabaseConfiguration dbConfig = null;
    
	@Override
	public void openInput(ReaderResource resource) {
		// TODO Auto-generated method stub
		wikiRes = resource.getResource();
		ssh =  new SSHConnection();
        dbConfig = new DatabaseConfiguration();
		dbConfig.setHost("127.0.0.1:1234");
        dbConfig.setDatabase("jwpl");
        dbConfig.setUser("wikilist");
        dbConfig.setPassword("likiwist");
        dbConfig.setLanguage(Language.english);
	}

	@Override
	public List<String> readInput() throws Exception {


        Wikipedia wiki = new Wikipedia(dbConfig);

        // May throw an exception, if the page does not exist.
        Page page = wiki.getPage(wikiRes);
        //System.out.println(page.getText());
		
		String lines[] = page.getText().split("\\n");
		output = new ArrayList<String>();

		for(String line: lines) {
			output.add(line);
		}
		return output;
	}

	@Override
	public void close() {
		// shutdown
		ssh.close();
	}

	@Override
	public void writeOutputToFile(String path, String text) {
		try {
			writer = new BufferedWriter(new FileWriter(new File(path)));
			writer.write(text);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
