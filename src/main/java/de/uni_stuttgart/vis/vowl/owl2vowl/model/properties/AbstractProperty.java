/*
 * AbstractProperty.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.properties;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.AbstractEntity;
import org.semanticweb.owlapi.model.IRI;

import java.util.*;

/**
 *
 */
public abstract class AbstractProperty extends AbstractEntity {
	private IRI domain = null;
	private IRI range = null;

	protected AbstractProperty(IRI iri, String type) {
		super(iri, type);
	}

	public IRI getDomain() {
		return domain;
	}

	public IRI getRange() {
		return range;
	}

	public void setDomain(IRI iri) {
		domain = iri;
	}

	public void setRange(IRI iri) {
		range = iri;
	}
}
