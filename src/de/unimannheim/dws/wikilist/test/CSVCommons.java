package de.unimannheim.dws.wikilist.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class CSVCommons {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Reader in = new BufferedReader(
					new FileReader(
							new File(
									"D:/Studium/Classes_Sem3/Seminar/Codebase/Links_to_Instances.csv")));
			;
			List<List<String>> result = new ArrayList<List<String>>();
			Iterable<CSVRecord> parser = CSVFormat.EXCEL.parse(in);
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
			System.out.println(result.toString());

		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}
