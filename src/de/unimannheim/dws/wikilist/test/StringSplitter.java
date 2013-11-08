package de.unimannheim.dws.wikilist.test;

import java.util.Collection;
import java.util.LinkedList;

public class StringSplitter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String line = "|valign=\"left\"|1994–1995";
		
		if(line.contains("align=")) {
			line = line.substring(nthIndexOf(line, "|", 2)+1);
		}
		
		System.out.println(line);
		
		System.out.println(wiki2dbpLink(getLink("'''[[Cheras - Kajang Expressway]]''' (CKE/Grand Saga) ''(part of Federal Route {{JKR|1}})''")));
		
		Collection<String> cells = breakLineIntoCells("|align=center|[[2005 Copa Libertadores Finals|2005]]");
		
		for (String string : cells) {
			System.out.println(string);
		}
		
		
		
	}
	
	public static int nthIndexOf(String source, String sought, int n) {
	    int index = source.indexOf(sought);
	    if (index == -1) return -1;

	    for (int i = 1; i < n; i++) {
	        index = source.indexOf(sought, index + 1);
	        if (index == -1) return -1;
	    }
	    return index;
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
		try {

			if (s.startsWith("[[") || s.startsWith("''[[") || s.startsWith("'''[[")) {
				if (s.contains("|"))
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

	/**
	 * Break line into cells.
	 * 
	 * @param line
	 *            the line
	 * @return the collection
	 */
	private static Collection<String> breakLineIntoCells(String line) {
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


}
