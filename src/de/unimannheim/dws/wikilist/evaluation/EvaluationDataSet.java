package de.unimannheim.dws.wikilist.evaluation;

import java.util.HashMap;
import java.util.List;

import de.unimannheim.dws.wikilist.CopyOfWikiList;
import de.unimannheim.dws.wikilist.util.EvaluationHelper;
import de.unimannheim.dws.wikilist.util.EvaluationResult;

public class EvaluationDataSet extends DataSet {

	/** Result of Evaluation. */
	private EvaluationResult evalRes = null;
	
	
	@Override
	public void create(DataSet dataSet,
			HashMap<String, String> dbpediaValues) throws Exception {
		
		EvaluationHelper evalHelper = new EvaluationHelper();
		evalRes = evalHelper.evaluate(dataSet.getFirstTable(), dbpediaValues, CopyOfWikiList.rdfTagPrefix+":"+CopyOfWikiList.rdfTag);	
		
	}
	
	/**
	 * Creates the data set.
	 * 
	 * @param wikiMarkUpList
	 *            the wiki mark up list
	 */
	public void create(List<List<String>> wikiMarkUpList,
			HashMap<String, String> dbpediaValues) {
		
	}
	
	
	
	public List<List<String>> getEvalMatrix() {
		// TODO
		return null;
	}
	
	public void writeOutputToCsv(
			String path,
					List<List<String>> data) {
		// TODO
	}

}
