package de.unimannheim.dws.wikilist.models;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 * The Class EvaluationResult.
 */
public class EvaluationResult {
	
	/** Number of Wiki Table Rows found. */
	protected int noOfWikiTableEntries = 0;
	
	/** Number of DBPedia Values retrieved (equals number of instances). */
	protected int noOfDBPediaValues = 0;
	
	/** The number of DBPedia Uri and Wiki Uri. */
	protected int noOfDBPUriWikiUri = 0;
	
	/** The number of DBPedia Uri and Wiki Literal. */
	protected int noOfDBPUriWikiLiteral = 0;

	/** The number of DBPedia Uri and Wiki Empty. */
	protected int noOfDBPUriWikiEmpty = 0;

	/** The number of DBPedia Literal and Wiki Uri. */
	protected int noOfDBPLiteralWikiUri = 0;

	/** The number of DBPedia Literal and Wiki Literal. */
	protected int noOfDBPLiteralWikiLiteral = 0;

	/** The number of DBPedia Literal and Wiki Empty. */
	protected int noOfDBPLiteralWikiEmpty = 0;
	
	/** The number of DBPedia Empty and Wiki Uri. */
	protected int noOfDBPEmptyWikiUri = 0;
	
	/** The number of DBPedia Empty and Wiki Literal. */
	protected int noOfDBPEmptyWikiLiteral = 0;

	/** The number of DBPedia Uri and Wiki Uri. */
	protected int noOfDBPEmptyWikiEmpty = 0;
	
	/** List of new RDF tuples. */
	protected List<List<String>> rdfTriples = null;
	
	
	
	
	/**
	 * Gets the eval matrix.
	 *
	 * @return the eval matrix
	 */
	public List<List<String>> getEvalMatrix() {

		List<List<String>> evalMatrix =  new ArrayList<List<String>>();


			/*
			 * First Row
			 */
			List<String> firstRow = new ArrayList<String>();
			firstRow.add("");
			firstRow.add("DBPedia Uri");
			firstRow.add("DBPedia Literal");
			firstRow.add("DBPedia Empty");
			firstRow.add("Total");
			evalMatrix.add(firstRow);

			/*
			 * Second Row
			 */
			List<String> secondRow = new ArrayList<String>();
			secondRow.add("WikiList Uri");
			secondRow.add("" + this.getNoOfDBPUriWikiUri());
			secondRow.add("" + this.getNoOfDBPLiteralWikiUri());
			secondRow.add("" + this.getNoOfDBPEmptyWikiUri());
			secondRow.add("" + this.getNoOfWikiUri());
			evalMatrix.add(secondRow);

			/*
			 * Third Row
			 */
			List<String> thirdRow = new ArrayList<String>();
			thirdRow.add("WikiList Literal");
			thirdRow.add("" + this.getNoOfDBPUriWikiLiteral());
			thirdRow.add("" + this.getNoOfDBPLiteralWikiLiteral());
			thirdRow.add("" + this.getNoOfDBPEmptyWikiLiteral());
			thirdRow.add("" + this.getNoOfWikiLiteral());
			evalMatrix.add(thirdRow);

			/*
			 * Fourth Row
			 */
			List<String> fourthRow = new ArrayList<String>();
			fourthRow.add("WikiList Empty");
			fourthRow.add("" + this.getNoOfDBPUriWikiEmpty());
			fourthRow.add("" + this.getNoOfDBPLiteralWikiEmpty());
			fourthRow.add("" + this.getNoOfDBPEmptyWikiEmpty());
			fourthRow.add("" + this.getNoOfWikiEmpty());
			evalMatrix.add(fourthRow);

			/*
			 * Fifth Row
			 */
			List<String> fifthRow = new ArrayList<String>();
			fifthRow.add("Total");
			fifthRow.add("" + this.getNoOfDBPediaUri());
			fifthRow.add("" + this.getNoOfDBPediaLiteral());
			fifthRow.add("" + this.getNoOfDBPediaEmpty());
			fifthRow.add("" + this.getNoOfTotal());
			evalMatrix.add(fifthRow);

		return evalMatrix;

	}

	
	/**
	 * Write output to csv.
	 *
	 * @param path the path
	 * @param data the data
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeOutputToCsv(String path, List<List<String>> data)
			throws IOException {

		// generate CSVPrinter Object
		// FileOutputStream csvBAOS = new FileOutputStream(new File(path));
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				new File(path)));
		// OutputStreamWriter csvWriter = new OutputStreamWriter(csvBAOS);
		CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);

		csvPrinter.print("sep=,");
		csvPrinter.println();

		if (data.get(0).get(0).equals("") && this != null) {
			csvPrinter.print("Number of Wiki Table rows = "
					+ this.getNoOfWikiTableEntries());
			csvPrinter.println();
			csvPrinter.print("Number of DBPedia values retrieved = "
					+ this.getNoOfDBPediaValues());
			csvPrinter.println();
		}

		for (List<String> list : data) {

			for (String string : list) {
				csvPrinter.print(string);
			}
			csvPrinter.println();
		}

		csvPrinter.flush();
		csvPrinter.close();

	}

	/**
	 * Adds the given Eval Result to the instance.
	 *
	 * @param in the in
	 */
	public void add(EvaluationResult in) {
		
		noOfWikiTableEntries += in.getNoOfWikiTableEntries();
		noOfDBPediaValues += in.getNoOfDBPediaValues();
		
		noOfDBPUriWikiUri += in.getNoOfDBPUriWikiUri();
		noOfDBPUriWikiLiteral += in.getNoOfDBPUriWikiLiteral();
		noOfDBPUriWikiEmpty += in.getNoOfDBPUriWikiEmpty();
		
		noOfDBPLiteralWikiUri += in.getNoOfDBPLiteralWikiUri();
		noOfDBPLiteralWikiLiteral += in.getNoOfDBPLiteralWikiLiteral();
		noOfDBPLiteralWikiEmpty += in.getNoOfDBPLiteralWikiEmpty();
		
		noOfDBPEmptyWikiUri += in.getNoOfDBPEmptyWikiUri();
		noOfDBPEmptyWikiLiteral += in.getNoOfDBPEmptyWikiLiteral();
		noOfDBPEmptyWikiEmpty += in.getNoOfDBPEmptyWikiEmpty();
		
		
	}
	
	
	
