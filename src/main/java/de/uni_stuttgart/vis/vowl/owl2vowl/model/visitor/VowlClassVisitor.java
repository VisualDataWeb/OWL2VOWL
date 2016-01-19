/*
 * VowlClassVisitor.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.visitor;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.VowlClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.VowlThing;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.datatypes.VowlDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.datatypes.VowlLiteral;

/**
 *
 */
public interface VowlClassVisitor {
	void visit(VowlThing vowlThing);

	void visit(VowlClass vowlClass);

	void visit(VowlLiteral vowlLiteral);

	void visit(VowlDatatype vowlDatatype);
}
