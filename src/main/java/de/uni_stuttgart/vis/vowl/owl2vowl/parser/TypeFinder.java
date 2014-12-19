package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Axiom_Annotations;
import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Standard_Iris;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.*;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.BaseDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.RdfsDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.RdfsLiteral;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper.ComparisonHelper;
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
		specialAxioms = Arrays.asList(Axiom_Annotations.AXIOM_OBJ_INTERSECTION, Axiom_Annotations.AXIOM_OBJ_UNION, Axiom_Annotations.AXIOM_OBJ_COMPLEMENT);
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

	// TODO if special type is in the first level and not in a nested like equivalent class?
	private boolean isSpecialClass(OWLClass theClass) {
		for (OWLAxiom baseAxiom : theClass.getReferencingAxioms(ontology)) {
			if (baseAxiom.getAxiomType().isAxiomType("EquivalentClasses")) {
				for (OWLClassExpression nestedClasses : baseAxiom.getNestedClassExpressions()) {
					if (specialAxioms.contains(nestedClasses.getClassExpressionType().toString())) {
						return true;
					}
				}
			}

			if(baseAxiom.getAxiomType().toString().equals("DisjointUnion")) {
				if(((OWLDisjointUnionAxiom)baseAxiom).getOWLClass() == theClass) {
					return true;
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
		return Standard_Iris.OWL_THING_CLASS_URI.equals(theClass.getIRI().toString()) || theClass
				.isOWLThing();
	}

	// TODO braucht man das überhaupt noch, da external nur noch ein Attribut ist?
	private boolean isExternal(OWLClass theClass) {
		return ComparisonHelper.hasDifferentNameSpace(theClass, ontology);
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
}
