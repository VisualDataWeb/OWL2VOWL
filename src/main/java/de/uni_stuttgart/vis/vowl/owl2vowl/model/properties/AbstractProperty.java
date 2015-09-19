/*
 * AbstractProperty.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.properties;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.AbstractEntity;
import org.semanticweb.owlapi.model.IRI;

import java.util.*;

/**
 *
 */
public abstract class AbstractProperty extends AbstractEntity {
	private Set<IRI> domains = new HashSet<>();
	private Set<IRI> ranges = new HashSet<>();

	protected AbstractProperty(IRI iri, String type) {
		super(iri, type);
	}

	public Set<IRI> getDomains() {
		return Collections.unmodifiableSet(domains);
	}

	public Set<IRI> getRanges() {
		return Collections.unmodifiableSet(ranges);
	}

	public void addDomain(IRI iri) {
		domains.add(iri);
	}

	public void addRange(IRI iri) {
		ranges.add(iri);
	}
}
