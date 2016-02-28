/*
 * Converter.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.converter;

import de.uni_stuttgart.vis.vowl.owl2vowl.export.types.Exporter;

/**
 *
 */
public interface Converter {

	void convert();

	void export(Exporter exporter) throws Exception;
}
