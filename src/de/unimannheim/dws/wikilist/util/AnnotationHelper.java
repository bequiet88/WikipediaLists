package de.unimannheim.dws.wikilist.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import de.unimannheim.dws.wikilist.WikiList;

// TODO: Auto-generated Javadoc
/**
 * The Class AnnotationHelper.
 */
public class AnnotationHelper {

	/** The tag. */
	String tag = null;

	/** The pattern. */
	Pattern p = null;

	/** The matcher. */
	Matcher m = null;

	/** The capture group */
	int capGroup = 1;

	/** The count. */
	int count = 0;

	/** The output. */
	ArrayList<String> output = null;

	/**
	 * Annotate.
	 * 
	 * @param input
	 *            Blank JWPL String-List
	 * @param regex
	 *            the regex
	 * @param tag
	 *            the tag
	 * @return output Annotated JWPL String-List
	 */
	public List<String> annotateForGold(List<String> input, String regex,
			String tag) {

		/*
		 * Annotation helper takes submitted values for Regex and RDF Tag,
		 * otherwise values for Regex and RDF Tag are taken from those assigned
		 * in WikiList Main class.
		 */
		if (tag == null)
			this.tag = WikiList.rdfTag;
		else
			this.tag = tag;

		if (regex == null)
			this.p = Pattern.compile(WikiList.regexAttribute);
		else
			this.p = Pattern.compile(regex);

		/*
		 * Reset Count.
		 */
		count = 0;

		output = new ArrayList<String>();

		/*
		 * Iterate over wikiMarkUp to manually mark all attributes in order to
		 * define a gold standard for reference
		 */

		for (String string : input) {

			m = p.matcher(string);

			if (m.find()) {
				string = m.replaceFirst("<" + tag + ">" + m.group() + "</"
						+ tag + ">");
				count++;
			}
			output.add(string);

		}

		return output;

	}

	public List<String> annotateForTraining(List<String> input,
			HashMap<String, String> dbpValues, String regexInstance,
			int captureGroup, String tag) throws Exception {

		/*
		 * Annotation helper takes submitted values for Regex and capture group,
		 * otherwise values for Regex and capture group are taken from those
		 * assigned in WikiList Main class.
		 */

		if (regexInstance == null)
			this.p = Pattern.compile(WikiList.regexInstances);
		else
			this.p = Pattern.compile(regexInstance);
		if (captureGroup < 1)
			this.capGroup = Integer.parseInt(WikiList.captureGroup);
		else
			this.capGroup = captureGroup;
		if (tag == null)
			this.tag = WikiList.rdfTag;
		else
			this.tag = tag;

		/*
		 * Reset Count.
		 */
		count = 0;

		output = new ArrayList<String>();

		/*
		 * Iterate over wikiMarkUp to mark all list elements already having
		 * assigned a value in DBPedia
		 */
		for (String string : input) {

			m = p.matcher(string);
			String link = "";
			if (m.find()) {
				link = "<http://dbpedia.org/resource/"
						+ m.group(capGroup).replace(" ", "_") + ">";
			}

			if (dbpValues.containsKey(link)) {

				/*
				 * Extract values from DBPedia and transform to comparable
				 * format
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

				// evaluate expression result on XML document
				XPathExpression expr = xpath
						.compile("//binding[@name = \"title\"]/uri/text()");

				// "/Employees/Employee[gender='Female']/name/text()");
				List<String> values = new ArrayList<String>();
				NodeList nodes = (NodeList) expr.evaluate(document,
						XPathConstants.NODESET);
				for (int i = 0; i < nodes.getLength(); i++)
					values.add(nodes.item(i).getNodeValue());

				for (String string2 : values) {

					String matchingValue = string2.replace(
							"http://dbpedia.org/resource/", "").replace("_",
							" ");

					if (string.contains(matchingValue)) {
						string = string.replaceFirst(matchingValue, "<" + tag + ">"
								+ matchingValue + "</" + tag + ">");
						count++;
						break;
					}
					
				}

			}
			output.add(string);
		}

		return output;
	}

	/**
	 * Gets the tag.
	 * 
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * Sets the tag.
	 * 
	 * @param tag
	 *            the new tag
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * Gets the p.
	 * 
	 * @return the p
	 */
	public Pattern getP() {
		return p;
	}

	/**
	 * Sets the p.
	 * 
	 * @param p
	 *            the new p
	 */
	public void setP(Pattern p) {
		this.p = p;
	}

	/**
	 * Gets the count.
	 * 
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
}
