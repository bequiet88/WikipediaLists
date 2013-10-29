package de.unimannheim.dws.wikilist.util;

import java.util.List;

public class EvaluationResult {
	
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
	
	/** List of new RDF tuples */
	protected List<List<String>> rdfTriples = null;
	
	public EvaluationResult() {
		// TODO Auto-generated constructor stub
	}

	public int getNoOfDBPUriWikiUri() {
		return noOfDBPUriWikiUri;
	}

	public void setNoOfDBPUriWikiUri(int noOfDBPUriWikiUri) {
		this.noOfDBPUriWikiUri = noOfDBPUriWikiUri;
	}

	public int getNoOfDBPUriWikiLiteral() {
		return noOfDBPUriWikiLiteral;
	}

	public void setNoOfDBPUriWikiLiteral(int noOfDBPUriWikiLiteral) {
		this.noOfDBPUriWikiLiteral = noOfDBPUriWikiLiteral;
	}

	public int getNoOfDBPUriWikiEmpty() {
		return noOfDBPUriWikiEmpty;
	}

	public void setNoOfDBPUriWikiEmpty(int noOfDBPUriWikiEmpty) {
		this.noOfDBPUriWikiEmpty = noOfDBPUriWikiEmpty;
	}

	public int getNoOfDBPLiteralWikiUri() {
		return noOfDBPLiteralWikiUri;
	}

	public void setNoOfDBPLiteralWikiUri(int noOfDBPLiteralWikiUri) {
		this.noOfDBPLiteralWikiUri = noOfDBPLiteralWikiUri;
	}

	public int getNoOfDBPLiteralWikiLiteral() {
		return noOfDBPLiteralWikiLiteral;
	}

	public void setNoOfDBPLiteralWikiLiteral(int noOfDBPLiteralWikiLiteral) {
		this.noOfDBPLiteralWikiLiteral = noOfDBPLiteralWikiLiteral;
	}

	public int getNoOfDBPLiteralWikiEmpty() {
		return noOfDBPLiteralWikiEmpty;
	}

	public void setNoOfDBPLiteralWikiEmpty(int noOfDBPLiteralWikiEmpty) {
		this.noOfDBPLiteralWikiEmpty = noOfDBPLiteralWikiEmpty;
	}

	public int getNoOfDBPEmptyWikiUri() {
		return noOfDBPEmptyWikiUri;
	}

	public void setNoOfDBPEmptyWikiUri(int noOfDBPEmptyWikiUri) {
		this.noOfDBPEmptyWikiUri = noOfDBPEmptyWikiUri;
	}

	public int getNoOfDBPEmptyWikiLiteral() {
		return noOfDBPEmptyWikiLiteral;
	}

	public void setNoOfDBPEmptyWikiLiteral(int noOfDBPEmptyWikiLiteral) {
		this.noOfDBPEmptyWikiLiteral = noOfDBPEmptyWikiLiteral;
	}

	public int getNoOfDBPEmptyWikiEmpty() {
		return noOfDBPEmptyWikiEmpty;
	}

	public void setNoOfDBPEmptyWikiEmpty(int noOfDBPEmptyWikiEmpty) {
		this.noOfDBPEmptyWikiEmpty = noOfDBPEmptyWikiEmpty;
	}

	public List<List<String>> getRdfTuples() {
		return rdfTriples;
	}

	public void setRdfTuples(List<List<String>> rdfTuples) {
		this.rdfTriples = rdfTuples;
	}
	
	
	
	

}
