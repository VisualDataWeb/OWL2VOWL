/*
 * DisjointUnion.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.containerElements;

import de.uni_stuttgart.vis.vowl.owl2vowl.export.JsonGeneratorVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.BaseEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class DisjointUnion extends BaseEntity {
	private BaseNode baseNode;
	private Set<BaseNode> disjointness;

	public DisjointUnion() {
		disjointness = new HashSet<BaseNode>();
		setName("anonymous");
	}

	public DisjointUnion(BaseNode baseNode, Set<BaseNode> disjointness) {
		this.baseNode = baseNode;
		this.disjointness = disjointness;
	}

	public BaseNode getBaseNode() {
		return baseNode;
	}

	public void setBaseNode(BaseNode baseNode) {
		this.baseNode = baseNode;
	}

	public Set<BaseNode> getDisjointness() {
		return disjointness;
	}

	public void setDisjointness(Set<BaseNode> disjointness) {
		this.disjointness = disjointness;
	}

	@Override
	public String toString() {
		return "DisjointUnion{" +
				"baseNode=" + baseNode +
				", disjointness=" + disjointness +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DisjointUnion that = (DisjointUnion) o;

		if (baseNode != null ? !baseNode.equals(that.baseNode) : that.baseNode != null)
			return false;
		if (disjointness != null ? !disjointness.equals(that.disjointness) : that.disjointness != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = baseNode != null ? baseNode.hashCode() : 0;
		result = 31 * result + (disjointness != null ? disjointness.hashCode() : 0);
		return result;
	}

	@Override
	public void accept(JsonGeneratorVisitor visitor) {
		visitor.visit(this);
	}
}
