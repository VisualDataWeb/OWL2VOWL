/*
 * SetWithoutNull.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model;

import java.util.HashSet;

/**
 *
 */
@SuppressWarnings("serial")
public class SetWithoutNull<E> extends HashSet<E> {
	@Override
	public boolean add(E e) {
		return e != null && super.add(e);

	}
}
