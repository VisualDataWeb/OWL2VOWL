/*
 * ProcessArrays.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.Constants;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties.BaseProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties.OwlDatatypeProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties.OwlObjectProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties.SubClassProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.BaseClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.OwlEquivalentClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.BaseDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import org.semanticweb.owlapi.model.*;

import java.util.*;

/**
 * @author Vincent Link, Eduard Marbach
 */
public class ProcessUnit {

	private OWLDataFactory factory;
	private MapData mapData;
	private OWLOntology ontology;

	public ProcessUnit(OWLOntology ontology, OWLDataFactory factory, MapData mapData) {
		this.ontology = ontology;
		this.factory = factory;
		this.mapData = mapData;
	}

	public void processClasses() {
		for (Map.Entry<String, BaseClass> i : this.mapData.getClassMap().entrySet()) {
			BaseClass currentClass = i.getValue();
			processAttributes(currentClass);
			processEquivalents(currentClass);
			processSubClasses(currentClass);
			processSuperClasses(currentClass);
		}

	}

	public void processDatatypes() {
		for (Map.Entry<String, BaseDatatype> currentElement : this.mapData.getDatatypeMap().entrySet()) {
			BaseDatatype currentDatatype = currentElement.getValue();
			// TODO later
		}
	}

	private void processEquivalents(BaseClass base) {
		if (base.getClass() != OwlEquivalentClass.class) {
			return;
		}

		OWLClass theClass = mapData.getOwlClasses().get(base.getIri());
		OwlEquivalentClass newBase = (OwlEquivalentClass) base;
		List<OwlEquivalentClass> equivalents = newBase.getEquivalentClasses();

		// Ignore class if not basis TODO is probably not correct yet. Because there could be equivalent
		// classes without a base in the equal namespace.
		if (hasDifferentNamespace(newBase.getIri(), ontology.getOntologyID().getOntologyIRI())) {
			return;
		}

		for (OWLClassExpression equiClassExpression : theClass.getEquivalentClasses(ontology)) {
			if (!equiClassExpression.isAnonymous()) {
				String iri = equiClassExpression.asOWLClass().getIRI().toString();
				OwlEquivalentClass equivClass = (OwlEquivalentClass) mapData.getClassMap().get(iri);

				if (equivClass != null) {
					// Move class to first position if the namespace is same with ontology.
					if (!hasDifferentNamespace(iri, ontology.getOntologyID().getOntologyIRI())) {
						equivalents.add(0, equivClass);
					} else {
						equivalents.add(equivClass);
					}
				}
			} else {
				Set<OWLEntity> equiClassExpressionSignatureSet = equiClassExpression.getSignature();

				for (OWLEntity owl_class_entity : equiClassExpressionSignatureSet) {
					String equiClassIRI = owl_class_entity.getIRI().toString();
					OwlEquivalentClass equivClass = (OwlEquivalentClass) mapData.getClassMap().get(equiClassIRI);

					if (equivClass != null) {
						// Move class to first position if the namespace is same with ontology.
						if (!hasDifferentNamespace(equiClassIRI, ontology.getOntologyID().getOntologyIRI())) {
							equivalents.add(0, equivClass);
						} else {
							equivalents.add(equivClass);
						}
					}
				}
			}
		}
	}

	private void processAttributes(BaseClass base) {
		OWLClass theClass = mapData.getOwlClasses().get(base.getIri());
		List<String> attributes = base.getAttributes();

		if (isExternal(theClass)) {
			attributes.add("external");
		}

		if (isDeprected(theClass)) {
			attributes.add("deprecated");
		}

		if (isEquivalentClass(theClass)) {
			attributes.add("equivalent");
		}
	}

