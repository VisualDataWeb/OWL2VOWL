/*
 * OWL_Class.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model;

import java.util.List;

/**
 * @author Eduard Marbach
 */
public class OWL_Class {
	private String name;
	private String type;
	private String iri;
	private List<OWL_Class> equivalentClasses;
	private List<OWL_Class> subClasses;
	private List<OWL_Class> superClasses;
	private List<OWL_Class> disjoints;

	/**
	 * Creates a new class object in owl form.
	 * Used for converting the RDF/XML and OWL/XML to the WebVOWL format.
	 */
	public OWL_Class() {

	}

	public void writeToJson() {

	}
}

