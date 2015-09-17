/*
 * VowlElementVisitor.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.VowlClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.VowlThing;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.VowlDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.VowlLiteral;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.properties.VowlDatatypeProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.properties.VowlObjectProperty;

/**
 *
 */
public interface VowlElementVisitor {
	void visit(VowlThing vowlThing);

	void visit(VowlClass vowlClass);

	void visit(VowlLiteral vowlLiteral);

	void visit(VowlDatatype vowlDatatype);

	void visit(VowlObjectProperty vowlObjectProperty);

	void visit(VowlDatatypeProperty vowlDatatypeProperty);
}
