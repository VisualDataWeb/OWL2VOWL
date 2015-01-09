/*
 * GeneralNodeParser.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.OntologyInformation;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 *
 */
public abstract class GeneralNodeParser extends GeneralParser {
	public GeneralNodeParser(OntologyInformation ontologyInformation, MapData mapData) {
		super(ontologyInformation, mapData);
	}
}
