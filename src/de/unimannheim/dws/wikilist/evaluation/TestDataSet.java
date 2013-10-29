package de.unimannheim.dws.wikilist.evaluation;

import java.util.HashMap;

public class TestDataSet extends DataSet {

	/* (non-Javadoc)
	 * @see de.unimannheim.dws.wikilist.evaluation.DataSet#create(java.util.List)
	 */
	@Override
	public void create(DataSet dataSet, HashMap<String, String> dbpValues) {

		this.annotWikiMarkUpList = dataSet.getWikiMarkUpList();
		

	}
}
