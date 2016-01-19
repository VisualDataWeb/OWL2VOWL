package de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ComparisonHelperTest {

	@Test
	public void testExtractBaseIRI() throws Exception {
		String resultIri = "http://test.de/working";

		String testIri1 = "http://test.de/working#worker";
		assertThat(ComparisonHelper.extractBaseIRI(testIri1), is(resultIri));

		String testIri2 = "http://test.de/working/worker/";
		assertThat(ComparisonHelper.extractBaseIRI(testIri2), is(resultIri));

		String testIri3 = "http://test.de/working/worker";
		assertThat(ComparisonHelper.extractBaseIRI(testIri3), is(resultIri));

		String testIri4 = "http://test.de/working#";
		assertThat(ComparisonHelper.extractBaseIRI(testIri4), is(resultIri));
	}
}
