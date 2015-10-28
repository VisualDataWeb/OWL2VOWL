/*
 * AbstractClas.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.AbstractNode;

import org.semanticweb.owlapi.model.IRI;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public abstract class AbstractClass extends AbstractNode implements HasDisjointUnion, HasIndividuals {
	protected Set<IRI> disjointUnion = new HashSet<>();
	protected Set<IRI> instances = new HashSet<>();
	protected Set<IRI> individuals = new HashSet<>();

	protected AbstractClass(IRI iri, String type) {
		super(iri, type);
	}

	@Override
	public Set getDisjoints() {
		return disjointUnion;
	}

	@Override
	public void addDisjoint(IRI disjointIri) {
		disjointUnion.add(disjointIri);
	}

	@Override
	public void addIndividual(IRI iri) {
		individuals.add(iri);
	}

	@Override
	public Set<IRI> getIndividuals() {
		return Collections.unmodifiableSet(individuals);
	}

	@Override
	public void addInstance(IRI iri) {
		instances.add(iri);
	}

	@Override
	public Set<IRI> getInstances() {
		return Collections.unmodifiableSet(instances);
	}
}
