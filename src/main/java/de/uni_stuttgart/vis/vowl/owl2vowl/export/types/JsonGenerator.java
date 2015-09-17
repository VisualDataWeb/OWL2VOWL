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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
		//invoke(root);
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
}
