package de.unimannheim.dws.wikilist.evaluation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;

/**
 * The Class DataSet.
 */
public abstract class DataSet {

	/** The writer. */
	BufferedWriter writer = null;

	/** The number of marked attributes. */
	protected int noOfMarkedAttributes = 0;

	/** The wiki mark up list. */
	protected List<String> annotWikiMarkUpList = null;

	/** The DBPedia instance (key) and attribute (value) map */
	HashMap<String, String> dbpediaValues = null;

	/**
	 * Creates the data set.
	 * 
	 * @param wikiMarkUpList
	 *            the wiki mark up list
	 */
	public abstract void create(List<String> wikiMarkUpList,
			HashMap<String, String> dbpediaValues) throws Exception;

	/**
	 * To string.
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {

		String out = "";
		if (annotWikiMarkUpList != null) {
			for (String line : annotWikiMarkUpList) {
				out += line + "\n";
			}
		}
		return out;
	}

	/**
	 * Write output to file.
	 * 
	 * @param path
	 *            the path
	 * @param text
	 *            the text
	 */
	public void writeOutputToFile(String path, String text) throws Exception {

		writer = new BufferedWriter(new FileWriter(new File(path)));
		writer.write(text);
		writer.close();

	}

	/**
	 * Gets the no of marked attributes.
	 * 
	 * @return the no of marked attributes
	 */
	public int getNoOfMarkedAttributes() {
		return noOfMarkedAttributes;
	}

	/**
	 * Sets the no of marked attributes.
	 * 
	 * @param noOfMarkedAttributes
	 *            the new no of marked attributes
	 */
	public void setNoOfMarkedAttributes(int noOfMarkedAttributes) {
		this.noOfMarkedAttributes = noOfMarkedAttributes;
	}

	/**
	 * Gets the wiki mark up list.
	 * 
	 * @return the wiki mark up list
	 */
	public List<String> getWikiMarkUpList() {
		return annotWikiMarkUpList;
	}

	/**
	 * Sets the wiki mark up list.
	 * 
	 * @param wikiMarkUpList
	 *            the new wiki mark up list
	 */
	public void setWikiMarkUpList(List<String> wikiMarkUpList) {
		this.annotWikiMarkUpList = wikiMarkUpList;
	}

}
