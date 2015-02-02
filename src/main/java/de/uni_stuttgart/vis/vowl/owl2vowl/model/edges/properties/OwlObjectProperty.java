/*
 * OwlObjectProperty.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties;

import de.uni_stuttgart.vis.vowl.owl2vowl.export.JsonGeneratorVisitor;

/**
 * @author Vincent Link, Eduard Marbach
 */
public class OwlObjectProperty extends BaseProperty {

	public OwlObjectProperty() {
		super();
		setType("owl:objectProperty");
	}

	@Override
	public void accept(JsonGeneratorVisitor visitor) {
		super.accept(visitor);
		visitor.visit(this);
	}
}
