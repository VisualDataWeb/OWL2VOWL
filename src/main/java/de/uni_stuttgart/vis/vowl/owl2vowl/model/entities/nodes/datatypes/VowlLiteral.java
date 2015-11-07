/*
 * VowlLiteral.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.datatypes;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.NodeType;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.annotation.Annotation;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.visitor.VowlElementVisitor;
import org.semanticweb.owlapi.model.IRI;

/**
 *
 */
public class VowlLiteral extends AbstractDatatype {
	protected static final String genericIri = "http://www.w3.org/2000/01/rdf-schema#Literal";

	public VowlLiteral(IRI iri) {
		super(iri, NodeType.TYPE_LITERAL);
		getAnnotations().addLabel(new Annotation("label", "Literal"));
	}

	public String getGenericIri() {
		return genericIri;
	}

	@Override
	public void accept(VowlElementVisitor visitor) {
		visitor.visit(this);
	}
}
