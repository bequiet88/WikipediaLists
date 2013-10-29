package de.unimannheim.dws.wikilist.evaluation;

import java.util.HashMap;

import de.unimannheim.dws.wikilist.WikiList;
import de.unimannheim.dws.wikilist.util.AnnotationHelper;

/**
 * The Class GoldDataSet.
 */
public class GoldDataSet extends DataSet {

	/* (non-Javadoc)
	 * @see de.unimannheim.dws.wikilist.evaluation.DataSet#create(java.util.List, java.util.HashMap)
	 */
	@Override
	public void create(DataSet dataSet, HashMap<String, String> dbpValues) {


		AnnotationHelper helper = new AnnotationHelper();
		
		this.annotWikiMarkUpList = helper.annotateForGold(dataSet.getWikiMarkUpList(), WikiList.regexAttribute, WikiList.rdfTag);
		
		this.noOfMarkedAttributes = helper.getCount();

	}

	
}
