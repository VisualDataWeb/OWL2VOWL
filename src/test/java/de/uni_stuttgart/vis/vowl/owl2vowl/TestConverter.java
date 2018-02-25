package de.uni_stuttgart.vis.vowl.owl2vowl;

import de.uni_stuttgart.vis.vowl.owl2vowl.converter.InputStreamConverter;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import java.io.InputStream;

public class TestConverter extends InputStreamConverter {

	public TestConverter(InputStream ontology) {
		super(ontology);
	}

	public VowlData getConvertedData() {
		if (!this.initialized) {
			this.convert();
		}
		return this.vowlData;
	}
}
