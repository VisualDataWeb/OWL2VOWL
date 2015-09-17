/*
 * AbstractProperty.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.properties;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.AbstractEntity;
import org.semanticweb.owlapi.model.IRI;

/**
 *
 */
public abstract class AbstractProperty extends AbstractEntity {
	protected AbstractProperty(IRI iri, String type) {
		super(iri, type);
	}
}
