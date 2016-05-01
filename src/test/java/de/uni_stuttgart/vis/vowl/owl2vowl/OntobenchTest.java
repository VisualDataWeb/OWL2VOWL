package de.uni_stuttgart.vis.vowl.owl2vowl;

import org.junit.Test;

/**
 * Crash test class to check if OWL2VOWL crashes when converting all possible constructs created with OntoBench.
 */
public class OntobenchTest {

	@Test
	public void crashTest() {
		new Owl2Vowl(getClass().getResourceAsStream("/full_ontobench_test.ttl")).getJsonAsString();
	}
}
