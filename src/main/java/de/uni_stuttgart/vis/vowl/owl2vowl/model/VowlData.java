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

	private Map<AbstractEntity, Integer> entityToId = new HashMap<>();
	private Map<IRI, AbstractEntity> entityMap = new HashMap<>();
	private Map<IRI, AbstractClass> classMap = new AllEntityMap<>(entityMap);

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

	protected AbstractEntity getEntityForIri(IRI iri) {
		if (entityMap.containsKey(iri)) {
			return classMap.get(iri);
		} else {
			throw new IllegalStateException("Can't find node for passed iri: " + iri);
		}
	}

	public int getIdForIri(IRI iri) {
		return getIdForEntity(getEntityForIri(iri));
	}

	public int getIdForEntity(AbstractEntity entity) {
		if (!entityToId.containsKey(entity)) {
			entityToId.put(entity, entityToId.keySet().size());
		}

		return entityToId.get(entity);
	}
}

class AllEntityMap<K, V extends AbstractEntity> extends HashMap<K, V> {
	private HashMap<K, V> mergeMap;

	public <Val extends AbstractEntity> AllEntityMap(Map<K, Val> mergeMap) {
		this.mergeMap = (HashMap<K, V>) mergeMap;
	}

	@Override
	public V put(K key, V value) {
		super.put(key, value);
		mergeMap.put(key, value);
		return value;
	}
}
