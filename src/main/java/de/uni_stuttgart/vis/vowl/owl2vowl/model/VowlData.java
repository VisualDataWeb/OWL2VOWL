/*
 * VowlData.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.AbstractNode;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.AbstractClass;
import org.semanticweb.owlapi.model.IRI;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains all data WebVOWL needs.
 */
public class VowlData {

	private HashMap<IRI, AbstractClass> classMap = new HashMap<>();

	public Map<IRI, AbstractClass> getClassMap() {
		return Collections.unmodifiableMap(classMap);
	}

	public void addClass(AbstractClass vowlClass) {
		classMap.put(vowlClass.getIri(), vowlClass);
	}

	public AbstractNode getNodeForIri(IRI iri) {
		if (classMap.containsKey(iri)) {
			return classMap.get(iri);
		} else {
			throw new IllegalStateException("Can't find node for passed iri: " + iri);
		}
	}
}
