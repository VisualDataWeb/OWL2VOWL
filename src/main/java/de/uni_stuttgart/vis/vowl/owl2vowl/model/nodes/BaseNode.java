package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Node_Types;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.JsonGeneratorVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.BaseEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties.BaseProperty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Abstract base class for all nodes like OwlClass, OwlDatatype etc.
 */
public abstract class BaseNode extends BaseEntity {
	private List<BaseNode> disjoints = new ArrayList<BaseNode>();
	private List<BaseProperty> outGoingEdges = new ArrayList<BaseProperty>();
	private List<BaseProperty> inGoingEdges = new ArrayList<BaseProperty>();
	private Set<BaseNode> existingComplements = new HashSet<BaseNode>();

	/**
	 * Default constructor.
	 */
	public BaseNode() {
	}

	public List<BaseNode> getDisjoints() {
		return disjoints;
	}

	public void setDisjoints(List<BaseNode> disjoints) {
		this.disjoints = disjoints;
	}

	/**
	 *
	 * @return A list of properties which has this node as domain.
	 */
	public List<BaseProperty> getOutGoingEdges() {
		return outGoingEdges;
	}

	/**
	 *
	 * @return A list of properties which has this node as range.
	 */
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

	/**
	 * Check if the node contains an already connected thing.
	 *
	 * @param node The node to check for connected things.
	 * @return True if there exists at least one connected thing otherwise false.
	 */
	public boolean containsConnectedThing(BaseNode node) {
		for (BaseProperty out : getOutGoingEdges()) {
			BaseNode range = out.getRange();
			String type = range.getType();

			if (type.equals(Node_Types.TYPE_THING) && range.getId().equals(node.getId())) {
				return true;
			}
		}

		for (BaseProperty in : getInGoingEdges()) {
			BaseNode domain = in.getDomain();
			String type = domain.getType();

			if (type.equals(Node_Types.TYPE_THING) && domain.getId().equals(node.getId())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @return All nodes which are complements of this node.
	 */
	public Set<BaseNode> getExistingComplements() {
		return existingComplements;
	}

	@Override
	public void accept(JsonGeneratorVisitor visitor) {
		super.accept(visitor);
		visitor.visit(this);
	}
}
