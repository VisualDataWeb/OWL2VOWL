/*
 * TestObject.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.visitors;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.OwlClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.OntologyInformation;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper.ElementFinder;
import org.semanticweb.owlapi.model.*;

/**
 *
 */
public class TestObject implements OWLClassExpressionVisitor {
	private OWLObject entity;
	private OntologyInformation ontologyInformation;
	private ElementFinder finder;
	private int index;

	public TestObject(OWLObject entity, OntologyInformation ontologyInformation, ElementFinder finder, int index) {
		this.entity = entity;
		this.ontologyInformation = ontologyInformation;
		this.finder = finder;
		this.index = index;
	}

	protected void print(OWLObject entity) {
		String print = "";

		for( int i = 0; i < 2 * index; i++) {
			print += " ";
		}

		print += "Visited: ";
		print += entity;
		System.out.println(print);
	}

	@Override
	public void visit(OWLClass ce) {
		for (OWLAxiom owlAxiom : ce.getReferencingAxioms(ontologyInformation.getOntology(), true)) {
			owlAxiom.accept(new TestEquivalentVisitor(ce, ontologyInformation, finder, index + 1));
		}

		System.out.println(ce);

		try {
			finder.findOrCreateClass(ce.getIRI().toString(), OwlClass.class);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visit(OWLObjectIntersectionOf ce) {

	}

	@Override
	public void visit(OWLObjectUnionOf ce) {

	}

	@Override
	public void visit(OWLObjectComplementOf ce) {

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
