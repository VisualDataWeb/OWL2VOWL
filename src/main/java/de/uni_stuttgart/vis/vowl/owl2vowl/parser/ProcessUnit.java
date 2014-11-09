/*
 * ProcessArrays.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.Constants;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.containerElements.DisjointUnion;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties.*;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.BaseClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.OwlEquivalentClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.SpecialClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.BaseDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper.AxiomParser;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper.ComparisonHelper;
import org.semanticweb.owlapi.model.*;

import java.util.*;

/**
 * @author Vincent Link, Eduard Marbach
 */
public class ProcessUnit {
	private OWLDataFactory factory;
	private MapData mapData;
	private OWLOntology ontology;
	private AxiomParser axiomParser;

	public ProcessUnit(OWLOntology ontology, OWLDataFactory factory, MapData mapData) {
		this.ontology = ontology;
		this.factory = factory;
		this.mapData = mapData;
		axiomParser = new AxiomParser();
	}

	public void processClasses() {
		for (Map.Entry<String, BaseClass> i : this.mapData.getClassMap().entrySet()) {
			BaseClass currentClass = i.getValue();
			processAttributes(currentClass);
			processEquivalents(currentClass);
			processSubClasses(currentClass);
			processSuperClasses(currentClass);
			processSpecialBehaviour(currentClass);
		}

	}

	private void processAxiomDisjointClasses(BaseClass currentClass, OWLEntity entity) {
		List<BaseNode> disjoints = currentClass.getDisjoints();

		for (OWLClassExpression o : axiomParser.getDisjoints(entity)) {
			disjoints.add(mapData.getClassMap().get(o.getClassesInSignature().iterator().next().getIRI().toString()));
		}

		for (BaseNode i : disjoints) {
			if (i != currentClass) {
				DisjointProperty prop = new DisjointProperty(i, currentClass);
				mapData.addDisjointProperty(prop);
			}
		}
	}
	private void processSpecialBehaviour(BaseClass currentClass) {
		if(!(currentClass instanceof SpecialClass)) {
			return;
		}

		OWLClass theClass = mapData.getOwlClasses().get(currentClass.getIri());
		SpecialClass working = (SpecialClass) currentClass;

		List<Set<OWLClass>> unions = axiomParser.searchInEquivalents(theClass, Constants.AXIOM_OBJ_UNION);
		List<Set<OWLClass>> intersections = axiomParser.searchInEquivalents(theClass, Constants.AXIOM_OBJ_INTERSECTION);
		List<Set<OWLClass>> complements = axiomParser.searchInEquivalents(theClass, Constants.AXIOM_OBJ_COMPLEMENT);

		for(OWLClass currentUnion : retrieveMainUnit(unions, theClass)) {
			BaseClass aClass = mapData.getClassMap().get(currentUnion.getIRI().toString());

			if (aClass == null) {
				Main.logger.error("Could not find correct intersection element in map: " + currentUnion);
				System.out.println("Problems during intersection searching: " + currentUnion);
				continue;
			}

			working.getUnions().add(aClass);
			working.setType(Constants.TYPE_UNION);
		}

		for (OWLClass curInteresection : retrieveMainUnit(intersections, theClass)) {
			BaseClass aClass = mapData.getClassMap().get(curInteresection.getIRI().toString());

			if (aClass == null) {
				Main.logger.error("Could not find correct intersection element in map: " + curInteresection);
				System.out.println("Problems during intersection searching: " + curInteresection);
				continue;
			}

			working.getIntersections().add(aClass);
			working.setType(Constants.TYPE_INTERSECTION);
		}

		for (OWLClass curComplement : retrieveMainUnit(complements, theClass)) {
			BaseClass aClass = mapData.getClassMap().get(curComplement.getIRI().toString());
			working.getComplements().add(aClass);
			working.setType(Constants.TYPE_COMPLEMENT);
		}
	}

