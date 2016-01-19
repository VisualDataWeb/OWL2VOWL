package de.uni_stuttgart.vis.vowl.owl2vowl.model.annotation;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Vowl_Lang;

/**
 * @author Eduard
 */
public class Annotation {
	public static final String TYPE_IRI = "iri";
	public static final String TYPE_LABEL = "label";
	private String identifier;
	private String language = Vowl_Lang.LANG_UNSET;
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

		int lastCut = -1;
		int firstCut = -1;

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

		String result;

		if (firstCut != -1 && lastCut != -1) {
			result = identifier.substring(firstCut + 1, lastCut);
		} else if (firstCut != -1) {
			result = identifier.substring(firstCut + 1);
		} else if (lastCut != -1){
			result = identifier.substring(0, lastCut);
		} else {
			result = identifier;
		}

		return result;
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
				"type='" + type + "\'" +
				", identifier='" + identifier + '\'' +
				", language='" + language + '\'' +
				", value='" + value + '\'' +
				'}';
	}
}
