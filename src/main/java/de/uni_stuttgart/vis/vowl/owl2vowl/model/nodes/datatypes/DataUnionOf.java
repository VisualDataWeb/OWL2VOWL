/*
 * DataUnionOf.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class DataUnionOf extends BaseDatatype {
	protected Set<BaseDatatype> unionOf;

	public DataUnionOf() {
		super();
		//setType(...); TODO
		unionOf = new HashSet<BaseDatatype>();
		setName("Data union of");
		setComment("Dummy implementation. Not the real extracted element because not supported currently.");
	}

	public Set<BaseDatatype> getUnionOf() {
		return unionOf;
	}

	public void addUnion(BaseDatatype node) {
		if (node == null) {
			return;
		}

		unionOf.add(node);
	}

	public boolean equalsUnion(List<BaseDatatype> classes) {
		for (BaseDatatype aClass : classes) {
			if (!unionOf.contains(aClass)) {
				return false;
			}
		}

		return true;
	}

	public boolean equalUnionIris(List<String> iriList) {
		int equalIris = 0;

		for (BaseDatatype baseNode : unionOf) {
			if (iriList.contains(baseNode.getIri())) {
				equalIris++;
			}
		}

		return equalIris == unionOf.size() && iriList.size() == unionOf.size();
	}

	@Override
	protected void setID() {
		id = "dataunionof" + counterObjects;
		counterObjects++;
	}
}
