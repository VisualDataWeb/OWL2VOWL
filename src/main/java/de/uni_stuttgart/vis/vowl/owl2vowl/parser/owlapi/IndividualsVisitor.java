/*
 * IndividualsVisitor.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.owlapi;

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
	protected IRI baseIRI;
	private OWLOntologyManager manager;
	private Logger logger = LogManager.getLogger(IndividualsVisitor.class);

	public IndividualsVisitor(VowlData vowlData, OWLIndividual owlIndividual, OWLClass baseClass, OWLOntologyManager manager) {
		this.vowlData = vowlData;
		this.manager = manager;
		this.baseIRI = baseClass.getIRI();
	}

	public IndividualsVisitor(VowlData vowlData, OWLOntologyManager manager, IRI baseIRI) {
		this.vowlData = vowlData;
		this.manager = manager;
		this.baseIRI = baseIRI;
	}

	@Override
	public void visit(@Nonnull OWLNamedIndividual owlNamedIndividual) {
		if (vowlData.getClassMap().containsKey(owlNamedIndividual.getIRI())) {
			vowlData.getClassForIri(baseIRI).addInstance(owlNamedIndividual.getIRI());
			vowlData.getGenerator().generateTypeOf(baseIRI, owlNamedIndividual.getIRI());
		} else {
			vowlData.getClassForIri(baseIRI).addIndividual(createIndividual(owlNamedIndividual));
		}
	}

	private IRI createIndividual(OWLNamedIndividual owlNamedIndividual) {
		VowlIndividual individual = vowlData.getGenerator().generateIndividual(owlNamedIndividual.getIRI());
		new AnnotationParser(vowlData, manager).parse(individual);

		return individual.getIri();
	}

	@Override
	public void visit(@Nonnull OWLAnonymousIndividual owlAnonymousIndividual) {
		logger.info("Anonymous individual: " + owlAnonymousIndividual);
	}
}
