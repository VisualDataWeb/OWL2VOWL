/*
 * TestClassEx.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.visitors;

import org.semanticweb.owlapi.model.*;

/**
 *
 */
public class TestClassEx implements OWLClassExpressionVisitorEx {
	private OWLEntity entity;

	public TestClassEx(OWLEntity entity) {
		this.entity = entity;
	}

	@Override
	public Object visit(OWLClass ce) {
		System.out.println(ce);
		return null;
	}

	@Override
	public Object visit(OWLObjectIntersectionOf ce) {
		return null;
	}

	@Override
	public Object visit(OWLObjectUnionOf ce) {
		return null;
	}

	@Override
	public Object visit(OWLObjectComplementOf ce) {
		return null;
	}

	@Override
	public Object visit(OWLObjectSomeValuesFrom ce) {
		return null;
	}

	@Override
	public Object visit(OWLObjectAllValuesFrom ce) {
		return null;
	}

	@Override
	public Object visit(OWLObjectHasValue ce) {
		return null;
	}

	@Override
	public Object visit(OWLObjectMinCardinality ce) {
		return null;
	}

	@Override
	public Object visit(OWLObjectExactCardinality ce) {
		return null;
	}

	@Override
	public Object visit(OWLObjectMaxCardinality ce) {
		return null;
	}

	@Override
	public Object visit(OWLObjectHasSelf ce) {
		return null;
	}

	@Override
	public Object visit(OWLObjectOneOf ce) {
		return null;
	}

	@Override
	public Object visit(OWLDataSomeValuesFrom ce) {
		return null;
	}

	@Override
	public Object visit(OWLDataAllValuesFrom ce) {
		return null;
	}

	@Override
	public Object visit(OWLDataHasValue ce) {
		return null;
	}

	@Override
	public Object visit(OWLDataMinCardinality ce) {
		return null;
	}

	@Override
	public Object visit(OWLDataExactCardinality ce) {
		return null;
	}

	@Override
	public Object visit(OWLDataMaxCardinality ce) {
		return null;
	}
}
