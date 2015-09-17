/*
 * Entity.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model;

import com.google.common.collect.ImmutableSet;
import com.sun.javafx.collections.UnmodifiableListSet;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.JsonGeneratorVisitor;
import org.semanticweb.owlapi.model.IRI;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public abstract class AbstractEntity implements HasEquivalents {

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

	public void accept(JsonGeneratorVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Set<IRI> getEquivalentsIris() {
		return Collections.unmodifiableSet(equivalents);
	}

	public void addEquivalentIri(IRI iri) {
		equivalents.add(iri);
	}
}