	/**
	 * Gets the no of dbp uri wiki uri.
	 *
	 * @return the no of dbp uri wiki uri
	 */
	public int getNoOfDBPUriWikiUri() {
		return noOfDBPUriWikiUri;
	}

	/**
	 * Sets the no of dbp uri wiki uri.
	 *
	 * @param noOfDBPUriWikiUri the new no of dbp uri wiki uri
	 */
	public void setNoOfDBPUriWikiUri(int noOfDBPUriWikiUri) {
		this.noOfDBPUriWikiUri = noOfDBPUriWikiUri;
	}

	/**
	 * Gets the no of dbp uri wiki literal.
	 *
	 * @return the no of dbp uri wiki literal
	 */
	public int getNoOfDBPUriWikiLiteral() {
		return noOfDBPUriWikiLiteral;
	}

	/**
	 * Sets the no of dbp uri wiki literal.
	 *
	 * @param noOfDBPUriWikiLiteral the new no of dbp uri wiki literal
	 */
	public void setNoOfDBPUriWikiLiteral(int noOfDBPUriWikiLiteral) {
		this.noOfDBPUriWikiLiteral = noOfDBPUriWikiLiteral;
	}

	/**
	 * Gets the no of dbp uri wiki empty.
	 *
	 * @return the no of dbp uri wiki empty
	 */
	public int getNoOfDBPUriWikiEmpty() {
		return noOfDBPUriWikiEmpty;
	}

	/**
	 * Sets the no of dbp uri wiki empty.
	 *
	 * @param noOfDBPUriWikiEmpty the new no of dbp uri wiki empty
	 */
	public void setNoOfDBPUriWikiEmpty(int noOfDBPUriWikiEmpty) {
		this.noOfDBPUriWikiEmpty = noOfDBPUriWikiEmpty;
	}

	/**
	 * Gets the no of dbp literal wiki uri.
	 *
	 * @return the no of dbp literal wiki uri
	 */
	public int getNoOfDBPLiteralWikiUri() {
		return noOfDBPLiteralWikiUri;
	}

	/**
	 * Sets the no of dbp literal wiki uri.
	 *
	 * @param noOfDBPLiteralWikiUri the new no of dbp literal wiki uri
	 */
	public void setNoOfDBPLiteralWikiUri(int noOfDBPLiteralWikiUri) {
		this.noOfDBPLiteralWikiUri = noOfDBPLiteralWikiUri;
	}

	/**
	 * Gets the no of dbp literal wiki literal.
	 *
	 * @return the no of dbp literal wiki literal
	 */
	public int getNoOfDBPLiteralWikiLiteral() {
		return noOfDBPLiteralWikiLiteral;
	}

	/**
	 * Sets the no of dbp literal wiki literal.
	 *
	 * @param noOfDBPLiteralWikiLiteral the new no of dbp literal wiki literal
	 */
	public void setNoOfDBPLiteralWikiLiteral(int noOfDBPLiteralWikiLiteral) {
		this.noOfDBPLiteralWikiLiteral = noOfDBPLiteralWikiLiteral;
	}

	/**
	 * Gets the no of dbp literal wiki empty.
	 *
	 * @return the no of dbp literal wiki empty
	 */
	public int getNoOfDBPLiteralWikiEmpty() {
		return noOfDBPLiteralWikiEmpty;
	}

	/**
	 * Sets the no of dbp literal wiki empty.
	 *
	 * @param noOfDBPLiteralWikiEmpty the new no of dbp literal wiki empty
	 */
	public void setNoOfDBPLiteralWikiEmpty(int noOfDBPLiteralWikiEmpty) {
		this.noOfDBPLiteralWikiEmpty = noOfDBPLiteralWikiEmpty;
	}

	/**
	 * Gets the no of dbp empty wiki uri.
	 *
	 * @return the no of dbp empty wiki uri
	 */
	public int getNoOfDBPEmptyWikiUri() {
		return noOfDBPEmptyWikiUri;
	}

