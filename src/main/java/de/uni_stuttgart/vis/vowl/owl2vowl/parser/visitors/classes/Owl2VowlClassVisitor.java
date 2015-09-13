/*
 * Owl2VowlClassVisitor.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.visitors.classes;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.BaseEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.BaseEdge;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties.*;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.*;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.*;

/**
 *
 */
public interface Owl2VowlClassVisitor {

	// BASE
	void visit(BaseEntity entity);

	void visit(BaseEdge entity);

	void visit(BaseProperty entity);

	/* PROPERTIES */

	void visit(TypeOfProperty entity);

	void visit(DisjointProperty entity);

	void visit(OwlDatatypeProperty entity);

	void visit(OwlObjectProperty entity);

	void visit(SubClassProperty entity);


	/* NODES */

	// CLASSES
	void visit(BaseNode entity);

	void visit(BaseClass entity);

	void visit(ExternalClass entity);

	void visit(OwlClass entity);

	void visit(OwlComplementOf entity);

	void visit(OwlDeprecatedClass entity);

	void visit(OwlEquivalentClass entity);

	void visit(OwlIntersectionOf entity);

	void visit(OwlThing entity);

	void visit(OwlUnionOf entity);

	void visit(RdfsClass entity);

	void visit(SpecialClass entity);

	void visit(AllValuesFrom entity);

	void visit(ObjectOneOf entity);

	// DATATYPES
	void visit(BaseDatatype entity);

	void visit(RdfsResource entity);

	void visit(RdfsDatatype entity);

	void visit(RdfsLiteral entity);

	void visit(DataComplementOf entity);

	void visit(DataIntersectionOf entity);

	void visit(DataOneOf entity);

	void visit(DataUnionOf entity);
}
