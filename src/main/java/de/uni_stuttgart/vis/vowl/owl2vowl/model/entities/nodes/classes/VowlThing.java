/*
 * VowlThing.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.NodeType;
import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Standard_Iris;

import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.VowlElementVisitor;
import org.semanticweb.owlapi.model.IRI;

/**
 *
 */
public class VowlThing extends AbstractClass {
	public static final String THING_IRI = Standard_Iris.OWL_THING_CLASS_URI;

	protected VowlThing(IRI iri, String type) {
		super(iri, type);
	}

	public VowlThing(IRI iri) {
		super(iri, NodeType.TYPE_THING);
	}

	@Override
	public void accept(VowlElementVisitor visitor) {
		visitor.visit(this);
	}
}
