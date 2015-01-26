/*
 * ComparisonHelper.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper;

import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.OntologyInformation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;

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

		return hasDifferentNameSpace(elementNamespace, ontologyIri);
	}

	public static boolean hasDifferentNameSpace(String elementNamespace, String ontologyIri) {
		if (elementNamespace.equals(ontologyIri)) {
			return false;
		}

		if (elementNamespace.equals(ontologyIri + "#")) {
			return false;
		}

		if (elementNamespace.contains("#")) {
			String elementNamespaceWithoutHashParameter = elementNamespace.split("#")[0];
			if (elementNamespaceWithoutHashParameter.equals(ontologyIri)) {
				return false;
			}
		}

		if (elementNamespace.contains("/") && !elementNamespace.endsWith("/")) {
			int lastSlashIndex = elementNamespace.lastIndexOf("/");
			int indexAfterSlash = lastSlashIndex + 1;

			String elementNamespaceWithoutLastPart = elementNamespace.substring(0, indexAfterSlash);

			if (elementNamespaceWithoutLastPart.equals(ontologyIri)) {
				return false;
			}
		}

		return true;
	}

	public static boolean hasDifferentNameSpace(OWLEntity entity, OntologyInformation information) {
		return hasDifferentNameSpace(entity.getIRI().toString(), information);
	}
}
