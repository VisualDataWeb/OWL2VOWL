/*
 * DataComplementOf.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class DataComplementOf extends BaseDatatype {

	protected Set<BaseDatatype> complementOf;

	public DataComplementOf() {
		super();
		//setType(...); TODO
		complementOf = new HashSet<BaseDatatype>();
		setName("Data complement of");
		setComment("Dummy implementation. Not the real extracted element because not supported currently.");
	}

	public Set<BaseDatatype> getComplementOf() {
		return complementOf;
	}

	public boolean isComplementOf(String iri) {
		for (BaseDatatype datatype : complementOf) {
			if (datatype.getIri().equals(iri)) {
				return true;
			}
		}

		return false;
	}

	@Override
	protected void setID() {
		id = "datacomplementof" + counterObjects;
		counterObjects++;
	}
}
