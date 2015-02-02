package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes;


import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Node_Types;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.JsonGeneratorVisitor;

public class OwlComplementOf extends SpecialClass {

	public OwlComplementOf() {
		super();
		setType(Node_Types.TYPE_COMPLEMENT);
	}

	@Override
	public void accept(JsonGeneratorVisitor visitor) {
		visitor.visit(this);
	}
}
