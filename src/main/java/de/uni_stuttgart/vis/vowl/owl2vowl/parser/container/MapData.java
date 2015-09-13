/*
 * MapData.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.container;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Vowl_Lang;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.BaseEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.OntologyInfo;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.OntologyMetric;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.containerElements.DisjointUnion;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties.BaseProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties.DisjointProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties.OwlDatatypeProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties.OwlObjectProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.*;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.BaseDatatype;
import org.semanticweb.owlapi.model.*;

import java.util.*;

/**
 *
 */
public class MapData {
	/*
		 * Data with own created objects.
		 */
	private Map<String, BaseNode> mergedMap = new HashMap<String, BaseNode>();
	private Map<String, BaseClass> classMap = new MergeNodeMap<String, BaseClass>(mergedMap);
	private Map<String, BaseDatatype> datatypeMap = new MergeNodeMap<String, BaseDatatype>(mergedMap);
	/**
	 * The key value of this map is an ID not a IRI!
	 */
	private Map<String, OwlThing> thingMap = new MergeNodeMap<String, OwlThing>(mergedMap);
	private Map<String, OwlUnionOf> unionMap = new MergeNodeMap<String, OwlUnionOf>(mergedMap);
	private Map<String, OwlIntersectionOf> intersectionMap = new MergeNodeMap<String, OwlIntersectionOf>(mergedMap);
	private Map<String, OwlComplementOf> complementMap = new MergeNodeMap<String, OwlComplementOf>(mergedMap);

	public Map<String, ObjectOneOf> getObjectOneOfMap() {
		return objectOneOfMap;
	}

	private Map<String, ObjectOneOf> objectOneOfMap = new MergeNodeMap<String, ObjectOneOf>(mergedMap);


	private Map<String, BaseProperty> mergedProperties = new HashMap<String, BaseProperty>();
	private Map<String, OwlObjectProperty> objectPropertyMap = new MergePropertyMap<String, OwlObjectProperty>(mergedProperties);
	private Map<String, OwlDatatypeProperty> datatypePropertyMap = new MergePropertyMap<String, OwlDatatypeProperty>(mergedProperties);
	private Map<String, DisjointProperty> disjointPropertyMap = new MergePropertyMap<String, DisjointProperty>(mergedProperties);
	private Map<String, BaseProperty> rdfProperties = new MergePropertyMap<String, BaseProperty>(mergedProperties);
	/* Helper collections */
	private Set<DisjointUnion> disjointUnions = new HashSet<DisjointUnion>();
	/*
	 * IRI as key and the owl objects as value.
	 */
	private Map<String, OWLClass> owlClasses = new HashMap<String, OWLClass>();
	private Map<String, OWLDatatype> owlDatatypes = new HashMap<String, OWLDatatype>();
	private Map<String, OWLObjectProperty> owlObjectProperties = new HashMap<String, OWLObjectProperty>();
	private Map<String, OWLDataProperty> owlDatatypeProperties = new HashMap<String, OWLDataProperty>();
	private OntologyInfo ontologyInfo = new OntologyInfo();
	private OntologyMetric ontologyMetric = new OntologyMetric();
	private Map<String, Map<String, List<OWLAxiom>>> entityToAxiom = new HashMap<String, Map<String, List<OWLAxiom>>>();
	private Set<String> availableLanguages = new HashSet<String>();

	private Map<String, Set<BaseEntity>> iriToEntity = new HashMap<String, Set<BaseEntity>>();

	public MapData() {
		// Default langauge always exists.
		availableLanguages.add(Vowl_Lang.LANG_DEFAULT);
	}

	public Set<String> getAvailableLanguages() {
		return availableLanguages;
	}

	public void setAvailableLanguages(Set<String> availableLanguages) {
		this.availableLanguages = availableLanguages;
	}

	public Set<DisjointUnion> getDisjointUnions() {
		return disjointUnions;
	}

	public void setDisjointUnions(Set<DisjointUnion> disjointUnions) {
		this.disjointUnions = disjointUnions;
	}

	public Map<String, BaseProperty> getMergedProperties() {
		return mergedProperties;
	}

	public Map<String, BaseNode> getMergedMap() {
		return mergedMap;
	}

	public Map<String, DisjointProperty> getDisjointPropertyMap() {
		return disjointPropertyMap;
	}

	public void setDisjointPropertyMap(Map<String, DisjointProperty> disjointPropertyMap) {
		this.disjointPropertyMap = disjointPropertyMap;
	}

	public boolean addDisjointProperty(DisjointProperty property) {
		for (Map.Entry<String, DisjointProperty> currentEntry : disjointPropertyMap.entrySet()) {
			if (currentEntry.getValue().equivalentDisjoints(property)) {
				return false;
			}
		}

		disjointPropertyMap.put(property.getId(), property);
		return true;
	}

	public Map<String, Map<String, List<OWLAxiom>>> getEntityToAxiom() {
		return entityToAxiom;
	}

	public void setEntityToAxiom(Map<String, Map<String, List<OWLAxiom>>> entityToAxiom) {
		this.entityToAxiom = entityToAxiom;
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

	public Map<String, BaseProperty> getRdfProperties() {
		return rdfProperties;
	}

	public Map<String, Set<BaseEntity>> getIriToEntity() {
		return iriToEntity;
	}
}

class MergeNodeMap<K, V extends BaseNode> extends HashMap<K, V> {
	private HashMap<K, V> mergeMap;

	public <Val extends BaseNode> MergeNodeMap(Map<K, Val> mergeMap) {
		this.mergeMap = (HashMap<K, V>) mergeMap;
	}

	@Override
	public V put(K key, V value) {
		super.put(key, value);
		mergeMap.put(key, value);
		return value;
	}
}

class MergePropertyMap<K, V extends BaseProperty> extends HashMap<K, V> {
	private HashMap<K, V> mergeMap;

	public <Val extends BaseProperty> MergePropertyMap(Map<K, Val> mergeMap) {
		this.mergeMap = (HashMap<K, V>) mergeMap;
	}

	@Override
	public V put(K key, V value) {
		super.put(key, value);
		mergeMap.put(key, value);
		return value;
	}
}
