/*
 * Dummy.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.export;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.OntologyInfo;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.OntologyMetric;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties.BaseProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.*;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.BaseDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Eduard Marbach
 * @version 1.0
 */
public class JsonGenerator {
	private Map<String, Object> root;
	private List<Object> namespace;
	private Map<String, Object> header;
	private Map<String, Object> metrics;
	private List<Object> _class;
	private List<Object> classAttribute;
	private List<Object> datatype;
	private List<Object> datatypeAttribute;
	private List<Object> objectProperty;
	private List<Object> objectPropertyAttribute;
	private MapData mapData;

	public JsonGenerator() {
		initialize();
	}

	private void initialize() {
		root = new LinkedHashMap<String, Object>();
		namespace = new ArrayList<Object>();
		header = new LinkedHashMap<String, Object>();
		metrics = new LinkedHashMap<String, Object>();
		_class = new ArrayList<Object>();
		classAttribute = new ArrayList<Object>();
		datatype = new ArrayList<Object>();
		datatypeAttribute = new ArrayList<Object>();
		objectProperty = new ArrayList<Object>();
		objectPropertyAttribute = new ArrayList<Object>();

		// Sets root
		root.put("namespace", namespace);
		root.put("header", header);
		root.put("metrics", metrics);
		root.put("class", _class);
		root.put("classAttribute", classAttribute);
		root.put("datatype", datatype);
		root.put("datatypeAttribute", datatypeAttribute);
		root.put("property", objectProperty);
		root.put("propertyAttribute", objectPropertyAttribute);
	}

	public void execute(MapData mapData) throws Exception {
		this.mapData = mapData;
		processNamespace();
		processHeader(mapData.getOntologyInfo());
		processMetrics(mapData.getOntologyMetric());
		processClasses(mapData.getClassMap());
		processDatatypes(mapData.getDatatypeMap());
		processProperties(mapData.getMergedProperties());
		processThings(mapData.getThingMap());
		processUnions(mapData.getUnionMap());
	}

