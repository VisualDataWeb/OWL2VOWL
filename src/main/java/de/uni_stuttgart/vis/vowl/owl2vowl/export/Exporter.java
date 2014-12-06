/*
 * OntologyWriter.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.export;

/**
 * Has a method for writing the string into a specific destination
 */
public interface Exporter {

	/**
	 * Writes the passed string into a specific target.
	 *
	 * @param text
	 */
	public void write(String text) throws Exception;
}
