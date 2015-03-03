/*
 * SpecialClass.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes;

import de.uni_stuttgart.vis.vowl.owl2vowl.export.JsonGeneratorVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class SpecialClass extends BaseClass {
	protected List<BaseNode> unionOf;
	protected List<BaseNode> intersectionOf;
	protected List<BaseNode> complementOf;

	public SpecialClass() {
		super();
		unionOf = new ArrayList<BaseNode>();
		intersectionOf = new ArrayList<BaseNode>();
		complementOf = new ArrayList<BaseNode>();
	}

	public List<BaseNode> getComplementOf() {
		return complementOf;
	}

	public void setComplementOf(List<BaseNode> complementOf) {
		this.complementOf = complementOf;
	}

	public List<BaseNode> getUnionOf() {
		return unionOf;
	}

	public void setUnionOf(List<BaseNode> unionOf) {
		this.unionOf = unionOf;
	}

	public List<BaseNode> getIntersectionOf() {
		return intersectionOf;
	}

	public void setIntersectionOf(List<BaseNode> intersectionOf) {
		this.intersectionOf = intersectionOf;
	}

	@Override
	public void accept(JsonGeneratorVisitor visitor) {
		super.accept(visitor);
		visitor.visit(this);
	}
}
