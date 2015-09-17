package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.AbstractClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.VowlClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.VowlThing;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;

public class OwlEquivalentsVisitor extends OWLObjectVisitorAdapter {

	private VowlData vowlData;

	public OwlEquivalentsVisitor(VowlData vowlData) {
		this.vowlData = vowlData;
	}

	@Override
	public void visit(OWLEquivalentClassesAxiom axiom) {
		// TODO implement
	}
}
