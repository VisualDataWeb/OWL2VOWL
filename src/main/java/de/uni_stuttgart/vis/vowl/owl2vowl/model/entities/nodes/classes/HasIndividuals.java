package de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes;

import org.semanticweb.owlapi.model.IRI;

import java.util.Set;

public interface HasIndividuals {

	void addIndividual(IRI iri);

	Set<IRI> getIndividuals();

	void addInstance(IRI iri);

	Set<IRI> getInstances();
}
