/*
 * OntologyMetric.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model;

/**
 *
 */
public class OntologyMetric {
	private int classCount;
	private int objectPropertyCount;
	private int dataPropertyCount;
	private int datatypeCount;
	private int propertyCount;
	private int nodeCount;

	/* Unused */
	private int individualCount;
	private int axiomCount;

	public OntologyMetric() {
	}

	public void calculateSums() {
		datatypeCount = dataPropertyCount;
		propertyCount = objectPropertyCount + dataPropertyCount;
		nodeCount = datatypeCount + classCount;
	}

	public int getIndividualCount() {
		return individualCount;
	}

	public void setIndividualCount(int individualCount) {
		this.individualCount = individualCount;
	}

	public int getClassCount() {
		return classCount;
	}

	public void setClassCount(int classCount) {
		this.classCount = classCount;
	}

	public int getObjectPropertyCount() {
		return objectPropertyCount;
	}

	public void setObjectPropertyCount(int objectPropertyCount) {
		this.objectPropertyCount = objectPropertyCount;
	}

	public int getDataPropertyCount() {
		return dataPropertyCount;
	}

	public void setDataPropertyCount(int dataPropertyCount) {
		this.dataPropertyCount = dataPropertyCount;
	}

	public int getAxiomCount() {
		return axiomCount;
	}

	public void setAxiomCount(int axiomCount) {
		this.axiomCount = axiomCount;
	}

	public int getPropertyCount() {
		return propertyCount;
	}

	public void setPropertyCount(int propertyCount) {
		this.propertyCount = propertyCount;
	}

	public int getDatatypeCount() {
		return datatypeCount;
	}

	public void setDatatypeCount(int datatypeCount) {
		this.datatypeCount = datatypeCount;
	}

	public int getNodeCount() {
		return nodeCount;
	}

	public void setNodeCount(int nodeCount) {
		this.nodeCount = nodeCount;
	}
}
