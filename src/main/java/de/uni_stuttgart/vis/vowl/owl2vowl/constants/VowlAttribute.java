package de.uni_stuttgart.vis.vowl.owl2vowl.constants;

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
	EQUIVALENT("equivalent");

	private final String intersection;

	VowlAttribute(String intersection) {

		this.intersection = intersection;
	}

	public String getIntersection() {
		return intersection;
	}
}
