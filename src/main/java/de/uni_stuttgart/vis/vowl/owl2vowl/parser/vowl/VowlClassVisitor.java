/*
 * VowlClassVisitor.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.VowlAttribute;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.AbstractNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;

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
		System.out.println("Not implemented VowlClassVisitor: " + c);
		logger.info("Not implemented for equivalents: " + c);
	}

	@Override
	public void visit(OWLObjectUnionOf ce) {
		System.out.println("Not implemented OWLObjectUnionOf: " + ce);
		logger.info("Not implemented for OWLObjectUnionOf: " + ce);
	}

	@Override
	public void visit(OWLObjectComplementOf ce) {
		if (ce.getOperand().isAnonymous()) {
			logger.info("Anonymous operand in object complement of.");
			return;
		}

		IRI baseClassIri = ce.getOperand().asOWLClass().getIRI();
		IRI complementIri = referencedClass.getIRI();

		vowlData.getClassForIri(baseClassIri).setComplement(complementIri);
		vowlData.getClassForIri(complementIri).setComplement(baseClassIri);
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
}
