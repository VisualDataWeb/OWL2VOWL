/*
 * LiteralReference.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.datatypes;

import org.semanticweb.owlapi.model.IRI;

/**
 *
 */
public class LiteralReference extends VowlLiteral {
	protected IRI referencedIri;

	public LiteralReference(IRI iri, IRI referencedIri) {
		super(iri);
		this.referencedIri = referencedIri;
	}

	public IRI getReferencedIri() {
		return referencedIri;
	}
}