	/**
	 * Sets the no of dbp empty wiki uri.
	 *
	 * @param noOfDBPEmptyWikiUri the new no of dbp empty wiki uri
	 */
	public void setNoOfDBPEmptyWikiUri(int noOfDBPEmptyWikiUri) {
		this.noOfDBPEmptyWikiUri = noOfDBPEmptyWikiUri;
	}

	/**
	 * Gets the no of dbp empty wiki literal.
	 *
	 * @return the no of dbp empty wiki literal
	 */
	public int getNoOfDBPEmptyWikiLiteral() {
		return noOfDBPEmptyWikiLiteral;
	}

	/**
	 * Sets the no of dbp empty wiki literal.
	 *
	 * @param noOfDBPEmptyWikiLiteral the new no of dbp empty wiki literal
	 */
	public void setNoOfDBPEmptyWikiLiteral(int noOfDBPEmptyWikiLiteral) {
		this.noOfDBPEmptyWikiLiteral = noOfDBPEmptyWikiLiteral;
	}

	/**
	 * Gets the no of dbp empty wiki empty.
	 *
	 * @return the no of dbp empty wiki empty
	 */
	public int getNoOfDBPEmptyWikiEmpty() {
		return noOfDBPEmptyWikiEmpty;
	}

	/**
	 * Sets the no of dbp empty wiki empty.
	 *
	 * @param noOfDBPEmptyWikiEmpty the new no of dbp empty wiki empty
	 */
	public void setNoOfDBPEmptyWikiEmpty(int noOfDBPEmptyWikiEmpty) {
		this.noOfDBPEmptyWikiEmpty = noOfDBPEmptyWikiEmpty;
	}

	/**
	 * Gets the rdf tuples.
	 *
	 * @return the rdf tuples
	 */
	public List<List<String>> getRdfTriples() {
		return rdfTriples;
	}

	/**
	 * Sets the rdf tuples.
	 *
	 * @param rdfTuples the new rdf tuples
	 */
	public void setRdfTriples(List<List<String>> rdfTuples) {
		this.rdfTriples = rdfTuples;
	}
	
	/**
	 * Gets the no of wiki uri.
	 *
	 * @return the no of wiki uri
	 */
	public int getNoOfWikiUri() {
		return noOfDBPEmptyWikiUri+noOfDBPUriWikiUri+noOfDBPLiteralWikiUri;
	}
	
	/**
	 * Gets the no of wiki literal.
	 *
	 * @return the no of wiki literal
	 */
	public int getNoOfWikiLiteral() {
		return noOfDBPEmptyWikiLiteral+noOfDBPUriWikiLiteral+noOfDBPLiteralWikiLiteral;
	}
	
	/**
	 * Gets the no of wiki empty.
	 *
	 * @return the no of wiki empty
	 */
	public int getNoOfWikiEmpty() {
		return noOfDBPEmptyWikiEmpty+noOfDBPUriWikiEmpty+noOfDBPLiteralWikiEmpty;
	}
	
	/**
	 * Gets the no of db pedia uri.
	 *
	 * @return the no of db pedia uri
	 */
	public int getNoOfDBPediaUri() {
		return noOfDBPUriWikiLiteral+noOfDBPUriWikiEmpty+noOfDBPUriWikiUri;
	}

	/**
	 * Gets the no of db pedia literal.
	 *
	 * @return the no of db pedia literal
	 */
	public int getNoOfDBPediaLiteral() {
		return noOfDBPLiteralWikiLiteral+noOfDBPLiteralWikiEmpty+noOfDBPLiteralWikiUri;
	}

	/**
	 * Gets the no of db pedia empty.
	 *
	 * @return the no of db pedia empty
	 */
	public int getNoOfDBPediaEmpty() {
		return noOfDBPEmptyWikiLiteral+noOfDBPEmptyWikiEmpty+noOfDBPEmptyWikiUri;
	}
	
	/**
	 * Gets the no of total.
	 *
	 * @return the no of total
	 */
	public int getNoOfTotal() {
		return getNoOfDBPediaEmpty()+getNoOfDBPediaLiteral()+getNoOfDBPediaUri();
	}

	/**
	 * Gets the no of wiki table entries.
	 *
	 * @return the no of wiki table entries
	 */
	public int getNoOfWikiTableEntries() {
		return noOfWikiTableEntries;
	}

	/**
	 * Sets the no of wiki table entries.
	 *
	 * @param noOfWikiTableEntries the new no of wiki table entries
	 */
	public void setNoOfWikiTableEntries(int noOfWikiTableEntries) {
		this.noOfWikiTableEntries = noOfWikiTableEntries;
	}

	/**
	 * Gets the no of db pedia values.
	 *
	 * @return the no of db pedia values
	 */
	public int getNoOfDBPediaValues() {
		return noOfDBPediaValues;
	}

	/**
	 * Sets the no of db pedia values.
	 *
	 * @param noOfDBPediaValues the new no of db pedia values
	 */
	public void setNoOfDBPediaValues(int noOfDBPediaValues) {
		this.noOfDBPediaValues = noOfDBPediaValues;
	}

}
