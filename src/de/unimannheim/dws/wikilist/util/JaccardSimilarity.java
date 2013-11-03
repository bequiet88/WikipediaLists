package de.unimannheim.dws.wikilist.util;

import java.util.*;

/*
 * Jaccard Similarity is a similarity function which is calculated by 
 * first tokenizing the strings into sets and then taking the ratio of
 * (weighted) intersection to their union 
 */
public class JaccardSimilarity {

	public static double jaccardSimilarity(String similar1, String similar2) {
		HashSet<String> h1 = new HashSet<String>();
		HashSet<String> h2 = new HashSet<String>();

		for (int i = 0; i < similar1.length(); i++) {
			h1.add(""+similar1.charAt(i));
		}
		System.out.println("h1 " + h1);
		for (int i = 0; i < similar2.length(); i++) {
			h2.add(""+similar2.charAt(i));
		}
		System.out.println("h2 " + h2);

		int sizeh1 = h1.size();
		// Retains all elements in h3 that are contained in h2 ie intersection
		h1.retainAll(h2);
		// h1 now contains the intersection of h1 and h2
		System.out.println("Intersection " + h1);

		h2.removeAll(h1);
		// h2 now contains unique elements
		System.out.println("Unique in h2 " + h2);

		// Union
		int union = sizeh1 + h2.size();
		int intersection = h1.size();

		return (double) intersection / union;

	}
}