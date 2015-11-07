/*
 * VowlIndividual.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.individuals;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.AbstractVowlObject;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.visitor.VowlElementVisitor;
import org.semanticweb.owlapi.model.IRI;

/**
 *
 */
public class VowlIndividual extends AbstractVowlObject {
	public VowlIndividual(IRI iri) {
		super(iri);
	}

	@Override
	public void accept(VowlElementVisitor visitor) {
		visitor.visit(this);
	}
}
