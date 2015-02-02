/*
 * JsonGeneratorVisitor.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.export;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.BaseEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.BaseEdge;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties.*;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.*;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.BaseDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.RdfsDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.RdfsLiteral;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class JsonGeneratorVisitorImpl implements JsonGeneratorVisitor {
	private Map<String, Object> entityJson;
	private Map<String, Object> entityAttributes;
	private boolean failure = false;

	public JsonGeneratorVisitorImpl() {
		entityJson = new LinkedHashMap<String, Object>();
		entityAttributes = new LinkedHashMap<String, Object>();
	}

	public Map<String, Object> getEntityJson() {
		return entityJson;
	}

	public Map<String, Object> getEntityAttributes() {
		return entityAttributes;
	}

	public boolean isFailure() {
		return failure;
	}

	@Override
	public void visit(BaseEntity entity) {
		entityJson.put("id", entity.getId());
		entityJson.put("type", entity.getType());

		List<Object> subClasses = new ArrayList<Object>();
		List<Object> superClasses = new ArrayList<Object>();

		for (BaseNode current : entity.getSubClasses()) {
			subClasses.add(current.getId());
		}

		// Apply super classes
		for (BaseNode current : entity.getSuperClasses()) {
			superClasses.add(current.getId());
		}

		entityAttributes.put("id", entity.getId());
		entityAttributes.put("label", entity.getLabels());
		entityAttributes.put("uri", entity.getIri());
		entityAttributes.put("comment", entity.getComments());
		entityAttributes.put("isDefinedBy", entity.getDefinedBy());
		entityAttributes.put("owlVersion", entity.getOwlVersion());
		entityAttributes.put("attributes", entity.getAttributes());
		entityAttributes.put("subClasses", subClasses);
		entityAttributes.put("superClasses", superClasses);
		entityAttributes.put("annotations", entity.getAnnotations());
	}

	@Override
	public void visit(BaseEdge entity) {
		if (entity.getDomain() == null || entity.getRange() == null) {
			failure = true;
			return;
		}

		entityAttributes.put("domain", entity.getDomain().getId());
		entityAttributes.put("range", entity.getRange().getId());
	}

	@Override
	public void visit(BaseProperty entity) {
		List<Object> equivalent = new ArrayList<Object>();
		List<Object> subProperty = new ArrayList<Object>();
		List<Object> superProperty = new ArrayList<Object>();
		List<Object> disjoints = new ArrayList<Object>();

		// Apply sub props
		for (String current : entity.getSubProperties()) {
			subProperty.add(current);
		}

		// Apply super props
		for (String current : entity.getSuperProperties()) {
			superProperty.add(current);
		}

		// Apply equivalents
		for (String current : entity.getEquivalents()) {
			equivalent.add(current);
		}

		// Apply disjoints
		for (String current : entity.getDisjoints()) {
			disjoints.add(current);
		}

		entityAttributes.put("inverse", entity.getInverseID());
		entityAttributes.put("equivalent", equivalent);
		entityAttributes.put("subproperty", subProperty);
		entityAttributes.put("superproperty", superProperty);
		entityAttributes.put("disjoint", disjoints);

		// Cardinality
		int exact = entity.getExactCardinality();
		int min = entity.getMinCardinality();
		int max = entity.getMaxCardinality();

		if (exact != -1) {
			entityAttributes.put("cardinality", entity.getExactCardinality());
		}

		if (min != -1) {
			entityAttributes.put("minCardinality", entity.getMinCardinality());
		}

		if (max != -1) {
			entityAttributes.put("maxCardinality", entity.getMaxCardinality());
		}
	}

	@Override
	public void visit(DisjointProperty entity) {

	}

	@Override
	public void visit(OwlDatatypeProperty entity) {

	}

	@Override
	public void visit(OwlObjectProperty entity) {

	}

	@Override
	public void visit(SubClassProperty entity) {

	}

	@Override
	public void visit(BaseNode entity) {

	}

	@Override
	public void visit(BaseClass entity) {
		entityAttributes.put("instances", entity.getNumberOfIndividuals());
	}

	@Override
	public void visit(ExternalClass entity) {

	}

	@Override
	public void visit(OwlClass entity) {

	}

	@Override
	public void visit(OwlComplementOf entity) {

	}

	@Override
	public void visit(OwlDeprecatedClass entity) {

	}

	@Override
	public void visit(OwlEquivalentClass entity) {
		List<Object> equivalent = new ArrayList<Object>();

		for (BaseNode current : entity.getEquivalentClasses()) {
			equivalent.add(current.getId());
		}

		entityAttributes.put("equivalent", equivalent);
	}

	@Override
	public void visit(OwlIntersectionOf entity) {

	}

	@Override
	public void visit(OwlThing entity) {

	}

	@Override
	public void visit(OwlUnionOf entity) {

	}

	@Override
	public void visit(RdfsClass entity) {

	}

	@Override
	public void visit(RdfsResource entity) {

	}

	@Override
	public void visit(SpecialClass entity) {
		List<Object> union = new ArrayList<Object>();
		List<Object> intersection = new ArrayList<Object>();
		List<Object> complement = new ArrayList<Object>();

		for (BaseNode current : entity.getUnions()) {
			union.add(current.getId());
		}

		for (BaseNode current : entity.getIntersections()) {
			intersection.add(current.getId());
		}

		for (BaseNode current : entity.getComplements()) {
			complement.add(current.getId());
		}

		entityAttributes.put("union", union);
		entityAttributes.put("intersection", intersection);
		entityAttributes.put("complement", complement);
	}

	@Override
	public void visit(BaseDatatype entity) {

	}

	@Override
	public void visit(RdfsDatatype entity) {

	}

	@Override
	public void visit(RdfsLiteral entity) {

	}
}
