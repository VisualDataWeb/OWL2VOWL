/*
 * TestVisitor.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.visitors;

import org.semanticweb.owlapi.model.*;

/**
 *
 */
public class TestVisitor implements OWLClassExpressionVisitor {
	private OWLClass currentClass;

	public TestVisitor(OWLClass currentClass) {
		this.currentClass = currentClass;
	}

	@Override
	public void visit(OWLClass ce) {
		System.out.println("--- " + currentClass);
		System.out.println("Class: " + ce);
		System.out.println();
	}

	@Override
	public void visit(OWLObjectIntersectionOf ce) {

	}

	@Override
	public void visit(OWLObjectUnionOf ce) {

	}

	@Override
	public void visit(OWLObjectComplementOf ce) {
		System.out.println("Complement: " + ce);

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
