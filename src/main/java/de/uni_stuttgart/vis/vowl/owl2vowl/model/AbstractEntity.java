package de.uni_stuttgart.vis.vowl.owl2vowl.model;

import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.VowlElementVisitor;
import org.semanticweb.owlapi.model.IRI;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractEntity implements HasEquivalents, VowlVisitable {

	private IRI iri;
	private String type;
	private Set<IRI> equivalents = new HashSet<>();

	protected AbstractEntity(IRI iri, String type) {
		this.iri = iri;
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public IRI getIri() {
		return iri;
	}

	@Override
	public Set<IRI> getEquivalentElements() {
		return Collections.unmodifiableSet(equivalents);
	}

	@Override
	public void addEquivalentElement(IRI iri) {
		equivalents.add(iri);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		AbstractEntity that = (AbstractEntity) o;

		return !(iri != null ? !iri.equals(that.iri) : that.iri != null);

	}

	@Override
	public int hashCode() {
		return iri != null ? iri.hashCode() : 0;
	}

	@Override
	public String toString() {
		return iri.toString();
	}
}
