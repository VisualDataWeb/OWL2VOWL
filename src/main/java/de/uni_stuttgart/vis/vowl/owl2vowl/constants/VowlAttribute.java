package de.uni_stuttgart.vis.vowl.owl2vowl.constants;

import org.codehaus.jackson.annotate.JsonValue;

public enum VowlAttribute {
	INTERSECTION("intersection"),
	OBJECT("object"),
	DEPRECATED("deprecated"),
	EXTERNAL("external"),
	RDF("rdf"),
	UNION("union"),
	COMPLEMENT("complement"),
	DATATYPE("datatype"),
	TRANSITIVE("transitive"),
	FUNCTIONAL("functional"),
	INVERSE_FUNCTIONAL("inverse functional"),
	SYMMETRIC("symmetric"),
	EQUIVALENT("equivalent"),
	IRREFLEXIVE("irreflexive"),
	ASYMMETRIC("asymmetric"),
	REFLEXIVE("reflexive"),
	IMPORTED("external"),
	ANONYMOUS("anonymous"),
	DISJOINTUNION("disjointUnion"),
	KEY("key"),
	ALL_VALUES("allValues"),
	SOME_VALUES("someValues");

	private final String value;

	VowlAttribute(String value) {

		this.value = value;
	}

	@JsonValue
	public String getValue() {
		return value;
	}
}