	private void processSubClasses(BaseClass base) {
		OWLClass theClass = mapData.getOwlClasses().get(base.getIri());
		List<BaseNode> subClasses = base.getSubClasses();

		for (OWLClassExpression subClassExpression : theClass.getSubClasses(ontology)) {
			if (!subClassExpression.isAnonymous()) {
				String subClassURI = subClassExpression.asOWLClass().getIRI().toString();
				// ignore subclass with the namespace of OWLClass Thing
				if (!Constants.OWL_THING_CLASS_URI.equals(subClassURI)) {
					BaseClass sub = mapData.getClassMap().get(subClassURI);

					if (sub != null) {
						subClasses.add(sub);
						SubClassProperty property = new SubClassProperty(sub, base);
						mapData.getObjectPropertyMap().put(property.getId(), property);
					}
				}

			}
		}
	}

	private void processSuperClasses(BaseClass base) {
		OWLClass theClass = mapData.getOwlClasses().get(base.getIri());
		List<BaseNode> superClasses = base.getSuperClasses();

		for (OWLClassExpression superClassExpression : theClass.getSuperClasses(ontology)) {
			if (!superClassExpression.isAnonymous()) {
				String superClassURI = superClassExpression.asOWLClass().getIRI().toString();
				// ignore subclass with the namespace of OWLClass Thing
				if (!Constants.OWL_THING_CLASS_URI.equals(superClassURI)) {
					BaseClass sub = mapData.getClassMap().get(superClassURI);

					if (sub != null) {
						superClasses.add(sub);
					}
				}

			}
		}
	}

	// TODO duplicated code
	private boolean isExternal(OWLClass theClass) {
		IRI ontoIRI = ontology.getOntologyID().getOntologyIRI();
		String definedBy = null;

		for (OWLAnnotation i : theClass.getAnnotations(ontology, factory.getRDFSIsDefinedBy())) {
			definedBy = i.getValue().toString();
		}

		if (ontoIRI == null && definedBy != null) {
			ontoIRI = IRI.create(definedBy);
		}

		return this.hasDifferentNamespace(theClass.getIRI().toString(), ontoIRI);
	}

	private boolean isDeprected(OWLClass theClass) {
		return false;
	}

	private boolean isEquivalentClass(OWLClass theClass) {
		return !ontology.getEquivalentClassesAxioms(theClass).isEmpty();
	}

	/**
	 * Checks if an element has a different namespace as an other element.
	 * The function will return true if the element's namespace doesn't contain the namespace of the URI.
	 *
	 * @param elementNamespace  the namespace of an element as string (URI to string)
	 * @param ontologyNamespace the namespace of the ontology as IRI
	 * @return true, if the namespace is different
	 */
	private boolean hasDifferentNamespace(String elementNamespace, IRI ontologyNamespace) {
		if (elementNamespace == null || ontologyNamespace == null) {
			return false;
		}

		return !(elementNamespace.contains(ontologyNamespace));
	}

	public void processProperties() {
		processObjectProperty();
		processDatatypeProperty();
	}

	private void processObjectProperty() {
		Map<String, OwlObjectProperty> objectPropertyMap = mapData.getObjectPropertyMap();

		for (Map.Entry<String, OwlObjectProperty> i : objectPropertyMap.entrySet()) {
			OwlObjectProperty currentProperty = i.getValue();
			processInverseID(currentProperty);
		}
	}

	private void processInverseID(BaseProperty property) {
		Map<String, OwlObjectProperty> objectPropertyMap = mapData.getObjectPropertyMap();
		Map<String, OwlDatatypeProperty> datatypePropertyMap = mapData.getDatatypePropertyMap();

		String iri = property.getInverseIRI();

		if (iri == null || iri.isEmpty()) {
			return;
		}

		if (objectPropertyMap.containsKey(iri)) {
			property.setInverseID(objectPropertyMap.get(iri).getId());
		} else if (datatypePropertyMap.containsKey(iri)) {
			property.setInverseID(datatypePropertyMap.get(iri).getId());
		} else {
			System.out.println("Missing inverse IRI: " + iri);
		}
	}

	private void processDatatypeProperty() {

	}
}
