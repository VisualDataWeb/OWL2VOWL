/*
 * ComparisonHelper.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper;

import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.OntologyInformation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.*;

import java.util.Set;

/**
 *
 */
public class ComparisonHelper {
	private static final Logger logger = LogManager.getLogger(ComparisonHelper.class);
	private static boolean hasDifferentNameSpace(String elementNamespace, OntologyInformation information) {
		if (elementNamespace == null) {
			logger.info("Namespace check: Element has no namespace!");
			return false;
		}

		String ontologyIri;

		// Use ontology iri or the iri path to the ontology.
		if (information.getOntology().isAnonymous()) {
			ontologyIri = information.getLoadedIri().toString();
		} else {
			ontologyIri = information.getOntology().getOntologyID().getOntologyIRI().toString();
		}

		int element = elementNamespace.indexOf("#");
		int elementSlash = elementNamespace.lastIndexOf("/");
		int ontologyIn = ontologyIri.indexOf("#");

		String newElement = elementNamespace;
		String newOntology = ontologyIri;

		if (element != -1) {
			newElement = newElement.substring(0, element);
		} else if (elementSlash != -1) {
			newElement = newElement.substring(0, elementSlash + 1);
		}

		if (ontologyIn != -1) {
			newOntology = newOntology.substring(0, ontologyIn);
		}

		return !(newElement.equals(newOntology));
	}

	public static boolean hasDifferentNameSpace(OWLEntity entity, OntologyInformation information) {
		OWLOntology ontology = information.getOntology();
		OWLDataFactory dataFactory = information.getFactory();

		if (entity.getAnnotations(ontology, dataFactory.getRDFSIsDefinedBy()).isEmpty()) {
			return hasDifferentNameSpace(entity.getIRI().toString(), information);
		} else {
			Set<OWLAnnotation> test = entity.getAnnotations(ontology, dataFactory.getRDFSIsDefinedBy());
			OWLAnnotation first = test.iterator().next();
			return hasDifferentNameSpace(first.getValue().toString(), information);
		}
	}
}
