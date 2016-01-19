/*
 * VowlThing.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.NodeType;
import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Standard_Iris;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.annotation.Annotation;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.visitor.VowlElementVisitor;
import org.semanticweb.owlapi.model.IRI;

/**
 *
 */
public class VowlThing extends AbstractClass {
	public static final String THING_IRI = Standard_Iris.OWL_THING_CLASS_URI;
	public static final IRI GENERIC_THING_IRI = IRI.create(THING_IRI);

	protected VowlThing(IRI iri, String type) {
		super(iri, type);
	}

	public VowlThing(IRI iri) {
		super(iri, NodeType.TYPE_THING);
		getAnnotations().addLabel(new Annotation("label", "Thing"));
	}

	@Override
	public void accept(VowlElementVisitor visitor) {
		visitor.visit(this);
	}
}
