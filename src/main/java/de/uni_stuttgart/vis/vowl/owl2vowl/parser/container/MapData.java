/*
 * MapData.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.container;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.OntologyInfo;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.OntologyMetric;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties.OwlDatatypeProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties.OwlObjectProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.*;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.BaseDatatype;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class MapData {
	/*
		 * Data with own created objects.
		 */
	private Map<String, BaseNode> mergedMap = new HashMap<>();
	private Map<String, BaseClass> classMap = new MergeMap<>(mergedMap);
	private Map<String, BaseDatatype> datatypeMap = new MergeMap<>(mergedMap);
	/**
	 * The key value of this map is an ID not a IRI!
	 */
	private Map<String, OwlThing> thingMap = new MergeMap<>(mergedMap);
	private Map<String, OwlUnionOf> unionMap = new MergeMap<>(mergedMap);
	private Map<String, OwlIntersectionOf> intersectionMap = new MergeMap<>(mergedMap);
	private Map<String, OwlComplementOf> complementMap = new MergeMap<>(mergedMap);
	private Map<String, OwlObjectProperty> objectPropertyMap = new HashMap<>();
	private Map<String, OwlDatatypeProperty> datatypePropertyMap = new HashMap<>();
	/*
	 * IRI as key and the owl objects as value.
	 */
	private Map<String, OWLClass> owlClasses = new HashMap<>();
	private Map<String, OWLDatatype> owlDatatypes = new HashMap<>();
	private Map<String, OWLObjectProperty> owlObjectProperties = new HashMap<>();
	private Map<String, OWLDataProperty> owlDatatypeProperties = new HashMap<>();
	private OntologyInfo ontologyInfo = new OntologyInfo();
	private OntologyMetric ontologyMetric = new OntologyMetric();

	public MapData() {
	}

	public Map<String, OwlUnionOf> getUnionMap() {
		return unionMap;
	}

	public void setUnionMap(Map<String, OwlUnionOf> unionMap) {
		this.unionMap = unionMap;
	}

	public Map<String, OwlIntersectionOf> getIntersectionMap() {
		return intersectionMap;
	}

	public void setIntersectionMap(Map<String, OwlIntersectionOf> intersectionMap) {
		this.intersectionMap = intersectionMap;
	}

	public Map<String, OwlComplementOf> getComplementMap() {
		return complementMap;
	}

	public void setComplementMap(Map<String, OwlComplementOf> complementMap) {
		this.complementMap = complementMap;
	}

	public OntologyMetric getOntologyMetric() {
		return ontologyMetric;
	}

	public OntologyInfo getOntologyInfo() {
		return ontologyInfo;
	}

	public Map<String, BaseClass> getClassMap() {
		return classMap;
	}

	public void setClassMap(Map<String, BaseClass> classMap) {
		this.classMap = classMap;
	}

	public Map<String, BaseDatatype> getDatatypeMap() {
		return datatypeMap;
	}

	public void setDatatypeMap(Map<String, BaseDatatype> datatypeMap) {
		this.datatypeMap = datatypeMap;
	}

	public Map<String, OwlObjectProperty> getObjectPropertyMap() {
		return objectPropertyMap;
	}

	public void setObjectPropertyMap(Map<String, OwlObjectProperty> objectPropertyMap) {
		this.objectPropertyMap = objectPropertyMap;
	}

	public Map<String, OwlDatatypeProperty> getDatatypePropertyMap() {
		return datatypePropertyMap;
	}

	public void setDatatypePropertyMap(Map<String, OwlDatatypeProperty> datatypePropertyMap) {
		this.datatypePropertyMap = datatypePropertyMap;
	}

	public Map<String, OwlThing> getThingMap() {
		return thingMap;
	}

	public void setThingMap(Map<String, OwlThing> thingMap) {
		this.thingMap = thingMap;
	}

	public Map<String, OWLClass> getOwlClasses() {
		return owlClasses;
	}

	public void setOwlClasses(Map<String, OWLClass> owlClasses) {
		this.owlClasses = owlClasses;
	}

	public Map<String, OWLDatatype> getOwlDatatypes() {
		return owlDatatypes;
	}

	public void setOwlDatatypes(Map<String, OWLDatatype> owlDatatypes) {
		this.owlDatatypes = owlDatatypes;
	}

	public Map<String, OWLObjectProperty> getOwlObjectProperties() {
		return owlObjectProperties;
	}

	public void setOwlObjectProperties(Map<String, OWLObjectProperty> owlObjectProperties) {
		this.owlObjectProperties = owlObjectProperties;
	}

	public Map<String, OWLDataProperty> getOwlDatatypeProperties() {
		return owlDatatypeProperties;
	}

	public void setOwlDatatypeProperties(Map<String, OWLDataProperty> owlDatatypeProperties) {
		this.owlDatatypeProperties = owlDatatypeProperties;
	}

	public BaseNode findNode(String iriOrString) {
		return mergedMap.get(iriOrString);
	}
}

class MergeMap<K, V extends BaseNode> extends HashMap<K, V> {
	private HashMap<K, V> mergeMap;

	public <Val extends BaseNode> MergeMap(Map<K, Val> mergeMap) {
		this.mergeMap = (HashMap<K, V>) mergeMap;
	}

	@Override
	public V put(K key, V value) {
		super.put(key, value);
		mergeMap.put(key, value);
		return value;
	}
}
