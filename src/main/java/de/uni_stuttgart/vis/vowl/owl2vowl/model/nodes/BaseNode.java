package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Node_Types;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.BaseEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties.BaseProperty;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseNode extends BaseEntity {
	private List<BaseNode> disjoints = new ArrayList<BaseNode>();
	private List<BaseProperty> outGoingEdges = new ArrayList<BaseProperty>();
	private List<BaseProperty> inGoingEdges = new ArrayList<BaseProperty>();

	public BaseNode() {
	}

	public List<BaseNode> getDisjoints() {
		return disjoints;
	}

	public void setDisjoints(List<BaseNode> disjoints) {
		this.disjoints = disjoints;
	}

	public List<BaseProperty> getOutGoingEdges() {
		return outGoingEdges;
	}

	public List<BaseProperty> getInGoingEdges() {
		return inGoingEdges;
	}

	/**
	 * Returns the owl:Thing which is already connected to the node.
	 *
	 * @return Owl:Thing which has connection to this node. Else null.
	 */
	public BaseNode getConnectedThing() {
		for (BaseProperty out : getOutGoingEdges()) {
			String type = out.getRange().getType();

			if (type.equals(Node_Types.TYPE_THING)) {
				return out.getRange();
			}
		}

		for (BaseProperty in : getInGoingEdges()) {
			String type = in.getDomain().getType();

			if (type.equals(Node_Types.TYPE_THING)) {
				return in.getDomain();
			}
		}

		return null;
	}
}
