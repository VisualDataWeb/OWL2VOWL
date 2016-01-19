/*
 * Dummy.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper;

/**
 * @author Eduard Marbach
 * @version 1.0
 */
public class IriFormatText {

	/**
	 * Cuts the begin and end quotes out of the string. Used with owlapi label annotations.
	 */
	public static String cutQuote(String cuttingObject) {
		if (cuttingObject == null) {
			return null;
		}

		int firstIndex = cuttingObject.indexOf("\"");
		int lastIndex = cuttingObject.lastIndexOf("\"");

		if (firstIndex != -1 && firstIndex != lastIndex) {
			return cuttingObject.substring(firstIndex + 1, lastIndex);
		} else {
			return cuttingObject;
		}
	}


	public static String extractNameFromIRI(String iri) {
		String name;

		if (iri.contains("#")) {
			// IRI contains a # -> take name behind #
			name = iri.substring(iri.indexOf("#") + 1);
		} else {
			if (iri.contains("/")) {
				// IRI contains / -> take name behind the last /
				name = iri.substring(iri.lastIndexOf("/") + 1);
			} else {
				// No suitable name found.
				name = "";
			}
		}

		return name;
	}
}
