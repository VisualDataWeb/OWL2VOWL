/*
 * JsonGeneratorVisitor.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.export;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.AbstractEntity;

/**
 *
 */
public interface JsonGeneratorVisitor {
	void visit(AbstractEntity entity);
}
