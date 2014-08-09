/*
 * ProcessArrays.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.BaseEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.Constants;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.classes.BaseClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.classes.OwlEquivalentClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.datatypes.BaseDatatype;
import org.semanticweb.owlapi.model.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Vincent Link, Eduard Marbach
 */
public class ProcessUnit {

	private OWLDataFactory factory;
	private OWLOntology ontology;
	private Map<String, BaseClass> theMap;
	private Map<String, OWLClass> owlClasses;
	private Map<String, BaseDatatype> datatypeMap;
	private Map<String, OWLDatatype> owlDatatypes;

	public ProcessUnit(OWLOntology ontology, OWLDataFactory factory) {
		this.ontology = ontology;
		this.factory = factory;
	}

	public void processClasses(Map<String, BaseClass> theMap, Map<String, OWLClass> owlClasses) {
		this.theMap = theMap;
		this.owlClasses = owlClasses;

		for (Map.Entry<String, BaseClass> i : this.theMap.entrySet()) {
			BaseClass currentClass = i.getValue();
			processAttributes(currentClass);
			processEquivalents(currentClass);
			processSubClasses(currentClass);
			processSuperClasses(currentClass);
		}

	}

	public void processDatatypes(Map<String, BaseDatatype> theMap, Map<String, OWLDatatype> owlDatatypes) {
		datatypeMap = theMap;
		this.owlDatatypes = owlDatatypes;

		for(Map.Entry<String, BaseDatatype> currentElement : this.datatypeMap.entrySet()) {
			BaseDatatype currentDatatype = currentElement.getValue();
			// TODO later
		}
	}

	private void processEquivalents(BaseClass base) {
		if (base.getClass() != OwlEquivalentClass.class) {
			return;
		}

		OWLClass theClass = owlClasses.get(base.getIri());
		OwlEquivalentClass newBase = (OwlEquivalentClass) base;
		List<BaseEntity> equivalents = newBase.getEquivalentClasses();


		for (OWLClassExpression equiClassExpression : theClass.getEquivalentClasses(ontology)) {
			if (!equiClassExpression.isAnonymous()) {
				BaseClass equivClass =
						theMap.get(equiClassExpression.asOWLClass().getIRI().toString());

				if (equivClass != null) {
					equivalents.add(equivClass);
				}
			} else {
				Set<OWLEntity> equiClassExpressionSignatureSet = equiClassExpression.getSignature();

				for (OWLEntity owl_class_entity : equiClassExpressionSignatureSet) {
					String equiClassIRI = owl_class_entity.getIRI().toString();
					BaseClass equivClass = theMap.get(equiClassIRI);

					if (equivClass != null) {
						equivalents.add(equivClass);
					}
				}
			}
		}
	}

	private void processAttributes(BaseClass base) {
		OWLClass theClass = owlClasses.get(base.getIri());
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

	/**
	 * TODO
	 * NOT SURE HOW TO CHECK THIS DIRECTLY.
	 * NOT USABLE YET!
	 *
	 * @param theClass
	 * @return
	 */
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

	private void processSubClasses(BaseClass base) {
		OWLClass theClass = owlClasses.get(base.getIri());
		List<BaseEntity> subClasses = base.getSubClasses();

		for (OWLClassExpression subClassExpression : theClass.getSubClasses(ontology)) {
			if (!subClassExpression.isAnonymous()) {
				String subClassURI = subClassExpression.asOWLClass().getIRI().toString();
				// ignore subclass with the namespace of OWLClass Thing
				if (!Constants.OWL_THING_CLASS_URI.equals(subClassURI)) {
					BaseClass sub = theMap.get(subClassURI);

					if (sub != null) {
						subClasses.add(sub);
					}
				}

			}
		}
	}

	private void processSuperClasses(BaseClass base) {
		OWLClass theClass = owlClasses.get(base.getIri());
		List<BaseEntity> superClasses = base.getSuperClasses();

		for (OWLClassExpression superClassExpression : theClass.getSuperClasses(ontology)) {
			if (!superClassExpression.isAnonymous()) {
				String superClassURI = superClassExpression.asOWLClass().getIRI().toString();
				// ignore subclass with the namespace of OWLClass Thing
				if (!Constants.OWL_THING_CLASS_URI.equals(superClassURI)) {
					BaseClass sub = theMap.get(superClassURI);

					if (sub != null) {
						superClasses.add(sub);
					}
				}

			}
		}
	}
}
