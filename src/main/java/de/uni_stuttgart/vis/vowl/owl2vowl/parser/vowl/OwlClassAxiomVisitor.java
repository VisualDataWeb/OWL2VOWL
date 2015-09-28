package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;

import java.util.Set;

public class OwlClassAxiomVisitor extends OWLObjectVisitorAdapter {

	private VowlData vowlData;
	private OWLClass owlClass;

	public OwlClassAxiomVisitor(VowlData vowlData, OWLClass owlClass) {
		this.vowlData = vowlData;
		this.owlClass = owlClass;
	}

	@Override
	protected void handleDefault(OWLObject axiom) {
		System.out.println(owlClass);
		System.out.println("\t" + axiom);
	}

	@Override
	public void visit(OWLEquivalentClassesAxiom axiom) {
		// TODO NamedClasses size != 1 means either
		if (axiom.getNamedClasses().size() != 1) {
			createEquivalentClass(axiom);
		}

		OWLClass referencedClass = axiom.getNamedClasses().iterator().next();

		Set<OWLClassExpression> expressionsWithoutRefClass = axiom.getClassExpressionsMinus(referencedClass);
		for (OWLClassExpression anonymExpressions : expressionsWithoutRefClass) {
			anonymExpressions.accept(new VowlClassVisitor(vowlData, referencedClass));
		}
	}

	protected void createEquivalentClass(OWLEquivalentClassesAxiom axiom) {

	}

	@Override
	public void visit(OWLSubClassOfAxiom axiom) {

	}
}
