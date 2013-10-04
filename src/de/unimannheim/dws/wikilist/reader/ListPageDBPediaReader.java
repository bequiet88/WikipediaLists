package de.unimannheim.dws.wikilist.reader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;

public class ListPageDBPediaReader implements
		IListPageReader<HashMap<String, String>> {

	private BufferedWriter writer = null;
	private ReaderResource dbpRes = null;
	private String queryStr = null;
	private Query query = null;
	private QueryExecution qexec = null;

	private String prefix = "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
			+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
			+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/>"
			+ "PREFIX dc: <http://purl.org/dc/elements/1.1/>"
			+ "PREFIX : <http://dbpedia.org/resource/>"
			+ "PREFIX dbpprop: <http://dbpedia.org/property/>"
			+ "PREFIX dbpedia: <http://dbpedia.org/>"
			+ "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>"
			+ "PREFIX dbo: <http://dbpedia.org/ontology/>";

	@Override
	public void openInput(ReaderResource resource) {

		dbpRes = resource;

	}

	@Override
	public HashMap<String, String> readInput() throws Exception {

		HashMap<String, String> result = new HashMap<String, String>();

		if (dbpRes != null) {

			for (String dbpInstance : dbpRes.getResources()) {

				queryStr = prefix
						+

						/*
						 * Example Query for
						 * http://en.wikipedia.org/wiki/List_of_Peers_1330-1339
						 * Instances
						 */
						"SELECT * WHERE {"
						// +"<http://dbpedia.org/resource/Henry_Plantagenet,_3rd_Earl_of_Leicster_and_Lancaster> dbpprop:title ?title. }";
						+ dbpInstance + " " + dbpRes.getAttrPrefix() + ":"
						+ dbpRes.getAttribute() + " ?" + dbpRes.getAttribute()
						+ ". }";

				query = QueryFactory.create(queryStr);

				// Remote execution.
				qexec = QueryExecutionFactory.sparqlService(
						"http://dbpedia.org/sparql", query);
				// Set the DBpedia specific timeout.
				((QueryEngineHTTP) qexec).addParam("timeout", "10000");

				// Execute.
				ResultSet rs = qexec.execSelect();

				// ResultSetFormatter.out(System.out, rs, query);
				// Add result to HashMap.
				result.put(dbpInstance, ResultSetFormatter.toList(rs).toString());
				
				this.close();
			}
		}
		return result;
	}

	@Override
	public void close() {
		if (qexec != null)
			qexec.close();
	}

	@Override
	public void writeOutputToFile(String path, String text) {
		try {
			writer = new BufferedWriter(new FileWriter(new File(path)));
			writer.write(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
