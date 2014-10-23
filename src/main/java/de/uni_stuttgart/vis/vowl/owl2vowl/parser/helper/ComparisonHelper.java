/*
 * ComparisonHelper.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper;

import org.semanticweb.owlapi.model.IRI;

/**
 *
 */
public class ComparisonHelper {
	public static boolean hasDifferentNamespace(String elementNamespace, IRI ontologyNamespace) {
		int element = elementNamespace.indexOf("#");
		int ontologyIn = ontologyNamespace.toString().indexOf("#");

		String newElement = elementNamespace;
		String newOntology = ontologyNamespace.toString();

		if (element != -1) {
			newElement = newElement.substring(0, element);
		}

		if (ontologyIn != -1) {
			newOntology = newOntology.substring(0, ontologyIn);
		}

		return !(newElement.equals(newOntology));
	}
}
