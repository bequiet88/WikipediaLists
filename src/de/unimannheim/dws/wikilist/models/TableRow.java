package de.unimannheim.dws.wikilist.models;

import java.util.List;

public class TableRow {

	private List<String> myTableRow = null;
	private List<String> dbpediaAttributes = null;

	public TableRow(List<String> row) {

		myTableRow = row;

	}

	public TableRow(List<String> row, List<String> dbpattr) {
		dbpediaAttributes = dbpattr;
		myTableRow = row;

	}

	public List<String> getMyTableRow() {
		return myTableRow;
	}

	public void setMyTableRow(List<String> myTableRow) {
		this.myTableRow = myTableRow;
	}

	public List<String> getDbpediaAttributes() {
		return dbpediaAttributes;
	}

	public void setDbpediaAttributes(List<String> dbpediaAttributes) {
		this.dbpediaAttributes = dbpediaAttributes;
	}

	
	
}
