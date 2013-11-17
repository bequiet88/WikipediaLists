package de.unimannheim.dws.wikilist.models;

import java.util.List;


/**
 * The Class TableRow.
 */
public class TableRow {

	/** The my table row. */
	private List<String> myTableRow = null;
	
	/** The dbpedia attributes. */
	private List<String> dbpediaAttributes = null;

	/**
	 * Instantiates a new table row.
	 *
	 * @param row the row
	 */
	public TableRow(List<String> row) {

		myTableRow = row;

	}

	/**
	 * Instantiates a new table row.
	 *
	 * @param row the row
	 * @param dbpattr the dbpattr
	 */
	public TableRow(List<String> row, List<String> dbpattr) {
		dbpediaAttributes = dbpattr;
		myTableRow = row;

	}

	/**
	 * Gets the my table row.
	 *
	 * @return the my table row
	 */
	public List<String> getMyTableRow() {
		return myTableRow;
	}

	/**
	 * Sets the my table row.
	 *
	 * @param myTableRow the new my table row
	 */
	public void setMyTableRow(List<String> myTableRow) {
		this.myTableRow = myTableRow;
	}

	/**
	 * Gets the dbpedia attributes.
	 *
	 * @return the dbpedia attributes
	 */
	public List<String> getDbpediaAttributes() {
		return dbpediaAttributes;
	}

	/**
	 * Sets the dbpedia attributes.
	 *
	 * @param dbpediaAttributes the new dbpedia attributes
	 */
	public void setDbpediaAttributes(List<String> dbpediaAttributes) {
		this.dbpediaAttributes = dbpediaAttributes;
	}

	
	
}
