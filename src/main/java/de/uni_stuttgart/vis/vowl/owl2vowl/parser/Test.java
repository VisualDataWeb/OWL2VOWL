/*
 * Test.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.util.Set;

/**
 * @author Eduard Marbach
 */
public class Test {
	public static void main(String[] args) {
		File ont = new File("C:\\Users\\Eduard\\Downloads\\index.rdfn");
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		try {
			OWLOntology ontology = manager.loadOntologyFromOntologyDocument(IRI.create(ont));
			Set<OWLClass> classes = ontology.getClassesInSignature();
			Set<OWLDatatype> datatypes = ontology.getDatatypesInSignature();
			Set<OWLObjectProperty> properties = ontology.getObjectPropertiesInSignature();

			for (OWLClass currentClass : classes) {
				System.out.println("Class: " + currentClass.toString());
				System.out.println("Individuals: " + currentClass.getIndividuals(ontology));
				System.out.println("Axiome: " + ontology.getAxioms(currentClass));
				System.out.println("Annotations: " + currentClass.getAnnotations(ontology));
				System.out.println("Assert. Annot.: " + currentClass.getAnnotationAssertionAxioms(ontology));
				System.out.println("Subclasses: " + currentClass.getSubClasses(ontology));
				System.out.println("Superclasses: " + currentClass.getSuperClasses(ontology));
				System.out.println(currentClass.getAnonymousIndividuals());
				System.out.println(currentClass.getClassExpressionType());
				System.out.println(currentClass.getEntityType());
				System.out.println(currentClass.getNestedClassExpressions());
				System.out.println(ontology.getDisjointClassesAxioms(currentClass));
				System.out.println(ontology.getDisjointUnionAxioms(currentClass));
				System.out.println(ontology.getEquivalentClassesAxioms(currentClass));
				System.out.println(ontology.getHasKeyAxioms(currentClass));
				System.out.println();
			}



		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
	}
}
