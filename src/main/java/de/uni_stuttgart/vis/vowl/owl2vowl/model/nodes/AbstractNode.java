/*
 * AbstractNode.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.AbstractEntity;
import org.semanticweb.owlapi.model.IRI;

/**
 *
 */
public abstract class AbstractNode extends AbstractEntity {
	protected AbstractNode(IRI iri, String type) {
		super(iri, type);
	}
}
