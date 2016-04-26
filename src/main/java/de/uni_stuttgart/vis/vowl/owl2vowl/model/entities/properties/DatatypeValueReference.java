package de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.PropertyAllSomeValue;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.HasReference;
import org.semanticweb.owlapi.model.IRI;

public class DatatypeValueReference extends VowlDatatypeProperty implements HasReference {
	private PropertyAllSomeValue value;
	private IRI referenceIri;

	public DatatypeValueReference(IRI iri, IRI referencedIri) {
		super(iri);
		this.referenceIri = referencedIri;
	}

	public PropertyAllSomeValue getValue() {
		return value;
	}

	public void setValue(PropertyAllSomeValue value) {
		this.value = value;
	}

	public IRI getReferenceIri() {
		return referenceIri;
	}

	public void setReferenceIri(IRI referenceIri) {
		this.referenceIri = referenceIri;
	}
}
