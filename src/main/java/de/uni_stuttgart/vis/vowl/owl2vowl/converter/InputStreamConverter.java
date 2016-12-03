package de.uni_stuttgart.vis.vowl.owl2vowl.converter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import org.semanticweb.owlapi.apibinding.OWLManager;

/**
 *
 */
public class InputStreamConverter extends AbstractConverter {
	protected InputStream mainOntology;
	protected Collection<InputStream> depdencyOntologies;
	private static final Logger logger = LogManager.getLogger(InputStreamConverter.class);

	public InputStreamConverter(InputStream ontology) {
		this(ontology, Collections.<InputStream>emptyList());
	}

	public InputStreamConverter(InputStream ontology, Collection<InputStream> necessaryExternals) {
		mainOntology = ontology;
		depdencyOntologies = Collections.unmodifiableCollection(necessaryExternals);
	}

	@Override
	protected void loadOntology() throws OWLOntologyCreationException {
		logger.info("Converting input streams...");
		
		manager = OWLManager.createOWLOntologyManager();

		for (InputStream depdencyOntology : depdencyOntologies) {
			manager.loadOntologyFromOntologyDocument(depdencyOntology);
		}

		ontology = manager.loadOntologyFromOntologyDocument(mainOntology);
		loadedOntologyPath = "file upload";

		String logOntoName;
		if (!ontology.isAnonymous()) {
			logOntoName = ontology.getOntologyID().getOntologyIRI().get().toString();
		} else {
			logOntoName = "Anonymous";
			logger.info("Ontology IRI is anonymous. Use loaded URI/IRI instead.");
		}
		logger.info("Ontologies loaded! Main Ontology: " + logOntoName);
	}
}
