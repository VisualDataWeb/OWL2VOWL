/*
 * HasDisjointUnion.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes;

import org.semanticweb.owlapi.model.IRI;

import java.util.Set;

/**
 * Interface for disjoint unions. Not necessary at the moment because realized as union construct.
 */
public interface HasDisjointUnion {
	Set getDisjoints();
	void addDisjoint(IRI disjointIri);
}
