package de.unimannheim.dws.wikilist.reader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListPageFileReader implements IListPageReader<List<String>> {

	BufferedReader reader = null;
	BufferedWriter writer = null;
	ArrayList<String> output = null;

	@Override
	public void openInput(ReaderResource resource) {

		try {
			reader = new BufferedReader(new FileReader(new File(
					resource.getResource())));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<String> readInput() {

		output = new ArrayList<String>();

		String line;
		try {
			line = reader.readLine();

			while (line != null) {

				output.add(line);
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return output;
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
