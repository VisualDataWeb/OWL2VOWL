/*
 * Owl2Vowl.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl;

import de.uni_stuttgart.vis.vowl.owl2vowl.export.types.BackupExporter;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Global class for easy to use of this library to include in other projects.
 */
public class Owl2Vowl {
	protected Converter converter;

	/**
	 * Without function yet
	 */
	public Owl2Vowl(OWLOntology ontology) {
		// TODO not implemented yet
	}

	/**
	 * Initialize the converter.
	 *
	 * @param ontology    The desired ontology to convert.
	 * @param ontologyIri The iri of the ontology. Should not be null!
	 */
	public Owl2Vowl(OWLOntology ontology, String ontologyIri) {
		converter = null;
	}

	/**
	 * Converts the ontology to the webvowl compatible format and returns the usable json as string.
	 *
	 * @return The webvowl compatible json format.
	 */
	public String getJsonAsString() {
		converter.convert();
		BackupExporter exporter = new BackupExporter();

		try {
			converter.export(exporter);
		} catch (Exception e) {
			throw new IllegalStateException("Problems during export happend. " + e);
		}

		return exporter.getConvertedJson();
	}
}
