/*
 * IndividualsVisitor.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.owlapi;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.annotation.Annotation;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.individuals.VowlIndividual;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.AnnotationParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;

/**
 *
 */
public class IndividualsVisitor implements OWLIndividualVisitor {

	private final VowlData vowlData;
	private final OWLIndividual owlIndividual;
	private OWLClass baseClass;
	private OWLOntologyManager manager;
	private Logger logger = LogManager.getLogger(IndividualsVisitor.class);

	public IndividualsVisitor(VowlData vowlData, OWLIndividual owlIndividual, OWLClass baseClass, OWLOntologyManager manager) {
		this.vowlData = vowlData;
		this.owlIndividual = owlIndividual;
		this.baseClass = baseClass;
		this.manager = manager;
	}

	@Override
	public void visit(@Nonnull OWLNamedIndividual owlNamedIndividual) {
		if (vowlData.getClassMap().containsKey(owlNamedIndividual.getIRI())) {
			vowlData.getClassForIri(baseClass.getIRI()).addInstance(owlNamedIndividual.getIRI());
		} else {
			vowlData.getClassForIri(baseClass.getIRI()).addIndividual(createIndividual(owlNamedIndividual));
		}
	}

	private IRI createIndividual(OWLNamedIndividual owlNamedIndividual) {
		VowlIndividual individual = vowlData.getGenerator().generateIndividual(owlNamedIndividual.getIRI());
		new AnnotationParser(vowlData, manager).parse(individual);

		return individual.getIri();
	}

	@Override
	public void visit(@Nonnull OWLAnonymousIndividual owlAnonymousIndividual) {
		System.out.println(baseClass);
		System.out.println("\tAnonymous individual");
		logger.info("Anonymous individual: " + owlAnonymousIndividual);
	}
}
