/*
 * TypeOfProperty.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties;

import de.uni_stuttgart.vis.vowl.owl2vowl.export.JsonGeneratorVisitor;

/**
 *
 */
public class TypeOfProperty extends BaseProperty {

	public TypeOfProperty() {
		super();
		setName("type");
	}

	@Override
	public void accept(JsonGeneratorVisitor visitor) {
		super.accept(visitor);
		visitor.visit(this);
	}
}
