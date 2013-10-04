package de.unimannheim.dws.wikilist;

import java.util.ArrayList;
import java.util.List;

import de.unimannheim.dws.wikilist.reader.IListPageReader;
import de.unimannheim.dws.wikilist.reader.ListPageWikiMarkupReader;
import de.unimannheim.dws.wikilist.reader.ReaderResource;
import de.unimannheim.dws.wikilist.util.Annotator;

public class WikiList {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args){

		/*
		 * Obtain Goldstandard with JWPL and Regex -> MinorThird XML Mark-Up File
		 */
		
		ListPageWikiMarkupReader myReader = new ListPageWikiMarkupReader();

		//myReader.openInput("D:/Studium/Classes_Sem3/Seminar/Codebase/african_american_writer.txt");
		
		ReaderResource myRes = new ReaderResource("List_of_Peers_1330-1339");
		
		myReader.openInput(myRes);
		
		
		List<String> myContent;
		try {
			myContent = (ArrayList<String>) myReader.readInput();
			/*
			 * List of used regexp:
			 * 	African American Writers
			 *   birthDate: (1[0-9]{3}|2[0-9]{3})
			 *  List of Peers 1330-1339
			 *  TODO finde regex für title: 
			 */
			
			
			String RDFtag = "";
			Annotator myAnnot = new Annotator("([A-Za-z0-9,' ]*\\|)?([A-Za-z0-9,' ]*)\\]\\]\\|\\|1(2|3)",
					RDFtag);

			myAnnot.annotate(myContent);

			System.out.println(myAnnot.toString());
			
			//myReader.writeOutput("D:/data_server/eclipse/DBPediaListAnnotator/data/annot_african_american_writer.txt", myAnnot.toString());
			myReader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		 * TODO Read CSV file with DBPedia instances
		 */


		/*
		 * TODO Retrieve Attributes from DBPedia with SPARQL
		 */
		
		/*
		 * TODO Mark matching attributes in JWPL -> MinorThird XML Mark-Up File 
		 */
		
		
		
		/*
		 * Load File with XML Mark-Ups into MinorThird
		 */
		
//		File dataDir = new File("data");
//
//		// load the data
//		// TextBaseLoader baseLoader = new TextBaseLoader();
//		// TextBase base = new BasicTextBase();
//		// This detects XML markup, and makes it available with
//		// getFileMarkup(). If you don't have XML markup, use
//		// "baseLoader.loadDir(base,dataDir)" instead.
//		// baseLoader.loadTaggedFiles(base,dataDir);
//
//		
//		try {
//			TextBaseLoader loader = new TextBaseLoader(
//					TextBaseLoader.DOC_PER_FILE, true);
//			loader.load(dataDir);
//			TextBase base = loader.getLabels().getTextBase();
//			System.out.println(loader.getLabels().toString());
//			BasicTextLabels labels = (BasicTextLabels) loader.getLabels();
//			
//			
//			for(String txt: labels.getTokenProperties()) {
//				System.out.println(txt);
//				
//			}
//			
//			//labels.saveAs(new File("D:/Studium/Classes_Sem3/Seminar/Codebase/result.txt"), "txt");
//			
//			
//			//labels.declareType("corrected");
//
//			TextBaseEditor editor = TextBaseEditor.edit(labels, null);
//			editor.getViewer().getGuessBox().setSelectedItem("candidate");
//			editor.getViewer().getTruthBox().setSelectedItem("corrected");
//			
//		/*
//		 * Instantiate & run a Annotation Extractor
//		 */
//			
//			AnnotatorTeacher annotTeacher = new TextLabelsAnnotatorTeacher(labels, tag);
//			
//			AnnotatorLearner annotLearner = new Recommended.VPSMMLearner();
//			
//			
//			edu.cmu.minorthird.text.Annotator annot = annotTeacher.train(annotLearner);
//			
//			
//			System.out.println(annot.explainAnnotation(labels, null));

//		    Load your data into a TextBase (extractor) or a DataSet (classifier).
//		    Instantiate an AnnotatorTeacher (extractor) or ClassifierTeacher (classifier).
//		    Configure the teacher.
//		    Instantiate an instance of AnnotatorLearner (extractor) or ClassifierLearner (classifier) the represents the desired learner algorithm.
//		    Configure the learner.
//		    Call teacher.train (learner) to create a trained extractor or classifier.
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}

		
		

	}

}
