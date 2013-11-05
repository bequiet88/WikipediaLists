package de.unimannheim.dws.wikilist.test;

public class StringSplitter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String line = "|valign=\"left\"|1994–1995";
		
		if(line.contains("align=")) {
			line = line.substring(nthIndexOf(line, "|", 2)+1);
		}
		
		System.out.println(line);
		
		System.out.println(getDBPediaLinkFromWikiLink("[[Duke of Cornwall]]"));
		
		
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
	
	
	private static String getDBPediaLinkFromWikiLink(String tableField) {

		String resLink = "";

		if (tableField.startsWith("[[")) {

			String[] resArray = tableField.split("\\|",2);

			if (resArray[0].endsWith("]]")) {
				resLink = "<http://dbpedia.org/resource/"
						+ resArray[0].substring(2, resArray[0].length() - 2)
								.replace(" ", "_") + ">";
			} else {
				resLink = "<http://dbpedia.org/resource/"
						+ resArray[0].substring(2).replace(" ", "_") + ">";
			}
		}

		return resLink;

	}

}
