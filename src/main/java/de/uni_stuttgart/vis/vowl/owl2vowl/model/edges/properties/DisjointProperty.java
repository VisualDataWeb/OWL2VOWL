/*
 * DisjointProperty.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.Constants;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;

/**
 *
 */
public class DisjointProperty extends OwlObjectProperty {
	/**
	 * contacts[0] = domainID, contacts[1] = rangeID
	 */
	private String[] contacts = new String[2];

	public DisjointProperty() {
		super();
		setType(Constants.PROP_TYPE_DISJOINT);
		setName("Subclass Of");
	}

	public DisjointProperty(BaseNode domain, BaseNode range) {
		super();
		setRange(range);
		setDomain(domain);
		contacts[0] = domain.getId();
		contacts[1] = range.getId();
		setType(Constants.PROP_TYPE_DISJOINT);
		setName("Disjoint With");
	}

	@Override
	public void setDomain(BaseNode domain) {
		super.setDomain(domain);
		contacts[0] = domain.getId();
	}

	@Override
	public void setRange(BaseNode range) {
		super.setRange(range);
		contacts[1] = range.getId();
	}

	public boolean equivalentDisjoints(DisjointProperty property) {
		return equivalentDisjoints(property.getDomain(), property.getRange());
	}

	public boolean equivalentDisjoints(BaseNode domain, BaseNode range) {
		boolean domainRange = domain.getId().equals(contacts[0]) && range.getId().equals(contacts[1]);
		boolean rangeDomain = range.getId().equals(contacts[0]) && domain.getId().equals(contacts[1]);

		return domainRange || rangeDomain;

	}
}
