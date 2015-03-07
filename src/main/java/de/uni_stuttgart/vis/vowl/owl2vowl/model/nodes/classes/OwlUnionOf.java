package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes;


import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Node_Types;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.JsonGeneratorVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;

import java.util.List;

public class OwlUnionOf extends SpecialClass {
	public OwlUnionOf() {
		super();
		setType(Node_Types.TYPE_UNION);
	}

	public void addUnion(BaseNode node) {
		if (node == null) {
			return;
		}

		unionOf.add(node);
	}

	public boolean equalsUnion(List<BaseNode> classes) {
		for (BaseNode aClass : classes) {
			if (!unionOf.contains(aClass)) {
				return false;
			}
		}

		return true;
	}

	public boolean equalUnionIris(List<String> iriList) {
		int equalIris = 0;

		for (BaseNode baseNode : unionOf) {
			if (iriList.contains(baseNode.getIri())) {
				equalIris++;
			}
		}

		return equalIris == unionOf.size() && iriList.size() == unionOf.size();
	}

	@Override
	public void accept(JsonGeneratorVisitor visitor) {
		super.accept(visitor);
		visitor.visit(this);
	}
}
