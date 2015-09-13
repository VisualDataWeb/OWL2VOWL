/*
 * DataIntersectionOf.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DataIntersectionOf extends BaseDatatype{
	public List<BaseDatatype> getIntersectionOf() {
		return intersectionOf;
	}

	protected List<BaseDatatype> intersectionOf;

	public DataIntersectionOf() {
		super();
		//setType(...); TODO
		intersectionOf = new ArrayList<BaseDatatype>();
		setName("Data intersection of");
		setComment("Dummy implementation. Not the real extracted element because not supported currently.");
	}

	@Override
	protected void setID() {
		id = "dataintersectionof" + counterObjects;
		counterObjects++;
	}
}
