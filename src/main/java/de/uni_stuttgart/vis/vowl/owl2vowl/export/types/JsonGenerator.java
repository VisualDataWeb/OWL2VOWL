/*
 * JsonGenerator.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.export.types;

import de.uni_stuttgart.vis.vowl.owl2vowl.export.JsonGeneratorVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.JsonGeneratorVisitorImpl;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.AbstractEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.ontology.OntologyInformation;
import de.uni_stuttgart.vis.vowl.owl2vowl.util.ProjectInformations;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.semanticweb.owlapi.model.IRI;

import java.util.*;

/**
 *
 */
public class JsonGenerator {
	private final String VERSION_INFORMATION = "Created with OWL2VOWL (version " + ProjectInformations.getVersion()
			+ "), http://vowl.visualdataweb.org";
	private Map<String, Object> root;
	private Map<String, Object> header;
	private Map<String, Object> metrics;
	private List<Object> namespace;
	private JsonGeneratorVisitor visitor;

	public JsonGenerator() {
		initialize();
	}

	private void initialize() {
		root = new LinkedHashMap<>();
		header = new LinkedHashMap<>();
		metrics = new LinkedHashMap<>();
		namespace = new ArrayList<>();

		root.put("_comment", VERSION_INFORMATION);
		root.put("header", header);
		root.put("namespace", namespace);
		root.put("metrics", metrics);

		// TODO For WebVOWL needed but currently not used
		namespace.add(new HashMap<>());
	}

	public void execute(VowlData vowlData) throws Exception {
		processHeader(vowlData);
		processMetrics();

		visitor = new JsonGeneratorVisitorImpl(vowlData, root);
		convertEntities(vowlData.getEntityMap());
	}

	public void export(Exporter exporter) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);
		mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
		invoke(root);
		exporter.write(mapper.writeValueAsString(root));
	}

	protected void processHeader(VowlData vowlData) {
		OntologyInformation ontologyInformation = vowlData.getOntologyInformation();
		header.put("languages", vowlData.getLanguages());
		header.put("title", JsonGeneratorVisitorImpl.getLabelsFromAnnotations(ontologyInformation.getTitles()));
		header.put("iri", ontologyInformation.getIri());
		header.put("version", ontologyInformation.getVersion());
		header.put("author", ontologyInformation.getAuthors());
		header.put("description", JsonGeneratorVisitorImpl.getLabelsFromAnnotations(ontologyInformation.getAnnotations().getDescription()));
		header.put("labels", JsonGeneratorVisitorImpl.getLabelsFromAnnotations(ontologyInformation.getAnnotations().getLabels()));
		header.put("comments", JsonGeneratorVisitorImpl.getLabelsFromAnnotations(ontologyInformation.getAnnotations().getComments()));
		header.put("other", ontologyInformation.getAnnotations().getIdentifierToAnnotation());
	}

	protected <V extends AbstractEntity> void convertEntities(Map<IRI, V> entityMap) {
		for (Map.Entry<IRI, V> irivEntry : entityMap.entrySet()) {
			V entity = irivEntry.getValue();
			entity.accept(visitor);
		}
	}

	public void processMetrics() {
		// TODO implement
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
}
