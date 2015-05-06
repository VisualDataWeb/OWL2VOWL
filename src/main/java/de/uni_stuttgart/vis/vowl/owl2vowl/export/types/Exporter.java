/*
 * OntologyWriter.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.export.types;

/**
 * Has a method for writing the string into a specific destination
 */
public interface Exporter {

	/**
	 * Writes the passed string into a specific target or returns a specific object.
	 *
	 * @param text The generated text.
	 */
	Object write(String text) throws Exception;
}
