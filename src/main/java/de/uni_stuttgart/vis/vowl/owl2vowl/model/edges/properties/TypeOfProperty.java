/*
 * TypeOfProperty.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Standard_Iris;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.JsonGeneratorVisitor;

/**
 *
 */
public class TypeOfProperty extends BaseProperty {

	public TypeOfProperty() {
		super();
		setName("type");
		setIri(Standard_Iris.TYPE_URI);
	}

	@Override
	public void accept(JsonGeneratorVisitor visitor) {
		super.accept(visitor);
		visitor.visit(this);
	}
}
