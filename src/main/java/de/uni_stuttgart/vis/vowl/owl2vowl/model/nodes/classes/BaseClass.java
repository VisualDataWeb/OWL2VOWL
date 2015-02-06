/*
 * OWL_Class.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Node_Types;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.JsonGeneratorVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.individuals.NamedIndividual;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Eduard Marbach
 */
public class BaseClass extends BaseNode {

	protected static int indexCounter = 1;
	protected int numberOfIndividuals = 0;
	protected Set<NamedIndividual> individuals;

	public BaseClass() {
		super();
		setType(Node_Types.TYPE_CLASS);
		setId("class" + indexCounter);
		individuals = new HashSet<NamedIndividual>();
		indexCounter++;
	}

	public Set<NamedIndividual> getIndividuals() {
		return individuals;
	}

	public void setIndividuals(Set<NamedIndividual> individuals) {
		this.individuals = individuals;
	}

	public int getNumberOfIndividuals() {
		return numberOfIndividuals;
	}

	public void setNumberOfIndividuals(int numberOfIndividuals) {
		this.numberOfIndividuals = numberOfIndividuals;
	}

	@Override
	public void accept(JsonGeneratorVisitor visitor) {
		super.accept(visitor);
		visitor.visit(this);
	}
}

