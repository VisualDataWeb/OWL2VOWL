/*
 * ComparisonHelper.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper;

import de.uni_stuttgart.vis.vowl.owl2vowl.parser.Main;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.Set;

/**
 *
 */
public class ComparisonHelper {
	public static boolean hasDifferentNamespace(String elementNamespace, IRI ontologyNamespace) {
		int element = elementNamespace.indexOf("#");
		int elementSlash = elementNamespace.lastIndexOf("/");

		int ontologyIn = ontologyNamespace.toString().indexOf("#");

		String newElement = elementNamespace;
		String newOntology = ontologyNamespace.toString();

		if (element != -1) {
			newElement = newElement.substring(0, element);
		} else if (elementSlash != -1) {
			newElement = newElement.substring(0, elementSlash + 1);
		}

		if (ontologyIn != -1) {
			newOntology = newOntology.substring(0, ontologyIn);
		}

		      /*
		System.out.println("ELEMENT: " + newElement);
		System.out.println("ONTOLOGY: " + newOntology);
		System.out.println();
		*/


		return !(newElement.equals(newOntology));
	}

	public static boolean hasDifferentNameSpace(OWLEntity entity, OWLOntology ontology) {
		if (entity.getAnnotations(ontology, Main.factory.getRDFSIsDefinedBy()).isEmpty()) {
			return hasDifferentNamespace(entity.getIRI().toString(), ontology.getOntologyID().getOntologyIRI());
		} else {
			Set<OWLAnnotation> test = entity.getAnnotations(ontology, Main.factory.getRDFSIsDefinedBy());
			OWLAnnotation first = test.iterator().next();
			return hasDifferentNamespace(first.getValue().toString(), ontology.getOntologyID().getOntologyIRI());
		}
	}
}
