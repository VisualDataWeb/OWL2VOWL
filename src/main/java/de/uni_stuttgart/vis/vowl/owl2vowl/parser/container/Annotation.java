/*
 * Annotation.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.container;

/**
 *
 */
public class Annotation {
	public static final String TYPE_IRI = "iri";
	public static final String TYPE_LABEL = "label";
	private String identifier;
	private String language;
	private String value;
	private String type;

	public Annotation(String identifier, String value) {
		this.identifier = extractIdentifier(identifier);
		this.value = value;
		this.type = "unset";
	}

	public Annotation(String identifier, String value, String type) {
		this.identifier = extractIdentifier(identifier);
		this.value = value;
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	private String extractIdentifier(String identifier) {
		int slash = identifier.lastIndexOf("/");
		int arrow = identifier.lastIndexOf(">");
		int doubleDot = identifier.lastIndexOf(":");
		int hash = identifier.lastIndexOf("#");

		int lastCut = identifier.length() - 1;
		int firstCut = 0;

		if (arrow != -1) {
			lastCut = arrow;
		}

		if (slash != -1 && firstCut < slash) {
			firstCut = slash;
		}

		if (doubleDot != -1 && firstCut < doubleDot) {
			firstCut = doubleDot;
		}

		if (hash != -1 && firstCut < hash) {
			firstCut = hash;
		}

		return identifier.substring(firstCut + 1, lastCut);
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Annotation{" +
				"identifier='" + identifier + '\'' +
				", language='" + language + '\'' +
				", value='" + value + '\'' +
				'}';
	}
}
