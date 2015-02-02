/*
 * BaseEdge.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.edges;

import de.uni_stuttgart.vis.vowl.owl2vowl.export.JsonGeneratorVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.BaseEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;

/**
 * @author Vincent Link, Eduard Marbach
 */
public class BaseEdge extends BaseEntity {
	private BaseNode domain;
	private BaseNode range;

	public BaseEdge() {
		super();
	}

	public BaseNode getDomain() {
		return domain;
	}

	public void setDomain(BaseNode domain) {
		this.domain = domain;
	}

	public BaseNode getRange() {
		return range;
	}

	public void setRange(BaseNode range) {
		this.range = range;
	}

	@Override
	public void accept(JsonGeneratorVisitor visitor) {
		super.accept(visitor);
		visitor.visit(this);
	}
}
