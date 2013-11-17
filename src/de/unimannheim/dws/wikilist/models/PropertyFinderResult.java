package de.unimannheim.dws.wikilist.models;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 * The Class PropertyFinderResult.
 */
public class PropertyFinderResult {

	/** The map. */
	private HashMap<String, Double> map;
	
	/**
	 * Instantiates a new property finder result.
	 *
	 * @param map the map
	 */
	public PropertyFinderResult(HashMap<String, Double> map) {
		this.map = map;
	}
	
	/**
	 * Instantiates a new property finder result.
	 */
	public PropertyFinderResult(){};
	
	/**
	 * Write output to csv.
	 *
	 * @param path the path
	 * @param data the data
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeOutputToCsv(String path, HashMap<String, Double> data)
			throws IOException {

		// generate CSVPrinter Object
		// FileOutputStream csvBAOS = new FileOutputStream(new File(path));
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				new File(path)));
		// OutputStreamWriter csvWriter = new OutputStreamWriter(csvBAOS);
		CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);

		csvPrinter.print("sep=,");
		csvPrinter.println();

		for (String key:data.keySet()) {
			csvPrinter.print(key);
			csvPrinter.print(data.get(key));
			csvPrinter.println();
		}

		csvPrinter.flush();
		csvPrinter.close();

	}

	/**
	 * Gets the map.
	 *
	 * @return the map
	 */
	public HashMap<String, Double> getMap() {
		return map;
	}

	/**
	 * Sets the map.
	 *
	 * @param map the map
	 */
	public void setMap(HashMap<String, Double> map) {
		this.map = map;
	}

}
