/*
 * VowlData.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.AbstractClass;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class VowlData {
	private Set<AbstractClass> classes = new HashSet<>();

	public void addClass(AbstractClass vowlClass) {
		classes.add(vowlClass);
	}
}
