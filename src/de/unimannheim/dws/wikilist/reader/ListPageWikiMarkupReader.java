package de.unimannheim.dws.wikilist.reader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.wikipedia.api.DatabaseConfiguration;
import de.tudarmstadt.ukp.wikipedia.api.Page;
import de.tudarmstadt.ukp.wikipedia.api.WikiConstants.Language;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.unimannheim.dws.wikilist.util.SSHConnection;

/**
 * The Class ListPageWikiMarkupReader.
 */
public class ListPageWikiMarkupReader implements IListPageReader<List<String>> {
	
	/** The writer. */
	BufferedWriter writer = null;
	
	/** The wiki res. */
	String wikiRes = null;
	
	/** The output. */
	ArrayList<String> output = null;
	
	/** The ssh. */
	SSHConnection ssh = null;
    
    /** The db config. */
    DatabaseConfiguration dbConfig = null;
    
	/* (non-Javadoc)
	 * @see de.unimannheim.dws.wikilist.reader.IListPageReader#openInput(de.unimannheim.dws.wikilist.reader.ReaderResource)
	 */
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

	/* (non-Javadoc)
	 * @see de.unimannheim.dws.wikilist.reader.IListPageReader#readInput()
	 */
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

	/* (non-Javadoc)
	 * @see de.unimannheim.dws.wikilist.reader.IListPageReader#close()
	 */
	@Override
	public void close() {
		// shutdown
		if(ssh != null) ssh.close();
	}


	/**
	 * Write output to file.
	 *
	 * @param path the path
	 * @param text the text
	 */
	public void writeOutputToFile(String path, String text) {
		try {
			writer = new BufferedWriter(new FileWriter(new File(path)));
			writer.write(text);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
