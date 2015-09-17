/*
 * OwlClassVisitor.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.visitor;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.AbstractClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.VowlClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.VowlThing;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;

/**
 *
 */
public class OwlClassVisitor extends OWLObjectVisitorAdapter {

	private VowlData vowlData;

	public OwlClassVisitor(VowlData vowlData) {
		this.vowlData = vowlData;
	}

	@Override
	public void visit(OWLClass ce) {
		AbstractClass clazz = null;

		if (ce.isOWLThing()) {
			clazz = new VowlThing();
		} else if (!ce.isAnonymous()) {
			clazz = new VowlClass(ce.getIRI());
		} else {
			// TODO Anonymous behaviour undefined
		}

		vowlData.addClass(clazz);
	}
}
