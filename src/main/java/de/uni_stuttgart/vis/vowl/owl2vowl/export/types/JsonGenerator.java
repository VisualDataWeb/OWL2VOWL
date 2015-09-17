/*
 * JsonGenerator.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.export.types;

import de.uni_stuttgart.vis.vowl.owl2vowl.export.JsonGeneratorVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.JsonGeneratorVisitorImpl;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.AbstractEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.VowlData;
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
	private VowlData vowlData;
	private JsonGeneratorVisitor visitor;

	public JsonGenerator() {
		initialize();
	}

	private void initialize() {
		root = new LinkedHashMap<>();
		root.put("_comment", VERSION_INFORMATION);
	}

	public void execute(VowlData vowlData) throws Exception {
		this.vowlData = vowlData;
		visitor = new JsonGeneratorVisitorImpl(vowlData, root);
		processHeader();
		processMetrics();

		convertEntities(vowlData.getClassMap());
	}

	public void export(Exporter exporter) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);
		mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
		invoke(root);
		exporter.write(mapper.writeValueAsString(root));
	}

	public void processHeader() {
		// TODO implement
	}

	public <V extends AbstractEntity> void convertEntities(Map<IRI, V> entityMap) {
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
