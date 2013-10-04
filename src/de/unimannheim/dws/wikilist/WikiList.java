package de.unimannheim.dws.wikilist;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.unimannheim.dws.wikilist.evaluation.GoldDataSet;
import de.unimannheim.dws.wikilist.evaluation.TestDataSet;
import de.unimannheim.dws.wikilist.evaluation.TrainingsDataSet;
import de.unimannheim.dws.wikilist.reader.ListPageCSVReader;
import de.unimannheim.dws.wikilist.reader.ListPageDBPediaReader;
import de.unimannheim.dws.wikilist.reader.ListPageWikiMarkupReader;
import de.unimannheim.dws.wikilist.reader.ReaderResource;
import de.unimannheim.dws.wikilist.util.AnnotationHelper;
import edu.cmu.minorthird.text.BasicTextLabels;
import edu.cmu.minorthird.text.TextBase;
import edu.cmu.minorthird.text.TextBaseLoader;
import edu.cmu.minorthird.text.gui.TextBaseEditor;
import edu.cmu.minorthird.text.learn.AnnotatorLearner;
import edu.cmu.minorthird.text.learn.AnnotatorTeacher;
import edu.cmu.minorthird.text.learn.TextLabelsAnnotatorTeacher;
import edu.cmu.minorthird.ui.Recommended;

public class WikiList {

	/*
	 * Class variables
	 */
	public static String rdfTag = "";
	public static String rdfTagPrefix = "";
	public static String wikiListURL = "";
	public static String wikiListName = "";
	public static String regexInstances = "";
	public static String captureGroup = "";
	public static String regexAttribute = "";

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) {
		try {
			/*
			 * Read List of Instances
			 */
			System.out.println("Read instance list ..");

			ReaderResource myCSVRes = new ReaderResource(
					"D:/Studium/Classes_Sem3/Seminar/Codebase/Links_to_Instances.csv");

			ListPageCSVReader myInstancesReader = new ListPageCSVReader();
			myInstancesReader.openInput(myCSVRes);
			List<List<String>> myInstancesList = myInstancesReader.readInput();
			myInstancesReader.close();
			List<String> myNobleList = myInstancesList.get(0);
			
			System.out.println("done!");


			/*
			 * General settings for this run
			 */
			System.out.println("General settings ..");
			
			rdfTag = "title";
			rdfTagPrefix = "dbpprop";
			wikiListURL = myNobleList.get(0);
			wikiListName = wikiListURL.replace("http://en.wikipedia.org/wiki/",
					"");
			regexInstances = myNobleList.get(1);
			captureGroup = myNobleList.get(2);
			regexAttribute = "(\\[[A-Za-z0-9,' ]*\\|)";
			List<String> dbpediaResources = new ArrayList<String>();
			for (int i = 3; i < myNobleList.size(); i++) {
				dbpediaResources.add(myNobleList.get(i));
			}
			
			System.out.println("done!");
			
			
			/*
			 * Obtain Test Data Set (plain Wiki Mark Up)
			 */
			System.out.println("Obtain Test Data Set ..");
			
			ReaderResource myTestRes = new ReaderResource(wikiListName);

			ListPageWikiMarkupReader myWikiReader = new ListPageWikiMarkupReader();
			myWikiReader.openInput(myTestRes);

			TestDataSet myTestData = new TestDataSet();
			myTestData.create(myWikiReader.readInput(), null);
			myWikiReader.close();
			
			System.out.println("done!");

			/*
			 * Obtain Gold Standard Data Set with JWPL and Regex
			 */
			System.out.println("Obtain Gold Data Set ..");
			
			GoldDataSet myGoldData = new GoldDataSet();
			myGoldData.create(myTestData.getWikiMarkUpList(), null);
			myGoldData.writeOutputToFile(
					"D:/Studium/Classes_Sem3/Seminar/Codebase/gold_title.txt",
					myGoldData.toString());
			
			System.out.println("done!");

			/*
			 * Obtain Training Data Set with JWPL and DBPedia Values
			 */
			System.out.println("Obtain Training Data Set ..");
			
			ReaderResource myTrainRes = new ReaderResource(dbpediaResources,
					rdfTag, rdfTagPrefix);

			ListPageDBPediaReader myDBPReader = new ListPageDBPediaReader();
			myDBPReader.openInput(myTrainRes);

			HashMap<String, String> myDBPValues = myDBPReader.readInput();
			
			TrainingsDataSet myTrainingsData = new TrainingsDataSet();
			myTrainingsData.create(myTestData.getWikiMarkUpList(), myDBPValues);
			myDBPReader.close();
			myTrainingsData
					.writeOutputToFile(
							"D:/Studium/Classes_Sem3/Seminar/Codebase/data/training_title.txt",
							myTrainingsData.toString());
			
			System.out.println("done!");
			
			/*
			 * List of used regexp: 
			 * African American Writers - birthDate:
			 * (1[0-9]{3}|2[0-9]{3}) 
			 * List of Peers 1330-1339 - title:
			 * (\[[A-Za-z0-9,' ]*\|)
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}

//		File dataDir = new File("D:/Studium/Classes_Sem3/Seminar/Codebase/data/");
//
//		// load the data
//		// TextBaseLoader baseLoader = new TextBaseLoader();
//		// TextBase base = new BasicTextBase();
//		// This detects XML markup, and makes it available with
//		// getFileMarkup(). If you don't have XML markup, use
//		// "baseLoader.loadDir(base,dataDir)" instead.
//		// baseLoader.loadTaggedFiles(base,dataDir);
//
//		try {
//			TextBaseLoader loader = new TextBaseLoader(
//					TextBaseLoader.DOC_PER_FILE, true);
//			loader.load(dataDir);
//			TextBase base = loader.getLabels().getTextBase();
//			System.out.println(loader.getLabels().toString());
//			BasicTextLabels labels = (BasicTextLabels) loader.getLabels();
//
//			for (String txt : labels.getTokenProperties()) {
//				System.out.println(txt);
//
//			}
//
//			// labels.saveAs(new
//			// File("D:/Studium/Classes_Sem3/Seminar/Codebase/result.txt"),
//			// "txt");
//
//			// labels.declareType("corrected");
//
//			TextBaseEditor editor = TextBaseEditor.edit(labels, null);
//			editor.getViewer().getGuessBox().setSelectedItem("candidate");
//			editor.getViewer().getTruthBox().setSelectedItem("corrected");
//
//			/*
//			 * Instantiate & run a Annotation Extractor
//			 */
//
//			AnnotatorTeacher annotTeacher = new TextLabelsAnnotatorTeacher(
//					labels, rdfTag);
//
//			AnnotatorLearner annotLearner = new Recommended.VPSMMLearner();
//
//			edu.cmu.minorthird.text.Annotator annot = annotTeacher
//					.train(annotLearner);
//
//			System.out.println(annot.explainAnnotation(labels, null));
//
//			// Load your data into a TextBase (extractor) or a DataSet
//			// (classifier).
//			// Instantiate an AnnotatorTeacher (extractor) or ClassifierTeacher
//			// (classifier).
//			// Configure the teacher.
//			// Instantiate an instance of AnnotatorLearner (extractor) or
//			// ClassifierLearner (classifier) the represents the desired learner
//			// algorithm.
//			// Configure the learner.
//			// Call teacher.train (learner) to create a trained extractor or
//			// classifier.
//
//		} catch (IOException e) {
//			e.printStackTrace();
//
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
	}	
}
