/*
 * Dummy.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.export;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.OntologyInfo;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.OntologyMetric;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties.BaseProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.BaseClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.OwlEquivalentClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.OwlThing;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.BaseDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * @author Eduard Marbach
 * @version 1.0
 */
public class JsonExporter {
	private JSONObject root;
	private JSONArray namespace;
	private JSONObject header;
	private JSONObject metrics;
	private JSONArray _class;
	private JSONArray classAttribute;
	private JSONArray datatype;
	private JSONArray datatypeAttribute;
	private JSONArray objectProperty;
	private JSONArray objectPropertyAttribute;
	private JSONArray datatypeProperty;
	private JSONArray datatypePropertyAttribute;
	private File outputFile;


	public JsonExporter(File outputFile) {
		this.outputFile = outputFile;
		this.initialize();
	}

	private void initialize() {
		root = new JSONObject();
		namespace = new JSONArray();
		header = new JSONObject();
		metrics = new JSONObject();
		_class = new JSONArray();
		classAttribute = new JSONArray();
		datatype = new JSONArray();
		datatypeAttribute = new JSONArray();
		objectProperty = new JSONArray();
		objectPropertyAttribute = new JSONArray();

		// Sets root
		root.put("namespace", namespace)
				.put("header", header)
				.put("metrics", metrics)
				.put("class", _class)
				.put("classAttribute", classAttribute)
				.put("datatype", datatype)
				.put("datatypeAttribute", datatypeAttribute)
				.put("property", objectProperty)
				.put("propertyAttribute", objectPropertyAttribute);
	}

	// TODO
	public void processNamespace() {
		// Namespace only sample because there wasn't one
		JSONObject sampleNamespace = new JSONObject();
		namespace.put(sampleNamespace);

	}

	public void processHeader(OntologyInfo info) {
		// Apply header
		header.put("title", info.getTitle())
				.put("uri", info.getIri())
				.put("version", info.getVersion())
				.put("author", info.getAuthor())
				.put("description", info.getDescription());
	}

	public void processClasses(Map<String, BaseClass> classes) {
		for (Map.Entry<String, BaseClass> baseClass : classes.entrySet()) {
			BaseClass currentClass = baseClass.getValue();

			JSONObject classJson = new JSONObject();

			JSONArray equivalent = new JSONArray();
			JSONArray attributes = new JSONArray();
			JSONArray union = new JSONArray();
			JSONArray intersection = new JSONArray();
			JSONArray complement = new JSONArray();
			JSONArray subClasses = new JSONArray();
			JSONArray superClasses = new JSONArray();

			classJson.put("id", currentClass.getId());
			classJson.put("type", currentClass.getType());

			_class.put(classJson);

			JSONObject classAttrJson = new JSONObject();

			classAttrJson.put("id", currentClass.getId());
			classAttrJson.put("label", currentClass.getName());
			classAttrJson.put("uri", currentClass.getIri());
			classAttrJson.put("comment", currentClass.getComment());
			// TODO not implemented yet
			classAttrJson.put("instances", 0);

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
					equivalent.put(entity.getId());
				}
			}

			// Apply attributes
			for (String attribute : currentClass.getAttributes()) {
				attributes.put(attribute);
			}

			// Apply sub classes
			for (BaseNode entity : currentClass.getSubClasses()) {
				subClasses.put(entity.getId());
			}

			// Apply super classes
			for (BaseNode entity : currentClass.getSuperClasses()) {
				superClasses.put(entity.getId());
			}

