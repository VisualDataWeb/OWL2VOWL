/*
 * ProcessArrays.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Axiom_Annotations;
import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Node_Types;
import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Standard_Iris;
import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Vowl_Lang;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.Vowl_Prop_Attr;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.containerElements.DisjointUnion;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties.*;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.BaseClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.OwlEquivalentClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.SpecialClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.BaseDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.OntologyInformation;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper.AxiomParser;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper.ComparisonHelper;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.visitors.IndividualVisitorImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.*;

import java.util.*;

/**
 */
public class ProcessUnit {
	private static final Logger logger = LogManager.getRootLogger();
	private OWLDataFactory factory;
	private MapData mapData;
	private OWLOntology ontology;
	private AxiomParser axiomParser;
	private OntologyInformation ontologyInformation;

	public ProcessUnit(OntologyInformation ontologyInformation, MapData mapData) {
		this.ontologyInformation = ontologyInformation;
		this.mapData = mapData;
		ontology = ontologyInformation.getOntology();
		factory = ontologyInformation.getFactory();
		axiomParser = new AxiomParser(ontologyInformation, mapData);
	}

	public void processClasses() {
		for (Map.Entry<String, BaseClass> i : this.mapData.getClassMap().entrySet()) {
			BaseClass currentClass = i.getValue();
			processAttributes(currentClass);
			processEquivalents(currentClass);
			processSubClasses(currentClass);
			processSuperClasses(currentClass);
			processIndividuals(currentClass);
			processAxioms(currentClass);
			processSpecialBehaviour(currentClass);
		}

		for (BaseNode baseNode : this.mapData.getMergedMap().values()) {
			removeExternalEquivalents(baseNode);
		}
	}

	private void processIndividuals(BaseClass currentClass) {
		OWLClass owlClass = mapData.getOwlClasses().get(currentClass.getIri());
		Set<OWLIndividual> individuals = owlClass.getIndividuals(ontology.getOWLOntologyManager().getOntologies());
		IndividualVisitorImpl visitor = new IndividualVisitorImpl(individuals.size(), mapData, ontology);

		for (OWLIndividual individual : individuals) {
			individual.accept(visitor);
		}

		currentClass.setNumberOfIndividuals(visitor.getSetSize());
		currentClass.setIndividuals(visitor.getIndividuals());

		for (OWLClass aClass : visitor.getInstances()) {
			BaseProperty newProp = new BaseProperty();
			newProp.setDomain(currentClass);
			newProp.setRange(mapData.getClassMap().get(aClass.getIRI().toString()));
			newProp.getLabels().put(Vowl_Lang.LANG_DEFAULT, "is a");
			mapData.getRdfProperties().put(newProp.getId(), newProp);
		}
	}

	private void removeExternalEquivalents(BaseNode baseNode) {
		if (baseNode.getClass() != OwlEquivalentClass.class || !baseNode.getAttributes().contains(Vowl_Prop_Attr.PROP_ATTR_IMPORT)) {
			return;
		}

		OwlEquivalentClass equivNode = (OwlEquivalentClass) baseNode;

		for (BaseClass baseClass : equivNode.getEquivalentClasses()) {
			if (!baseClass.getAttributes().contains(Vowl_Prop_Attr.PROP_ATTR_IMPORT)) {
//				System.out.println(equivNode.getIri() + " , " + equivNode.getEquivalentClasses());
				equivNode.getEquivalentClasses().clear();
				return;
			}
		}
	}

