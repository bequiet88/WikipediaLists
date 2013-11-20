package de.unimannheim.dws.wikilist.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import de.unimannheim.dws.wikilist.models.PropertyFinderResult;
import de.unimannheim.dws.wikilist.models.Triple;

/**
 * The Class PropertyFinderHelper.
 */
public class PropertyFinderHelper {

	/**
	 * Find uri property.
	 * 
	 * @param dbpValues
	 *            the dbp values
	 * @return the property finder result
	 * @throws Exception
	 *             the exception
	 */
	public PropertyFinderResult findUriProperty(
			HashMap<String, String> dbpValues) throws Exception {

		/*
		 * Initiate Property Finder Result.
		 */
		PropertyFinderResult result = new PropertyFinderResult();
		HashMap<String, Double> resultMap = new HashMap<String, Double>();
		result.setMap(resultMap);

		List<Triple<String, String, String>> completeTriples = new ArrayList<Triple<String, String, String>>();

		/*
		 * Iterate over HashMap to retrieve property from XML
		 */
		for (String key : dbpValues.keySet()) {

			/*
			 * Initiate XML Parser
			 */
			InputSource source = new InputSource(new StringReader(
					dbpValues.get(key)));

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;

			db = dbf.newDocumentBuilder();
			Document document = db.parse(source);

			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();

			/*
			 * Extract property Uri values from DBPedia and transform to
			 * comparable format
			 */

			XPathExpression expr = xpath
					.compile("//binding[@name = \"property\"]/uri/text()");

			List<String> uris = new ArrayList<String>();
			NodeList nodes = (NodeList) expr.evaluate(document,
					XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++)
				uris.add(nodes.item(i).getNodeValue());

			if (uris.size() > 0) {

				Triple<String, String, String> triple = null;

				for (String dbpUri : uris) {

					/*
					 * Initialize triple and add to complete list
					 */
					triple = new Triple<String, String, String>();
					String[] tripleValues = key.split("::", 3);
					triple.setFirst(tripleValues[0]);
					triple.setSecond("<" + dbpUri + ">");
					triple.setThird(tripleValues[2]);
					completeTriples.add(triple);

				}
			}
		}

		/*
		 * Iterate over triples and build hashmap with property as key and all
		 * matching triples as values
		 */

		if (completeTriples.size() > 0) {

			HashMap<String, List<Triple<String, String, String>>> tripleMap = new HashMap<String, List<Triple<String, String, String>>>();

			for (Triple<String, String, String> triple : completeTriples) {

				String prop = triple.getSecond();
				List<Triple<String, String, String>> tripleList = new ArrayList<Triple<String, String, String>>();

				if (!tripleMap.containsKey(prop)) {
					tripleList.add(triple);
					tripleMap.put(prop, tripleList);

				} else {
					tripleList = tripleMap.get(prop);
					tripleList.add(triple);
					tripleMap.put(prop, tripleList);
				}
			}

			/*
			 * Calculate Confidence of each property
			 */
			for (String prop : tripleMap.keySet()) {
				resultMap.put(prop, (double) tripleMap.get(prop).size()
						/ completeTriples.size());
			}
		}

		return result;

	}

