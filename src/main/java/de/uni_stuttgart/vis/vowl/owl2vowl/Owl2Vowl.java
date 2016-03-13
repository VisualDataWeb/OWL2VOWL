/*
 * Owl2Vowl.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl;

import de.uni_stuttgart.vis.vowl.owl2vowl.converter.Converter;
import de.uni_stuttgart.vis.vowl.owl2vowl.converter.IRIConverter;
import de.uni_stuttgart.vis.vowl.owl2vowl.converter.InputStreamConverter;
import de.uni_stuttgart.vis.vowl.owl2vowl.converter.OntologyConverter;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.types.BackupExporter;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.InputStream;
import java.util.Collection;

/**
 * Global class for easy to use of this library to include in other projects.
 */
public class Owl2Vowl {
	protected Converter converter;

	public Owl2Vowl(OWLOntology ontology) {
		converter = new OntologyConverter(ontology);
	}

	public Owl2Vowl(OWLOntology ontology, String ontologyIri) {
		converter = new OntologyConverter(ontology, ontologyIri);
	}

	public Owl2Vowl(IRI ontologyIri) {
		converter = new IRIConverter(ontologyIri);
	}

	public Owl2Vowl(IRI ontologyIri, Collection<IRI> dependencies) {
		converter = new IRIConverter(ontologyIri, dependencies);
	}

	public Owl2Vowl(InputStream ontology) {
		converter = new InputStreamConverter(ontology);
	}

	public Owl2Vowl(InputStream ontology, Collection<InputStream> dependencies) {
		converter = new InputStreamConverter(ontology, dependencies);
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
			throw new IllegalStateException("Problems during export happend. ", e);
		}

		return exporter.getConvertedJson();
	}
}
