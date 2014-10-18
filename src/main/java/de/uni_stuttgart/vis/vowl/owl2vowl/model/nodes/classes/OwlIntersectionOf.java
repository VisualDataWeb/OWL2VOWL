package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes;


import de.uni_stuttgart.vis.vowl.owl2vowl.model.Constants;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;

import java.util.ArrayList;
import java.util.List;

public class OwlIntersectionOf extends SpecialClass {

	public OwlIntersectionOf() {
		super();
		setType(Constants.TYPE_INTERSECTION);
	}

	public void addIntersection(BaseNode node) {
		if(node == null) {
			return;
		}

		intersections.add(node);
	}

	public boolean equalsIntersections(List<BaseNode> classes) {
		for (BaseNode aClass : classes) {
			if (!intersections.contains(aClass)) {
				return false;
			}
		}

		return true;
	}

	public boolean equalIntersectionIris(List<String> iriList) {
		int equalIris = 0;

		for (String aIri : iriList) {
			for(BaseNode aUnion : intersections) {
				if(aUnion.getIri().equals(aIri)) {
					equalIris++;
					break;
				}
			}
		}

		return equalIris == iriList.size();
	}
}
