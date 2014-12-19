/*
 * OWL_Class.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Node_Types;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;

/**
 * @author Eduard Marbach
 */
public class BaseClass extends BaseNode {

	protected static int indexCounter = 1;
	protected int numberOfIndividuals = 0;

	public BaseClass() {
		super();
		setType(Node_Types.TYPE_CLASS);
		setId("class" + indexCounter);
		indexCounter++;
	}

	public int getNumberOfIndividuals() {
		return numberOfIndividuals;
	}

	public void setNumberOfIndividuals(int numberOfIndividuals) {
		this.numberOfIndividuals = numberOfIndividuals;
	}

	public void printClass() {
		System.out.println("BaseClass{");
		System.out.println("   name=" + getName() + ",");
		System.out.println("   comment=" + getComment() + ",");
		System.out.println("   type=" + getType() + ",");
		System.out.println("   iri=" + getIri() + ",");
		System.out.println("   id=" + getId() + ",");
		System.out.println("   definedBy=" + getDefinedBy() + ",");
		System.out.println("   owlVersion=" + getOwlVersion() + ",");
		System.out.println("   attributes=" + getAttributes() + ",");
		System.out.println("   subClasses=" + getSubClasses() + ",");
		System.out.println("   superClasses=" + getSuperClasses() + ",");
		System.out.println("   disjoints=" + getDisjoints() + ",");
	}

	@Override
	public String toString() {
		return getName();
	}
}

