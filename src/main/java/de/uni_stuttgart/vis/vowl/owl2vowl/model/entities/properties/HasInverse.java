package de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties;

import org.semanticweb.owlapi.model.IRI;

/**
 * @author Eduard
 */
public interface HasInverse {
	void addInverse(IRI iri);

	// TODO could be multiple inverses?
	IRI getInverse();
}
