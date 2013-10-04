package de.unimannheim.dws.wikilist.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Annotator {

	String tag = null;
	Pattern p = null;

	ArrayList<String> output = null;

	public Annotator(String regex, String tag) {
		this.tag = tag;
		p = Pattern.compile(regex);
	}

	public List<String> annotate(List<String> input) {

		output = new ArrayList<String>();

		for (String string : input) {

			Matcher m = p.matcher(string);

			if (m.find()) {
				string = m.replaceFirst("<" + tag + ">" + m.group() + "</"
						+ tag + ">");
			}
			output.add(string);

		}

		return output;

	}

	@Override
	public String toString() {

		String out = "";
		if (output != null) {
			for (String line : output) {
				out += line + "\n";
			}
		}
		return out;

	}

}
