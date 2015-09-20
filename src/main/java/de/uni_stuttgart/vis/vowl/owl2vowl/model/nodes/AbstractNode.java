package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.AbstractEntity;
import org.semanticweb.owlapi.model.IRI;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractNode extends AbstractEntity implements HasUnions, HasIntersections, HasComplement {

	private IRI complement;
	private Set<IRI> intersectionElements = new HashSet<>();
	private Set<IRI> unionElements = new HashSet<>();
	private Set<IRI> inGoingProperties = new HashSet<>();
	private Set<IRI> outGoingProperties = new HashSet<>();

	protected AbstractNode(IRI iri, String type) {
		super(iri, type);
	}

	public Set<IRI> getInGoingProperties() {
		return inGoingProperties;
	}

	public Set<IRI> getOutGoingProperties() {
		return outGoingProperties;
	}

	@Override
	public IRI getComplement() {
		return complement;
	}

	@Override
	public void setComplement(IRI complement) {
		this.complement = complement;
	}

	@Override
	public void addElementToIntersection(IRI iri) {
		intersectionElements.add(iri);
	}

	@Override
	public Set<IRI> getElementOfIntersection() {
		return Collections.unmodifiableSet(intersectionElements);
	}

	@Override
	public void addElementToUnion(IRI iri) {
		unionElements.add(iri);
	}

	@Override
	public Set<IRI> getElementsOfUnion() {
		return Collections.unmodifiableSet(unionElements);
	}

	public void addInGoingProperty(IRI property) {
		inGoingProperties.add(property);
	}

	public void addOutGoingProperty(IRI property) {
		outGoingProperties.add(property);
	}
}
