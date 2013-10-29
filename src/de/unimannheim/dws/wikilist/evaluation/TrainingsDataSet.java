package de.unimannheim.dws.wikilist.evaluation;

import java.util.HashMap;

import de.unimannheim.dws.wikilist.WikiList;
import de.unimannheim.dws.wikilist.util.AnnotationHelper;

public class TrainingsDataSet extends DataSet {

	@Override
	public void create(DataSet dataSet,
			HashMap<String, String> dbpediaValues) throws Exception  {
		
		this.dbpediaValues = dbpediaValues;

		AnnotationHelper helper = new AnnotationHelper();
		
		this.annotWikiMarkUpList = helper.annotateForTraining(dataSet.getWikiMarkUpList(), dbpediaValues, WikiList.regexInstances, Integer.parseInt(WikiList.captureGroup), WikiList.rdfTag);
		
		this.noOfMarkedAttributes = helper.getCount();

		

	}

}
