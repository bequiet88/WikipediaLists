package de.unimannheim.dws.wikilist.reader;

import java.util.List;

public class ReaderResource {

	private String resource;
	private List<String> resources;
	private String attribute;
	private String attrPrefix;

	/**
	 * Constructor for DBPedia Reader
	 * 
	 * @param resources
	 * @param attribute
	 * @param attrPrefix
	 */
	public ReaderResource(List<String> resources, String attribute,
			String attrPrefix) {
		super();
		this.resources = resources;
		this.attribute = attribute;
		this.attrPrefix = attrPrefix;
	}

	/**
	 * Constructor for Text-File, WikiMarkUp, CSV
	 * 
	 * @param resource
	 *            is path to file or title of wiki page
	 */
	public ReaderResource(String resource) {
		super();
		this.resource = resource;
	}

	public String getResource() {
		return resource;
	}

	public List<String> getResources() {
		return resources;
	}

	public String getAttribute() {
		return attribute;
	}

	public String getAttrPrefix() {
		return attrPrefix;
	}

}