	public void export(Exporter exporter) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		//mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
		mapper.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);
		exporter.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root));
	}

	// TODO
	public void processNamespace() {
		// Namespace only sample because there wasn't one
		Map<String, Object> sampleNamespace = new LinkedHashMap<String, Object>();
		namespace.add(sampleNamespace);
	}

	public void processHeader(OntologyInfo info) {
		// Apply header
		header.put("title", info.getLanguageToTitle());
		header.put("uri", info.getIri());
		header.put("version", info.getVersion());
		header.put("author", info.getAuthor());
		header.put("description", info.getLanguageToDescription());
		header.put("languages", mapData.getAvailableLanguages());
	}

	public void processClasses(Map<String, BaseClass> classes) {
		for (Map.Entry<String, BaseClass> baseClass : classes.entrySet()) {
			BaseClass currentClass = baseClass.getValue();

			Map<String, Object> classJson = new LinkedHashMap<String, Object>();

			List<Object> equivalent = new ArrayList<Object>();
			List<Object> attributes = new ArrayList<Object>();
			List<Object> union = new ArrayList<Object>();
			List<Object> intersection = new ArrayList<Object>();
			List<Object> complement = new ArrayList<Object>();
			List<Object> subClasses = new ArrayList<Object>();
			List<Object> superClasses = new ArrayList<Object>();

			classJson.put("id", currentClass.getId());
			classJson.put("type", currentClass.getType());

			_class.add(classJson);

			Map<String, Object> classAttrJson = new LinkedHashMap<String, Object>();

			classAttrJson.put("id", currentClass.getId());
			classAttrJson.put("label", currentClass.getLabels());
			classAttrJson.put("uri", currentClass.getIri());
			classAttrJson.put("comment", currentClass.getComments());
			classAttrJson.put("instances", currentClass.getNumberOfIndividuals());
			classAttrJson.put("equivalent", equivalent);
			classAttrJson.put("attributes", attributes);
			classAttrJson.put("union", union);
			classAttrJson.put("intersection", intersection);
			classAttrJson.put("complement", complement);
			classAttrJson.put("subClasses", subClasses);
			classAttrJson.put("superClasses", superClasses);

			if (currentClass.getClass() == OwlEquivalentClass.class) {
				OwlEquivalentClass equivalentClass = (OwlEquivalentClass) currentClass;

				for (BaseNode entity : equivalentClass.getEquivalentClasses()) {
					equivalent.add(entity.getId());
				}
			}

			if (currentClass instanceof SpecialClass) {
				SpecialClass equivalentClass = (SpecialClass) currentClass;

				for (BaseNode entity : equivalentClass.getUnions()) {
					union.add(entity.getId());
				}

				for (BaseNode entity : equivalentClass.getIntersections()) {
					intersection.add(entity.getId());
				}

				for (BaseNode entity : equivalentClass.getComplements()) {
					complement.add(entity.getId());
				}
			}

			// Apply attributes
			for (String attribute : currentClass.getAttributes()) {
				attributes.add(attribute);
			}

			// Apply sub classes
			for (BaseNode entity : currentClass.getSubClasses()) {
				subClasses.add(entity.getId());
			}

			// Apply super classes
			for (BaseNode entity : currentClass.getSuperClasses()) {
				superClasses.add(entity.getId());
			}

			classAttribute.add(classAttrJson);
		}

	}

	public void processDatatypes(Map<String, BaseDatatype> datatypes) {
		for (Map.Entry<String, BaseDatatype> baseDatatype : datatypes.entrySet()) {
			BaseDatatype currentDatatype = baseDatatype.getValue();

			Map<String, Object> datatypesJson = new LinkedHashMap<String, Object>();

			List<Object> attributes = new ArrayList<Object>();
			List<Object> subClasses = new ArrayList<Object>();
			List<Object> superClasses = new ArrayList<Object>();

			datatypesJson.put("id", currentDatatype.getId());
			datatypesJson.put("type", currentDatatype.getType());

			datatype.add(datatypesJson);

			Map<String, Object> datatypeAttrJson = new LinkedHashMap<String, Object>();

			datatypeAttrJson.put("id", currentDatatype.getId());
			datatypeAttrJson.put("uri", currentDatatype.getIri());
			datatypeAttrJson.put("label", currentDatatype.getLabels());
			datatypeAttrJson.put("comment", currentDatatype.getComments());
			datatypeAttrJson.put("attributes", attributes);
			datatypeAttrJson.put("subClasses", subClasses);
			datatypeAttrJson.put("superClasses", superClasses);

			// Apply attributes
			for (String attribute : currentDatatype.getAttributes()) {
				attributes.add(attribute);
			}

			// Apply sub classes
			for (BaseNode entity : currentDatatype.getSubClasses()) {
				subClasses.add(entity.getId());
			}

			// Apply super classes
			for (BaseNode entity : currentDatatype.getSuperClasses()) {
				superClasses.add(entity.getId());
			}

			datatypeAttribute.add(datatypeAttrJson);
		}

	}

	public void processThings(Map<String, OwlThing> things) {
		for (Map.Entry<String, OwlThing> baseClass : things.entrySet()) {
			BaseClass currentClass = baseClass.getValue();

			Map<String, Object> classJson = new LinkedHashMap<String, Object>();

			classJson.put("id", currentClass.getId());
			classJson.put("type", currentClass.getType());

			_class.add(classJson);

			Map<String, Object> classAttrJson = new LinkedHashMap<String, Object>();

			classAttrJson.put("id", currentClass.getId());
			classAttrJson.put("label", currentClass.getName());
			classAttrJson.put("uri", currentClass.getIri());

			classAttribute.add(classAttrJson);
		}
	}

	public void processMetrics(OntologyMetric metric) {
		metrics.put("classCount", metric.getClassCount());
		metrics.put("datatypeCount", metric.getDatatypeCount());
		metrics.put("objectPropertyCount", metric.getObjectPropertyCount());
		metrics.put("datatypePropertyCount", metric.getDataPropertyCount());
		metrics.put("propertyCount", metric.getPropertyCount());
		metrics.put("nodeCount", metric.getNodeCount());
		metrics.put("axiomCount", metric.getAxiomCount());
		metrics.put("individualCount", metric.getIndividualCount());
	}

	private void processUnions(Map<String, OwlUnionOf> unionMap) {
		for (Map.Entry<String, OwlUnionOf> baseClass : unionMap.entrySet()) {
			OwlUnionOf currentClass = baseClass.getValue();

			Map<String, Object> classJson = new LinkedHashMap<String, Object>();

			List<Object> attributes = new ArrayList<Object>();
			List<Object> union = new ArrayList<Object>();

			classJson.put("id", currentClass.getId());
			classJson.put("type", currentClass.getType());
			classJson.put("label", "Union");

			_class.add(classJson);

			Map<String, Object> classAttrJson = new LinkedHashMap<String, Object>();

			classAttrJson.put("id", currentClass.getId());
			classAttrJson.put("uri", currentClass.getIri());
			classAttrJson.put("comment", currentClass.getComment());

			classAttrJson.put("attributes", attributes);
			classAttrJson.put("union", union);

			// Apply attributes
			for (String attribute : currentClass.getAttributes()) {
				attributes.add(attribute);
			}

			for (BaseNode entity : currentClass.getUnions()) {
				union.add(entity.getId());
			}

			classAttribute.add(classAttrJson);
		}
	}

	public <V extends BaseProperty> void processProperties(Map<String, V> propertyMap) {
		for (Map.Entry<String, V> baseProperty : propertyMap.entrySet()) {
			BaseProperty currentProperty = baseProperty.getValue();

			if (currentProperty.getDomain() == null || currentProperty.getRange() == null) {
//				System.out.println("Skip " + currentProperty.getName() + " property.");
				continue;
			}

			Map<String, Object> propertyJson = new LinkedHashMap<String, Object>();

			List<Object> equivalent = new ArrayList<Object>();
			List<Object> attributes = new ArrayList<Object>();
			List<Object> subProperty = new ArrayList<Object>();
			List<Object> superProperty = new ArrayList<Object>();
			List<Object> disjoints = new ArrayList<Object>();

			propertyJson.put("id", currentProperty.getId());
			propertyJson.put("type", currentProperty.getType());

			objectProperty.add(propertyJson);

			Map<String, Object> dataAttrJson = new LinkedHashMap<String, Object>();

			dataAttrJson.put("id", currentProperty.getId());
			dataAttrJson.put("uri", currentProperty.getIri());
			dataAttrJson.put("label", currentProperty.getLabels());
			dataAttrJson.put("comment", currentProperty.getComments());
			dataAttrJson.put("domain", currentProperty.getDomain().getId());
			dataAttrJson.put("range", currentProperty.getRange().getId());
			dataAttrJson.put("inverse", currentProperty.getInverseID());
			dataAttrJson.put("equivalent", equivalent);
			dataAttrJson.put("attributes", attributes);
			dataAttrJson.put("subproperty", subProperty);
			dataAttrJson.put("superproperty", superProperty);
			dataAttrJson.put("disjoint", disjoints);

			// Cardinality
			int exact = currentProperty.getExactCardinality();
			int min = currentProperty.getMinCardinality();
			int max = currentProperty.getMaxCardinality();

			if (exact != -1){
				dataAttrJson.put("cardinality", currentProperty.getExactCardinality());
			}

			if (min != -1) {
				dataAttrJson.put("minCardinality", currentProperty.getMinCardinality());
			}

			if (max != -1) {
				dataAttrJson.put("maxCardinality", currentProperty.getMaxCardinality());
			}

			// Apply attributes
			for (String attribute : currentProperty.getAttributes()) {
				attributes.add(attribute);
			}

			// Apply sub classes
			for (String entity : currentProperty.getSubProperties()) {
				subProperty.add(entity);
			}

			for (String entity : currentProperty.getSuperProperties()) {
				superProperty.add(entity);
			}


			// Apply equivalents
			for (String entity : currentProperty.getEquivalents()) {
				equivalent.add(entity);
			}

			// Apply disjoints
			for (String entity : currentProperty.getDisjoints()) {
				disjoints.add(entity);
			}

			objectPropertyAttribute.add(dataAttrJson);
		}
	}
}
