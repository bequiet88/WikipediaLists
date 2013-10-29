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

public class EvaluationHelper {

	// Cases DBPedia Eval:
	// x-path uri x-path literal else
	// DBPedia URL DBPedia Literal DBPedia n/a
	// find [ ] & equals Wiki URL
	// equals Literal
	// else Wiki n/a

	public EvaluationResult evaluate(List<List<String>> wikiTable,
			HashMap<String, String> dbpValues, String tag) throws Exception {

		/*
		 * Initiate Evaluation Result.
		 */
		EvaluationResult result = new EvaluationResult();

		/*
		 * Iterate over wikiTable to mark all list elements already having
		 * assigned a value in DBPedia
		 */
		for (List<String> tableRow : wikiTable) {

			String link = getDBPediaLinkFromWikiLink(tableRow
					.get(CopyOfWikiList.columnInstance));

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

				XPathExpression expr = xpath
						.compile("//binding[@name = \"title\"]/uri/text()");

				// "/Employees/Employee[gender='Female']/name/text()");

				List<String> uris = new ArrayList<String>();
				NodeList nodes = (NodeList) expr.evaluate(document,
						XPathConstants.NODESET);
				for (int i = 0; i < nodes.getLength(); i++)
					uris.add(nodes.item(i).getNodeValue());

				if (uris.size() > 0) {

					for (String dbpUri : uris) {

						String matchingValueUri = getDBPediaLinkFromWikiLink(tableRow
								.get(CopyOfWikiList.columnPosition));
						String matchingValueLiteral = dbpUri.replace(
								"http://dbpedia.org/resource/", "").replace(
								"_", " ");

						/*
						 * Check DBPedia Uri and Wiki Uri
						 */

						if (!matchFound && matchingValueUri.contains(dbpUri)) {
							result.noOfDBPUriWikiUri++;
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
							result.noOfDBPUriWikiLiteral++;
							matchFound = true;
							break;
						}

						/*
						 * Check DBPedia Uri and Wiki Empty
						 */
						else if (!matchFound
								&& tableRow.get(CopyOfWikiList.columnPosition)
										.equals("")) {
							result.noOfDBPUriWikiEmpty++;
							matchFound = true;
							break;
						}

					}
				}
				/*
				 * If there is a DBPedia Uri but no match was found, add as Wiki Empty
				 */
				else if (uris.size() > 0 && !matchFound) {
					result.noOfDBPUriWikiEmpty++;
					matchFound = true;
				}

				/*
				 * Extract Literal values from DBPedia and transform to
				 * comparable format
				 */

				expr = xpath
						.compile("//binding[@name = \"title\"]/literal/text()");

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

						if (!matchFound
								&& tableRow.get(CopyOfWikiList.columnPosition)
										.startsWith("[[")
								&& JaccardSimilarity.jaccardSimilarity(tableRow
										.get(CopyOfWikiList.columnPosition),
										dbpLiteral) > 0.45) {
							result.noOfDBPLiteralWikiUri++;
							matchFound = true;
							
							
							/*
							 * Add match to RDF Triple List
							 */
							List<String> rdfTriple = new ArrayList<String>();
							rdfTriple.add(link);
							rdfTriple.add(CopyOfWikiList.rdfTagPrefix+":"+CopyOfWikiList.rdfTag);
							rdfTriple.add(getDBPediaLinkFromWikiLink(tableRow
									.get(CopyOfWikiList.columnPosition)));
							result.rdfTriples.add(rdfTriple);
							break;
						}

						/*
						 * Check DBPedia Literal and Wiki Literal
						 */
						else if (!matchFound
								&& JaccardSimilarity.jaccardSimilarity(tableRow
										.get(CopyOfWikiList.columnPosition),
										dbpLiteral) > 0.8) {
							result.noOfDBPLiteralWikiLiteral++;
							matchFound = true;
							break;
						}
						
						/*
						 * Check DBPedia Literal and Wiki Empty
						 */
						else if (!matchFound
								&& tableRow.get(CopyOfWikiList.columnPosition)
										.equals("")) {
							result.noOfDBPLiteralWikiEmpty++;
							matchFound = true;
							break;
						}

					}
				}
				/*
				 * If there is a DBPedia Literal but no match was found, add as Wiki Empty
				 */
				else if (literals.size() > 0 && !matchFound) {
					result.noOfDBPLiteralWikiEmpty++;
					matchFound = true;
				}
			}

		}

		return result;
	}

	private String getDBPediaLinkFromWikiLink(String tableField) {

		String resLink = "";

		if (tableField.startsWith("[[")) {

			String[] resArray = tableField.split("|", 2);
			resArray[1].substring(2);

			resLink = "<http://dbpedia.org/resource/"
					+ resArray[1].substring(2).replace(" ", "_") + ">";
		}

		return resLink;

	}

}
