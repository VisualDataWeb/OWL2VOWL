package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.Constants;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.*;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.BaseDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.RdfsDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.RdfsLiteral;
import org.semanticweb.owlapi.model.*;

public class TypeFinder {
	private OWLOntology ontology;
	private OWLDataFactory factory;

	public TypeFinder(OWLOntology ontology, OWLDataFactory factory) {
		this.ontology = ontology;
		this.factory = factory;
	}

	public BaseClass findVowlClass(OWLClass theClass) {
		if (isThing(theClass)) {
			return new OwlThing();
		} else if (isEquivalentClass(theClass)) {
			return new OwlEquivalentClass();
		} else if (isExternal(theClass)) {
			return new ExternalClass();
		} else {
			return new OwlClass();
		}
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
