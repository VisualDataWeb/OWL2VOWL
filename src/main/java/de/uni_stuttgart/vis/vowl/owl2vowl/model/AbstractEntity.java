package de.uni_stuttgart.vis.vowl.owl2vowl.model;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.VowlAttribute;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.annotation.Annotations;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlGenerationEnum;
import org.semanticweb.owlapi.model.IRI;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractEntity implements HasEquivalents, HasSubEntities, VowlVisitable {
	private IRI iri;
	private String type;
	private Set<VowlAttribute> attributes = new HashSet<>();
	private Set<IRI> equivalents = new HashSet<>();
	private Annotations annotations = new Annotations();
	private Set<IRI> subEntities = new HashSet<>();
	private Set<IRI> superEntities = new HashSet<>();

	private VowlGenerationEnum generated = VowlGenerationEnum.AUTOMATIC;

	protected AbstractEntity(IRI iri, String type) {
		this.iri = iri;
		this.type = type;
	}

	public VowlGenerationEnum getGenerated() {
		return generated;
	}

	public void setGenerated(VowlGenerationEnum generated) {
		this.generated = generated;
	}

	public Set<VowlAttribute> getAttributes() {
		return Collections.unmodifiableSet(attributes);
	}

	public void addAttribute(VowlAttribute attribute) {
		attributes.add(attribute);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public Annotations getAnnotations() {
		return annotations;
	}

	@Override
	public Set<IRI> getSuperEntities() {
		return Collections.unmodifiableSet(superEntities);
	}

	@Override
	public void addSuperEntity(IRI iri) {
		superEntities.add(iri);
	}

	@Override
	public Set<IRI> getSubEntities() {
		return Collections.unmodifiableSet(subEntities);
	}

	@Override
	public void addSubEntity(IRI iri) {
		subEntities.add(iri);
	}
}
