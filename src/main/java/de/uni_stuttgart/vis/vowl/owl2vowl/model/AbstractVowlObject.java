/*
 * AbstractVowlObject.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.annotation.Annotations;
import org.semanticweb.owlapi.model.IRI;

/**
 *
 */
public abstract class AbstractVowlObject implements HasIri, HasAnnotations {
	protected IRI iri;
	private Annotations annotations = new Annotations();

	public AbstractVowlObject(IRI iri) {
		this.iri = iri;
	}

	@Override
	public Annotations getAnnotations() {
		return annotations;
	}

	@Override
	public IRI getIri() {
		return iri;
	}
}