	private void processAxioms(BaseClass currentClass) {
		OWLEntity entity = mapData.getOwlClasses().get(currentClass.getIri());
		axiomParser.parseAxioms(entity);

		processAxiomDisjointClasses(currentClass, entity);
		processAxiomDisjointUnion(entity);
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

	private void processAxiomDisjointUnion(OWLEntity entity) {
		Set<DisjointUnion> disjointUnions = axiomParser.getDisjointUnions(entity);

		if (!disjointUnions.isEmpty()) {
			for (DisjointUnion currentDisUn : disjointUnions) {
				if (currentDisUn.getBaseNode().getClass() != SpecialClass.class) {
					break;
				}

				SpecialClass currentNode = (SpecialClass) currentDisUn.getBaseNode();

				currentNode.setType(Node_Types.TYPE_UNION);

				currentNode.setUnions(new ArrayList<BaseNode>(currentDisUn.getDisjointness()));

				int i = 0;
				List<BaseNode> baseNodeList = new ArrayList<BaseNode>(currentDisUn.getDisjointness());

				while (i < baseNodeList.size()) {
					DisjointProperty prop;

					if (i + 1 == baseNodeList.size()) {
						prop = new DisjointProperty(baseNodeList.get(i), baseNodeList.get(0));
					} else {
						prop = new DisjointProperty(baseNodeList.get(i), baseNodeList.get(i + 1));
					}

					mapData.addDisjointProperty(prop);
					i++;
				}
			}
		}
	}

	private void processSpecialBehaviour(BaseClass currentClass) {
		if (!(currentClass instanceof SpecialClass)) {
			return;
		}

		OWLClass theClass = mapData.getOwlClasses().get(currentClass.getIri());
		SpecialClass working = (SpecialClass) currentClass;

		List<Set<OWLClass>> unions = axiomParser.searchInEquivalents(theClass, Axiom_Annotations.AXIOM_OBJ_UNION);
		List<Set<OWLClass>> intersections = axiomParser.searchInEquivalents(theClass, Axiom_Annotations.AXIOM_OBJ_INTERSECTION);
		List<Set<OWLClass>> complements = axiomParser.searchInEquivalents(theClass, Axiom_Annotations.AXIOM_OBJ_COMPLEMENT);

		for (OWLClass currentUnion : retrieveMainUnit(unions, theClass)) {
			BaseClass aClass = mapData.getClassMap().get(currentUnion.getIRI().toString());

			if (aClass == null) {
				logger.error("Could not find correct intersection element in map: " + currentUnion);
				continue;
			}

			working.getUnions().add(aClass);
			working.setType(Node_Types.TYPE_UNION);
		}

		for (OWLClass curInteresection : retrieveMainUnit(intersections, theClass)) {
			BaseClass aClass = mapData.getClassMap().get(curInteresection.getIRI().toString());

			if (aClass == null) {
				logger.error("Could not find correct intersection element in map: " + curInteresection);
				continue;
			}

			working.getIntersections().add(aClass);
			working.setType(Node_Types.TYPE_INTERSECTION);
		}

		for (OWLClass curComplement : retrieveMainUnit(complements, theClass)) {
			BaseClass aClass = mapData.getClassMap().get(curComplement.getIRI().toString());
			working.getComplements().add(aClass);
			working.setType(Node_Types.TYPE_COMPLEMENT);
		}
	}

	private Set<OWLClass> retrieveMainUnit(List<Set<OWLClass>> elementList, OWLEntity entity) {
		Set<OWLClass> merged = new TreeSet<OWLClass>();

		for (Set<OWLClass> currentSet : elementList) {
			if (!currentSet.contains(entity.asOWLClass())) {
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

		/*
		// Ignore class if not basis TODO is probably not correct yet. Because there could be equivalent
		// classes without a base in the equal namespace.
		if (hasDifferentNamespace(newBase.getIri(), ontology.getOntologyID().getOntologyIRI())) {
			return;
		}
		*/

		for (OWLClassExpression equiClassExpression : theClass.getEquivalentClasses(ontology)) {
			if (!equiClassExpression.isAnonymous()) {
				String iri = equiClassExpression.asOWLClass().getIRI().toString();
				BaseClass equivClass = mapData.getClassMap().get(iri);

				if (equivClass != null) {
					if (equivalents.contains(equivClass)) {
						continue;
					}

					// Move class to first position if the namespace is same with ontology.
					if (!ComparisonHelper.hasDifferentNameSpace(equiClassExpression.asOWLClass(), ontologyInformation)) {
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
						if (!ComparisonHelper.hasDifferentNameSpace(owl_class_entity, ontologyInformation)) {
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

			Set<BaseClass> test = new HashSet<BaseClass>(equivalentClass.getEquivalentClasses());

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
				if (!Standard_Iris.OWL_THING_CLASS_URI.equals(subClassURI)) {
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
				if (!Standard_Iris.OWL_THING_CLASS_URI.equals(superClassURI)) {
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
			boolean b = ComparisonHelper.hasDifferentNameSpace(theClass, ontologyInformation);
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

		return ComparisonHelper.hasDifferentNameSpace(theClass, ontologyInformation);
	}

	private boolean isEquivalentClass(OWLClass theClass) {
		return !ontology.getEquivalentClassesAxioms(theClass).isEmpty();
	}

	public void processProperties() {
		processEquivalentProperties();
		processObjectProperty();
		processDatatypeProperty();
	}

	private void processEquivalentProperties() {
		Map<String, BaseProperty> mergedProperties = mapData.getMergedProperties();

		// TODO change arrays to Objects and not only Strings
		for (BaseProperty baseProperty : mergedProperties.values()) {
			for (String s : baseProperty.getEquivalents()) {
				BaseProperty equiv = mapData.getMergedProperties().get(s);

				if (equiv == null) {
					continue;
				}

				Set<String> equivs = new HashSet<String>(equiv.getEquivalents());

				// Add all equivalents and the calling property. But remove the property of the current element.
				equivs.addAll(baseProperty.getEquivalents());
				equivs.add(baseProperty.getIri());
				equivs.remove(s);

				equiv.setEquivalents(new ArrayList<String>(equivs));
			}
		}


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
		List<String> disjoints = new ArrayList<String>();
		List<String> equivalents = new ArrayList<String>();
		List<String> subProperties = new ArrayList<String>();
		List<String> superProperties = new ArrayList<String>();

		Map<String, BaseProperty> test = new HashMap<String, BaseProperty>();
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
//			System.out.println("Missing inverse IRI: " + iri);
		}
	}

	private void processDatatypeProperty() {
		Map<String, OwlDatatypeProperty> objectPropertyMap = mapData.getDatatypePropertyMap();

		for (Map.Entry<String, OwlDatatypeProperty> i : objectPropertyMap.entrySet()) {
			OwlDatatypeProperty currentProperty = i.getValue();
			processPropFieldConvert(currentProperty);
		}
	}

	public void processAxioms() {
		axiomParser.processAxioms(ontology);
	}
}
