package de.unimannheim.dws.wikilist.test;

import java.io.StringReader;
import java.util.ArrayList;
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

public class XMLTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			String xml = "<?xml version=\"1.0\"?>\n<sparql xmlns=\"http://www.w3.org/2005/sparql-results#\">\n<head>\n<variable name=\"prop\"/>\n<variable name=\"value\"/>\n</head>\n<results>\n</results>\n</sparql>";

			InputSource source = new InputSource(new StringReader(xml));

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;

			db = dbf.newDocumentBuilder();
			Document document = db.parse(source);

			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();

			
            //evaluate expression result on XML document
			 XPathExpression expr =
		                xpath.compile("//results");////binding[@name = \"title\"]/uri/text()");
			 
			 //"/Employees/Employee[gender='Female']/name/text()");
			List<String> value = new ArrayList<String>(); 
			NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++)
                value.add(nodes.item(i).getNodeValue());
			
			System.out.println("uris=" + value );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
