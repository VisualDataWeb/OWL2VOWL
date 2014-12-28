/*
 * BaseProperty.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Property_Types;
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
	protected int exactCardinality = -1;
	protected int minCardinality = -1;
	protected int maxCardinality = -1;

	public BaseProperty() {
		super();
		setType(Property_Types.PROP_TYPE_RDF);
		setId("property" + indexCounter);
		indexCounter++;
	}

	public int getExactCardinality() {
		return exactCardinality;
	}

	public void setExactCardinality(int exactCardinality) {
		this.exactCardinality = exactCardinality;
	}

	public int getMinCardinality() {
		return minCardinality;
	}

	public void setMinCardinality(int minCardinality) {
		this.minCardinality = minCardinality;
	}

	public int getMaxCardinality() {
		return maxCardinality;
	}

	public void setMaxCardinality(int maxCardinality) {
		this.maxCardinality = maxCardinality;
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

	@Override
	public String toString() {
		return "BaseProperty{" +
				"subProperties=" + subProperties +
				", superProperties=" + superProperties +
				", disjoints=" + disjoints +
				", equivalents=" + equivalents +
				", inverseIRI='" + inverseIRI + '\'' +
				", inverseID='" + inverseID + '\'' +
				", exactCardinality=" + exactCardinality +
				", minCardinality=" + minCardinality +
				", maxCardinality=" + maxCardinality +
				"} " + super.toString();
	}
}
