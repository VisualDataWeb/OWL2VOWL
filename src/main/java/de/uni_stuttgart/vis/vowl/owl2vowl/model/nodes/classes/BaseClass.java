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
}

