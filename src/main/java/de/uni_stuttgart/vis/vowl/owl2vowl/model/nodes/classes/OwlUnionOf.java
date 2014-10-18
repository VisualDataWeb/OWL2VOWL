package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes;


import de.uni_stuttgart.vis.vowl.owl2vowl.model.Constants;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;

import java.util.ArrayList;
import java.util.List;

public class OwlUnionOf extends SpecialClass {
	public OwlUnionOf() {
		super();
		setType(Constants.TYPE_UNION);
	}

	public void addUnion(BaseNode node) {
		if(node == null) {
			return;
		}

		unions.add(node);
	}

	public boolean equalsUnion(List<BaseNode> classes) {
		for (BaseNode aClass : classes) {
			if (!unions.contains(aClass)) {
				return false;
			}
		}

		return true;
	}

	public boolean equalUnionIris(List<String> iriList) {
		int equalIris = 0;

		for (String aIri : iriList) {
			for(BaseNode aUnion : unions) {
				if(aUnion.getIri().equals(aIri)) {
					equalIris++;
					break;
				}
			}
		}

		return equalIris == iriList.size();
	}

	@Override
	public String toString() {
		return "OwlUnionOf_" +
				this.id +
				"_{" +
				"unions=" + unions +
				'}';
	}
}
