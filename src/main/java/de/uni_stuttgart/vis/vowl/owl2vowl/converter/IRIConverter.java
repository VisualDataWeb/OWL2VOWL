package de.uni_stuttgart.vis.vowl.owl2vowl.converter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.util.Collection;
import java.util.Collections;
import org.semanticweb.owlapi.apibinding.OWLManager;

public class IRIConverter extends AbstractConverter {
	protected IRI mainOntology;
	protected Collection<IRI> depdencyOntologies;
	private static final Logger logger = LogManager.getLogger(IRIConverter.class);

	public IRIConverter(IRI ontologyIRI) {
		this(ontologyIRI, Collections.<IRI>emptyList());
	}

	public IRIConverter(IRI ontologyIRI, Collection<IRI> necessaryExternals) {
		mainOntology = ontologyIRI;
		depdencyOntologies = Collections.unmodifiableCollection(necessaryExternals);
	}

	@Override
	protected void loadOntology() throws OWLOntologyCreationException {
		logger.info("Converting IRIs...");
		logger.info("Loading ontologies ... [" + mainOntology + ",  " + depdencyOntologies + "]");
		
		manager = OWLManager.createOWLOntologyManager();

		if (!depdencyOntologies.isEmpty()) {
			for (IRI externalIRI : depdencyOntologies) {
				manager.loadOntology(externalIRI);
			}
			logger.info("External ontologies loaded!");
		}

		ontology = manager.loadOntology(mainOntology);
		loadedOntologyPath = mainOntology.toString();

		String logOntoName;
		if (!ontology.isAnonymous()) {
			logOntoName = ontology.getOntologyID().getOntologyIRI().get().toString();
		} else {
			logOntoName = mainOntology.toString();
			logger.info("Ontology IRI is anonymous. Use loaded URI/IRI instead.");
		}
		logger.info("Ontologies loaded! Main Ontology: " + logOntoName);
	}
}
