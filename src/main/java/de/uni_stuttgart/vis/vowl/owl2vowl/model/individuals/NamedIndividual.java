/*
 * NamedIndividual.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.individuals;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Vowl_Lang;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.Annotation;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class NamedIndividual {
	protected String iri;
	protected Map<String, String> labels;
	protected Map<String, List<Annotation>> annotations;
	public NamedIndividual() {
		labels = new LinkedHashMap<String, String>();
		annotations = new HashMap<String, List<Annotation>>();
	}

	public String getIri() {
		return iri;
	}

	public void setIri(String iri) {
		this.iri = iri;
	}

	public Map<String, String> getLabels() {
		return labels;
	}

	public void setLabels(Map<String, String> labels) {
		this.labels = labels;
	}

	public Map<String, List<Annotation>> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(Map<String, List<Annotation>> annotations) {
		this.annotations = annotations;
	}

	@Override
	public String toString() {
		return "NamedIndividual{" +
				"labels=" + labels +
				"annotations=" + annotations +
				"} ";
	}

	public void setName(String individualName) {
		labels.put(Vowl_Lang.LANG_DEFAULT, individualName);
	}
}
