/*
 * AbstractProperty.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.SetWithoutNull;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.AbstractEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.visitor.VowlPropertyVisitor;
import org.semanticweb.owlapi.model.IRI;

import java.util.Set;

/**
 *
 */
public abstract class AbstractProperty extends AbstractEntity implements HasInverse, HasCardinality {
	private Set<IRI> domains = new SetWithoutNull<>();
	private Set<IRI> ranges = new SetWithoutNull<>();
	private Set<IRI> referencedIris = new SetWithoutNull<>();

	// Merged entities used only for json generation!
	private IRI mergedDomain;
	private IRI mergedRange;

	public IRI getMergedDomain() {
		return mergedDomain;
	}

	public void setMergedDomain(IRI mergedDomain) {
		this.mergedDomain = mergedDomain;
	}

	public IRI getMergedRange() {
		return mergedRange;
	}

	public void setMergedRange(IRI mergedRange) {
		this.mergedRange = mergedRange;
	}
	private IRI inverse = null;
	private int minCardinality;
	private int maxCardinality;
	private int exactCardinality;

	protected AbstractProperty(IRI iri, String type) {
		super(iri, type);
	}

	public Set<IRI> getDomains() {
		return domains;
	}

	public Set<IRI> getRanges() {
		return ranges;
	}

	public void addDomain(IRI iri) {
		domains.add(iri);
	}

	public void addRange(IRI iri) {
		ranges.add(iri);
	}

	public IRI getJsonDomain() {
		return domains.size() == 1 ? domains.iterator().next() : mergedDomain;
	}

	public IRI getJsonRange() {
		return ranges.size() == 1 ? ranges.iterator().next() : mergedRange;
	}

	@Override
	public IRI getInverse() {
		return inverse;
	}

	@Override
	public void addInverse(IRI iri) {
		this.inverse = iri;
	}

	@Override
	public Integer getExactCardinality() {
		return exactCardinality;
	}

	@Override
	public void setExactCardinality(Integer value) {
		exactCardinality = value;
	}

	@Override
	public Integer getMaxCardinality() {
		return maxCardinality;
	}

	@Override
	public void setMaxCardinality(Integer value) {
		maxCardinality = value;
	}

	@Override
	public Integer getMinCardinality() {
		return minCardinality;
	}

	@Override
	public void setMinCardinality(Integer value) {
		minCardinality = value;
	}

	
	@Override
	public void releaseMemory() {
		if (domains!=null) domains.clear();
		if (ranges!=null) ranges.clear();
		if (referencedIris!=null) referencedIris.clear();
		
		
		domains 		= null;
		ranges			= null;
		referencedIris	= null;
		
	}
	
	public abstract void accept(VowlPropertyVisitor visitor);

	public Set<IRI> getReferencedIris() {
		return referencedIris;
	}

	public void addReferencedProperty(IRI reference) {
		referencedIris.add(reference);
	}
}
