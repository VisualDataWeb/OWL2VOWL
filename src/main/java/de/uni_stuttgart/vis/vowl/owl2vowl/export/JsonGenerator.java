/*
 * Dummy.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.export;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.BaseEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.OntologyInfo;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.OntologyMetric;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.BaseEdge;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.BaseClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.BaseDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import de.uni_stuttgart.vis.vowl.owl2vowl.util.ProjectInformations;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.util.*;

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
	private final String VERSION_INFORMATION = "Created with OWL2VOWL (version " + ProjectInformations.getVersion()
			+ "), http://vowl.visualdataweb.org";

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
		root.put("_comment", VERSION_INFORMATION);
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
		processEntities(mapData.getClassMap());
		processEntities(mapData.getIntersectionMap());
		processEntities(mapData.getDatatypeMap());
		processEntities(mapData.getMergedProperties());
		processEntities(mapData.getThingMap());
		processEntities(mapData.getUnionMap());
	}

	public void export(Exporter exporter) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);
		mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
		invoke(root);
		exporter.write(mapper.writeValueAsString(root));
	}

	/**
	 * Used to remove empty collections out of the json.
	 *
	 * @param jsonObj Object to be invoked.
	 */
	private void invoke(Map<?, Object> jsonObj) {
		Iterator<?> it = jsonObj.keySet().iterator();

		while (it.hasNext()) {
			Object key = it.next();

			Object o = jsonObj.get(key);

			if (o instanceof Map) {
				Map<?, Object> casted = (Map<?, Object>) o;

				if (casted.isEmpty()) {
					it.remove();
				} else {
					invoke(casted);
				}
			}

			if (o instanceof Collection) {
				Collection<Object> casted = (Collection<Object>) o;

				if (casted.isEmpty()) {
					it.remove();
				} else {
					invoke(casted);
				}
			}

			if (o instanceof String) {
				String casted = (String) o;

				if (casted.isEmpty()) {
					it.remove();
				}
			}
		}
	}

	/**
	 * Used to remove empty collections out of the json.
	 *
	 * @param jsonObj Object to be invoked.
	 */
	private void invoke(Collection<Object> jsonObj) {
		Iterator<Object> it = jsonObj.iterator();

		while (it.hasNext()) {
			Object o = it.next();

			if (o instanceof Map) {
				Map<?, Object> casted = (Map<?, Object>) o;

				if (casted.size() == 0) {
					it.remove();
				} else {
					invoke(casted);
				}
			}

			if (o instanceof Collection) {
				Collection<Object> casted = (Collection<Object>) o;

				if (casted.size() == 0) {
					it.remove();
				} else {
					invoke(casted);
				}
			}

			if (o instanceof String) {
				String casted = (String) o;

				if (casted.isEmpty()) {
					it.remove();
				}
			}
		}
	}

	// TODO
	public void processNamespace() {
		// Namespace only sample because there wasn't one
		Map<String, Object> sampleNamespace = new LinkedHashMap<String, Object>();
		namespace.add(sampleNamespace);
	}

	public void processHeader(OntologyInfo info) {
		// Apply header
		header.put("languages", mapData.getAvailableLanguages());
		header.put("title", info.getLanguageToTitle());
		header.put("uri", info.getIri());
		header.put("version", info.getVersion());
		header.put("author", info.getAuthor());
		header.put("description", info.getLanguageToDescription());
		header.put("seeAlso", info.getSeeAlso());
		header.put("issued", info.getIssued());
		header.put("license", info.getLicense());
		header.put("rdfsLabel", info.getRdfsLabel());
		header.put("other", info.getOthers());
	}

	public <V extends BaseEntity> void processEntities(Map<String, V> entityMap) {
		for (Map.Entry<String, V> entry : entityMap.entrySet()) {
			V baseEntity = entry.getValue();
			JsonGeneratorVisitor visitor = new JsonGeneratorVisitorImpl();
			baseEntity.accept(visitor);

			if (baseEntity instanceof BaseClass) {
				_class.add(visitor.getEntityJson());
				classAttribute.add(visitor.getEntityAttributes());
			}

			if (baseEntity instanceof BaseDatatype) {
				datatype.add(visitor.getEntityJson());
				datatypeAttribute.add(visitor.getEntityAttributes());
			}

			if (baseEntity instanceof BaseEdge) {
				if (!visitor.isFailure()) {
					objectProperty.add(visitor.getEntityJson());
					objectPropertyAttribute.add(visitor.getEntityAttributes());
				} else {
					// TODO
				}
			}
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
}
