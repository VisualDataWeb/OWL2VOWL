package de.uni_stuttgart.vis.vowl.owl2vowl.model;

import java.util.ArrayList;
import java.util.List;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.classes.BaseClass;


public abstract class BaseEntity {
	private String name;
	private String comment;
	private String type;
	private String iri;
	private String id;
	private String definedBy;
	private String owlVersion;
	private List<String> attributes;
	private List<BaseEntity> subClasses;
	private List<BaseEntity> superClasses;
	private List<BaseEntity> disjoints;

	/**
	 * Creates a new class object in owl form.
	 * Used for converting the RDF/XML and OWL/XML to the WebVOWL format.
	 */
	public BaseEntity() {
		attributes = new ArrayList<>();
		subClasses = new ArrayList<>();
		superClasses = new ArrayList<>();
		disjoints = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIri() {
		return iri;
	}

	public void setIri(String iri) {
		this.iri = iri;
	}

	public List<String> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}

	public List<BaseEntity> getSubClasses() {
		return subClasses;
	}

	public void setSubClasses(List<BaseEntity> subClasses) {
		this.subClasses = subClasses;
	}

	public List<BaseEntity> getSuperClasses() {
		return superClasses;
	}

	public void setSuperClasses(List<BaseEntity> superClasses) {
		this.superClasses = superClasses;
	}

	public List<BaseEntity> getDisjoints() {
		return disjoints;
	}

	public void setDisjoints(List<BaseEntity> disjoints) {
		this.disjoints = disjoints;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOwlVersion() {
		return owlVersion;
	}

	public void setOwlVersion(String owlVersion) {
		this.owlVersion = owlVersion;
	}

	public String getDefinedBy() {
		return definedBy;
	}

	public void setDefinedBy(String definedBy) {
		this.definedBy = definedBy;
	}

	@Override public String toString() {
		return "BaseEntity{" +
			"name='" + name + '\'' +
			", comment='" + comment + '\'' +
			", type='" + type + '\'' +
			", iri='" + iri + '\'' +
			", id='" + id + '\'' +
			", definedBy='" + definedBy + '\'' +
			", owlVersion='" + owlVersion + '\'' +
			", attributes=" + attributes +
			", subClasses=" + subClasses +
			", superClasses=" + superClasses +
			", disjoints=" + disjoints +
			'}';
	}
}
