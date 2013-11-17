package de.unimannheim.dws.wikilist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.unimannheim.dws.wikilist.evaluation.EvaluationDataSet;
import de.unimannheim.dws.wikilist.evaluation.TestDataSet;
import de.unimannheim.dws.wikilist.reader.ListPageCSVReader;
import de.unimannheim.dws.wikilist.reader.ListPageDBPediaReader;
import de.unimannheim.dws.wikilist.reader.ListPageWikiMarkupReader;
import de.unimannheim.dws.wikilist.reader.ReaderResource;
import de.unimannheim.dws.wikilist.util.EvaluationResult;
import de.unimannheim.dws.wikilist.util.ProcessTable;

public class CopyOfWikiList {

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

		/*
		 * General settings for this run
		 */

		for (List<String> list : myListsList) {

			if (list.get(0).contains("X"))
				continue;

			if (!list.get(3).equals("Table"))
				continue;

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

			List<String> dbpediaAttributes = new ArrayList<String>();
			for (int i = 6; i < list.size(); i++) {
				dbpediaAttributes.add(list.get(i));
			}

			/*
			 * Find corresponding list of DBPedia instances
			 */
			List<String> dbpediaResources = ProcessTable.getColumn(
					myTestData.getFirstTable(), columnInstance);

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
			if (dbpediaResources.size() == 0 || dbpediaAttributes.size() == 0)
				continue;

			/*
			 * Reset column counter.
			 */
			columnPosition = 0;

			for (String string : dbpediaAttributes) {

				/*
				 * If column is empty, continue
				 */
				if (string == null || string.trim().equals("")) {
					columnPosition++;
					continue;
				}

				/*
				 * If column is instance column, continue
				 */
				if (columnPosition == columnInstance
						|| string.equals("Column of Entity")) {
					columnPosition++;
					continue;
				}

				System.out.println("Start processing of attribute " + string);

				String[] dbpediaAttribute = string.split(":");
				rdfTag = dbpediaAttribute[1];
				rdfTagPrefix = dbpediaAttribute[0];

				/****************************************************************
				 * Obtain Evaluation Data Set with JWPL and DBPedia Values
				 * merged
				 ****************************************************************/

				System.out.println("Obtain Evaluation Data Set ..");

				ReaderResource myEvalRes = new ReaderResource(dbpediaResources,
						rdfTag, rdfTagPrefix);

				ListPageDBPediaReader myDBPReader = new ListPageDBPediaReader();
				myDBPReader.openInput(myEvalRes);

				HashMap<String, String> myDBPValues;
				// try {
				myDBPValues = myDBPReader.readInput();
				// } catch (Exception e) {
				// e.printStackTrace();
				// columnPosition++;
				// continue;
				// }
				myDBPReader.close();

				EvaluationDataSet myEvalData = new EvaluationDataSet();
				try {
					myEvalData.create(myTestData, myDBPValues);
				} catch (Exception e) {
					e.printStackTrace();
					columnPosition++;
					continue;
				}

				List<List<String>> myEvalMatrix = myEvalData.getEvalRes()
						.getEvalMatrix();

				/*
				 * Write Result Matrix to file
				 */

				try {
					int fileCount = 1;
					File file = new File(pathToResult
							+ "results/evaluation_"
							+ wikiListName.replace('/', '_').replace(':', '_')
									.replace('"', '_') + "_" + rdfTag + ".csv");
					if (file.exists()) {
						myEvalData.getEvalRes().writeOutputToCsv(
								pathToResult
										+ "results/evaluation_"
										+ wikiListName.replace('/', '_')
												.replace(':', '_')
												.replace('"', '_') + "_"
										+ rdfTag + "_" + fileCount++ + ".csv",
								myEvalMatrix);
					}

					else {
						myEvalData.getEvalRes().writeOutputToCsv(
								pathToResult
										+ "results/evaluation_"
										+ wikiListName.replace('/', '_')
												.replace(':', '_')
												.replace('"', '_') + "_"
										+ rdfTag + ".csv", myEvalMatrix);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				

				/*
				 * Calculate Totals
				 */
				evalRes.add(myEvalData.getEvalRes());
				try {
					evalRes.writeOutputToCsv(pathToResult
							+ "results/evaluation_total.csv",
							evalRes.getEvalMatrix());
				} catch (IOException e) {
					e.printStackTrace();
				}			
				catch (Exception e) {
					e.printStackTrace();
				}
				

				/*
				 * Prepare RDF Output
				 */
				List<List<String>> myRdfTriples = myEvalData.getEvalRes()
						.getRdfTriples();

				if (myRdfTriples != null) {

					// List<Model> newRdfTriples = new ArrayList<Model>();
					// for (List<String> list2 : myRdfTriples) {
					//
					// // Create an empty graph
					// Model model = ModelFactory.createDefaultModel();
					//
					// // Create the resource
					// Resource postcon = model.createResource(list2
					// .get(0));
					//
					// // Create the predicate (property)
					// Property value = model.createProperty(
					// list2.get(1), "value");
					//
					// // Add the properties with associated values
					// // (objects)
					// postcon.addProperty(value, list2.get(2));
					// // postcon.addProperty(related,
					// //
					// "http://burningbird.net/articles/monsters2.htm");
					//
					// // Print RDF/XML of model to system output
					// model.write(new PrintWriter(System.out));
					// newRdfTriples.add(model);
					// }

					/*
					 * Write new RDF Triples to File
					 */
					try {
						int fileCount = 1;
						File file = new File(pathToResult
								+ "results/newTriples_"
								+ wikiListName.replace('/', '_').replace(':', '_')
										.replace('"', '_') + "_" + rdfTag + ".csv");
						if (file.exists()) {
							myEvalData.getEvalRes().writeOutputToCsv(
									pathToResult
											+ "results/newTriples_"
											+ wikiListName.replace('/', '_')
													.replace(':', '_')
													.replace('"', '_') + "_"
											+ rdfTag + "_" + fileCount++ + ".csv",
											myRdfTriples);
						}

						else {
							myEvalData.getEvalRes().writeOutputToCsv(
									pathToResult
											+ "results/newTriples_"
											+ wikiListName.replace('/', '_')
													.replace(':', '_')
													.replace('"', '_') + "_"
											+ rdfTag + ".csv", myRdfTriples);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					
				}

				System.out.println("done!");
				columnPosition++;
			}
		}
	}

}
