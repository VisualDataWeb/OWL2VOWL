package de.uni_stuttgart.vis.vowl.owl2vowl.model.entities;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.VowlAttribute;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.AbstractVowlObject;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.SetWithoutNull;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlGenerationEnum;
import org.semanticweb.owlapi.model.IRI;

import java.util.*;

public abstract class AbstractEntity extends AbstractVowlObject implements HasEquivalents, HasSubEntities {
	private String type;
	private Set<VowlAttribute> attributes = new HashSet<>();
	private Set<IRI> equivalents = new SetWithoutNull<>();
	private List<IRI> sortedEquivalents = new ArrayList<>();
	private Set<IRI> subEntities = new SetWithoutNull<>();
	private Set<IRI> superEntities = new SetWithoutNull<>();
	private VowlGenerationEnum generated = VowlGenerationEnum.AUTOMATIC;
	private boolean exportToJson = true;

	protected AbstractEntity(IRI iri, String type) {
		super(iri);
		this.type = type;
	}

	public List<IRI> getSortedEquivalents() {
		return Collections.unmodifiableList(sortedEquivalents);
	}

	public void setSortedEquivalents(List<IRI> sortedEquivalents) {
		this.sortedEquivalents = sortedEquivalents;
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

	public boolean isExportToJson() {
		return exportToJson;
	}

	public void setExportToJson(boolean exportToJson) {
		this.exportToJson = exportToJson;
	}
	
	// Memory Handler required? 
	
	public void releaseMemory() {
		attributes.clear();
		equivalents.clear();
		sortedEquivalents.clear();
		subEntities.clear();
		superEntities.clear();
		
		attributes        = null;
		equivalents       = null;
		sortedEquivalents = null;
		subEntities       = null;
		superEntities     = null;

	}
	
}
