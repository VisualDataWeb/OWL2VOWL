/*
 * VowlClassVisitor.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.classes;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.VowlAttribute;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.AbstractNode;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.AbstractClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 *
 */
public class VowlClassVisitor extends OWLClassExpressionVisitorAdapter {
	private final VowlData vowlData;
	private final OWLClass referencedClass;
	Logger logger = LogManager.getLogger(VowlClassVisitor.class);

	public VowlClassVisitor(VowlData vowlData, OWLClass referencedClass) {
		this.vowlData = vowlData;
		this.referencedClass = referencedClass;
	}

	@Override
	protected void handleDefault(OWLClassExpression c) {
		logger.info("Not implemented for equivalents: " + c);
	}

	@Override
	public void visit(OWLObjectUnionOf ce) {
		Set<OWLClassExpression> operands = ce.getOperands();
		AbstractNode node = vowlData.getClassForIri(referencedClass.getIRI());

		for (OWLClassExpression operand : operands) {
			if (!operand.isAnonymous()) {
				node.addElementToUnion(operand.asOWLClass().getIRI());
				node.addAttribute(VowlAttribute.UNION);
			} else {
				// TODO Anonymous undefined behaviour
				logger.info("Anonymous exists in unions.");
			}
		}
	}

	@Override
	public void visit(OWLObjectComplementOf ce) {
		if (ce.getOperand().isAnonymous()) {
			logger.info("Anonymous operand in object complement of.");
			return;
		}

		IRI baseClassIri = ce.getOperand().asOWLClass().getIRI();
		IRI complementIri = referencedClass.getIRI();

		// TODO where to set the complement?
		//vowlData.getClassForIri(baseClassIri).addComplement(complementIri);
		vowlData.getClassForIri(complementIri).addComplement(baseClassIri);
		vowlData.getClassForIri(complementIri).addAttribute(VowlAttribute.COMPLEMENT);
	}

	@Override
	public void visit(OWLObjectIntersectionOf ce) {
		Set<OWLClassExpression> operands = ce.getOperands();
		AbstractNode node = vowlData.getClassForIri(referencedClass.getIRI());

		for (OWLClassExpression operand : operands) {
			if (!operand.isAnonymous()) {
				node.addElementToIntersection(operand.asOWLClass().getIRI());
				node.addAttribute(VowlAttribute.INTERSECTION);
			} else {
				// TODO Anonymous undefined behaviour
				logger.info("Anonymous exists in intersections.");
			}
		}
	}

	@Override
	public void visit(OWLObjectOneOf ce) {
		AbstractClass oneOfClass = vowlData.getClassForIri(referencedClass.getIRI());
		ce.getIndividuals().forEach(individual -> individual.accept(new OWLIndividualVisitor() {
			@Override
			public void visit(@Nonnull OWLNamedIndividual owlNamedIndividual) {
				oneOfClass.addIndividual(owlNamedIndividual.getIRI());
			}

			@Override
			public void visit(@Nonnull OWLAnonymousIndividual owlAnonymousIndividual) {
				// TODO anonymous behaviour of individuals
			}
		}));
	}
}
