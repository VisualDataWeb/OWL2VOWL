/*
 * OneOf.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class OneOf extends BaseClass{
	protected List<BaseNode> oneOf;

	public OneOf() {
		super();
		//setType(...); TODO
		oneOf = new ArrayList<BaseNode>();
		setName("One Of");
	}

	public List<BaseNode> getOneOf() {
		return oneOf;
	}
}
