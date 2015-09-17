/*
 * VowlDatatypeProperty.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.properties;

import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.VowlElementVisitor;
import org.semanticweb.owlapi.model.IRI;

/**
 *
 */
public class VowlDatatypeProperty extends AbstractProperty {
	public VowlDatatypeProperty(IRI iri) {
		// TODO
		super(iri, "TODO");
	}

	@Override
	public void accept(VowlElementVisitor visitor) {
		visitor.visit(this);
	}
}