/*
 * OwlDatatypeProperty.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties;

import de.uni_stuttgart.vis.vowl.owl2vowl.export.JsonGeneratorVisitor;

/**
 * @author Vincent Link, Eduard Marbach
 */
public class OwlDatatypeProperty extends BaseProperty {

	public OwlDatatypeProperty() {
		super();
		setType("owl:datatypeProperty");
	}

	@Override
	public void accept(JsonGeneratorVisitor visitor) {
		visitor.visit(this);
	}
}
