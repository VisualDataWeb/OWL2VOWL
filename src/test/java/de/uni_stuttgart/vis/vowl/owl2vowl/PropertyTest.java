package de.uni_stuttgart.vis.vowl.owl2vowl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.AbstractProperty;
import org.junit.Test;


public class PropertyTest {

	@Test
	public void shouldProcessAnonymousInverseRelationCorrectly() {
		TestConverter converter = new TestConverter(getClass().getResourceAsStream("/inverse-anonym-test.ttl"));
		VowlData data = converter.getConvertedData();

		AbstractProperty propertyA = data.getPropertyForIri(Constants.getIRIWithTestNamespace("propertyA"));
		AbstractProperty propertyB = data.getPropertyForIri(Constants.getIRIWithTestNamespace("propertyB"));

		assertThat(propertyA.getJsonDomain(), is(Constants.getIRIWithTestNamespace("classA")));
		assertThat(propertyA.getJsonRange(), is(Constants.getIRIWithTestNamespace("classB")));

		assertThat(propertyB.getJsonDomain(), is(Constants.getIRIWithTestNamespace("classB")));
		assertThat(propertyB.getJsonRange(), is(Constants.getIRIWithTestNamespace("classA")));
	}
}
