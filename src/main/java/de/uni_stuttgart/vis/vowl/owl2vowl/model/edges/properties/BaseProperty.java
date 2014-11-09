/*
 * BaseProperty.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.BaseEdge;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vincent Link, Eduard Marbach
 */
public class BaseProperty extends BaseEdge {
	protected static int indexCounter = 1;
	protected List<String> subProperties = new ArrayList<String>();
	protected List<String> superProperties = new ArrayList<String>();
	protected List<String> disjoints = new ArrayList<String>();
	protected List<String> equivalents = new ArrayList<String>();
	protected String inverseIRI;
	protected String inverseID;

	public BaseProperty() {
		super();
		setType("rdfs:Property");
		setId("property" + indexCounter);
		indexCounter++;
	}

	public List<String> getSuperProperties() {
		return superProperties;
	}

	public void setSuperProperties(List<String> superProperties) {
		this.superProperties = superProperties;
	}

	public List<String> getEquivalents() {
		return equivalents;
	}

	public void setEquivalents(List<String> equivalents) {
		this.equivalents = equivalents;
	}

	public List<String> getDisjoints() {
		return disjoints;
	}

	public void setDisjoints(List<String> disjoints) {
		this.disjoints = disjoints;
	}

	public List<String> getSubProperties() {
		return subProperties;
	}

	public void setSubProperties(List<String> subProperties) {
		this.subProperties = subProperties;
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
