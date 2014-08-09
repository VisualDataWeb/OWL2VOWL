/*
 * Dummy.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.pipes;

/**
 * @author Eduard Marbach
 * @version 1.0
 */
public class FormatText {
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
}
