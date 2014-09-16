/*
 * OntologyInfo.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model;

/**
 * This class saves all information of the processed ontology.
 */
public class OntologyInfo {
	private String author;
	private String iri;
	private String version;
	private String title;
	private String description;
	private String seeAlso;
	private String issued;
	private String license;
	private String rdfsLabel;

	public OntologyInfo() {
		super();
	}

	public String getSeeAlso() {
		return seeAlso;
	}

	public void setSeeAlso(String seeAlso) {
		this.seeAlso = seeAlso;
	}

	public String getIssued() {
		return issued;
	}

	public void setIssued(String issued) {
		this.issued = issued;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getRdfsLabel() {
		return rdfsLabel;
	}

	public void setRdfsLabel(String rdfsLabel) {
		this.rdfsLabel = rdfsLabel;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
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
}
