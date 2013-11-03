package de.unimannheim.dws.wikilist.test;

public class StringSplitter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println(getDBPediaLinkFromWikiLink("[[Duke of Cornwall]]"));
		
		
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
