package de.unimannheim.dws.wikilist.evaluation;

import java.util.HashMap;
import java.util.List;


import de.unimannheim.dws.wikilist.CopyOfWikiList;
import de.unimannheim.dws.wikilist.models.EvaluationResult;
import de.unimannheim.dws.wikilist.util.EvaluationHelper;

/**
 * The Class EvaluationDataSet.
 */
public class EvaluationDataSet extends DataSet {

	/** Result of Evaluation. */
	private EvaluationResult evalRes = null;





	/* (non-Javadoc)
	 * @see de.unimannheim.dws.wikilist.evaluation.DataSet#create(de.unimannheim.dws.wikilist.evaluation.DataSet, java.util.HashMap)
	 */
	@Override
	public void create(DataSet dataSet, HashMap<String, String> dbpediaValues)
			throws Exception {

		List<List<String>> firstTable = dataSet.getFirstTable();


		EvaluationHelper evalHelper = new EvaluationHelper();
		evalRes = evalHelper.evaluate(firstTable, dbpediaValues,
				CopyOfWikiList.rdfTagPrefix + ":" + CopyOfWikiList.rdfTag);
		
		evalRes.setNoOfWikiTableEntries(firstTable.size());
		evalRes.setNoOfDBPediaValues(dbpediaValues.size());

	}


	/**
	 * Gets the eval res.
	 *
	 * @return the eval res
	 */
	public EvaluationResult getEvalRes() {
		return evalRes;
	}


}
