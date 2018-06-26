/*
 * OntologyInformation.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.ontology;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.annotation.Annotation;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.annotation.Annotations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class OntologyInformation {
	private static final Logger logger = LogManager.getLogger(OntologyInformation.class);
	protected Set<String> authors = new HashSet<>();
	protected String iri;
	protected String version;
	protected List<Annotation> titles = new ArrayList<>();
	protected Annotations annotations = new Annotations();

	public OntologyInformation() {
	}

	public Logger getLogger() {
		return logger;
	}
	
	public List<Annotation> getTitles() {
		return titles;
	}

	public void setTitles(List<Annotation> titles) {
		this.titles = titles;
	}

	public void addTitle(Annotation annotation) {
		titles.add(annotation);
	}

	public Annotations getAnnotations() {
		return annotations;
	}

	public Set<String> getAuthors() {
		return authors;
	}

	public void addAuthor(String author) {
		authors.add(author);
	}

	public String getIri() {
		return iri;
	}

	public void setIri(String iri) {
		this.iri = iri;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
