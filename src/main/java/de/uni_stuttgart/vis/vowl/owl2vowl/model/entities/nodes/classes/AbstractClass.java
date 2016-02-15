package de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.SetWithoutNull;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.AbstractNode;
import org.semanticweb.owlapi.model.IRI;

import java.util.Collections;
import java.util.Set;

public abstract class AbstractClass extends AbstractNode implements HasDisjointUnion, HasIndividuals {

	protected Set<IRI> disjointUnion = new SetWithoutNull<>();
	protected Set<IRI> instances = new SetWithoutNull<>();
	protected Set<IRI> individuals = new SetWithoutNull<>();

	protected AbstractClass(IRI iri, String type) {
		super(iri, type);
	}


	@Override
	public Set<IRI> getDisjointUnion() {
		return disjointUnion;
	}

	@Override
	public void addDisjointUnion(IRI disjointIri) {
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
