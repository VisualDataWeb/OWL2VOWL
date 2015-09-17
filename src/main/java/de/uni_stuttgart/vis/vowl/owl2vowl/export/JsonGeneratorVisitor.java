/*
 * JsonGeneratorVisitor.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.export;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.AbstractEntity;

import java.util.Map;

/**
 *
 */
public interface JsonGeneratorVisitor {
	void visit(AbstractEntity entity);
}
