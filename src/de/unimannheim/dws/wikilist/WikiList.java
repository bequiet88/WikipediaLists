package de.unimannheim.dws.wikilist;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import de.unimannheim.dws.wikilist.evaluation.GoldDataSet;
import de.unimannheim.dws.wikilist.evaluation.TestDataSet;
import de.unimannheim.dws.wikilist.evaluation.TrainingsDataSet;
import de.unimannheim.dws.wikilist.reader.ListPageCSVReader;
import de.unimannheim.dws.wikilist.reader.ListPageDBPediaReader;
import de.unimannheim.dws.wikilist.reader.ListPageWikiMarkupReader;
import de.unimannheim.dws.wikilist.reader.ReaderResource;
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

		Logger log = Logger.getLogger(WikiList.class);

		/*****************************
		 * Data Collection
		 *****************************/
		try {

			/*
			 * Read List of Instances TODO: Make a loop for more than one row in
			 * Instance List TODO: Read list of attributes available in one list
			 * of instances
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

			if (false) {
				/*
				 * Obtain Test Data Set (plain Wiki Mark Up)
				 */
				System.out.println("Obtain Test Data Set ..");

				ReaderResource myTestRes = new ReaderResource(wikiListName);

				ListPageWikiMarkupReader myWikiReader = new ListPageWikiMarkupReader();
				myWikiReader.openInput(myTestRes);

				TestDataSet myTestData = new TestDataSet();
				myTestData.create(myWikiReader.readInput(), null);
				myTestData.writeOutputToFile(
						"D:/Studium/Classes_Sem3/Seminar/Codebase/testdata/test_"
								+ wikiListName + "_" + rdfTag + ".txt",
						myTestData.toString());
				myWikiReader.close();

				System.out.println("done!");

				/*
				 * Obtain Gold Standard Data Set with JWPL and Regex TODO: Work
				 * with columns (may use JWPL library to access a specific
				 * column
				 */
				System.out.println("Obtain Gold Data Set ..");

				GoldDataSet myGoldData = new GoldDataSet();
				myGoldData.create(myTestData.getWikiMarkUpList(), null);

				System.out.println("done!");

				/*
				 * Obtain Training Data Set with JWPL and DBPedia Values
				 */
				System.out.println("Obtain Training Data Set ..");

				ReaderResource myTrainRes = new ReaderResource(
						dbpediaResources, rdfTag, rdfTagPrefix);

				ListPageDBPediaReader myDBPReader = new ListPageDBPediaReader();
				myDBPReader.openInput(myTrainRes);

				HashMap<String, String> myDBPValues = myDBPReader.readInput();

				TrainingsDataSet myTrainingsData = new TrainingsDataSet();
				myTrainingsData.create(myTestData.getWikiMarkUpList(),
						myDBPValues);
				myDBPReader.close();
				myTrainingsData.writeOutputToFile(
						"D:/Studium/Classes_Sem3/Seminar/Codebase/traindata/training_"
								+ wikiListName + "_" + rdfTag + ".txt",
						myTrainingsData.toString());

				System.out.println("done!");

				System.out.println("Marked attributes in Gold: "
						+ myGoldData.getNoOfMarkedAttributes()
						+ " - Marked attributes in Training: "
						+ myTrainingsData.getNoOfMarkedAttributes());

			}

			/*
			 * List of used regexp: African American Writers - birthDate:
			 * (1[0-9]{3}|2[0-9]{3}) List of Peers 1330-1339 - title:
			 * (\[[A-Za-z0-9,' ]*\|)
			 */

			File dataDir = new File(
					"D:/Studium/Classes_Sem3/Seminar/Codebase/traindata/training_"
							+ wikiListName + "_" + rdfTag + ".txt");
			File testDir = new File(
					"D:/Studium/Classes_Sem3/Seminar/Codebase/testdata/test_"
							+ wikiListName + "_" + rdfTag + ".txt");

			/*****************************
			 * MinorThird
			 *****************************/

			/*
			 * Load Training Data into MinorThird
			 */

			TextBaseLoader loader = new TextBaseLoader(
					TextBaseLoader.DOC_PER_LINE, TextBaseLoader.USE_XML);
			TextBase base = loader.load(dataDir);
			Iterator<Span> spanIter = base.documentSpanIterator();

			System.out.println("Spans of Text Base\n");

			while (spanIter.hasNext()) {
				Span currSpan = spanIter.next();

				System.out.println(currSpan.asString() + " "
						+ currSpan.getDocumentId());

			}

			System.out
					.println("Print Labels\n" + loader.getLabels().toString());
			BasicTextLabels labels = (BasicTextLabels) loader.getLabels();

			// labels.saveAs(new File(
			// "D:/Studium/Classes_Sem3/Seminar/Codebase/labels_title.txt"),
			// "Minorthird TextLabels");
			// TextBaseEditor editor = TextBaseEditor.edit(labels, null);
			// editor.getViewer().getGuessBox().setSelectedItem("title");

			/*
			 * Load Test Data into MinorThird
			 */

			TextBaseLoader testLoader = new TextBaseLoader(
					TextBaseLoader.DOC_PER_LINE, TextBaseLoader.USE_XML);
			testLoader.load(testDir);
			BasicTextLabels testLabels = (BasicTextLabels) loader.getLabels();

			/*
			 * Instantiate an Annotator
			 */

			long startTime = System.nanoTime();
			AnnotatorTeacher annotTeacher = new TextLabelsAnnotatorTeacher(
					labels, "title");

			AnnotatorLearner annotLearner = new CRFAnnotatorLearner();

			Annotator annot = annotTeacher.train(annotLearner);
			long endTime = System.nanoTime();

			long elapsedTime = endTime - startTime;

			double elapsedTimeSec = elapsedTime / 1000000000;

			
			/*
			 * Create a Splitter
			 */

			Splitter<Span> crossValSplitter = new CrossValSplitter<Span>(3);

			/*
			 * Run Experiment
			 */

			TextLabelsExperiment experiment = new TextLabelsExperiment(labels,
					crossValSplitter, annotLearner, "title", "newTitles");

			/*
			 * Get Model Evaluation
			 */

			experiment.doExperiment();
			new ViewerFrame("Annotation Learner Experiment", experiment.toGUI());
			ExtractionEvaluation eval = experiment.getEvaluation();

			/*
			 * Apply model to Testdata Set
			 */

			annot.annotate(testLabels);

			System.out.println("\nPredicted Entries:\n");

			int counter = 0;

			for (Iterator<Span> i = testLabels.instanceIterator("_prediction"); i
					.hasNext();) {
				Span span = i.next();
				System.out.println(span.toString());
				counter++;
			}

			System.out.println("Number of predicted attributes: " + counter);
			System.out.println("Elapsed Time for Training (sec)"
					+ elapsedTimeSec);


			// myTestData.setNoOfMarkedAttributes(counter);

			/*
			 * Evaluation
			 * 
			 * TODO: Evaluate against Gold Data Set a) New information found b)
			 * Higher quality (literal information becoming URI information) c)
			 * No new data found
			 * 
			 * TODO: Also save properties of classification run (eval) such as:
			 * Learner name, time elapsed, confusion matrix, f-score
			 */

			
			/*
			 * TODO: Prepare RDF Output
			 */

			// Load your data into a TextBase (extractor) or a DataSet
			// (classifier).
			// Instantiate an AnnotatorTeacher (extractor) or ClassifierTeacher
			// (classifier).
			// Configure the teacher.
			// Instantiate an instance of AnnotatorLearner (extractor) or
			// ClassifierLearner (classifier) the represents the desired learner
			// algorithm.
			// Configure the learner.
			// Call teacher.train (learner) to create a trained extractor or
			// classifier.

		} catch (IOException e) {
			e.printStackTrace();

		} catch (ParseException e) {
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
