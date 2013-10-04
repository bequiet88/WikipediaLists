package de.unimannheim.dws.wikilist.reader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class ListPageFileReader.
 */
public class ListPageFileReader implements IListPageReader<List<String>> {

	/** The reader. */
	BufferedReader reader = null;

	/** The writer. */
	BufferedWriter writer = null;

	/** The output. */
	ArrayList<String> output = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.unimannheim.dws.wikilist.reader.IListPageReader#openInput(de.unimannheim
	 * .dws.wikilist.reader.ReaderResource)
	 */
	@Override
	public void openInput(ReaderResource resource) throws Exception {

		reader = new BufferedReader(new FileReader(new File(
				resource.getResource())));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unimannheim.dws.wikilist.reader.IListPageReader#readInput()
	 */
	@Override
	public List<String> readInput() throws Exception {

		output = new ArrayList<String>();

		String line;

		line = reader.readLine();

		while (line != null) {

			output.add(line);
			line = reader.readLine();
		}

		return output;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unimannheim.dws.wikilist.reader.IListPageReader#close()
	 */
	@Override
	public void close() throws Exception {
		if (reader != null)
			reader.close();
		if(writer != null)
			writer.close();
	}

	/**
	 * Write output to file.
	 * 
	 * @param path
	 *            the path
	 * @param text
	 *            the text
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
