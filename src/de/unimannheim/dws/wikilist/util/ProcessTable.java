package de.unimannheim.dws.wikilist.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.tudarmstadt.ukp.wikipedia.api.DatabaseConfiguration;
import de.tudarmstadt.ukp.wikipedia.api.Page;
import de.tudarmstadt.ukp.wikipedia.api.WikiConstants.Language;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.unimannheim.dws.wikilist.models.Pair;

// TODO: Auto-generated Javadoc
/**
 * The Class ProcessTable.
 */
public class ProcessTable {

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws Exception
	 *             the exception
	 */
	public static void main(String[] args) throws Exception {
		// setup
		SSHConnection ssh = new SSHConnection();
		DatabaseConfiguration dbConfig = new DatabaseConfiguration();
		dbConfig.setHost("127.0.0.1:1234");
		dbConfig.setDatabase("jwpl");
		dbConfig.setUser("wikilist");
		dbConfig.setPassword("likiwist");
		dbConfig.setLanguage(Language.english);

		// do something
		Wikipedia wiki = new Wikipedia(dbConfig);

		Page page = new Page(wiki,
		// "List_of_lesbian,_gay,_bisexual_or_transgender-related_films_of_1991");
		// "List_of_Pennsylvania_state_historical_markers_in_Jefferson_County");
		// "List_of_horror_films_of_2001");
		// "List_of_places_in_Florida:_T-V");
				"List_of_Copa_Libertadores_winning_managers");
		System.out.println(page.getText());
		System.out.println("---");

		List<List<String>> table = parseFirstTable(page.getText());
		for (List<String> row : table) {
			for (String cell : row)
				System.out.print(cell + "\t");
			System.out.println();
		}
		System.out.println(table.size());

		for (String link : getColumn(table,2)) {
			System.out.println(link);
		}

		// shutdown
		ssh.close();
	}

	/**
	 * Parses the first table.
	 * 
	 * @param wikipage
	 *            the wikipage
	 * @return the list
	 */
	public static List<List<String>> parseFirstTable(String wikipage) {
		List<List<String>> result = new LinkedList<List<String>>();

		String[] lines = wikipage.split("\n");

		Map<Integer, Pair<String, Integer>> rowSpanCache = new HashMap<Integer, Pair<String, Integer>>();
		List<String> tableRowCache = new LinkedList<String>();
		String lineCache = "";

		boolean tableTypeOne = true;

		List<List<String>> tableTypeIdentifier = new LinkedList<List<String>>();

		/*
		 * Determine correct table type
		 */
		for (String line : lines) {
			// if(line.startsWith(!) )

			if (!line.startsWith("|"))
				continue;
			// line separators
			if (line.startsWith("|-")) {
				continue;
			}
			lineCache = line;

			Collection<String> cellStrings = breakLineIntoCells(line);
			tableTypeIdentifier.add(new LinkedList<String>(cellStrings));

		}

		int majorityLength = cleanTable(tableTypeIdentifier);

		if (majorityLength == 1) {
			tableTypeOne = true;
		} else {
			tableTypeOne = false;
		}
		/*
		 * Parse Table
		 */

		for (String line : lines) {

			if (!line.startsWith("|"))
				continue;

			if (line.startsWith("|-") && tableTypeOne == true) {
				if (tableRowCache.size() != 0) {
					result.add(tableRowCache);
					tableRowCache = new LinkedList<String>();
				}
				continue;
			}

			// line separators
			else if (line.startsWith("|-")) {
				continue;
			}

			/*
			 * Wiki Table Type 1: Multiple JWPL Rows for one Table Row
			 */
			if (tableTypeOne == true) {
				if (line.contains("align=")) {
					line = line.substring(nthIndexOf(line, "|", 2) + 1);
					tableRowCache.add(line);
				} else {
					tableRowCache.add(line.substring(line.indexOf("|") + 1));
				}
			}

			/*
			 * Wiki Table Type 2: One JWPL Row for one Table Row
			 */

			if (tableTypeOne == false) {

				Collection<String> cellStrings = breakLineIntoCells(line);

				List<String> cells = new LinkedList<String>();

				int column = 0;

				int rowspanRead = 0;

				for (String cell : cellStrings) {
					// omit cell format
					if (cell.contains("align=")) {
						cell = cell.substring(nthIndexOf(cell, "|", 1) + 1);
					}
					// fill from rowspan cache, if necessary
					if (rowSpanCache.containsKey(column)) {
						cells.add(rowSpanCache.get(column).getFirst());
						int newCount = rowSpanCache.get(column).getSecond() - 1;
						if (newCount == 0)
							rowSpanCache.remove(column);
						else
							rowSpanCache.get(column).setSecond(newCount);

						// normal parsing
					} else {
						cell = cell.trim();
						if (cell.length() == 0) {
							cells.add(cell);
							continue;
						}
						// formatting cells
						if (cell.indexOf("=\"") > 0) {
							if (cell.indexOf("rowspan=\"") >= 0)
								rowspanRead = Integer
										.parseInt(cell.substring(
												cell.indexOf("rowspan=\"") + 9,
												cell.indexOf(
														"\"",
														cell.indexOf("rowspan=\"") + 10)));
							// TODO handle colspan
						} else {
							if (rowspanRead > 0) {
								rowSpanCache.put(column,
										new Pair<String, Integer>(cell,
												rowspanRead - 1));
								rowspanRead = 0;
							}
							cells.add(cell);
							column++;
						}
					}
				}
				result.add(cells);
			}

			// stop after last table line
			if (line.startsWith("|}") && tableRowCache.size() != 0) {
				result.add(tableRowCache);
				break;
			}
			if (line.startsWith("|}"))
				break;
		}

		cleanTable(result);

		return result;
	}

