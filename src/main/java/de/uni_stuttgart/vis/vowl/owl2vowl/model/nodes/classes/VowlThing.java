/*
 * VowlThing.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.NodeType;
import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Standard_Iris;

import org.semanticweb.owlapi.model.IRI;

/**
 *
 */
public class VowlThing extends AbstractClass {
	protected VowlThing(IRI iri, String type) {
		super(iri, type);
	}

	public VowlThing() {
		super(IRI.create(Standard_Iris.OWL_THING_CLASS_URI), NodeType.TYPE_THING);
	}
}
