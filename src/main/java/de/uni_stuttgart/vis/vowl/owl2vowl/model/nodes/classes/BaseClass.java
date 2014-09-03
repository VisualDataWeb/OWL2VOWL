/*
 * OWL_Class.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.Constants;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;

/**
 * @author Eduard Marbach
 */
public class BaseClass extends BaseNode {

	public BaseClass() {
		setType(Constants.TYPE_CLASS);
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