	/**
	 * Gets the column.
	 * 
	 * @param table
	 *            the table
	 * @param column
	 *            the column
	 * @return the column
	 */
	public static List<String> getColumn(List<List<String>> table, int column) {
		List<String> result = new ArrayList<String>();
		for (List<String> row : table) {
			if (column < row.size())
				result.add(wiki2dbpLink(getLink(row.get(column))));
		}
		return result;
	}
	
	/**
	 * Gets the column.
	 * 
	 * @param table
	 *            the table
	 * @param column
	 *            the column
	 * @return the column
	 */
	public static List<String> getPlainColumn(List<List<String>> table, int column) {
		List<String> result = new ArrayList<String>();
		for (List<String> row : table) {
			if (column < row.size())
				result.add(row.get(column));
		}
		return result;
	}
	

	/**
	 * Extract a link from a string.
	 * 
	 * @param s
	 *            the s
	 * @return the link
	 */
	public static String getLink(String s) {
		// System.out.println(s);
		s = s.trim();
		try {

			if (s.startsWith("[[") || s.startsWith("''[[") || s.startsWith("'''[[")) {
				
				String t = s.substring(s.indexOf("[[") + 2, s.indexOf("]]"));
				if (t.contains("|"))
					return s.substring(s.indexOf("[[") + 2, s.indexOf("|"));
				else
					return s.substring(s.indexOf("[[") + 2, s.indexOf("]]"));
			} else if (s.startsWith("{{") && s.contains("[[")) {
				String substring = s.substring(s.indexOf("[[") + 2,
						s.indexOf("]]"));
				if (substring.contains("|"))
					return substring.substring(0, s.indexOf("|"));
				else
					return substring;

			} else if (s.startsWith("{{Sortname") || s.startsWith("{{sortname")) {
				String[] sArray = s.split("\\|");
				if (sArray.length == 3)
					return sArray[1] + " "
							+ sArray[2].substring(0, sArray[2].indexOf("}}"));
				else
					return "";
			} else
				return "";
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Wiki2dbp link.
	 * 
	 * @param s
	 *            the s
	 * @return the string
	 */
	public static String wiki2dbpLink(String s) {
		if (s.equals(""))
			return "";
		return "<http://dbpedia.org/resource/" + s.replaceAll(" ", "_") + ">";
	}

	// removes all entries that do not match the common table length
	/**
	 * Clean table.
	 * 
	 * @param table
	 *            the table
	 * @return the int
	 */
	private static int cleanTable(List<List<String>> table) {

		// count
		Map<Integer, Integer> lengthCount = new HashMap<Integer, Integer>();
		for (List<String> line : table) {
			int count = line.size();
			if (!lengthCount.containsKey(count))
				lengthCount.put(count, 1);
			else
				lengthCount.put(count, lengthCount.get(count) + 1);
		}

		// find max
		int majorityLength = 0;
		int majorityCount = 0;
		for (Map.Entry<Integer, Integer> entry : lengthCount.entrySet()) {
			if (entry.getValue() > majorityCount) {
				majorityLength = entry.getKey();
				majorityCount = entry.getValue();
			}
		}

		// discard lines
		Iterator<List<String>> it = table.iterator();
		while (it.hasNext()) {
			List<String> next = it.next();
			if (next.size() > majorityLength + 1
					|| next.size() < majorityLength)
				it.remove();
			// if (it.next().size() > majorityLength+1 || it.next().size() <
			// majorityLength-1)
			// it.remove();
		}

		return majorityLength;

	}

	/**
	 * Break line into cells.
	 * 
	 * @param line
	 *            the line
	 * @return the collection
	 */
	private static Collection<String> breakLineIntoCells(String line) {
		// omit cell format
		if (line.contains("align=")) {
			line = line.substring(nthIndexOf(line, "|", 1) + 1);
		}
		Collection<String> result = new LinkedList<String>();
		int numOpeningLinkBrackets = 0;
		int numOpeningCurlyBraces = 0;
		StringBuilder currentCell = new StringBuilder();
		for (int i = 1; i < line.length() - 1; i++) {
			char c = line.charAt(i);
			char c2 = line.charAt(i + 1);
			if (c == '|' && c2 == '|' && numOpeningLinkBrackets == 0
					&& numOpeningCurlyBraces == 0) {
				result.add(currentCell.toString());
				currentCell = new StringBuilder();
				i++;
				// } else if (c == '|' && numOpeningLinkBrackets == 0
				// && numOpeningCurlyBraces == 0) {
				// result.add(currentCell.toString());
				// currentCell = new StringBuilder();
			} else {
				currentCell.append(c);
				if (c == '[')
					numOpeningLinkBrackets++;
				if (c == ']')
					numOpeningLinkBrackets--;
				if (c == '{')
					numOpeningCurlyBraces++;
				if (c == '}') {
					numOpeningCurlyBraces--;
				}
			}
		}
		result.add(currentCell.toString());

		return result;
	}

	/**
	 * Nth index of.
	 * 
	 * @param source
	 *            the source
	 * @param sought
	 *            the sought
	 * @param n
	 *            the n
	 * @return the int
	 */
	private static int nthIndexOf(String source, String sought, int n) {
		int index = source.indexOf(sought);
		if (index == -1)
			return -1;

		for (int i = 1; i < n; i++) {
			index = source.indexOf(sought, index + 1);
			if (index == -1)
				return -1;
		}
		return index;
	}
}
