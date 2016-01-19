/*
 * VowlElementVisitor.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.visitor;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.individuals.VowlIndividual;

/**
 *
 */
public interface VowlElementVisitor extends VowlClassVisitor, VowlPropertyVisitor {
	void visit(VowlIndividual vowlIndividual);
}
