package de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes;

import org.semanticweb.owlapi.model.IRI;

public interface HasComplement {
	IRI getComplement();

	void setComplement(IRI complement);
}
