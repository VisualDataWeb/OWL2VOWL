/*
 * VowlElementVisitor.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.VowlClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.VowlThing;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.datatypes.VowlDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.datatypes.VowlLiteral;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.VowlDatatypeProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.VowlObjectProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.individuals.VowlIndividual;

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

	void visit(VowlIndividual vowlIndividual);
}
