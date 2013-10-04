package de.unimannheim.dws.wikilist.reader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class ListPageCSVReader implements IListPageReader<List<List<String>>> {

	private BufferedReader reader = null;
	private BufferedWriter writer = null;

	@Override
	public void openInput(ReaderResource resource) {
		// TODO Auto-generated method stub
		try {
			reader = new BufferedReader(
					new FileReader(
							new File(resource.getResource()
									)));
			;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<List<String>> readInput() throws Exception {

		List<List<String>> result = new ArrayList<List<String>>();
		Iterable<CSVRecord> parser = CSVFormat.EXCEL.parse(reader);
		boolean firstRecord = true;
		for (CSVRecord record : parser) {
			if (firstRecord)
				firstRecord = false;
			else {
				List<String> recordFields = new ArrayList<String>();
				Iterator<String> iter = record.iterator();
				while (iter.hasNext()) {
					recordFields.add(iter.next());
				}
				result.add(recordFields);
			}
		}

		return result;
	}

	@Override
	public void close() {
		try {
			reader.close();
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
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
