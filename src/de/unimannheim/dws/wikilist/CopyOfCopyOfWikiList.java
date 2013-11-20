package de.unimannheim.dws.wikilist;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.unimannheim.dws.wikilist.evaluation.TestDataSet;
import de.unimannheim.dws.wikilist.models.EvaluationResult;
import de.unimannheim.dws.wikilist.models.PropertyFinderResult;
import de.unimannheim.dws.wikilist.models.TableRow;
import de.unimannheim.dws.wikilist.models.Triple;
import de.unimannheim.dws.wikilist.reader.ListPageCSVReader;
import de.unimannheim.dws.wikilist.reader.ListPageDBPediaReader;
import de.unimannheim.dws.wikilist.reader.ListPageWikiMarkupReader;
import de.unimannheim.dws.wikilist.reader.ReaderResource;
import de.unimannheim.dws.wikilist.util.ProcessTable;
import de.unimannheim.dws.wikilist.util.PropertyFinderHelper;

/**
 * The Class CopyOfCopyOfWikiList.
 */
public class CopyOfCopyOfWikiList {

	/*
	 * Class variables
	 */
	/** The rdf tag. */
	public static String rdfTag = "";

	/** The rdf tag prefix. */
	public static String rdfTagPrefix = "";

	/** The wiki list url. */
	public static String wikiListURL = "";

	/** The wiki list name. */
	public static String wikiListName = "";

	/** The regex instances. */
	public static String regexInstances = "";

	/** The column instance. */
	public static int columnInstance = -1;

	/** The capture group. */
	public static String captureGroup = "";

	/** The column position. */
	public static int columnPosition = 0;

	/** The path to result. */
	public static String pathToResult = "D:/Studium/Classes_Sem3/Seminar/Codebase/";//"C:/Users/d049650/Documents/Uni_Workspace/";

	/** The eval res. */
	public static EvaluationResult evalRes = new EvaluationResult();

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
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

		boolean isFirstColumn;

