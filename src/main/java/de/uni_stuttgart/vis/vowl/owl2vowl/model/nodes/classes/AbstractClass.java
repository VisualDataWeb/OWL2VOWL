/*
 * AbstractClas.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.AbstractNode;

import org.semanticweb.owlapi.model.IRI;

/**
 *
 */
public abstract class AbstractClass extends AbstractNode {
	protected AbstractClass(IRI iri, String type) {
		super(iri, type);
	}
}
