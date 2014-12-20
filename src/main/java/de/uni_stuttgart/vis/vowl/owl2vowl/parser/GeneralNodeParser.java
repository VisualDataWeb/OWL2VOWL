/*
 * GeneralNodeParser.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 *
 */
public abstract class GeneralNodeParser extends GeneralParser {
	public GeneralNodeParser(OWLOntology ontology, OWLDataFactory factory, MapData mapData, OWLOntologyManager ontologyManager) {
		super(ontology, factory, mapData, ontologyManager);
	}
}