			classAttribute.put(classAttrJson);
		}

	}

	public void processDatatypes(Map<String, BaseDatatype> datatypes) {
		for (Map.Entry<String, BaseDatatype> baseDatatype : datatypes.entrySet()) {
			BaseDatatype currentDatatype = baseDatatype.getValue();

			JSONObject datatypesJson = new JSONObject();

			JSONArray attributes = new JSONArray();
			JSONArray subClasses = new JSONArray();
			JSONArray superClasses = new JSONArray();

			datatypesJson.put("id", currentDatatype.getId());
			datatypesJson.put("type", currentDatatype.getType());

			datatype.put(datatypesJson);

			JSONObject datatypeAttrJson = new JSONObject();

			datatypeAttrJson.put("id", currentDatatype.getId());
			datatypeAttrJson.put("label", currentDatatype.getName());
			datatypeAttrJson.put("uri", currentDatatype.getIri());
			datatypeAttrJson.put("comment", currentDatatype.getComment());
			// TODO not implemented yet
			datatypeAttrJson.put("instances", 0);

			datatypeAttrJson.put("attributes", attributes);
			datatypeAttrJson.put("subClasses", subClasses);
			datatypeAttrJson.put("superClasses", superClasses);


			// Apply attributes
			for (String attribute : currentDatatype.getAttributes()) {
				attributes.put(attribute);
			}

			// Apply sub classes
			for (BaseNode entity : currentDatatype.getSubClasses()) {
				subClasses.put(entity.getId());
			}

			// Apply super classes
			for (BaseNode entity : currentDatatype.getSuperClasses()) {
				superClasses.put(entity.getId());
			}

			datatypeAttribute.put(datatypeAttrJson);
		}

	}

	public void processThings(Map<String, OwlThing> things) {
		for (Map.Entry<String, OwlThing> baseClass : things.entrySet()) {
			BaseClass currentClass = baseClass.getValue();

			JSONObject classJson = new JSONObject();

			classJson.put("id", currentClass.getId());
			classJson.put("type", currentClass.getType());

			_class.put(classJson);

			JSONObject classAttrJson = new JSONObject();

			classAttrJson.put("id", currentClass.getId());
			classAttrJson.put("label", currentClass.getName());
			classAttrJson.put("uri", currentClass.getIri());

			classAttribute.put(classAttrJson);
		}
	}

	public void processMetrics(OntologyMetric metric) {
		metrics.put("classCount", metric.getClassCount())
				.put("datatypeCount", metric.getDatatypeCount())
				.put("objectPropertyCount", metric.getObjectPropertyCount())
				.put("datatypePropertyCount", metric.getDataPropertyCount())
				.put("propertyCount", metric.getPropertyCount())
				.put("nodeCount", metric.getNodeCount())
				.put("axiomCount", metric.getAxiomCount());
	}

	public void close() throws IOException {
		FileWriter writer = new FileWriter(outputFile);
		writer.write(root.toString());
		writer.close();
	}

	public <V extends BaseProperty> void processProperties(Map<String, V> propertyMap) {
		for (Map.Entry<String, V> baseProperty : propertyMap.entrySet()) {
			BaseProperty currentProperty = baseProperty.getValue();

			if (currentProperty.getDomain() == null || currentProperty.getRange() == null) {
				System.out.println("Skip " + currentProperty.getName() + " property.");
				continue;
			}

			JSONObject propertyJson = new JSONObject();

			JSONArray equivalent = new JSONArray();
			JSONArray attributes = new JSONArray();
			JSONArray subProperty = new JSONArray();
			JSONArray disjoints = new JSONArray();

			propertyJson.put("id", currentProperty.getId());
			propertyJson.put("type", currentProperty.getType());

			objectProperty.put(propertyJson);

			JSONObject dataAttrJson = new JSONObject();

			dataAttrJson.put("id", currentProperty.getId());
			dataAttrJson.put("label", currentProperty.getName());
			dataAttrJson.put("uri", currentProperty.getIri());
			dataAttrJson.put("comment", currentProperty.getComment());
			dataAttrJson.put("domain", currentProperty.getDomain().getId());
			dataAttrJson.put("range", currentProperty.getRange().getId());
			dataAttrJson.put("inverse", currentProperty.getInverseID());
			// TODO not implemented yet
			dataAttrJson.put("instances", 0);

			dataAttrJson.put("equivalent", equivalent);
			dataAttrJson.put("attributes", attributes);
			dataAttrJson.put("subproperty", subProperty);
			dataAttrJson.put("disjoint", disjoints);

			// TODO equivalents durchgehen

			// Apply attributes
			for (String attribute : currentProperty.getAttributes()) {
				attributes.put(attribute);
			}

			// Apply sub classes
			for (String entity : currentProperty.getSubProperties()) {
				subProperty.put(entity);
			}

			// Apply sub classes
			for (String entity : currentProperty.getEquivalents()) {
				equivalent.put(entity);
			}

			// Apply sub classes
			for (String entity : currentProperty.getDisjoints()) {
				disjoints.put(entity);
			}

			objectPropertyAttribute.put(dataAttrJson);
		}
	}

	public void execute(MapData mapData) throws IOException {
		processNamespace();
		processHeader(mapData.getOntologyInfo());
		processMetrics(mapData.getOntologyMetric());
		processClasses(mapData.getClassMap());
		processDatatypes(mapData.getDatatypeMap());
		processProperties(mapData.getObjectPropertyMap());
		processProperties(mapData.getDatatypePropertyMap());
		processThings(mapData.getThingMap());
		close();
	}
}
