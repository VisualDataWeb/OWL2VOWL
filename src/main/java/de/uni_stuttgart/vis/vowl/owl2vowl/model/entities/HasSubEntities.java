package de.uni_stuttgart.vis.vowl.owl2vowl.model.entities;

import org.semanticweb.owlapi.model.IRI;

import java.util.Set;

/**
 * @author Eduard
 */
public interface HasSubEntities {
	void addSubEntity(IRI iri);

	Set<IRI> getSubEntities();

	void addSuperEntity(IRI iri);

	Set<IRI> getSuperEntities();
}
