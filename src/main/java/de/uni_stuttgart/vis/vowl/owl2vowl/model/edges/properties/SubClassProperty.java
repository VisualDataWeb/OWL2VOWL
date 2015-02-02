/*
 * SubClassProperty.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Property_Types;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.JsonGeneratorVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;

/**
 *
 */
public class SubClassProperty extends OwlObjectProperty {
	public SubClassProperty() {
		super();
		setType(Property_Types.PROP_TYPE_SUBCLASS);
		setName("Subclass Of");
	}

	public SubClassProperty(BaseNode domain, BaseNode range) {
		super();
		setRange(range);
		setDomain(domain);
		setType(Property_Types.PROP_TYPE_SUBCLASS);
		setName("Subclass Of");
	}

	@Override
	public void accept(JsonGeneratorVisitor visitor) {
		super.accept(visitor);
		visitor.visit(this);
	}
}
