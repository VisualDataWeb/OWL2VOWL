/*
 * VowlLiteral.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.datatypes;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.NodeType;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.visitor.VowlElementVisitor;
import org.semanticweb.owlapi.model.IRI;

/**
 *
 */
public class VowlLiteral extends AbstractDatatype {

	public VowlLiteral(IRI iri) {
		super(iri, NodeType.TYPE_LITERAL);
	}

	@Override
	public void accept(VowlElementVisitor visitor) {
		visitor.visit(this);
	}
}
