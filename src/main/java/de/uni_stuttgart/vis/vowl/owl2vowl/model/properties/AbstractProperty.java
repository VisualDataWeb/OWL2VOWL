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
public abstract class AbstractProperty extends AbstractEntity implements HasInverse, HasCardinality {
	private IRI domain = null;
	private IRI range = null;
	private IRI inverse = null;
	private int minCardinality;
	private int maxCardinality;
	private int exactCardinality;

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

	@Override
	public IRI getInverse() {
		return inverse;
	}

	@Override
	public void addInverse(IRI iri) {
		this.inverse = iri;
	}

	@Override
	public void setExactCardinality(Integer value) {
		exactCardinality = value;
	}

	@Override
	public void setMaxCardinality(Integer value) {
		maxCardinality = value;
	}

	@Override
	public void setMinCardinality(Integer value) {
		minCardinality = value;
	}

	@Override
	public Integer getExactCardinality() {
		return exactCardinality;
	}

	@Override
	public Integer getMaxCardinality() {
		return maxCardinality;
	}

	@Override
	public Integer getMinCardinality() {
		return minCardinality;
	}
}
