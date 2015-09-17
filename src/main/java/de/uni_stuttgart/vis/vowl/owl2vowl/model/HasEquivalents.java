/*
 * HasEquivalents.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model;

import org.semanticweb.owlapi.model.IRI;

import java.util.Set;

/**
 *
 */
public interface HasEquivalents {
	Set<IRI> getEquivalentsIris();
}
