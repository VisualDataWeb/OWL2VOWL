/*
 * OntologyInformation.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.container;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 *
 */
public class OntologyInformation {
	private OWLOntology ontology;
	private OWLDataFactory factory;
	private OWLOntologyManager manager;
	private IRI loadedIri;

	public OntologyInformation(OWLOntology ontology, OWLDataFactory factory, OWLOntologyManager manager, IRI loadedIri) {
		this.ontology = ontology;
		this.factory = factory;
		this.manager = manager;
		this.loadedIri = loadedIri;
	}

	public OntologyInformation(OWLOntology ontology, IRI loadedIri) {
		this.ontology = ontology;
		this.loadedIri = loadedIri;
		factory = ontology.getOWLOntologyManager().getOWLDataFactory();
		manager = ontology.getOWLOntologyManager();
	}

	public OWLOntology getOntology() {
		return ontology;
	}

	public void setOntology(OWLOntology ontology) {
		this.ontology = ontology;
	}

	public OWLDataFactory getFactory() {
		return factory;
	}

	public void setFactory(OWLDataFactory factory) {
		this.factory = factory;
	}

	public OWLOntologyManager getManager() {
		return manager;
	}

	public void setManager(OWLOntologyManager manager) {
		this.manager = manager;
	}

	public IRI getLoadedIri() {
		return loadedIri;
	}

	public void setLoadedIri(IRI loadedIri) {
		this.loadedIri = loadedIri;
	}
}
