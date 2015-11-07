/*
 * VowlVisitable.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.visitor.VowlElementVisitor;

/**
 *
 */
public interface VowlVisitable {

	void accept(VowlElementVisitor visitor);
}
