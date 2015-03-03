package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes;


import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Node_Types;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.JsonGeneratorVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;

import java.util.Collection;
import java.util.List;

public class OwlIntersectionOf extends SpecialClass {

	public OwlIntersectionOf() {
		super();
		setType(Node_Types.TYPE_INTERSECTION);
	}

	public void addIntersection(BaseNode node) {
		if (node == null) {
			return;
		}

		intersectionOf.add(node);
	}

	public void addIntersections(Collection<? extends BaseNode> nodes) {
		if (nodes == null) {
			return;
		}

		intersectionOf.addAll(nodes);
	}

	public boolean equalsIntersections(List<BaseNode> classes) {
		for (BaseNode aClass : classes) {
			if (!intersectionOf.contains(aClass)) {
				return false;
			}
		}

		return true;
	}

	public boolean equalIntersectionIris(List<String> iriList) {
		int equalIris = 0;

		for (String aIri : iriList) {
			for (BaseNode aUnion : intersectionOf) {
				if (aUnion.getIri().equals(aIri)) {
					equalIris++;
					break;
				}
			}
		}

		return equalIris == iriList.size();
	}

	@Override
	public void accept(JsonGeneratorVisitor visitor) {
		super.accept(visitor);
		visitor.visit(this);
	}
}
