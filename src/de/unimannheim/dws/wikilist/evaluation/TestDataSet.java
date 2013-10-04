package de.unimannheim.dws.wikilist.evaluation;

import java.util.HashMap;
import java.util.List;

public class TestDataSet extends DataSet {

	/* (non-Javadoc)
	 * @see de.unimannheim.dws.wikilist.evaluation.DataSet#create(java.util.List)
	 */
	@Override
	public void create(List<String> wikiMarkUpList, HashMap<String, String> dbpValues) {

		this.annotWikiMarkUpList = wikiMarkUpList;
		

	}

}