	/**
	 * Find literal property.
	 * 
	 * @param dbpValues
	 *            the dbp values
	 * @return the property finder result
	 * @throws Exception
	 *             the exception
	 */
	public PropertyFinderResult findLiteralProperty(
			HashMap<String, String> dbpCache, HashMap<String, String> literals)
			throws Exception {

		/*
		 * Initiate Property Finder Result.
		 */
		PropertyFinderResult result = new PropertyFinderResult();
		HashMap<String, Double> resultMap = new HashMap<String, Double>();
		result.setMap(resultMap);

		/*
		 * Iterate over Literal HashMap
		 */
		for (String key : literals.keySet()) {

			/*
			 * Read XML DBPedia result from Cache
			 */
			String dbpResult = dbpCache.get(key + "::$prop::$value");
			
			if (dbpResult != null) {

				/*
				 * Initiate XML Parser to retrieve result pairs
				 */
				InputSource source = new InputSource(
						new StringReader(dbpResult));

				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db;

				db = dbf.newDocumentBuilder();
				Document document = db.parse(source);

				XPathFactory xpathFactory = XPathFactory.newInstance();
				XPath xpath = xpathFactory.newXPath();

				XPathExpression expr = xpath.compile("//result");// binding[@name
																	// =
																	// \"property\"]/uri/text()");

				List<String> resultPairs = new ArrayList<String>();
				NodeList nodes = (NodeList) expr.evaluate(document,
						XPathConstants.NODESET);
				for (int i = 0; i < nodes.getLength(); i++) 
					
//					String dbpProp = null;
//					String dbpVal = null;
//
//					NodeList childNodes = nodes.item(i).getChildNodes();
//					
//					for (int j = 0; j < childNodes.getLength(); j++) {
//						
//						if(childNodes.item(j).getNodeName().equals("binding")) {
//							
//							childNodes.item(j).getAttributes()
//							
//							
//						}
//						
//						
//						
//					}
//					
//					
//					
//				}
					resultPairs.add(nodes.item(i).getNodeValue());


				if (resultPairs.size() > 0) {

					for (String resultPair : resultPairs) {

						String dbpProp = null;
						String dbpVal = null;						/*
						 * Read DBPedia Property and corresponding value
						 */
						InputSource sourcePair = new InputSource(
								new StringReader(resultPair));

						DocumentBuilderFactory dbfPair = DocumentBuilderFactory
								.newInstance();
						DocumentBuilder dbPair;

						dbPair = dbfPair.newDocumentBuilder();
						Document documentPair = dbPair.parse(sourcePair);

						XPathFactory xpathFactoryPair = XPathFactory.newInstance();
						XPath xpathPair = xpathFactoryPair.newXPath();
						
						XPathExpression exprPair = xpathPair
								.compile("//binding[@name = \"prop\"]/uri/text()");

						NodeList nodesPair = (NodeList) exprPair.evaluate(documentPair,
								XPathConstants.NODESET);
						if (nodesPair.getLength() == 1) {
							dbpProp = nodesPair.item(0).getNodeValue();
						}

						exprPair = xpath
								.compile("//binding[@name = \"value\"]/literal/text()");

						nodesPair = (NodeList) exprPair.evaluate(documentPair,
								XPathConstants.NODESET);
						if (nodesPair.getLength() == 1) {
							dbpVal = nodesPair.item(0).getNodeValue();
						}

						if (dbpProp != null && dbpVal != null) {

							/*
							 * calculate jaccard similarity for pair's value and
							 * literal's value
							 */
							double sim = JaccardSimilarity.jaccardSimilarity(
									ProcessTable.cleanTableCell(literals.get(key)), dbpVal);
							if (sim > 0.5) {

								/*
								 * sum up similarities
								 */
								if (resultMap.containsKey("<" + dbpProp + ">")) {
									double helperSim = resultMap.get("<"
											+ dbpProp + ">")
											+ sim;
									resultMap.put("<" + dbpProp + ">",
											helperSim);
								} else {
									resultMap.put("<" + dbpProp + ">", sim);
								}
							}
						}
					}
				}
			}			
		}	
		

		/*
		 * Calculate Confidence of each property
		 * Iterate over resultMap and divide by literals.size()
		 */
		for (String prop : resultMap.keySet()) {
			resultMap.put(prop, (double) resultMap.get(prop)
					/ literals.size());
		}
		
		return result;

	}

	/**
	 * Return max confidence.
	 * 
	 * @param res
	 *            the res
	 * @return the string
	 */
	public String returnMaxConfidence(PropertyFinderResult res) {

		if (res.getMap().size() == 0) {
			return "error";
		} else {
			// find max
			double maxConf = 0;
			String maxProp = "";
			for (Map.Entry<String, Double> entry : res.getMap().entrySet()) {
				if (entry.getValue() > maxConf) {
					maxProp = entry.getKey();
					maxConf = entry.getValue();
				}
			}
			return maxProp;
		}
	}

}
