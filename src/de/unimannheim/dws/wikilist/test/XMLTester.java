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

import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XMLTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			String xml = "<?xml version=\"1.0\"?><sparql xmlns=\"http://www.w3.org/2005/sparql-results#\"><head><variable name=\"prop\"/><variable name=\"value\"/></head><results><result><binding name=\"prop\"><uri>http://www.w3.org/1999/02/22-rdf-syntax-ns#type</uri></binding><binding name=\"value\"><uri>http://www.w3.org/2002/07/owl#Thing</uri></binding></result><result><binding name=\"prop\"><uri>http://dbpedia.org/ontology/abstract</uri></binding><binding name=\"value\"><literal xml:lang=\"it\"></literal></binding></result><result><binding name=\"prop\"><uri>http://dbpedia.org/ontology/abstract</uri></binding><binding name=\"value\"><literal xml:lang=\"de\">Henry de Beaumont, 1. Baron Beaumont war ein Adliger aus dem Hause Brienne und Begründer der englischen Linie seines Geschlechtes.</literal></binding></result><result><binding name=\"prop\"><uri>http://dbpedia.org/ontology/abstract</uri></binding><binding name=\"value\"><literal xml:lang=\"en\">Henry de Beaumont, jure uxoris 4th Earl of Buchan and suo jure 1st Baron</literal></binding></result></results></sparql>";
					//"<?xml version=\"1.0\"?>\n<sparql xmlns=\"http://www.w3.org/2005/sparql-results#\">\n<head>\n<variable name=\"prop\"/>\n<variable name=\"value\"/>\n</head>\n<results>\n</results>\n</sparql>";

			InputSource source = new InputSource(new StringReader(xml));

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;

			db = dbf.newDocumentBuilder();
			Document document = db.parse(source);

			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();

			
            //evaluate expression result on XML document
			 XPathExpression expr =
		                xpath.compile("//result");////binding[@name = \"title\"]/uri/text()");
			 
			 //"/Employees/Employee[gender='Female']/name/text()");
			 

				String dbpProp = null;
				String dbpVal = null;
				
			List<String> value = new ArrayList<String>(); 
			NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++) {
				dbpProp = null;
				dbpVal = null;
            	NodeList childNodes = nodes.item(i).getChildNodes();
            	
            	for(int j = 0; j < nodes.getLength(); j++) {
            		
            		
            		if(childNodes.item(j) == null) {
            			break;
            		}
            		if(childNodes.item(j).getNodeName().equals("binding")) {
            			
            			NamedNodeMap attr = childNodes.item(j).getAttributes();
            			
            			if(attr.getNamedItem("name").getNodeValue().equals("prop")) {
            				
            				dbpProp = childNodes.item(j).getChildNodes().item(0).getTextContent();
            				// hier suche uri und nehme den Text
            				
            			}
            			
            			if(attr.getNamedItem("name").getNodeValue().equals("value")) {
            				
            				childNodes.item(j).getChildNodes();
            				// hier suche literal und nehme den Text
            				Node literalNode = childNodes.item(j).getChildNodes().item(0);
            				if(literalNode.getNodeName().equals("literal")) {
            					dbpVal = literalNode.getTextContent();   					
            				}
            				
            				
            			}
            			
            		}
            	}
  
            	if (dbpProp != null && dbpVal != null && !dbpProp.isEmpty() && !dbpVal.isEmpty()) {
            		System.out.println("Prop = " + dbpProp + ", value = " + dbpVal);
            	}
                
            }
			System.out.println("uris=" + value );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
