package de.unimannheim.dws.wikilist.evaluation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.output.ByteArrayOutputStream;

import de.unimannheim.dws.wikilist.CopyOfWikiList;
import de.unimannheim.dws.wikilist.util.EvaluationHelper;
import de.unimannheim.dws.wikilist.util.EvaluationResult;

public class EvaluationDataSet extends DataSet {

	/** Result of Evaluation. */
	private EvaluationResult evalRes = null;

	/** Number of Wiki Table Rows found */
	private int noOfWikiTableEntries = 0;

	/** Number of DBPedia Values retrieved (equals number of instances) */
	private int noOfDBPediaValues = 0;

	@Override
	public void create(DataSet dataSet, HashMap<String, String> dbpediaValues)
			throws Exception {

		List<List<String>> firstTable = dataSet.getFirstTable();
		noOfWikiTableEntries = firstTable.size();
		noOfDBPediaValues = dbpediaValues.size();

		EvaluationHelper evalHelper = new EvaluationHelper();
		evalRes = evalHelper.evaluate(firstTable, dbpediaValues,
				CopyOfWikiList.rdfTagPrefix + ":" + CopyOfWikiList.rdfTag);

	}

	public List<List<String>> getEvalMatrix() {

		List<List<String>> evalMatrix = null;

		if (evalRes != null) {

			if (evalMatrix == null)
				evalMatrix = new ArrayList<List<String>>();

			/*
			 * First Row
			 */
			List<String> firstRow = new ArrayList<String>();
			firstRow.add("");
			firstRow.add("DBPedia Uri");
			firstRow.add("DBPedia Literal");
			firstRow.add("DBPedia Empty");
			firstRow.add("Total");
			evalMatrix.add(firstRow);

			/*
			 * Second Row
			 */
			List<String> secondRow = new ArrayList<String>();
			secondRow.add("WikiList Uri");
			secondRow.add("" + evalRes.getNoOfDBPUriWikiUri());
			secondRow.add("" + evalRes.getNoOfDBPLiteralWikiUri());
			secondRow.add("" + evalRes.getNoOfDBPEmptyWikiUri());
			secondRow.add("" + evalRes.getNoOfWikiUri());
			evalMatrix.add(secondRow);

			/*
			 * Third Row
			 */
			List<String> thirdRow = new ArrayList<String>();
			thirdRow.add("WikiList Literal");
			thirdRow.add("" + evalRes.getNoOfDBPUriWikiLiteral());
			thirdRow.add("" + evalRes.getNoOfDBPLiteralWikiLiteral());
			thirdRow.add("" + evalRes.getNoOfDBPEmptyWikiLiteral());
			thirdRow.add("" + evalRes.getNoOfWikiLiteral());
			evalMatrix.add(thirdRow);

			/*
			 * Fourth Row
			 */
			List<String> fourthRow = new ArrayList<String>();
			fourthRow.add("WikiList Empty");
			fourthRow.add("" + evalRes.getNoOfDBPUriWikiEmpty());
			fourthRow.add("" + evalRes.getNoOfDBPLiteralWikiEmpty());
			fourthRow.add("" + evalRes.getNoOfDBPEmptyWikiEmpty());
			fourthRow.add("" + evalRes.getNoOfWikiEmpty());
			evalMatrix.add(fourthRow);

			/*
			 * Fifth Row
			 */
			List<String> fifthRow = new ArrayList<String>();
			fifthRow.add("Total");
			fifthRow.add("" + evalRes.getNoOfDBPediaUri());
			fifthRow.add("" + evalRes.getNoOfDBPediaLiteral());
			fifthRow.add("" + evalRes.getNoOfDBPediaEmpty());
			fifthRow.add("" + evalRes.getNoOfTotal());
			evalMatrix.add(fifthRow);

		}

		return evalMatrix;

	}

	public List<List<String>> getRdfTriples() {
		if (evalRes != null) {
			return evalRes.getRdfTuples();
		} else
			return null;
	}

	public void writeOutputToCsv(String path, List<List<String>> data)
			throws IOException {

		// generate CSVPrinter Object
		// FileOutputStream csvBAOS = new FileOutputStream(new File(path));
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				new File(path)));
		// OutputStreamWriter csvWriter = new OutputStreamWriter(csvBAOS);
		CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);

		csvPrinter.print("sep=,");
		csvPrinter.println();

		if (data.get(0).get(0).equals("")) {
			csvPrinter.print("Number of Wiki Table rows = "
					+ noOfWikiTableEntries);
			csvPrinter.println();
			csvPrinter.print("Number of DBPedia values retrieved = "
					+ noOfDBPediaValues);
			csvPrinter.println();
		}

		for (List<String> list : data) {

			for (String string : list) {
				csvPrinter.print(string);
			}
			csvPrinter.println();
		}

		csvPrinter.flush();
		csvPrinter.close();

	}

}
