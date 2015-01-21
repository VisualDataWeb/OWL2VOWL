/*
 * ComparisonHelperTest.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.helper;

import de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper.ComparisonHelper;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class ComparisonHelperTest {

	private boolean compare(String elementIri, String ontologyIri) {
		return ComparisonHelper.hasDifferentNameSpace(elementIri, ontologyIri);
	}

	@Test
	public void testLiteralAtEndOfOntologyIri() {
		assertFalse(compare("http://gs1.org/voc/FasteningTypeCode", "http://gs1.org/voc/"));
		assertFalse(compare("http://xmlns.com/foaf/0.1/PersonalProfileDocument", "http://xmlns.com/foaf/0.1/"));
	}

	@Test
	public void testHashAndLiteralAtEndOfOntologyIri() {
		assertFalse(compare("http://ontovibe.visualdataweb.org#PlainClass", "http://ontovibe.visualdataweb.org"));
	}

	@Test
	public void testHashAtEndOfOntologyIri() {
		assertFalse(compare("http://purl.org/muto/core#", "http://purl.org/muto/core"));
	}

	@Test
	public void testEqualToOntologyIri() {
		assertFalse(compare("http://xmlns.com/foaf/0.1/", "http://xmlns.com/foaf/0.1/"));
	}
}
