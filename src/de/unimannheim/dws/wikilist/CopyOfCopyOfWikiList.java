package de.unimannheim.dws.wikilist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import de.unimannheim.dws.wikilist.evaluation.EvaluationDataSet;
import de.unimannheim.dws.wikilist.evaluation.TestDataSet;
import de.unimannheim.dws.wikilist.models.EvaluationResult;
import de.unimannheim.dws.wikilist.models.TableRow;
import de.unimannheim.dws.wikilist.models.Triple;
import de.unimannheim.dws.wikilist.reader.ListPageCSVReader;
import de.unimannheim.dws.wikilist.reader.ListPageDBPediaReader;
import de.unimannheim.dws.wikilist.reader.ListPageWikiMarkupReader;
import de.unimannheim.dws.wikilist.reader.ReaderResource;
import de.unimannheim.dws.wikilist.util.ProcessTable;
import de.unimannheim.dws.wikilist.util.PropertyFinderHelper;

public class CopyOfCopyOfWikiList {

	/*
	 * Class variables
	 */
	public static String rdfTag = "";
	public static String rdfTagPrefix = "";
	public static String wikiListURL = "";
	public static String wikiListName = "";
	public static String regexInstances = "";
	public static int columnInstance = -1;
	public static String captureGroup = "";
	public static int columnPosition = 0;
	public static String pathToResult = "D:/Studium/Classes_Sem3/Seminar/Codebase/";
	public static EvaluationResult evalRes = new EvaluationResult();

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) {

		// Logger log = Logger.getLogger(CopyOfWikiList.class);

		/*****************************
		 * Data Collection
		 *****************************/

		/*
		 * Read List of Instances Read list of attributes available in one list
		 * of instances
		 */
		System.out.println("Read table list ..");

		ReaderResource myCSVRes = new ReaderResource(pathToResult
				+ "SampleInstances.tsv");

		ListPageCSVReader myListsReader = new ListPageCSVReader();
		List<List<String>> myListsList = new ArrayList<List<String>>();

		try {
			myListsReader.openInput(myCSVRes);
			myListsList = myListsReader.readInput();
			myListsReader.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		List<TableRow> myRowList = new ArrayList<TableRow>();

		System.out.println("done!");

		// System.out.println("Read instance list ..");
		//
		// ReaderResource myCSVRes2 = new ReaderResource(
		// "C:/Users/d049650/Documents/Uni_Workspace/LinkstoInstances.csv");
		//
		// ListPageCSVReader myInstancesReader = new
		// ListPageCSVReader();
		// myInstancesReader.openInput(myCSVRes2);
		// List<List<String>> myInstancesList = myInstancesReader
		// .readInput();
		// myInstancesReader.close();
		//
		// System.out.println("done!");

		for (List<String> list : myListsList) {

			if (list.get(0).contains("X"))
				continue;

			if (!list.get(3).equals("Table"))
				continue;

			/*
			 * General settings for this run
			 */

			wikiListURL = list.get(1);
			wikiListName = wikiListURL.replace("http://en.wikipedia.org/wiki/",
					"");

			System.out.println("Start processing of table " + wikiListName);

			/*
			 * Obtain plain Wiki Mark Up (formerly Test Data Set)
			 */
			System.out.println("Obtain Plain Wiki Mark Up ..");

			ReaderResource myTestRes = new ReaderResource(wikiListName);

			ListPageWikiMarkupReader myWikiReader = new ListPageWikiMarkupReader();
			myWikiReader.openInput(myTestRes);

			TestDataSet myTestData = new TestDataSet();

			try {
				myTestData.setWikiMarkUpList(myWikiReader.readInput());
			} catch (Exception e1) {
				e1.printStackTrace();
				continue;
			}

			myTestData.setFirstTable(ProcessTable.parseFirstTable(myTestData
					.toString()));

			try {
				myTestData.writeOutputToFile(pathToResult
						+ "results/wikilist_"
						+ wikiListName.replace('/', '_').replace(':', '_')
								.replace('"', '_') + ".txt",
						myTestData.toString());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			myWikiReader.close();

			System.out.println("done!");

			/*
			 * Read list of DBPedia Attributes which are clear without ambiguity
			 */
			try {
				columnInstance = Integer.parseInt(list.get(5)) - 1;
			} catch (NumberFormatException e) {
				System.out
						.println("For table "
								+ wikiListName
								+ "'s instance column was set ambiguously. Aborting the processing of this table.");
				continue;
			}

			// List<String> dbpediaAttributes = new ArrayList<String>();
			// for (int i = 6; i < list.size(); i++) {
			// dbpediaAttributes.add(list.get(i));
			// }

			/*
			 * Find list of DBPedia instances
			 */
			List<String> dbpediaResources = ProcessTable.getColumn(
					myTestData.getFirstTable(), columnInstance);
			HashMap<String, String> dbpediaResMap = new HashMap<String, String>();
			int counter = 0;
			for (String string : dbpediaResources) {
				dbpediaResMap.put("row" + counter, string);
				counter++;
			}

			//
			// for (List<String> instanceList : myInstancesList) {
			//
			// if (instanceList.get(0).equals(wikiListURL)) {
			// regexInstances = instanceList.get(1);
			// captureGroup = instanceList.get(2);
			// for (int i = 3; i < instanceList.size(); i++) {
			// dbpediaResources.add(instanceList.get(i));
			// }
			//
			// }
			// }

			/*
			 * If size > 0 iterate over the instances for each DBPedia
			 * attribute.
			 */
			if (dbpediaResMap.size() == 0)
				continue;

			/*
			 * Initialize TableRow of Sample List Table
			 */
			TableRow myCurrRow = new TableRow(list);
			List<String> dbpediaAttributes = new ArrayList<String>();
			myCurrRow.setDbpediaAttributes(dbpediaAttributes);

			/*
			 * Reset column counter.
			 */
			columnPosition = 0;

			for (int i = 0; i < myTestData.getFirstTable().size(); i++) {

				/*
				 * If column is instance column, continue
				 */
				if (columnPosition == columnInstance) {
					columnPosition++;
					dbpediaAttributes.add("Column of Entity");
					continue;
				}

				/*
				 * Retrieve content for this column of JWPL table
				 */
				List<String> colValues = ProcessTable.getPlainColumn(
						myTestData.getFirstTable(), i);
				/*
				 * Initialize Triple List for this column
				 */
				List<Triple<String, String, String>> myTriples = new ArrayList<Triple<String, String, String>>();

				/*
				 * Iterate over column
				 */
				for (String string2 : colValues) {
					String dbpValue = ProcessTable.wiki2dbpLink(ProcessTable
							.getLink(string2));

					/*
					 * Generate triple
					 */
					if (dbpValue != "") {
						Triple<String, String, String> myTriple = new Triple<String, String, String>();
						myTriple.setFirst(dbpediaResMap.get("row" + i));
						myTriple.setSecond("$property");
						myTriple.setThird(dbpValue);
						myTriples.add(myTriple);
					}

					/*
					 * TODO: Process literal values of JWPL table
					 */
					else {

					}

				}

				/*
				 * Query DBPedia to find suitable Property
				 */
				ReaderResource myDbpRes = new ReaderResource(myTriples);
				ListPageDBPediaReader myDbpReader = new ListPageDBPediaReader();
				myDbpReader.openInput(myDbpRes);
				
				PropertyFinderHelper myPropFinder = new PropertyFinderHelper();
				try {
					dbpediaAttributes.add(myPropFinder.findUriProperty(myDbpReader.readInput()));
				} catch (Exception e) {
					e.printStackTrace();
					columnPosition++;
					continue;
				}
				myDbpReader.close();
				
//
//				/*
//				 * Write Result Matrix to file
//				 */
//
//				try {
//					int fileCount = 1;
//					File file = new File(pathToResult
//							+ "results/evaluation_"
//							+ wikiListName.replace('/', '_').replace(':', '_')
//									.replace('"', '_') + "_" + rdfTag + ".csv");
//					if (file.exists()) {
//						myEvalData.getEvalRes().writeOutputToCsv(
//								pathToResult
//										+ "results/evaluation_"
//										+ wikiListName.replace('/', '_')
//												.replace(':', '_')
//												.replace('"', '_') + "_"
//										+ rdfTag + "_" + fileCount++ + ".csv",
//								myEvalMatrix);
//					}
//
//					else {
//						myEvalData.getEvalRes().writeOutputToCsv(
//								pathToResult
//										+ "results/evaluation_"
//										+ wikiListName.replace('/', '_')
//												.replace(':', '_')
//												.replace('"', '_') + "_"
//										+ rdfTag + ".csv", myEvalMatrix);
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				}

				
				System.out.println("done!");
				columnPosition++;
			}
			
			myRowList.add(myCurrRow);
			
		}
		
		/*
		 * Pass List of Table Rows to new value evaluation
		 */
		
		CopyOfWikiList.wikiListEvaluation(myRowList);

	}

}
