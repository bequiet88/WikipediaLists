package de.unimannheim.dws.wikilist.evaluation;

import java.util.HashMap;
import java.util.List;

import de.unimannheim.dws.wikilist.WikiList;
import de.unimannheim.dws.wikilist.util.AnnotationHelper;

public class TrainingsDataSet extends DataSet {

	@Override
	public void create(List<String> wikiMarkUpList,
			HashMap<String, String> dbpediaValues) throws Exception  {
		
		this.dbpediaValues = dbpediaValues;

		AnnotationHelper helper = new AnnotationHelper();
		
		this.annotWikiMarkUpList = helper.annotateForTraining(wikiMarkUpList, dbpediaValues, WikiList.regexInstances, Integer.parseInt(WikiList.captureGroup), WikiList.rdfTag);
		
		this.noOfMarkedAttributes = helper.getCount();

		

	}

}
