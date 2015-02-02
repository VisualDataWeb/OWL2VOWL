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
	protected List<BaseNode> unions;
	protected List<BaseNode> intersections;
	protected List<BaseNode> complements;

	public SpecialClass() {
		super();
		unions = new ArrayList<BaseNode>();
		intersections = new ArrayList<BaseNode>();
		complements = new ArrayList<BaseNode>();
	}

	public List<BaseNode> getComplements() {
		return complements;
	}

	public void setComplements(List<BaseNode> complements) {
		this.complements = complements;
	}

	public List<BaseNode> getUnions() {
		return unions;
	}

	public void setUnions(List<BaseNode> unions) {
		this.unions = unions;
	}

	public List<BaseNode> getIntersections() {
		return intersections;
	}

	public void setIntersections(List<BaseNode> intersections) {
		this.intersections = intersections;
	}

	@Override
	public void accept(JsonGeneratorVisitor visitor) {
		super.accept(visitor);
		visitor.visit(this);
	}
}
