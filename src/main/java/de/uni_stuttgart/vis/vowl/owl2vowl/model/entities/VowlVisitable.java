/*
 * VowlVisitable.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.entities;

import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.VowlElementVisitor;

/**
 *
 */
public interface VowlVisitable {

	void accept(VowlElementVisitor visitor);
}
