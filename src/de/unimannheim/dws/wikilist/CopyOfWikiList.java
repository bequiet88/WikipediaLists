package de.unimannheim.dws.wikilist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.unimannheim.dws.wikilist.evaluation.EvaluationDataSet;
import de.unimannheim.dws.wikilist.evaluation.TestDataSet;
import de.unimannheim.dws.wikilist.models.EvaluationResult;
import de.unimannheim.dws.wikilist.models.TableRow;
import de.unimannheim.dws.wikilist.reader.ListPageCSVReader;
import de.unimannheim.dws.wikilist.reader.ListPageDBPediaReader;
import de.unimannheim.dws.wikilist.reader.ListPageWikiMarkupReader;
import de.unimannheim.dws.wikilist.reader.ReaderResource;
import de.unimannheim.dws.wikilist.util.ProcessTable;

/**
 * The Class CopyOfWikiList.
 */
public class CopyOfWikiList {

	/*
	 * Class variables
	 */
	/** The rdf tag. */
	public static String rdfTag = "";

	/** The rdf tag prefix. */
	public static String rdfTagPrefix = "";
	
	/** The rdf property url. */
	public static String rdfPropUrl = "";

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
	public static String pathToResult = "D:/Studium/Classes_Sem3/Seminar/Codebase/";

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

		System.out.println("done!");

		List<TableRow> myRowList = new ArrayList<TableRow>();

		for (List<String> list : myListsList) {

			TableRow tblr = new TableRow(list);

			List<String> dbpediaAttributes = new ArrayList<String>();
			for (int i = 6; i < list.size(); i++) {
				dbpediaAttributes.add(list.get(i));
			}
			tblr.setDbpediaAttributes(dbpediaAttributes);
			myRowList.add(tblr);
		}

		CopyOfWikiList.wikiListEvaluation(myRowList);

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

	}

	/**
	 * Wiki list evaluation.
	 * 
	 * @param myListsList
	 *            the my lists list
	 */
	public static void wikiListEvaluation(List<TableRow> myListsList) {

		/*
		 * General settings for this run
		 */

		for (TableRow row : myListsList) {

			List<String> list = row.getMyTableRow();

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
			if (dbpediaResources.size() == 0
					|| row.getDbpediaAttributes().size() == 0)
				continue;

			/*
			 * Reset column counter.
			 */
			columnPosition = 0;

			for (String string : row.getDbpediaAttributes()) {

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
						|| string.equals("Column of Entity")
						|| string.equals("error")) {
					columnPosition++;
					continue;
				}

				System.out.println("Start processing of attribute " + string);

				if (string.startsWith("<http")) {

					rdfPropUrl = string;
					
					if (string.contains("http://xmlns.com/foaf/0.1/")) {
						String[] dbpediaAttribute = string.split("/");
						rdfTag = dbpediaAttribute[5];
						rdfTagPrefix = dbpediaAttribute[3];
					} else if (string.contains("http://purl.org/dc/elements/1.1/")) {
						String[] dbpediaAttribute = string.split("/");
						rdfTag = dbpediaAttribute[6];
						rdfTagPrefix = dbpediaAttribute[3];
					} else if (string.contains("http://dbpedia.org/resource/")) {
						String[] dbpediaAttribute = string.split("/");
						rdfTag = dbpediaAttribute[4];
						rdfTagPrefix = "";
					} else if (string.contains("http://dbpedia.org/property/")) {
						String[] dbpediaAttribute = string.split("/");
						rdfTag = dbpediaAttribute[4];
						rdfTagPrefix = "dbpprop";
					} else if (string.contains("http://dbpedia.org/ontology/")) {
						String[] dbpediaAttribute = string.split("/");
						rdfTag = dbpediaAttribute[4];
						rdfTagPrefix = "dbpedia-owl";
					} else if (string.contains("http://dbpedia.org/")) {
						String[] dbpediaAttribute = string.split("/");
						rdfTag = dbpediaAttribute[3];
						rdfTagPrefix = "dbpedia";
					}
				} else {
					String[] dbpediaAttribute = string.split(":");
					rdfTag = dbpediaAttribute[1];
					rdfTagPrefix = dbpediaAttribute[0];
					
					if(rdfTagPrefix.equals("foaf")) {
						rdfPropUrl = "<http://xmlns.com/foaf/0.1/"+rdfTag+">";
					}
					else if (rdfTagPrefix.equals("dc")) {
						rdfPropUrl = "<http://purl.org/dc/elements/1.1/"
								+ rdfTag + ">";
					}
					else if (rdfTagPrefix.equals("")) {
						rdfPropUrl = "<http://dbpedia.org/resource/"
								+ rdfTag + ">";
					}
					else if (rdfTagPrefix.equals("dbpprop")) {
						rdfPropUrl = "<http://dbpedia.org/property/"
								+ rdfTag + ">";
					}
					else if (rdfTagPrefix.equals("dbpedia-owl") || rdfTagPrefix.equals("owl")) {
						rdfPropUrl = "<http://dbpedia.org/ontology/"
								+ rdfTag + ">";
					}
					else if (rdfTagPrefix.equals("dbpedia")) {
						rdfPropUrl = "<http://dbpedia.org/"
								+ rdfTag + ">";
					}

				}

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
				} catch (Exception e) {
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
				} catch (Exception e) {
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
								+ wikiListName.replace('/', '_')
										.replace(':', '_').replace('"', '_')
								+ "_" + rdfTag + ".csv");
						if (file.exists()) {
							myEvalData.getEvalRes().writeOutputToCsv(
									pathToResult
											+ "results/newTriples_"
											+ wikiListName.replace('/', '_')
													.replace(':', '_')
													.replace('"', '_') + "_"
											+ rdfTag + "_" + fileCount++
											+ ".csv", myRdfTriples);
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
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

				System.out.println("done!");
				columnPosition++;
			}
		}
	}

}
