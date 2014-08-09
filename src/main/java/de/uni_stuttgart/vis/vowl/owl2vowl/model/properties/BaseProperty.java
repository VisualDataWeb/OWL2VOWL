/*
 * BaseProperty.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.properties;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.BaseEntity;

/**
 * @author Vincent Link, Eduard Marbach
 */
public class BaseProperty extends BaseEntity {
	private String domain = "";
	private String range = "";

	public BaseProperty() {
		setType("rdfs:Property");
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}
}
