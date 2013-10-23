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
        
        Page page = new Page(wiki,"List_of_Peers_1330-1339");
        System.out.println(page.getText());
        System.out.println("---");
        
        List<List<String>> table = parseFirstTable(page.getText());
        for(List<String> row : table) {
        	for(String cell: row) 
        		System.out.print(cell + "\t");
        	System.out.println();
        }
        
		// shutdown
		ssh.close();
		
	}

	public static List<List<String>> parseFirstTable(String wikipage) {
		List<List<String>> result = new LinkedList<List<String>>();
		
		String[] lines = wikipage.split("\n");
		
		Map<Integer,Pair<String,Integer>> rowSpanCache = new HashMap<Integer,Pair<String,Integer>>();
		
		for(String line: lines) {
			// find first table line
			if(!line.startsWith("|"))
				continue;
			
			// line separators
			if(line.startsWith("|-"))
				continue;
			
			List<String> cells = new LinkedList<String>();

			Collection<String> cellStrings = breakLineIntoCells(line);
			
			int column = 0;
			
			int rowspanRead = 0;
			
			for(String cell : cellStrings) {
				// fill from rowspan cache, if necessary
				if(rowSpanCache.containsKey(column)) {
					cells.add(rowSpanCache.get(column).getFirst());
					int newCount = rowSpanCache.get(column).getSecond()-1;
					if(newCount==0)
						rowSpanCache.remove(column);
					else
						rowSpanCache.get(column).setSecond(newCount);
					
				// normal parsing
				} else {
					cell = cell.trim();
					if(cell.length()==0)
						continue;
					// formatting cells
					if(cell.indexOf("=\"")>0) {
						if(cell.indexOf("rowspan=\"")>=0)
							rowspanRead = Integer.parseInt(cell.substring(cell.indexOf("rowspan=\"")+9, cell.indexOf("\"", cell.indexOf("rowspan=\"")+10)));
						// TODO handle colspan
					} else {
						if(rowspanRead>0) {
							rowSpanCache.put(column, new Pair<String,Integer>(cell,rowspanRead-1));
							rowspanRead = 0;
						}
						cells.add(cell);
						column++;
					}
				}
			}
			result.add(cells);
			
			// stop after last table line
			if(line.startsWith("|}"))
				break;
		}
				
		cleanTable(result);
		
		return result;
	}
	
	// removes all entries that do not match the common table length
	private static void cleanTable(List<List<String>> table) {
		
		// count
		Map<Integer,Integer> lengthCount = new HashMap<Integer,Integer>();
		for(List<String> line : table) {
			int count = line.size();
			if(!lengthCount.containsKey(count))
				lengthCount.put(count,1);
			else
				lengthCount.put(count, lengthCount.get(count)+1);
		}
		
		// find max
		int majorityLength = 0;
		int majorityCount = 0;
		for(Map.Entry<Integer,Integer> entry : lengthCount.entrySet()) {
			if(entry.getValue()>majorityCount) {
				majorityLength = entry.getKey();
				majorityCount = entry.getValue();
			}
		}
		
		// discard lines
		Iterator<List<String>> it = table.iterator();
		while(it.hasNext()) {
			if(it.next().size()!=majorityLength)
				it.remove();
		}
		
	}

	private static Collection<String> breakLineIntoCells(String line) {
		Collection<String> result = new LinkedList<String>();
		int numOpeningLinkBrackets = 0;
		StringBuilder currentCell = new StringBuilder();
		for(int i=0;i<line.length();i++) {
			char c = line.charAt(i);
			if(c=='|' && numOpeningLinkBrackets==0) {
				result.add(currentCell.toString());
				currentCell = new StringBuilder();
			} else {
				currentCell.append(c);
				if(c=='[')
					numOpeningLinkBrackets++;
				if(c==']')
					numOpeningLinkBrackets--;
			}
		}
		result.add(currentCell.toString());
		
		return result;
	}
}
