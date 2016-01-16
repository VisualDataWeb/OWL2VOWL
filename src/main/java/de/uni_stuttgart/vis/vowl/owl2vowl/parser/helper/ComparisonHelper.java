/*
 * Tes.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;

public class ComparisonHelper {
	private static final Logger logger = LogManager.getLogger(ComparisonHelper.class);

	public static boolean hasDifferentNameSpace(OWLEntity entity, OWLOntology ontology, String loadedIri) {
		return hasDifferentNameSpace(entity.getIRI().toString(), ontology, loadedIri);
	}

	public static boolean hasDifferentNameSpace(String elementIri, String ontologyIri) {
		// The trailing hash has no meaning and can be removed
		String trimmedElementIri = removeTrailingHash(elementIri);
		String trimmedOntologyIri = removeTrailingHash(ontologyIri);

		if (trimmedElementIri.equals(trimmedOntologyIri)) {
			return false;
		}

		if (trimmedElementIri.contains("#")) {
			String elementNamespaceWithoutHashParameter = trimmedElementIri.split("#")[0];
			if (elementNamespaceWithoutHashParameter.equals(trimmedOntologyIri)) {
				return false;
			}
		}

		if (trimmedElementIri.contains("/") && !trimmedElementIri.endsWith("/")) {
			int lastSlashIndex = trimmedElementIri.lastIndexOf("/");
			int indexAfterSlash = lastSlashIndex + 1;

			String elementNamespaceWithoutLastPart = trimmedElementIri.substring(0, indexAfterSlash);

			if (elementNamespaceWithoutLastPart.equals(trimmedOntologyIri)) {
				return false;
			}
		}

		return true;
	}

	public static boolean hasDifferentNameSpace(String elementNamespace, OWLOntology ontology, String loadedIri) {
		if (elementNamespace == null) {
			logger.info("Namespace check: Element has no namespace!");
			return false;
		}

		String ontologyIri;

		// Use ontology iri or the iri path to the ontology.
		if (ontology.isAnonymous()) {
			ontologyIri = loadedIri;
		} else {
			ontologyIri = ontology.getOntologyID().getOntologyIRI().get().toString();
		}

		return hasDifferentNameSpace(elementNamespace, ontologyIri);
	}

	public static String extractBaseIRI(String iri) {
		if (iri.endsWith("#")) {
			// e.g. http://test.de/working#
			return iri.substring(0, iri.lastIndexOf("#"));
		}

		if (iri.contains("#")) {
			// e.g. http://test.de/working#theworker
			iri = iri.split("#")[0];
			return iri;
		}

		if (iri.endsWith("/")) {
			// e.g. http://test.de/working/theworker/
			iri = iri.substring(0, iri.lastIndexOf("/"));
		}

		// e.g. http://test.de/working/theworker
		return iri.substring(0, iri.lastIndexOf("/"));
	}


	private static String removeTrailingHash(String text) {
		return text.replaceAll("#$", "");
	}
}
