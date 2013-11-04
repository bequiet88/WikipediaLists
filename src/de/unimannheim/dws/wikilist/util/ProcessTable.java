package de.unimannheim.dws.wikilist.util;

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

public class ProcessTable {
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
				//"List_of_lesbian,_gay,_bisexual_or_transgender-related_films_of_1991");
		 //"List_of_Pennsylvania_state_historical_markers_in_Jefferson_County");
				"List_of_Peers_1330-1339");
		System.out.println(page.getText());
		System.out.println("---");

		List<List<String>> table = parseFirstTable(page.getText());
		for (List<String> row : table) {
			for (String cell : row)
				System.out.print(cell + "\t");
			System.out.println();
		}
		System.out.println(table.size());

		for (String link : getColumn(table, 0)) {
			System.out.println(link);
		}

		// shutdown
		ssh.close();
	}

	public static List<List<String>> parseFirstTable(String wikipage) {
		List<List<String>> result = new LinkedList<List<String>>();

		String[] lines = wikipage.split("\n");

		Map<Integer, Pair<String, Integer>> rowSpanCache = new HashMap<Integer, Pair<String, Integer>>();
		List<String> tableRowCache = new LinkedList<String>();

		boolean tableTypeOne = true;

		for (String line : lines) {
			// find first table line
			if (!line.startsWith("|"))
				continue;

			/*
			 * Determine correct table type
			 */

			if (line.startsWith("|--")) {
				tableTypeOne = true;
				if (tableRowCache.size() != 0) {
					result.add(tableRowCache);
					tableRowCache = new LinkedList<String>();
				}
				continue;
			}
			
			// line separators
			if (line.startsWith("|-")) {
				tableTypeOne = false;
				continue;
			}

			/*
			 * Wiki Table Type 1: Multiple JWPL Rows for one Table Row
			 */
			if (tableTypeOne) {
				tableRowCache.add(line.substring(line.indexOf("|") + 1));
			}

			/*
			 * Wiki Table Type 2: One JWPL Row for one Table Row
			 */

			if (!tableTypeOne) {

				Collection<String> cellStrings = breakLineIntoCells(line);

				List<String> cells = new LinkedList<String>();

				int column = 0;

				int rowspanRead = 0;

				for (String cell : cellStrings) {
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
						if (cell.length() == 0)
							continue;
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

	// removes all entries that do not match the common table length
	private static void cleanTable(List<List<String>> table) {

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
			if (it.next().size() > majorityLength + 1
					|| it.next().size() < majorityLength)
				it.remove();
			// if (it.next().size() > majorityLength+1 || it.next().size() <
			// majorityLength-1)
			// it.remove();
		}

	}

	private static Collection<String> breakLineIntoCells(String line) {
		Collection<String> result = new LinkedList<String>();
		int numOpeningLinkBrackets = 0;
		int numOpeningCurlyBraces = 0;
		StringBuilder currentCell = new StringBuilder();
		for (int i = 0; i < line.length() - 1; i++) {
			char c = line.charAt(i);
			char c2 = line.charAt(i + 1);
			if (c == '|' && c2 == '|' && numOpeningLinkBrackets == 0
					&& numOpeningCurlyBraces == 0) {
				result.add(currentCell.toString());
				currentCell = new StringBuilder();
				i++;
			} else if (c == '|' && numOpeningLinkBrackets == 0
					&& numOpeningCurlyBraces == 0) {
				result.add(currentCell.toString());
				currentCell = new StringBuilder();
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

	public static List<String> getColumn(List<List<String>> table, int column) {
		List<String> result = new LinkedList<String>();
		for (List<String> row : table) {
			if (column < row.size())
				result.add(wiki2dbpLink(getLink(row.get(column))));
		}
		return result;
	}

	/**
	 * Extract a link from a string
	 * 
	 * @param s
	 * @return
	 */
	public static String getLink(String s) {
		// System.out.println(s);
		if (s.startsWith("[[") || s.startsWith("''[[")) {
			if (s.contains("|"))
				return s.substring(s.indexOf("[[") + 2, s.indexOf("|"));
			else
				return s.substring(s.indexOf("[[") + 2, s.indexOf("]]"));
		} else if (s.startsWith("{{") && s.contains("[[")) {
			String substring = s
					.substring(s.indexOf("[[") + 2, s.indexOf("]]"));
			if (substring.contains("|"))
				return substring.substring(0, s.indexOf("|"));
			else
				return substring;

		} else if (s.startsWith("{{Sortname")) {
			String[] sArray = s.split("\\|");
			if (sArray.length == 3)
				return sArray[1] + " " + sArray[2];
			else
				return "";
		} else
			return "";
	}

	public static String wiki2dbpLink(String s) {
		if (s.equals(""))
			return "";
		return "<http://dbpedia.org/resource/" + s.replaceAll(" ", "_") + ">";
	}
}
