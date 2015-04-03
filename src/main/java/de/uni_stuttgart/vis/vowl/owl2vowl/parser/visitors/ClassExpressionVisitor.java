/*
 * ClassExpressionVisitor.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.visitors;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Node_Types;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.BaseClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.SpecialClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.OntologyInformation;
import org.semanticweb.owlapi.model.*;

/**
 *
 */
public class ClassExpressionVisitor implements OWLClassExpressionVisitor {

	private MapData information;
	private OWLEntity entity;
	private SpecialClass target;

	public ClassExpressionVisitor(MapData information, OWLEntity entity, SpecialClass target) {
		this.information = information;
		this.entity = entity;
		this.target = target;
	}

	@Override
	public void visit(OWLClass ce) {

	}

	@Override
	public void visit(OWLObjectIntersectionOf ce) {

	}

	@Override
	public void visit(OWLObjectUnionOf ce) {

	}

	@Override
	public void visit(OWLObjectComplementOf ce) {
		if (!ce.getOperand().isAnonymous()) {
			OWLClass owlClass = ce.getOperand().asOWLClass();
			String iri = owlClass.getIRI().toString();
			if (!target.getIri().equals(iri)) {
				BaseNode baseNode = information.getMergedMap().get(iri);
				baseNode.getExistingComplements().add(target);
				target.getComplementOf().add(baseNode);
				target.setType(Node_Types.TYPE_COMPLEMENT);
			} else {
				// Visited object and target are the same so do not set complement from the object itself.
				// e.q. Visited object Class1 and the target is Class1. Class1 is not the complement of itself!
			}
		} else {
			// TODO What to do if complement operand is anonymous?
		}

	}

	@Override
	public void visit(OWLObjectSomeValuesFrom ce) {

	}

	@Override
	public void visit(OWLObjectAllValuesFrom ce) {

	}

	@Override
	public void visit(OWLObjectHasValue ce) {

	}

	@Override
	public void visit(OWLObjectMinCardinality ce) {

	}

	@Override
	public void visit(OWLObjectExactCardinality ce) {

	}

	@Override
	public void visit(OWLObjectMaxCardinality ce) {

	}

	@Override
	public void visit(OWLObjectHasSelf ce) {

	}

	@Override
	public void visit(OWLObjectOneOf ce) {

	}

	@Override
	public void visit(OWLDataSomeValuesFrom ce) {

	}

	@Override
	public void visit(OWLDataAllValuesFrom ce) {

	}

	@Override
	public void visit(OWLDataHasValue ce) {

	}

	@Override
	public void visit(OWLDataMinCardinality ce) {

	}

	@Override
	public void visit(OWLDataExactCardinality ce) {

	}

	@Override
	public void visit(OWLDataMaxCardinality ce) {

	}
}
