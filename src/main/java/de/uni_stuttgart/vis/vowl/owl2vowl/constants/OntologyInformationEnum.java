/*
 * OntologyInformationEnum.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.constants;

import org.codehaus.jackson.annotate.JsonValue;

/**
 *
 */
public enum OntologyInformationEnum {
	TITLE("title"),
	VERSION("versionInfo"),
	AUTHOR("creator");

	private final String value;

	OntologyInformationEnum(String value) {

		this.value = value;
	}

	@JsonValue
	public String getValue() {
		return value;
	}
}
