package de.unimannheim.dws.wikilist.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;


import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import de.unimannheim.dws.wikilist.CopyOfWikiList;
import de.unimannheim.dws.wikilist.models.EvaluationResult;

public class EvaluationHelper {

	// Cases DBPedia Eval:
	// x-path uri x-path literal else
	// DBPedia URL DBPedia Literal DBPedia n/a
	// find [ ] & equals Wiki URL
	// equals Literal
	// else Wiki n/a

	
	
	/**
	 * Evaluate.
	 *
	 * @param wikiTable the wiki table
	 * @param dbpValues the dbp values
	 * @param tag the tag
	 * @return the evaluation result
	 * @throws Exception the exception
	 */
	public EvaluationResult evaluate(List<List<String>> wikiTable,
			HashMap<String, String> dbpValues, String tag) throws Exception {

		/*
		 * Initiate Evaluation Result.
		 */
		EvaluationResult result = new EvaluationResult();
		result.setRdfTriples(new ArrayList<List<String>>());
		
		List<List<String>> helperTriple = new ArrayList<List<String>>();

		/*
		 * Iterate over wikiTable to mark all list elements already having
		 * assigned a value in DBPedia
		 */
		int rowCount = 0;
		for (List<String> tableRow : wikiTable) {

			String link = ProcessTable.wiki2dbpLink(ProcessTable
					.getLink(tableRow.get(CopyOfWikiList.columnInstance)));

			System.out.println("Wiki to DBPedia Link, Row " + ++rowCount + ": "
					+ link);

			if (dbpValues.containsKey(link)) {

				boolean matchFound = false;

				/*
				 * Initiate XML Parser
				 */
				InputSource source = new InputSource(new StringReader(
						dbpValues.get(link)));

				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db;

				db = dbf.newDocumentBuilder();
				Document document = db.parse(source);

				XPathFactory xpathFactory = XPathFactory.newInstance();
				XPath xpath = xpathFactory.newXPath();

				/*
				 * Extract Uri values from DBPedia and transform to comparable
				 * format
				 */

				XPathExpression expr = xpath.compile("//binding[@name = \""
						+ CopyOfWikiList.rdfTag + "\"]/uri/text()");

				// "/Employees/Employee[gender='Female']/name/text()");

				List<String> uris = new ArrayList<String>();
				NodeList nodes = (NodeList) expr.evaluate(document,
						XPathConstants.NODESET);
				for (int i = 0; i < nodes.getLength(); i++)
					uris.add(nodes.item(i).getNodeValue());

				if (uris.size() > 0) {

					for (String dbpUri : uris) {

						String matchingValueUri = ProcessTable
								.wiki2dbpLink(ProcessTable.getLink(tableRow
										.get(CopyOfWikiList.columnPosition)));
						String matchingValueLiteral = dbpUri.replace(
								"http://dbpedia.org/resource/", "").replace(
								"_", " ");

						/*
						 * Check DBPedia Uri and Wiki Uri
						 */

						if (!matchFound && matchingValueUri.contains(dbpUri)) {
							result.setNoOfDBPUriWikiUri(result.getNoOfDBPUriWikiUri() + 1);
							matchFound = true;
							break;
						}

						/*
						 * Check DBPedia Uri and Wiki Literal
						 */
						else if (!matchFound
								&& JaccardSimilarity.jaccardSimilarity(tableRow
										.get(CopyOfWikiList.columnPosition),
										matchingValueLiteral) > 0.7) {
							result.setNoOfDBPUriWikiLiteral(result.getNoOfDBPUriWikiLiteral() + 1);
							matchFound = true;
							break;
						}

						/*
						 * Check DBPedia Uri and Wiki Empty
						 */
						else if (!matchFound
								&& (tableRow.get(CopyOfWikiList.columnPosition).trim().equals("")
										|| tableRow.get(CopyOfWikiList.columnPosition).equals(
												"&nbsp;") || tableRow.get(CopyOfWikiList.columnPosition).trim().equals(
														"-"))) {
							result.setNoOfDBPUriWikiEmpty(result.getNoOfDBPUriWikiEmpty() + 1);
							matchFound = true;
							break;
						}

					}
					if (matchFound)
						continue;
				}
				/*
				 * If there is a DBPedia Uri but no match was found, add as Wiki
				 * Empty
				 */
				if (uris.size() > 0 && !matchFound) {
					result.setNoOfDBPUriWikiEmpty(result.getNoOfDBPUriWikiEmpty() + 1);
					matchFound = true;
					continue;
				}

				/*
				 * Extract Literal values from DBPedia and transform to
				 * comparable format
				 */

				expr = xpath.compile("//binding[@name = \""
						+ CopyOfWikiList.rdfTag + "\"]/literal/text()");

				List<String> literals = new ArrayList<String>();
				nodes = (NodeList) expr.evaluate(document,
						XPathConstants.NODESET);
				for (int i = 0; i < nodes.getLength(); i++)
					literals.add(nodes.item(i).getNodeValue());

				if (literals.size() > 0) {

					for (String dbpLiteral : literals) {

						/*
						 * Check DBPedia Literal and Wiki Uri
						 */
						String matchingValueUri = ProcessTable
								.wiki2dbpLink(ProcessTable.getLink(tableRow
										.get(CopyOfWikiList.columnPosition)));

						if (!matchFound
								&& !matchingValueUri.equals("")
								&& JaccardSimilarity.jaccardSimilarity(tableRow
										.get(CopyOfWikiList.columnPosition),
										dbpLiteral) > 0.45) {
							result.setNoOfDBPLiteralWikiUri(result.getNoOfDBPLiteralWikiUri() + 1);
							matchFound = true;

							/*
							 * Add match to RDF Triple List
							 */
							List<String> rdfTriple = new ArrayList<String>();
							rdfTriple.add(link);
							rdfTriple.add(CopyOfWikiList.rdfPropUrl);
							rdfTriple.add(matchingValueUri);
							helperTriple = result.getRdfTriples();
							helperTriple.add(rdfTriple);
							result.setRdfTriples(helperTriple);
							break;
						}

						/*
						 * Check DBPedia Literal and Wiki Literal
						 */
						else if (!matchFound
								&& JaccardSimilarity.jaccardSimilarity(tableRow
										.get(CopyOfWikiList.columnPosition),
										dbpLiteral) > 0.8) {
							result.setNoOfDBPLiteralWikiLiteral(result.getNoOfDBPLiteralWikiLiteral() + 1);
							matchFound = true;
							break;
						}

						/*
						 * Check DBPedia Literal and Wiki Empty
						 */
						else if (!matchFound
								&& (tableRow.get(CopyOfWikiList.columnPosition)
										.trim().equals("")
										|| tableRow.get(
												CopyOfWikiList.columnPosition)
												.equals("&nbsp;") || tableRow
										.get(CopyOfWikiList.columnPosition)
										.trim().equals("-"))) {
							result.setNoOfDBPLiteralWikiEmpty(result.getNoOfDBPLiteralWikiEmpty() + 1);
							matchFound = true;
							break;
						}

					}
					if (matchFound)
						continue;
				}
				/*
				 * If there is a DBPedia Literal but no match was found, add as
				 * Wiki Empty
				 */
				if (literals.size() > 0 && !matchFound) {
					result.setNoOfDBPLiteralWikiEmpty(result.getNoOfDBPLiteralWikiEmpty() + 1);
					matchFound = true;
					continue;
				}

				/*
				 * In case neither a Uri nor a literal is present in DBPedia
				 * Entry, process as DBPedia Empty
				 */

				/*
				 * Check DBPedia Empty and Wiki Uri
				 */

				String matchingValueUri = ProcessTable
						.wiki2dbpLink(ProcessTable.getLink(tableRow
								.get(CopyOfWikiList.columnPosition)));

				if (!matchFound && !matchingValueUri.equals("")) {
					result.setNoOfDBPEmptyWikiUri(result.getNoOfDBPEmptyWikiUri() + 1);
					matchFound = true;

					/*
					 * Add match to RDF Triple List
					 */
					List<String> rdfTriple = new ArrayList<String>();
					rdfTriple.add(link);
					rdfTriple.add(CopyOfWikiList.rdfPropUrl);
					rdfTriple.add(matchingValueUri);
					helperTriple = result.getRdfTriples();
					helperTriple.add(rdfTriple);
					result.setRdfTriples(helperTriple);
				}

				/*
				 * Check DBPedia Empty and Wiki Literal
				 */
				else if (!matchFound
						&& !tableRow.get(CopyOfWikiList.columnPosition).trim()
								.equals("")
						&& !tableRow.get(CopyOfWikiList.columnPosition).equals(
								"&nbsp;")
						&& !tableRow.get(CopyOfWikiList.columnPosition).trim().equals(
								"-")) {
					result.setNoOfDBPEmptyWikiLiteral(result.getNoOfDBPEmptyWikiLiteral() + 1);
					matchFound = true;

					/*
					 * Add match to RDF Triple List
					 */
					List<String> rdfTriple = new ArrayList<String>();
					rdfTriple.add(link);
					rdfTriple.add(CopyOfWikiList.rdfPropUrl);
					rdfTriple.add(tableRow.get(CopyOfWikiList.columnPosition));
					helperTriple = result.getRdfTriples();
					helperTriple.add(rdfTriple);
					result.setRdfTriples(helperTriple);
				}

				/*
				 * Everything that reaches this code is dealt as DBPedia Empty
				 * and Wiki Empty
				 */
				else {
					result.setNoOfDBPEmptyWikiEmpty(result.getNoOfDBPEmptyWikiEmpty() + 1);
					matchFound = true;
				}

			}

		}

		return result;
	}

//	private String getDBPediaLinkFromWikiLink(String tableField) {
//
//		String resLink = "";
//
//		if (tableField.startsWith("[[")) {
//
//			String[] resArray = tableField.split("\\|", 2);
//
//			if (resArray[0].endsWith("]]")) {
//				resLink = "<http://dbpedia.org/resource/"
//						+ resArray[0].substring(2, resArray[0].length() - 2)
//								.replace(" ", "_") + ">";
//			} else {
//				resLink = "<http://dbpedia.org/resource/"
//						+ resArray[0].substring(2).replace(" ", "_") + ">";
//			}
//		}
//
//		return resLink;
//
//	}

}
