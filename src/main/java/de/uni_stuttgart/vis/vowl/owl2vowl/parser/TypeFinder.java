package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.Constants;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.*;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.BaseDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.RdfsDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.RdfsLiteral;
import org.semanticweb.owlapi.model.*;

import java.util.Arrays;
import java.util.List;

public class TypeFinder {
	private OWLOntology ontology;
	private OWLDataFactory factory;
	private List<String> specialAxioms;

	public TypeFinder(OWLOntology ontology, OWLDataFactory factory) {
		this.ontology = ontology;
		this.factory = factory;
		specialAxioms = Arrays.asList(Constants.AXIOM_OBJ_INTERSECTION, Constants.AXIOM_OBJ_UNION, Constants.AXIOM_OBJ_COMPLEMENT);
	}

	public BaseClass findVowlClass(OWLClass theClass) {
		if (isThing(theClass)) {
			return new OwlThing();
		} else if (isSpecialClass(theClass)) {
			return new SpecialClass();
		} else if (isEquivalentClass(theClass)) {
			return new OwlEquivalentClass();
		} else if (isExternal(theClass)) {
			return new ExternalClass();
		} else {
			return new BaseClass();
		}
	}

	private boolean isSpecialClass(OWLClass theClass) {
		for (OWLAxiom baseAxiom : theClass.getReferencingAxioms(ontology)) {
			if (baseAxiom.getAxiomType().isAxiomType("EquivalentClasses")) {
				for (OWLClassExpression nestedClasses : baseAxiom.getNestedClassExpressions()) {
					if (specialAxioms.contains(nestedClasses.getClassExpressionType().toString())) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * .
	 *
	 * @param theDatatype the OWL datatype.
	 * @return The correct datatype object
	 */
	public BaseDatatype findVowlDatatype(OWLDatatype theDatatype) {
		if (isPlainLiteral(theDatatype)) {
			return new RdfsLiteral();
		} else {
			return new RdfsDatatype();
		}
	}

	private boolean isPlainLiteral(OWLDatatype theDatatype) {
		return theDatatype.isRDFPlainLiteral();
	}

	private boolean isThing(OWLClass theClass) {
		return Constants.OWL_THING_CLASS_URI.equals(theClass.getIRI().toString()) || theClass
				.isOWLThing();
	}

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
}
