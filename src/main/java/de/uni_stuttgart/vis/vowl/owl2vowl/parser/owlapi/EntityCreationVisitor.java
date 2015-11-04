package de.uni_stuttgart.vis.vowl.owl2vowl.parser.owlapi;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.AbstractClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.VowlClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.datatypes.VowlDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.VowlDatatypeProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.VowlObjectProperty;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;

public class EntityCreationVisitor extends OWLObjectVisitorAdapter {

	private VowlData vowlData;

	public EntityCreationVisitor(VowlData vowlData) {
		this.vowlData = vowlData;
	}

	@Override
	public void visit(OWLClass ce) {
		AbstractClass clazz = null;

		if (ce.isOWLThing() || ce.isOWLNothing()) {
			// General class do not create here
			return;
		} else if (!ce.isAnonymous()) {
			clazz = new VowlClass(ce.getIRI());
		} else {
			// TODO Anonymous behaviour undefined
		}

		vowlData.addClass(clazz);
	}

	@Override
	public void visit(OWLLiteral node) {
	}

	@Override
	public void visit(OWLDatatype node) {
		vowlData.addDatatype(new VowlDatatype(node.getIRI()));
	}

	@Override
	public void visit(OWLObjectProperty property) {
		VowlObjectProperty prop = null;

		if(!property.isAnonymous()) {
			prop = new VowlObjectProperty(property.getIRI());
		} else {
			// TODO anonymous behaviour
		}

		vowlData.addObjectProperty(prop);
	}

	@Override
	public void visit(OWLDataProperty property) {
		VowlDatatypeProperty prop = null;

		if(!property.isAnonymous()) {
			prop = new VowlDatatypeProperty(property.getIRI());
		} else {
			// TODO anonymous behaviour
		}

		vowlData.addDatatypeProperty(prop);
	}
}
