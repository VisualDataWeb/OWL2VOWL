/*
 * BaseVisitor.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.visitors;

import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.OntologyInformation;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper.ElementFinder;
import org.semanticweb.owlapi.model.OWLObject;

/**
 *
 */
public abstract class AbstractVowlVisitor {

	protected OWLObject topLevelElement;
	protected OWLObject upperStageCaller;
	protected OntologyInformation ontologyInformation;
	protected ElementFinder finder;

	public AbstractVowlVisitor(OWLObject topLevelElement, OWLObject upperStageCaller, OntologyInformation ontologyInformation, ElementFinder finder) {
		this.topLevelElement = topLevelElement;
		this.upperStageCaller = upperStageCaller;
		this.ontologyInformation = ontologyInformation;
		this.finder = finder;
	}
}
