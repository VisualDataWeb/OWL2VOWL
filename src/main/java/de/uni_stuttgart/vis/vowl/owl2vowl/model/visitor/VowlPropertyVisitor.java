/*
 * VowlPropertyVisitor.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.visitor;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.TypeOfProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.VowlDatatypeProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.VowlObjectProperty;

/**
 *
 */
public interface VowlPropertyVisitor {
	void visit(VowlObjectProperty vowlObjectProperty);

	void visit(VowlDatatypeProperty vowlDatatypeProperty);

	void visit(TypeOfProperty typeOfProperty);
}
