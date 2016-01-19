package de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.SetWithoutNull;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.AbstractEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.VowlThing;
import org.semanticweb.owlapi.model.IRI;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractNode extends AbstractEntity implements HasUnions, HasIntersections, HasComplement {

	private Set<IRI> complements = new SetWithoutNull<>();
	private Set<IRI> intersectionElements = new SetWithoutNull<>();
	private Set<IRI> unionElements = new SetWithoutNull<>();
	private Set<IRI> inGoingProperties = new SetWithoutNull<>();
	private Set<IRI> outGoingProperties = new SetWithoutNull<>();

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
	public Set<IRI> getComplements() {
		return Collections.unmodifiableSet(complements);
	}

	@Override
	public void addComplement(IRI iri) {
		if (iri.equals(VowlThing.GENERIC_THING_IRI)){
			return;
		}
		complements.add(iri);
	}

	@Override
	public void addElementToIntersection(IRI iri) {
		if (iri.equals(VowlThing.GENERIC_THING_IRI)){
			return;
		}
		intersectionElements.add(iri);
	}

	@Override
	public Set<IRI> getElementOfIntersection() {
		return Collections.unmodifiableSet(intersectionElements);
	}

	@Override
	public void addElementToUnion(IRI iri) {
		if (iri.equals(VowlThing.GENERIC_THING_IRI)){
			return;
		}
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
