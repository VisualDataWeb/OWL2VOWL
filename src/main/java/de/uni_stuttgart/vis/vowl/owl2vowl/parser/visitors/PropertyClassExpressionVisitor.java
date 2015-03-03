/*
 * PropertyClassExpressionVisitor.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.visitors;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.SpecialClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import org.semanticweb.owlapi.model.*;

import java.util.List;

/**
 *
 */
public class PropertyClassExpressionVisitor implements OWLClassExpressionVisitor {

	private MapData information;
	private List<String> ranges;

	public PropertyClassExpressionVisitor(MapData information, List<String> ranges) {
		this.information = information;
		this.ranges = ranges;
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
			BaseNode baseNode = information.getMergedMap().get(iri);

			for (BaseNode node : baseNode.getExistingComplements()) {
				ranges.add(node.getIri());
			}
		} else {
			// TODO
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
