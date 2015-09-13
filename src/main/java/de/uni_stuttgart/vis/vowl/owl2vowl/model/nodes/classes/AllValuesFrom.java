/*
 * AllValuesFrom.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.BaseEntity;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class AllValuesFrom extends BaseClass {

	private Set<BaseEntity> valuesFrom;
	private Set<String> valuesFromIris;

	public AllValuesFrom() {
		super();
		valuesFrom = new HashSet<BaseEntity>();
		valuesFromIris = new HashSet<String>();
	}

	public Set<BaseEntity> getValuesFrom() {
		return valuesFrom;
	}

	public Set<String> getValuesFromIris() {
		return valuesFromIris;
	}
}
