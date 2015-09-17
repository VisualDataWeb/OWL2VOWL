/*
 * VowlElementVisitor.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.VowlClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.VowlThing;

/**
 *
 */
public interface VowlElementVisitor {
	void visit(VowlThing vowlThing);

	void visit(VowlClass vowlClass);
}
