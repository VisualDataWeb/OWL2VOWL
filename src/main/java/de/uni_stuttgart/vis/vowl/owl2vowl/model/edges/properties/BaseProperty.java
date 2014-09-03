/*
 * BaseProperty.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.BaseEdge;

/**
 * @author Vincent Link, Eduard Marbach
 */
public class BaseProperty extends BaseEdge {
	private String inverseIRI;
	private String inverseID;
	protected static int indexCounter = 1;

	public BaseProperty() {
		super();
		setType("rdfs:Property");
		setId("property" + indexCounter);
		indexCounter++;
	}

	public String getInverseIRI() {
		return inverseIRI;
	}

	public void setInverseIRI(String inverseIRI) {
		this.inverseIRI = inverseIRI;
	}

	public String getInverseID() {
		return inverseID;
	}

	public void setInverseID(String inverseID) {
		this.inverseID = inverseID;
	}
}
