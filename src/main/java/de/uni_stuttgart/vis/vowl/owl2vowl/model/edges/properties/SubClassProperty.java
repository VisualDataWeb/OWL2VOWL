/*
 * SubClassProperty.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.Constants;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;

/**
 *
 */
public class SubClassProperty extends OwlObjectProperty {
	public SubClassProperty() {
		super();
		setType(Constants.PROP_TYPE_SUBCLASS);
		setName("Subclass Of");
	}

	public SubClassProperty(BaseNode domain, BaseNode range) {
		super();
		setRange(range);
		setDomain(domain);
		setType(Constants.PROP_TYPE_SUBCLASS);
		setName("Subclass Of");
	}
}
