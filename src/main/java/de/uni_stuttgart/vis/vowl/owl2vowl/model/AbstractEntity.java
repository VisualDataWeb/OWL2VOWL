/*
 * Entity.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model;

import de.uni_stuttgart.vis.vowl.owl2vowl.export.JsonGeneratorVisitor;
import org.semanticweb.owlapi.model.IRI;

/**
 *
 */
public abstract class AbstractEntity {

	private IRI iri;
	private String type;

	protected AbstractEntity(IRI iri, String type) {
		this.iri = iri;
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public IRI getIri() {
		return iri;
	}

	public void accept(JsonGeneratorVisitor visitor) {
		visitor.visit(this);
	}
}
