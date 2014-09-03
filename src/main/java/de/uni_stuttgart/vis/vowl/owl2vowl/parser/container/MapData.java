/*
 * MapData.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.container;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties.OwlDatatypeProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties.OwlObjectProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.BaseClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.OwlThing;
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
	private Map<String, BaseClass> classMap = new HashMap<>();
	private Map<String, BaseDatatype> datatypeMap = new HashMap<>();
	private Map<String, OwlObjectProperty> objectPropertyMap = new HashMap<>();
	private Map<String, OwlDatatypeProperty> datatypePropertyMap = new HashMap<>();
	/**
	 * The key value of this map is an ID not a IRI!
	 */
	private Map<String, OwlThing> thingMap = new HashMap<>();
	/*
	 * IRI as key and the owl objects as value.
	 */
	private Map<String, OWLClass> owlClasses = new HashMap<>();
	private Map<String, OWLDatatype> owlDatatypes = new HashMap<>();
	private Map<String, OWLObjectProperty> owlObjectProperties = new HashMap<>();
	private Map<String, OWLDataProperty> owlDatatypeProperties = new HashMap<>();

	public MapData() {
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
}
