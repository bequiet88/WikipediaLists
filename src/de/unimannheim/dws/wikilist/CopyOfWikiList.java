package de.unimannheim.dws.wikilist;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import de.unimannheim.dws.wikilist.evaluation.EvaluationDataSet;
import de.unimannheim.dws.wikilist.evaluation.TestDataSet;
import de.unimannheim.dws.wikilist.reader.ListPageCSVReader;
import de.unimannheim.dws.wikilist.reader.ListPageDBPediaReader;
import de.unimannheim.dws.wikilist.reader.ListPageWikiMarkupReader;
import de.unimannheim.dws.wikilist.reader.ReaderResource;
import de.unimannheim.dws.wikilist.util.ProcessTable;
import edu.cmu.minorthird.classify.Splitter;
import edu.cmu.minorthird.classify.experiments.CrossValSplitter;
import edu.cmu.minorthird.text.Annotator;
import edu.cmu.minorthird.text.BasicTextLabels;
import edu.cmu.minorthird.text.Span;
import edu.cmu.minorthird.text.TextBase;
import edu.cmu.minorthird.text.TextBaseLoader;
import edu.cmu.minorthird.text.learn.AnnotatorLearner;
import edu.cmu.minorthird.text.learn.AnnotatorTeacher;
import edu.cmu.minorthird.text.learn.TextLabelsAnnotatorTeacher;
import edu.cmu.minorthird.text.learn.experiments.ExtractionEvaluation;
import edu.cmu.minorthird.text.learn.experiments.TextLabelsExperiment;
import edu.cmu.minorthird.ui.Recommended.CRFAnnotatorLearner;
import edu.cmu.minorthird.util.gui.ViewerFrame;

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
	public static String regexAttribute = "";
	public static int columnPosition = 0;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) {

		Logger log = Logger.getLogger(CopyOfWikiList.class);

		/*****************************
		 * Data Collection
		 *****************************/
		try {

			if (true) {
				/*
				 * Read List of Instances Read list of attributes available in
				 * one list of instances
				 */
				System.out.println("Read table list ..");

				ReaderResource myCSVRes = new ReaderResource(
						"D:/Studium/Classes_Sem3/Seminar/Codebase/sample_lists.csv");

				ListPageCSVReader myListsReader = new ListPageCSVReader();
				myListsReader.openInput(myCSVRes);
				List<List<String>> myListsList = myListsReader.readInput();
				myListsReader.close();

				System.out.println("done!");

				System.out.println("Read instance list ..");

				ReaderResource myCSVRes2 = new ReaderResource(
						"D:/Studium/Classes_Sem3/Seminar/Codebase/Links_to_Instances.csv");

				ListPageCSVReader myInstancesReader = new ListPageCSVReader();
				myInstancesReader.openInput(myCSVRes2);
				List<List<String>> myInstancesList = myInstancesReader
						.readInput();
				myInstancesReader.close();

				System.out.println("done!");

				/*
				 * General settings for this run
				 */

				for (List<String> list : myListsList) {

					if (list.get(0).contains("X"))
						continue;

					if (!list.get(3).equals("Table"))
						continue;

					wikiListURL = list.get(1);
					wikiListName = wikiListURL.replace(
							"http://en.wikipedia.org/wiki/", "");

					System.out.println("Start processing of table "
							+ wikiListName);

					List<String> dbpediaResources = new ArrayList<String>();

					/*
					 * Find corresponding list of DBPedia instances
					 */

					for (List<String> instanceList : myInstancesList) {

						if (instanceList.get(1).equals(wikiListURL)) {
							regexInstances = instanceList.get(1);
							captureGroup = instanceList.get(2);
							for (int i = 3; i < instanceList.size(); i++) {
								dbpediaResources.add(instanceList.get(i));
							}

						}
					}

					/*
					 * Read list of DBPedia Attributes which are clear without
					 * ambiguity
					 */
					try {
						columnInstance = Integer.parseInt(list.get(4)) - 1;
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
					 * If size > 0 iterate over the instances for each DBPedia
					 * attribute.
					 */
					if (dbpediaResources.size() == 0
							|| dbpediaAttributes.size() == 0)
						continue;

					/*
					 * Reset column counter.
					 */
					columnPosition = 0;

					for (String string : dbpediaAttributes) {

						// TODO: Implement new logic

						if (columnPosition == columnInstance)
							continue;

						System.out.println("Start processing of attribute "
								+ string);

						String[] dbpediaAttribute = string.split(":");
						rdfTag = dbpediaAttribute[1];
						rdfTagPrefix = dbpediaAttribute[0];
						regexAttribute = "(\\[[A-Za-z0-9,' ]*\\|)";

						/*
						 * Obtain plain Wiki Mark Up (formerly Test Data Set)
						 */
						System.out.println("Obtain Plain Wiki Mark Up ..");

						ReaderResource myTestRes = new ReaderResource(
								wikiListName);

						ListPageWikiMarkupReader myWikiReader = new ListPageWikiMarkupReader();
						myWikiReader.openInput(myTestRes);

						TestDataSet myTestData = new TestDataSet();
						myTestData.setWikiMarkUpList(myWikiReader.readInput());

						myTestData.setFirstTable(ProcessTable
								.parseFirstTable(myTestData.toString()));

						myTestData.writeOutputToFile(
								"D:/Studium/Classes_Sem3/Seminar/Codebase/testdata/test_"
										+ wikiListName + "_" + rdfTag + ".txt",
								myTestData.toString());
						myWikiReader.close();

						System.out.println("done!");

						// /*
						// * Obtain Gold Standard Data Set with JWPL and Regex
						// * TODO: Work with columns (may use JWPL library to
						// * access a specific column
						// */
						// System.out.println("Obtain Gold Data Set ..");
						//
						// GoldDataSet myGoldData = new GoldDataSet();
						// myGoldData.create(myTestData.getWikiMarkUpList(),
						// null);
						//
						// System.out.println("done!");

						/****************************************************************
						 * Obtain Evaluation Data Set with JWPL and DBPedia
						 * Values merged
						 ****************************************************************/

						System.out.println("Obtain Evaluation Data Set ..");

						ReaderResource myEvalRes = new ReaderResource(
								dbpediaResources, rdfTag, rdfTagPrefix);

						ListPageDBPediaReader myDBPReader = new ListPageDBPediaReader();
						myDBPReader.openInput(myEvalRes);

						HashMap<String, String> myDBPValues = myDBPReader
								.readInput();
						myDBPReader.close();

						EvaluationDataSet myEvalData = new EvaluationDataSet();
						myEvalData.create(myTestData, myDBPValues);

						List<List<String>> myEvalMatrix = myEvalData
								.getEvalMatrix();

						myEvalData.writeOutputToCsv(
								"D:/Studium/Classes_Sem3/Seminar/Codebase/traindata/evaluation_"
										+ wikiListName + "_" + rdfTag + ".csv",
								myEvalMatrix);

						/*
						 * TODO: Prepare RDF Output
						 */

						System.out.println("done!");
						columnPosition++;
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();

		} catch (ParseException e) {
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
