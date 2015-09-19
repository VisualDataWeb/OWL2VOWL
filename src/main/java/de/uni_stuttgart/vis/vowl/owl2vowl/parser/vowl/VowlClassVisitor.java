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
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
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
