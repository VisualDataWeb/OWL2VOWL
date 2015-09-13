/*
 * ObjectOneOf.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.BaseEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.individuals.NamedIndividual;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class ObjectOneOf extends BaseNode {

	protected BaseNode baseNode;
	protected List<NamedIndividual> oneOf;

	public ObjectOneOf() {
		oneOf = new ArrayList<NamedIndividual>();
	}

	public boolean contain(String iri) {
		if (iri == null || iri.isEmpty()) {
			return false;
		}

		for (NamedIndividual node : oneOf) {
			if (node.getIri().equals(iri)) {
				return true;
			}
		}

		return false;
	}

	public boolean contains(NamedIndividual node) {
		return oneOf.contains(node);
	}

	public boolean matchesIndividualList(List<NamedIndividual> matchingList) {
		Set<Object> set1 = new HashSet<Object>();
		set1.addAll(matchingList);
		Set<Object> set2 = new HashSet<Object>();
		set2.addAll(oneOf);

		return set1.equals(set2);
	}

	public boolean matchesIriList(List<String> matchingList) {
		int matchingIndex = 0;

		for (NamedIndividual individual : oneOf) {
			if (matchingList.contains(individual.getIri())) {
				matchingIndex++;
			}
		}

		return matchingIndex == matchingList.size();
	}

	public void setBaseNode(BaseNode baseNode) {
		this.baseNode = baseNode;
	}

	public List<NamedIndividual> getOneOf() {

		return oneOf;
	}

	public BaseNode getBaseNode() {
		return baseNode;
	}
}
