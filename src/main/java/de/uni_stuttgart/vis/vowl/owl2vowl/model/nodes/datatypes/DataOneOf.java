/*
 * DataOneOf.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DataOneOf extends BaseDatatype {
	protected List<BaseNode> oneOf;

	public DataOneOf() {
		super();
		//setType(...); TODO
		oneOf = new ArrayList<BaseNode>();
		setName("Data One Of");
		setID();
	}

	public List<BaseNode> getOneOf() {
		return oneOf;
	}

	@Override
	protected void setID() {
		id = "dataoneof" + counterObjects;
		counterObjects++;
	}
}
