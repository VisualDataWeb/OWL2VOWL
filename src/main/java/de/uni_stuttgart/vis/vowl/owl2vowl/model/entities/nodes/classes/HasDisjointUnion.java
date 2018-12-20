/*
 * HasDisjointUnion.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes;

import org.semanticweb.owlapi.model.IRI;

import java.util.Set;

/**
 * Interface to enable disjoint union functionality in classes.
 */
public interface HasDisjointUnion {
	Set<IRI> getDisjointUnion();
	void addDisjointUnion(IRI disjointIri);
}
