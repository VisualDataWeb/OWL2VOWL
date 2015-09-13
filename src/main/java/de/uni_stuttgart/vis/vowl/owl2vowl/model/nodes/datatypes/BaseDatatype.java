package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Node_Types;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.JsonGeneratorVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;

/**
 * TODO Define hierachy
 */
public abstract class BaseDatatype extends BaseNode {
	protected static int counterObjects = 1;


	protected BaseDatatype() {
		setType(Node_Types.TYPE_DATATYPE);
		setID();
	}

	protected abstract void setID();

	@Override
	public void accept(JsonGeneratorVisitor visitor) {
		super.accept(visitor);
		visitor.visit(this);
	}
}
