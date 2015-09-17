/*
 * VowlThing.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Node_Types;
import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Standard_Iris;
import org.semanticweb.owlapi.model.IRI;

/**
 *
 */
public class VowlThing extends AbstractNode {
	protected VowlThing(IRI iri, String type) {
		super(iri, type);
	}

	public VowlThing() {
		super(IRI.create(Standard_Iris.OWL_THING_CLASS_URI), Node_Types.TYPE_THING);
	}
}
