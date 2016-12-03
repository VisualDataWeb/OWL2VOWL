package de.uni_stuttgart.vis.vowl.owl2vowl.converter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 *
 */
public class OntologyConverter extends AbstractConverter {
	private static final Logger logger = LogManager.getLogger(OntologyConverter.class);

	public OntologyConverter(OWLOntology ontology) {
		this.ontology = ontology;
	}

	public OntologyConverter(OWLOntology ontology, String ontologyIRI) {
		this(ontology);
		loadedOntologyPath = ontologyIRI;
	}

	@Override
	protected void loadOntology() throws OWLOntologyCreationException {
		logger.info("Converting Ontolgy...");
		logger.info("Loading ontology ... [" + ontology + "]");

		manager = ontology.getOWLOntologyManager();
		loadedOntologyPath = "Direct ontology";

		String logOntoName;
		if (!ontology.isAnonymous()) {
			logOntoName = ontology.getOntologyID().getOntologyIRI().get().toString();
		} else {
			logOntoName = "Anonymous";
			logger.info("Ontology IRI is anonymous.");
		}
		logger.info("Ontologies loaded! Main Ontology: " + logOntoName);
	}
}
