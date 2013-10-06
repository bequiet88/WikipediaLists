package de.unimannheim.dws.wikilist.test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;

import edu.cmu.minorthird.classify.Splitter;
import edu.cmu.minorthird.classify.experiments.CrossValSplitter;
import edu.cmu.minorthird.classify.experiments.FixedTestSetSplitter;
import edu.cmu.minorthird.text.BasicTextLabels;
import edu.cmu.minorthird.text.Span;
import edu.cmu.minorthird.text.TextBase;
import edu.cmu.minorthird.text.TextBaseLoader;
import edu.cmu.minorthird.text.gui.TextBaseEditor;
import edu.cmu.minorthird.text.learn.AnnotatorLearner;
import edu.cmu.minorthird.text.learn.AnnotatorTeacher;
import edu.cmu.minorthird.text.learn.TextLabelsAnnotatorTeacher;
import edu.cmu.minorthird.text.learn.experiments.ExtractionEvaluation;
import edu.cmu.minorthird.text.learn.experiments.TextLabelsExperiment;
import edu.cmu.minorthird.ui.Recommended.VPSMMLearner2;
import edu.cmu.minorthird.util.gui.ViewerFrame;

/**
 * Example of how to label data with the text package. Invoke this with
 * arguments: DATADIR LABELFILE [mixupFile].
 * 
 * For a demo, add demos\sampleMixup\ to your classpath and invoke it with no
 * arguments.
 * 
 * @author wcohen
 */
public class MinorThirdPlayground {
	public static void main(String[] args) {
		File dataDir = new File(
				"D:/Studium/Classes_Sem3/Seminar/Codebase/training_title.txt");
		File testDir = new File(
				"D:/Studium/Classes_Sem3/Seminar/Codebase/gold_title.txt");

		// load the data
		// TextBaseLoader baseLoader = new TextBaseLoader();
		// TextBase base = new BasicTextBase();
		// This detects XML markup, and makes it available with
		// getFileMarkup(). If you don't have XML markup, use
		// "baseLoader.loadDir(base,dataDir)" instead.
		// baseLoader.loadTaggedFiles(base,dataDir);

		try {
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
			//
			//
			TextBaseEditor editor = TextBaseEditor.edit(labels, null);
			editor.getViewer().getGuessBox().setSelectedItem("title");

			/*
			 * Load Test Data into MinorThird
			 */

			TextBaseLoader testLoader = new TextBaseLoader(
					TextBaseLoader.DOC_PER_LINE, TextBaseLoader.USE_XML);
			TextBase testBase = testLoader.load(testDir);
			BasicTextLabels testLabels = (BasicTextLabels) loader.getLabels();

			/*
			 * Instantiate an Annotator
			 */

			AnnotatorTeacher annotTeacher = new TextLabelsAnnotatorTeacher(
					labels, "title");

			AnnotatorLearner annotLearner = new VPSMMLearner2();

			edu.cmu.minorthird.text.Annotator annot = annotTeacher
					.train(annotLearner);

			/*
			 * Create a Splitter
			 */

			Splitter<Span> crossValSplitter = new CrossValSplitter<Span>(5);

			Splitter<Span> fixedTestSplitter = new FixedTestSetSplitter<Span>(
					testBase.documentSpanIterator());

			/*
			 * Run Experiment
			 */

			 TextLabelsExperiment experiment = new
			 TextLabelsExperiment(labels,
			 crossValSplitter, annotLearner, "title", "newTitles");

//			TextLabelsExperiment experiment = new TextLabelsExperiment(labels,
//					fixedTestSplitter, testLabels, annotLearner, "title", null,
//					"newTitles");

			experiment.doExperiment();
			new ViewerFrame("Annotation Learner Experiment", experiment.toGUI());

			
			Iterator<Span> resIter = experiment.getTestLabels().closureIterator("newTitles");
			
			while (resIter.hasNext()) {
				Span currSpan = spanIter.next();

				System.out.println(currSpan.asString() + " "
						+ currSpan.getDocumentId());

			}



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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}