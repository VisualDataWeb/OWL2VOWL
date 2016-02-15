package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.NodeType;
import de.uni_stuttgart.vis.vowl.owl2vowl.constants.VowlAttribute;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.VowlClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.VowlThing;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.datatypes.VowlDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.datatypes.VowlLiteral;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.TypeOfProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.VowlDatatypeProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.VowlObjectProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.individuals.VowlIndividual;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.visitor.VowlElementVisitor;

import java.util.Collection;

public class TypeSetter implements VowlElementVisitor {
	@Override
	public void visit(VowlThing vowlThing) {

	}

	@Override
	public void visit(VowlClass vowlClass) {
		vowlClass.setType(getClassType(vowlClass.getAttributes()));
	}

	@Override
	public void visit(VowlLiteral vowlLiteral) {

	}

	@Override
	public void visit(VowlDatatype vowlDatatype) {

	}

	@Override
	public void visit(VowlObjectProperty vowlObjectProperty) {

	}

	@Override
	public void visit(VowlDatatypeProperty vowlDatatypeProperty) {

	}

	@Override
	public void visit(VowlIndividual vowlIndividual) {

	}

	@Override
	public void visit(TypeOfProperty typeOfProperty) {

	}

	/**
	 * 1. Intersection, Union, Complement, DisjointUnion
	 * 2. Equivalent
	 * 3. rdf
	 */
	protected String getClassType(Collection<VowlAttribute> attributes) {
		if (attributes.contains(VowlAttribute.INTERSECTION)) {
			return NodeType.TYPE_INTERSECTION;
		}

		if (attributes.contains(VowlAttribute.UNION)) {
			return NodeType.TYPE_UNION;
		}

		if (attributes.contains(VowlAttribute.COMPLEMENT)) {
			return NodeType.TYPE_COMPLEMENT;
		}

		if (attributes.contains(VowlAttribute.DISJOINTUNION)) {
			return NodeType.TYPE_DISJOINTUNION;
		}

		if (attributes.contains(VowlAttribute.EQUIVALENT)) {
			return NodeType.TYPE_EQUIVALENT;
		}

		if (attributes.contains(VowlAttribute.RDF)) {
			return NodeType.TYPE_RDFSCLASS;
		}

		return NodeType.TYPE_CLASS;
	}
}
