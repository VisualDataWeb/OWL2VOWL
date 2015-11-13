/*
 * JsonGeneratorVisitor.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.export;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Standard_Iris;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.annotation.Annotation;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.VowlClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.VowlThing;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.datatypes.*;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.AbstractProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.TypeOfProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.VowlDatatypeProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.VowlObjectProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.individuals.VowlIndividual;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.IRI;

import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO Gemeinsamkeiten auslagern
 */
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

	protected void populateJsonRoot() {
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

	@Override
	public void visit(VowlThing vowlThing) {
		Map<String, Object> thingObject = new HashMap<>();
		thingObject.put("id", vowlData.getIdForEntity(vowlThing));
		thingObject.put("type", vowlThing.getType());

		_class.add(thingObject);

		Map<String, Object> thingAttributeObject = new HashMap<>();

		// TODO
		thingAttributeObject.put("id", vowlData.getIdForEntity(vowlThing));
		thingAttributeObject.put("label", 0);
		thingAttributeObject.put("iri", Standard_Iris.OWL_THING_CLASS_URI);

		classAttribute.add(thingAttributeObject);
	}

	@Override
	public void visit(VowlClass vowlClass) {
		Map<String, Object> classObject = new HashMap<>();
		classObject.put("id", vowlData.getIdForEntity(vowlClass));
		classObject.put("type", vowlClass.getType());

		_class.add(classObject);

		Map<String, Object> classAttributeObject = new HashMap<>();

		classAttributeObject.put("id", vowlData.getIdForEntity(vowlClass));
		classAttributeObject.put("label", getLabelsFromAnnotations(vowlClass.getAnnotations().getLabels()));
		classAttributeObject.put("iri", vowlClass.getIri().toString());
		classAttributeObject.put("description", getLabelsFromAnnotations(vowlClass.getAnnotations().getDescription()));
		classAttributeObject.put("comment", getLabelsFromAnnotations(vowlClass.getAnnotations().getComments()));
		classAttributeObject.put("superClasses", getListWithIds(vowlClass.getSuperEntities()));
		classAttributeObject.put("subClasses", getListWithIds(vowlClass.getSubEntities()));
		classAttributeObject.put("annotations", vowlClass.getAnnotations().getIdentifierToAnnotation());
		classAttributeObject.put("union", getListWithIds(vowlClass.getElementsOfUnion()));
		classAttributeObject.put("intersection", getListWithIds(vowlClass.getElementOfIntersection()));
		classAttributeObject.put("attributes", vowlClass.getAttributes());
		classAttributeObject.put("equivalent", getListWithIds(vowlClass.getEquivalentElements()));
		classAttributeObject.put("complement", getListWithIds(vowlClass.getComplements()));
		classAttributeObject.put("instances", vowlClass.getInstances().size());
		classAttributeObject.put("individuals", createIndividualsJson(vowlClass.getIndividuals()));

		classAttribute.add(classAttributeObject);
	}

	private Object createIndividualsJson(Set<IRI> individuals) {
		List<Object> individualList = new ArrayList<>();

		for (IRI individualIri : individuals) {
			VowlIndividual individual = vowlData.getIndividualMap().get(individualIri);

			Map<String, Object> fields = new HashMap<>();
			fields.put("iri", individual.getIri().toString());
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

		Map<String, Object> objectMap = new HashMap<>();
		objectMap.put("id", vowlData.getIdForEntity(vowlLiteral));
		objectMap.put("type", reference.getType());

		_class.add(objectMap);

		Map<String, Object> attributeMap = new HashMap<>();

		attributeMap.put("id", vowlData.getIdForEntity(vowlLiteral));
		attributeMap.put("label", getLabelsFromAnnotations(reference.getAnnotations().getLabels()));
		attributeMap.put("iri", vowlLiteral.getGenericIri());

		classAttribute.add(attributeMap);
	}

	@Override
	public void visit(VowlDatatype vowlDatatype) {
		if (!(vowlDatatype instanceof DatatypeReference)) {
			// Skip datatype if it's not a reference node.
			return;
		}

		AbstractDatatype reference = vowlData.getDatatypeForIri(((DatatypeReference) vowlDatatype).getReferencedIri());

		Map<String, Object> classObject = new HashMap<>();
		classObject.put("id", vowlData.getIdForEntity(vowlDatatype));
		classObject.put("type", reference.getType());

		_class.add(classObject);

		Map<String, Object> classAttributeObject = new HashMap<>();

		// TODO
		classAttributeObject.put("id", vowlData.getIdForEntity(vowlDatatype));
		classAttributeObject.put("label", getLabelsFromAnnotations(reference.getAnnotations().getLabels()));
		classAttributeObject.put("iri", reference.getIri().toString());

		/*
		classAttributeObject.put("description", getLabelsFromAnnotations(vowlDatatype.getAnnotations().getDescription()));
		classAttributeObject.put("comment", getLabelsFromAnnotations(vowlDatatype.getAnnotations().getComments()));
		// TODO nötig?
		classAttributeObject.put("isDefinedBy", 0);
		classAttributeObject.put("owlVersion", 0);
		classAttributeObject.put("superClasses", getListWithIds(vowlDatatype.getSuperEntities()));
		classAttributeObject.put("subClasses", getListWithIds(vowlDatatype.getSubEntities()));
		classAttributeObject.put("annotations", vowlDatatype.getAnnotations().getIdentifierToAnnotation());
		classAttributeObject.put("union", getListWithIds(vowlDatatype.getElementsOfUnion()));
		classAttributeObject.put("intersection", getListWithIds(vowlDatatype.getElementOfIntersection()));
		classAttributeObject.put("attributes", vowlDatatype.getAttributes());
		classAttributeObject.put("equivalent", getListWithIds(vowlDatatype.getEquivalentElements()));
		// TODO can a complement not be a list?
		classAttributeObject.put("complement", getListWithIds(vowlDatatype.getComplements()));
		*/

		classAttribute.add(classAttributeObject);
	}

	@Override
	public void visit(VowlObjectProperty vowlObjectProperty) {
		addProperty(vowlObjectProperty);
	}

	@Override
	public void visit(VowlDatatypeProperty vowlDatatypeProperty) {
		addProperty(vowlDatatypeProperty);
	}

	protected void addProperty(AbstractProperty property) {
		if (property.getDomains().isEmpty() || property.getRanges().isEmpty()) {
			logger.info("Domain or range is empty in property: " + property);
			return;
		}

		Map<String, Object> object = new HashMap<>();
		object.put("id", vowlData.getIdForEntity(property));
		object.put("type", property.getType());

		propertyList.add(object);

		Map<String, Object> propertyAttributes = new HashMap<>();
		propertyAttributes.put("domain", vowlData.getIdForIri(property.getJsonDomain()));
		propertyAttributes.put("range", vowlData.getIdForIri(property.getJsonRange()));
		propertyAttributes.put("id", vowlData.getIdForEntity(property));
		propertyAttributes.put("label", getLabelsFromAnnotations(property.getAnnotations().getLabels()));
		propertyAttributes.put("iri", property.getIri().toString());
		propertyAttributes.put("description", getLabelsFromAnnotations(property.getAnnotations().getDescription()));
		propertyAttributes.put("comment", getLabelsFromAnnotations(property.getAnnotations().getComments()));
		propertyAttributes.put("attributes", property.getAttributes());
		propertyAttributes.put("annotations", property.getAnnotations().getIdentifierToAnnotation());
		propertyAttributes.put("inverse", getIdForIri(property.getInverse()));
		propertyAttributes.put("superproperty", getListWithIds(property.getSuperEntities()));
		propertyAttributes.put("subproperty", getListWithIds(property.getSubEntities()));
		propertyAttributes.put("equivalent", getListWithIds(property.getEquivalentElements()));
		propertyAttributes.put("minCardinality", getCardinality(property.getMinCardinality()));
		propertyAttributes.put("maxCardinality", getCardinality(property.getMaxCardinality()));
		propertyAttributes.put("cardinality", getCardinality(property.getExactCardinality()));

		propertyAttributeList.add(propertyAttributes);
	}

	@Override
	public void visit(VowlIndividual vowlIndividual) {

	}

	@Override
	public void visit(TypeOfProperty typeOfProperty) {
		if (typeOfProperty.getDomains().isEmpty() || typeOfProperty.getRanges().isEmpty()) {
			logger.info("Domain or range is empty in typeof property: " + typeOfProperty);
			return;
		}

		Map<String, Object> object = new HashMap<>();
		object.put("id", vowlData.getIdForEntity(typeOfProperty));
		object.put("type", typeOfProperty.getType());

		propertyList.add(object);

		Map<String, Object> propertyAttributes = new HashMap<>();
		propertyAttributes.put("domain", vowlData.getIdForIri(typeOfProperty.getJsonDomain()));
		propertyAttributes.put("range", vowlData.getIdForIri(typeOfProperty.getJsonRange()));
		propertyAttributes.put("id", vowlData.getIdForEntity(typeOfProperty));
		propertyAttributes.put("label", getLabelsFromAnnotations(typeOfProperty.getAnnotations().getLabels()));
		propertyAttributes.put("iri", typeOfProperty.getIri().toString());

		propertyAttributeList.add(propertyAttributes);
	}

	protected List<String> getListWithIds(Collection<IRI> iriList) {
		return iriList.stream().map(iri -> String.valueOf(vowlData.getIdForIri(iri))).collect(Collectors.toList());
	}

	protected String getIdForIri(IRI iri) {
		if (iri == null) {
			return null;
		}

		return vowlData.getIdForIri(iri);
	}

	public static Map<String, String> getLabelsFromAnnotations(Collection<Annotation> annotations) {
		Map<String, String> languageToValue = new HashMap<>();

		for (Annotation annotation : annotations) {
			languageToValue.put(annotation.getLanguage(), annotation.getValue());
		}

		return languageToValue;
	}

	/**
	 *
	 * @return Empty string if value is <= 0 else the value.
	 */
	protected String getCardinality(Integer value) {
		if (value <= 0) {
			return StringUtils.EMPTY;
		}

		return value.toString();
	}
}
