package de.uni_stuttgart.vis.vowl.owl2vowl.model.entities;

import org.semanticweb.owlapi.model.IRI;

public interface HasReference {

	IRI getReferenceIri();

	void setReferenceIri(IRI referenceIri);
}
