package de.uni_stuttgart.vis.vowl.owl2vowl.export;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.VowlAttribute;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.annotation.Annotation;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.AbstractEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.HasReference;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.VowlClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.VowlThing;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.datatypes.*;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.AbstractProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.TypeOfProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.VowlDatatypeProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.VowlObjectProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.individuals.VowlIndividual;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.IRI;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonGeneratorVisitorImpl implements JsonGeneratorVisitor {
	private final VowlData vowlData;
	private final Map<String, Object> root;
	private List<Object> _class;
	private List<Object> classAttribute;
	private List<Object> datatype;
	private List<Object> datatypeAttribute;
	private List<Object> propertyList;
	private List<Object> propertyAttributeList;
	private Logger logger = LogManager.getLogger(JsonGeneratorVisitorImpl.class);

	public JsonGeneratorVisitorImpl(VowlData vowlData, Map<String, Object> root) {
		this.vowlData = vowlData;
		this.root = root;
		populateJsonRoot();
	}

	public static Map<String, String> getLabelsFromAnnotations(Collection<Annotation> annotations) {
		Map<String, String> languageToValue = new HashMap<>();

		for (Annotation annotation : annotations) {
			languageToValue.put(annotation.getLanguage(), annotation.getValue());
		}

		return languageToValue;
	}

	private void populateJsonRoot() {
		_class = new ArrayList<>();
		classAttribute = new ArrayList<>();
		datatype = new ArrayList<>();
		datatypeAttribute = new ArrayList<>();
		propertyList = new ArrayList<>();
		propertyAttributeList = new ArrayList<>();
		root.put("class", _class);
		root.put("classAttribute", classAttribute);
		root.put("datatype", datatype);
		root.put("datatypeAttribute", datatypeAttribute);
		root.put("property", propertyList);
		root.put("propertyAttribute", propertyAttributeList);
	}

	private void addCommonFields(AbstractEntity entity, Map<String, Object> object, Map<String, Object> attributes) {
		object.put("id", vowlData.getIdForEntity(entity));
		object.put("type", entity.getType());

		attributes.put("id", vowlData.getIdForEntity(entity));

		addCommonNonAnonymAttributes(entity, attributes);
	}

	private void addCommonNonAnonymAttributes(AbstractEntity entity, Map<String, Object> attributes) {
		if (!entity.getAttributes().contains(VowlAttribute.ANONYMOUS)) {
			attributes.put("label", getLabelsFromAnnotations(entity.getAnnotations().getLabels()));
			attributes.put("iri", entity.getIri().toString());

			if (entity.getBaseIri() != null) {
				attributes.put("baseIri", entity.getBaseIri().toString());
			}
		}
	}

	@Override
	public void visit(VowlThing vowlThing) {
		Map<String, Object> object = new HashMap<>();
		Map<String, Object> attributes = new HashMap<>();

		addCommonFields(vowlThing, object, attributes);
		attributes.put("iri", VowlThing.GENERIC_THING_IRI.toString());

		_class.add(object);
		classAttribute.add(attributes);
	}

	@Override
	public void visit(VowlClass vowlClass) {
		Map<String, Object> object = new HashMap<>();
		Map<String, Object> attributes = new HashMap<>();

		addCommonFields(vowlClass, object, attributes);
		attributes.put("description", getLabelsFromAnnotations(vowlClass.getAnnotations().getDescription()));
		attributes.put("comment", getLabelsFromAnnotations(vowlClass.getAnnotations().getComments()));
		attributes.put("superClasses", getListWithIds(vowlClass.getSuperEntities()));
		attributes.put("subClasses", getListWithIds(vowlClass.getSubEntities()));
		attributes.put("annotations", vowlClass.getAnnotations().getIdentifierToAnnotation());
		attributes.put("union", getListWithIds(vowlClass.getElementsOfUnion()));
		attributes.put("disjointUnion", getListWithIds(vowlClass.getDisjointUnion()));
		attributes.put("intersection", getListWithIds(vowlClass.getElementOfIntersection()));
		attributes.put("attributes", vowlClass.getAttributes());
		attributes.put("equivalent", getListWithIds(vowlClass.getSortedEquivalents()));
		attributes.put("complement", getListWithIds(vowlClass.getComplements()));
		attributes.put("instances", vowlClass.getInstances().size());
		attributes.put("individuals", createIndividualsJson(vowlClass.getIndividuals()));
		_class.add(object);
		classAttribute.add(attributes);
	}

	private Object createIndividualsJson(Set<IRI> individuals) {
		List<Object> individualList = new ArrayList<>();

		for (IRI individualIri : individuals) {
			VowlIndividual individual = vowlData.getIndividualMap().get(individualIri);

			Map<String, Object> fields = new HashMap<>();
			fields.put("iri", individual.getIri().toString());
			fields.put("baseIri", individual.getBaseIri().toString());
			fields.put("labels", getLabelsFromAnnotations(individual.getAnnotations().getLabels()));
			fields.put("description", getLabelsFromAnnotations(individual.getAnnotations().getDescription()));
			fields.put("comment", getLabelsFromAnnotations(individual.getAnnotations().getComments()));
			fields.put("annotations", individual.getAnnotations().getIdentifierToAnnotation());

			individualList.add(fields);
		}

		return individualList;
	}

	@Override
	public void visit(VowlLiteral vowlLiteral) {
		if (!(vowlLiteral instanceof LiteralReference)) {
			// Skip literal if it's not a reference node.
			return;
		}

		AbstractDatatype reference = vowlData.getDatatypeForIri(((LiteralReference) vowlLiteral).getReferencedIri());
		Map<String, Object> object = new HashMap<>();
		Map<String, Object> attributes = new HashMap<>();

		object.put("id", vowlData.getIdForEntity(vowlLiteral));
		object.put("type", reference.getType());

		attributes.put("id", vowlData.getIdForEntity(vowlLiteral));
		attributes.put("label", getLabelsFromAnnotations(reference.getAnnotations().getLabels()));
		attributes.put("iri", vowlLiteral.getGenericIri());

		_class.add(object);
		classAttribute.add(attributes);
	}

	@Override
	public void visit(VowlDatatype vowlDatatype) {
		if (!(vowlDatatype instanceof DatatypeReference)) {
			// Skip datatype if it's not a reference node.
			return;
		}

		AbstractDatatype reference = vowlData.getDatatypeForIri(((DatatypeReference) vowlDatatype).getReferencedIri());
		Map<String, Object> object = new HashMap<>();
		Map<String, Object> attributes = new HashMap<>();

		object.put("id", vowlData.getIdForEntity(vowlDatatype));
		object.put("type", reference.getType());

		attributes.put("id", vowlData.getIdForEntity(vowlDatatype));
		attributes.put("label", getLabelsFromAnnotations(reference.getAnnotations().getLabels()));
		attributes.put("iri", reference.getIri().toString());
		attributes.put("baseIri", reference.getBaseIri().toString());

		_class.add(object);
		classAttribute.add(attributes);
	}

	@Override
	public void visit(VowlObjectProperty vowlObjectProperty) {
		addProperty(vowlObjectProperty);
	}

	@Override
	public void visit(VowlDatatypeProperty vowlDatatypeProperty) {
		addProperty(vowlDatatypeProperty);
	}

	private void addProperty(AbstractProperty property) {
		if (property instanceof HasReference) {
			addReferenceProperty(property);
			return;
		}

		if (property.getDomains().isEmpty() || property.getRanges().isEmpty()) {
			logger.info("Domain or range is empty in property: " + property);
			return;
		}

		Map<String, Object> object = new HashMap<>();
		Map<String, Object> attributes = new HashMap<>();

		addCommonFields(property, object, attributes);
		attributes.put("domain", vowlData.getIdForIri(property.getJsonDomain()));
		attributes.put("range", vowlData.getIdForIri(property.getJsonRange()));
		attributes.put("description", getLabelsFromAnnotations(property.getAnnotations().getDescription()));
		attributes.put("comment", getLabelsFromAnnotations(property.getAnnotations().getComments()));
		attributes.put("attributes", property.getAttributes());
		attributes.put("annotations", property.getAnnotations().getIdentifierToAnnotation());
		attributes.put("inverse", getIdForIri(property.getInverse()));
		attributes.put("superproperty", getListWithIds(property.getSuperEntities()));
		attributes.put("subproperty", getListWithIds(property.getSubEntities()));
		attributes.put("equivalent", getListWithIds(property.getSortedEquivalents()));
		attributes.put("minCardinality", getCardinality(property.getMinCardinality()));
		attributes.put("maxCardinality", getCardinality(property.getMaxCardinality()));
		attributes.put("cardinality", getCardinality(property.getExactCardinality()));
		propertyList.add(object);
		propertyAttributeList.add(attributes);
	}

	private void addReferenceProperty(AbstractProperty property) {
		if (property.getDomains().isEmpty() || property.getRanges().isEmpty()) {
			logger.info("Domain or range is empty in property: " + property);
			return;
		}

		Map<String, Object> object = new HashMap<>();
		Map<String, Object> attributes = new HashMap<>();
		AbstractProperty reference = vowlData.getPropertyForIri(((HasReference) property).getReferenceIri());

		object.put("id", vowlData.getIdForEntity(property));
		object.put("type", property.getType());

		attributes.put("id", vowlData.getIdForEntity(property));

		addCommonNonAnonymAttributes(reference, attributes);

		attributes.put("domain", vowlData.getIdForIri(property.getJsonDomain()));
		attributes.put("range", vowlData.getIdForIri(property.getJsonRange()));
		attributes.put("description", getLabelsFromAnnotations(reference.getAnnotations().getDescription()));
		attributes.put("comment", getLabelsFromAnnotations(reference.getAnnotations().getComments()));
		attributes.put("attributes", Stream.concat(property.getAttributes().stream(), reference.getAttributes().stream()).collect(Collectors.toSet()));
		attributes.put("annotations", reference.getAnnotations().getIdentifierToAnnotation());
		attributes.put("inverse", getIdForIri(reference.getInverse()));
		attributes.put("superproperty", getListWithIds(reference.getSuperEntities()));
		attributes.put("subproperty", getListWithIds(reference.getSubEntities()));
		attributes.put("equivalent", getListWithIds(reference.getSortedEquivalents()));
		attributes.put("minCardinality", getCardinality(property.getMinCardinality()));
		attributes.put("maxCardinality", getCardinality(property.getMaxCardinality()));
		attributes.put("cardinality", getCardinality(property.getExactCardinality()));

		propertyList.add(object);
		propertyAttributeList.add(attributes);
	}

	@Override
	public void visit(VowlIndividual vowlIndividual) {
		// Individuals not needed in json.
	}

	@Override
	public void visit(TypeOfProperty typeOfProperty) {
		if (typeOfProperty.getDomains().isEmpty() || typeOfProperty.getRanges().isEmpty()) {
			logger.info("Domain or range is empty in typeof property: " + typeOfProperty);
			return;
		}

		Map<String, Object> object = new HashMap<>();
		Map<String, Object> attributes = new HashMap<>();

		addCommonFields(typeOfProperty, object, attributes);
		// Special property which should not have a own iri
		attributes.remove("iri");
		attributes.remove("baseIri");
		attributes.put("domain", vowlData.getIdForIri(typeOfProperty.getJsonDomain()));
		attributes.put("range", vowlData.getIdForIri(typeOfProperty.getJsonRange()));

		propertyList.add(object);
		propertyAttributeList.add(attributes);
	}

	private List<String> getListWithIds(Collection<IRI> iriList) {
		return iriList.stream().map(iri -> String.valueOf(vowlData.getIdForIri(iri))).collect(Collectors.toList());
	}

	private String getIdForIri(IRI iri) {
		if (iri == null) {
			return null;
		}

		return vowlData.getIdForIri(iri);
	}

	/**
	 * @return Empty string if value is <= 0 else the value.
	 */
	private String getCardinality(Integer value) {
		if (value <= 0) {
			return StringUtils.EMPTY;
		}

		return value.toString();
	}
}
