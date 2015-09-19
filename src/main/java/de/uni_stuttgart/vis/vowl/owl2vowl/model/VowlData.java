/*
 * VowlData.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.AbstractNode;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.AbstractClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.AbstractDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.properties.VowlDatatypeProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.properties.VowlObjectProperty;
import org.semanticweb.owlapi.model.IRI;

import java.util.*;

/**
 * Contains all data WebVOWL needs.
 */
public class VowlData {

	private Map<AbstractEntity, Integer> entityToId = new HashMap<>();
	private Map<IRI, AbstractEntity> entityMap = new HashMap<>();
	private Map<IRI, AbstractClass> classMap = new AllEntityMap<>(entityMap);
	private Map<IRI, AbstractDatatype> datatypeMap = new AllEntityMap<>(entityMap);
	private Map<IRI, VowlObjectProperty> objectPropertyMap = new AllEntityMap<>(entityMap);
	private Map<IRI, VowlDatatypeProperty> datatypePropertyMap = new AllEntityMap<>(entityMap);
	private Set<String> languages = new HashSet<>();

	public Map<IRI, AbstractEntity> getEntityMap() {
		return Collections.unmodifiableMap(entityMap);
	}

	public Map<IRI, AbstractDatatype> getDatatypeMap() {
		return Collections.unmodifiableMap(datatypeMap);
	}

	public Map<IRI, VowlObjectProperty> getObjectPropertyMap() {
		return Collections.unmodifiableMap(objectPropertyMap);
	}

	public Map<IRI, VowlDatatypeProperty> getDatatypePropertyMap() {
		return Collections.unmodifiableMap(datatypePropertyMap);
	}

	public Map<IRI, AbstractClass> getClassMap() {
		return Collections.unmodifiableMap(classMap);
	}

	public AbstractNode getClassForIri(IRI iri) {
		if (classMap.containsKey(iri)) {
			return classMap.get(iri);
		} else {
			throw new IllegalStateException("Can't find node for passed iri: " + iri);
		}
	}

	public AbstractDatatype getDatatypeForIri(IRI iri) {
		if (datatypeMap.containsKey(iri)) {
			return datatypeMap.get(iri);
		} else {
			throw new IllegalStateException("Can't find node for passed iri: " + iri);
		}
	}

	public VowlObjectProperty getObjectPropertyForIri(IRI iri) {
		if (objectPropertyMap.containsKey(iri)) {
			return objectPropertyMap.get(iri);
		} else {
			throw new IllegalStateException("Can't find node for passed iri: " + iri);
		}
	}

	public VowlDatatypeProperty getDatatypePropertyForIri(IRI iri) {
		if (datatypePropertyMap.containsKey(iri)) {
			return datatypePropertyMap.get(iri);
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

	public void addClass(AbstractClass vowlClass) {
		classMap.put(vowlClass.getIri(), vowlClass);
	}

	public void addDatatype(AbstractDatatype datatype) {
		datatypeMap.put(datatype.getIri(), datatype);
	}

	public void addObjectProperty(VowlObjectProperty prop) {
		objectPropertyMap.put(prop.getIri(), prop);
	}

	public void addDatatypeProperty(VowlDatatypeProperty prop) {
		datatypePropertyMap.put(prop.getIri(), prop);
	}

	public Set<String> getLanguages() {
		return Collections.unmodifiableSet(languages);
	}

	public void addLanguage(String language) {
		languages.add(language);
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