	private Set<OWLClass> retrieveMainUnit(List<Set<OWLClass>> elementList, OWLEntity entity) {
		Set<OWLClass> merged = new TreeSet<>();

		for(Set<OWLClass> currentSet : elementList) {
			if(!currentSet.contains(entity.asOWLClass())) {
				merged.addAll(currentSet);
			}
		}

		return merged;
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
		List<BaseClass> equivalents = newBase.getEquivalentClasses();

		// Ignore class if not basis TODO is probably not correct yet. Because there could be equivalent
		// classes without a base in the equal namespace.
		if (hasDifferentNamespace(newBase.getIri(), ontology.getOntologyID().getOntologyIRI())) {
			return;
		}

		for (OWLClassExpression equiClassExpression : theClass.getEquivalentClasses(ontology)) {
			if (!equiClassExpression.isAnonymous()) {
				String iri = equiClassExpression.asOWLClass().getIRI().toString();
				BaseClass equivClass = mapData.getClassMap().get(iri);

				if (equivClass != null) {
					if (equivalents.contains(equivClass)) {
						continue;
					}

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
					BaseClass equivClass = mapData.getClassMap().get(equiClassIRI);

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

		for (BaseClass element : equivalents) {
			if (element.getClass() != OwlEquivalentClass.class) {
				return;
			}

			OwlEquivalentClass equivalentClass = (OwlEquivalentClass) element;

			Set<BaseClass> test = new HashSet<>(equivalentClass.getEquivalentClasses());

			test.addAll(equivalents);
			test.add(base);
			test.remove(element);
			equivalentClass.setEquivalentClasses(test);
		}
	}

	private void processAttributes(BaseClass base) {
		OWLClass theClass = mapData.getOwlClasses().get(base.getIri());
		List<String> attributes = base.getAttributes();

		if (isExternal(theClass)) {
			attributes.add("external");
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
		if (true) {
			boolean b = ComparisonHelper.hasDifferentNameSpace(theClass, ontology);
			return b;
		}
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
		return ComparisonHelper.hasDifferentNamespace(elementNamespace, ontologyNamespace);
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
			processPropFieldConvert(currentProperty);
		}
	}

	/**
	 * Converts the property fields: equivalents, disjoints and subProperties from the IRI to the
	 * correct ID of the property.
	 *
	 * @param currentProperty The property to process.
	 */
	private void processPropFieldConvert(BaseProperty currentProperty) {
		List<String> disjoints = new ArrayList<>();
		List<String> equivalents = new ArrayList<>();
		List<String> subProperties = new ArrayList<>();
		List<String> superProperties = new ArrayList<>();

		Map<String, BaseProperty> test = new HashMap<>();
		test.putAll(mapData.getDatatypePropertyMap());
		test.putAll(mapData.getObjectPropertyMap());

		for (String currentIRI : currentProperty.getDisjoints()) {
			if (test.containsKey(currentIRI)) {
				disjoints.add(test.get(currentIRI).getId());
			}
		}

		for (String currentIRI : currentProperty.getEquivalents()) {
			if (test.containsKey(currentIRI)) {
				equivalents.add(test.get(currentIRI).getId());
			}
		}

		for (String currentIRI : currentProperty.getSubProperties()) {
			if (test.containsKey(currentIRI)) {
				subProperties.add(test.get(currentIRI).getId());
			}
		}

		for (String currentIRI : currentProperty.getSuperProperties()) {
			if (test.containsKey(currentIRI)) {
				superProperties.add(test.get(currentIRI).getId());
			}
		}

		currentProperty.setSuperProperties(superProperties);
		currentProperty.setSubProperties(subProperties);
		currentProperty.setEquivalents(equivalents);
		currentProperty.setDisjoints(disjoints);
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
		Map<String, OwlDatatypeProperty> objectPropertyMap = mapData.getDatatypePropertyMap();

		for (Map.Entry<String, OwlDatatypeProperty> i : objectPropertyMap.entrySet()) {
			OwlDatatypeProperty currentProperty = i.getValue();
			processPropFieldConvert(currentProperty);
		}
	}
}
