/*
 * OntologyInfo.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model;

import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.Annotation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class saves all information of the processed ontology.
 */
public class OntologyInfo {
	private static final Logger logger = LogManager.getLogger(OntologyInfo.class);
	private List<String> author;
	private String iri;
	private String version;
	private String title;
	private String description;
	private List<String> seeAlso;
	private List<String> issued;
	private List<String> license;
	private List<String> rdfsLabel;
	private Map<String, String> languageToTitle;
	private Map<String, String> languageToDescription;
	private Map<String, String> languageToLabel;
	private Map<String, List<Annotation>> others;

	public OntologyInfo() {
		super();
		author = new ArrayList<String>();
		seeAlso = new ArrayList<String>();
		issued = new ArrayList<String>();
		license = new ArrayList<String>();
		languageToLabel = new HashMap<String, String>();
		languageToDescription = new HashMap<String, String>();
		languageToTitle = new HashMap<String, String>();
		rdfsLabel = new ArrayList<String>();
		others = new HashMap<String, List<Annotation>>();
	}

	public Map<String, List<Annotation>> getOthers() {
		return others;
	}

	public void addAnnotatin(String key, Annotation annotation) {
		if (key == null || annotation == null) {
			logger.error("An ontology info annotation with key: " + key + " is null!");
			return;
		}

		if (others.containsKey(key)) {
			others.get(key).add(annotation);
		} else {
			List<Annotation> annotations = new ArrayList<Annotation>();
			annotations.add(annotation);
			others.put(key, annotations);
		}
	}

	public List<String> getAuthor() {
		return author;
	}

	public void setAuthor(List<String> author) {
		this.author = author;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getSeeAlso() {
		return seeAlso;
	}

	public void setSeeAlso(List<String> seeAlso) {
		this.seeAlso = seeAlso;
	}

	public List<String> getIssued() {
		return issued;
	}

	public void setIssued(List<String> issued) {
		this.issued = issued;
	}

	public List<String> getLicense() {
		return license;
	}

	public void setLicense(List<String> license) {
		this.license = license;
	}

	public List<String> getRdfsLabel() {
		return rdfsLabel;
	}

	public Map<String, String> getLanguageToTitle() {
		return languageToTitle;
	}

	public void setLanguageToTitle(Map<String, String> languageToTitle) {
		this.languageToTitle = languageToTitle;
	}

	public Map<String, String> getLanguageToDescription() {
		return languageToDescription;
	}

	public void setLanguageToDescription(Map<String, String> languageToDescription) {
		this.languageToDescription = languageToDescription;
	}

	public Map<String, String> getLanguageToLabel() {
		return languageToLabel;
	}

	public void setLanguageToLabel(Map<String, String> languageToLabel) {
		this.languageToLabel = languageToLabel;
	}

	public void addAuthor(String s) {
		author.add(s);
	}

	public void addIssued(String s) {
		issued.add(s);
	}

	public void addLicense(String s) {
		license.add(s);
	}

	public void addSeeAlso(String s) {
		seeAlso.add(s);
	}

	public void addLabel(String s) {
		rdfsLabel.add(s);
	}
}