		for (List<String> list : myListsList) {

			if (list.get(0).contains("X"))
				continue;

			if (!list.get(3).equals("Table"))
				continue;

			/*
			 * General settings for this run
			 */

			try {
				wikiListURL = URLDecoder.decode(list.get(1), "UTF-8");
			} catch (UnsupportedEncodingException e2) {
				wikiListURL = list.get(1);
			}
			wikiListName = list.get(1).replace("http://en.wikipedia.org/wiki/",
					"");

			System.out.println("Start processing of table " + wikiListName);

			/*
			 * Obtain plain Wiki Mark Up (formerly Test Data Set)
			 */
			System.out.println("Obtain Plain Wiki Mark Up ..");

			ReaderResource myTestRes = new ReaderResource(wikiListName);

			ListPageWikiMarkupReader myWikiReader = new ListPageWikiMarkupReader();

			TestDataSet myTestData = new TestDataSet();

			try {
				myWikiReader.openInput(myTestRes);
				myTestData.setWikiMarkUpList(myWikiReader.readInput());
			} catch (Exception e1) {
				e1.printStackTrace();
				// myWikiReader.close();
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
				// myWikiReader.close();
			}
			// myWikiReader.close();

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

			/*
			 * Initialize cache for literal query triples
			 */
			isFirstColumn = true;
			List<Triple<String, String, String>> myLiteralTriplesCache = new ArrayList<Triple<String, String, String>>();
			HashMap<String, String> myLiteralCache = new HashMap<String, String>();

			/*
			 * Loop through all columns of List
			 */

			for (int i = 0; i < myTestData.getFirstTable().get(0).size(); i++) {

				PropertyFinderResult
						.setNoOfInvestigatedCols(PropertyFinderResult
								.getNoOfInvestigatedCols() + 1);

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
				 * Initialize Triple Lists for this column
				 */
				List<Triple<String, String, String>> myUriTriples = new ArrayList<Triple<String, String, String>>();
				HashMap<String, String> myLiteralMap = new HashMap<String, String>();
				//List<Triple<String, String, String>> myLiteralTriples = new ArrayList<Triple<String, String, String>>();

				/*
				 * Iterate over rows of column
				 */
				int rowCounter = 0;

				for (String row : colValues) {
					String dbpValue = ProcessTable.wiki2dbpLink(ProcessTable
							.getLink(row));

					/*
					 * Generate Uri triples
					 */
					if (dbpValue != "") {
						Triple<String, String, String> myTriple = new Triple<String, String, String>();
						myTriple.setFirst(dbpediaResMap.get("row" + rowCounter));
						myTriple.setSecond("$property");
						myTriple.setThird(dbpValue);
						myUriTriples.add(myTriple);
					}
					/*
					 * Generate literal triples
					 */
					else {
						myLiteralMap.put(dbpediaResMap.get("row" + rowCounter), row);
					}

					/*
					 * Build up complete list of literal triples for the sake of
					 * caching
					 */
					if (isFirstColumn == true) {
						Triple<String, String, String> myTriple = new Triple<String, String, String>();
						myTriple.setFirst(dbpediaResMap.get("row" + rowCounter));
						myTriple.setSecond("$prop");
						myTriple.setThird("$value");
						myLiteralTriplesCache.add(myTriple);
					}
					rowCounter++;
				}

				isFirstColumn = false;

				/*
				 * Query DBPedia to find suitable Property
				 */

				PropertyFinderHelper myPropFinder = new PropertyFinderHelper();
				PropertyFinderResult myPropRes = null;
				ReaderResource myDbpRes = null;
				ListPageDBPediaReader myDbpReader = new ListPageDBPediaReader();

				try {
					if (myUriTriples.size() > 0) {

						myDbpRes = new ReaderResource(myUriTriples);
						myDbpReader.openInput(myDbpRes);

						myPropRes = myPropFinder.findUriProperty(myDbpReader
								.readInput());
						if (!myPropFinder.returnMaxConfidence(myPropRes)
								.equals("error")) {
							dbpediaAttributes.add(myPropFinder
									.returnMaxConfidence(myPropRes));
							PropertyFinderResult.setNoOfFoundCols(PropertyFinderResult
									.getNoOfFoundCols() + 1);
						} else if (myLiteralMap.size() > 0) {
							if (myLiteralCache.size() == 0) {
								myDbpRes = new ReaderResource(
										myLiteralTriplesCache);
								myDbpReader = new ListPageDBPediaReader();
								myDbpReader.openInput(myDbpRes);
								myLiteralCache = myDbpReader.readInput();
							}
							myPropRes = myPropFinder
									.findLiteralProperty(myLiteralCache, myLiteralMap);
							dbpediaAttributes.add(myPropFinder
									.returnMaxConfidence(myPropRes));
							PropertyFinderResult.setNoOfFoundCols(PropertyFinderResult
									.getNoOfFoundCols() + 1);
						}

					} else if (myLiteralMap.size() > 0 && myUriTriples.size() == 0) {
						if (myLiteralCache.size() == 0) {
							myDbpRes = new ReaderResource(myLiteralTriplesCache);
							myDbpReader = new ListPageDBPediaReader();
							myDbpReader.openInput(myDbpRes);
							myLiteralCache = myDbpReader.readInput();
						}
						myPropRes = myPropFinder
								.findLiteralProperty(myLiteralCache, myLiteralMap);
						dbpediaAttributes.add(myPropFinder
								.returnMaxConfidence(myPropRes));
						PropertyFinderResult.setNoOfFoundCols(PropertyFinderResult
								.getNoOfFoundCols() + 1);
					}
					else {
						dbpediaAttributes.add("error");								
					}
				} catch (Exception e) {
					e.printStackTrace();
					dbpediaAttributes.add("error");
					columnPosition++;
					myDbpReader.close();
					continue;
				}
				myDbpReader.close();

				/*
				 * Write Resulting confidences to file
				 */
				if (myPropRes.getMap().size() > 0) {

					try {
						int fileCount = 1;
						File file = new File(pathToResult
								+ "results/confidences_"
								+ wikiListName.replace('/', '_')
										.replace(':', '_').replace('"', '_')
								+ "_columne_" + i + ".csv");
						if (file.exists()) {
							myPropRes.writeOutputToCsv(
									pathToResult
											+ "results/evaluation_"
											+ wikiListName.replace('/', '_')
													.replace(':', '_')
													.replace('"', '_')
											+ "_column_" + i + "_"
											+ fileCount++ + ".csv",
									myPropRes.getMap());
						}

						else {
							myPropRes.writeOutputToCsv(
									pathToResult
											+ "results/evaluation_"
											+ wikiListName.replace('/', '_')
													.replace(':', '_')
													.replace('"', '_')
											+ "_column_" + i + ".csv",
									myPropRes.getMap());
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				System.out.println("done with column " + i + "!");
				columnPosition++;
			}

			System.out.println(dbpediaAttributes.toString());
			myRowList.add(myCurrRow);

		}

		/*
		 * Pass List of Table Rows to new value evaluation
		 */
		System.out.println("No of columns look at: "
				+ PropertyFinderResult.getNoOfInvestigatedCols());
		System.out.println("No of properties found: "
				+ PropertyFinderResult.getNoOfFoundCols());
		// CopyOfWikiList.wikiListEvaluation(myRowList);

	}

}
