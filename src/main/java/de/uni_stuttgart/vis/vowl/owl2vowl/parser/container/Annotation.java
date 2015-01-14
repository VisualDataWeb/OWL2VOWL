/*
 * Annotation.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.container;

/**
 *
 */
public class Annotation {
	private String identifier;
	private String language;
	private String value;

	public Annotation(String identifier, String value) {
		this.identifier = extractIdentifier(identifier);
		this.value = value;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	private String extractIdentifier(String identifier) {
		return identifier.substring(identifier.lastIndexOf("/") + 1, identifier.lastIndexOf(">"));
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
