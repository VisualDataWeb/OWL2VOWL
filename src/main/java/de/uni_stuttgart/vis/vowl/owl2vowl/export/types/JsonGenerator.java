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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.semanticweb.owlapi.model.IRI;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
public class JsonGenerator {

	private static final Logger logger = LogManager.getLogger(JsonGenerator.class);

	private final String VERSION_INFORMATION = "Created with OWL2VOWL (version " + ProjectInformations.getVersion()
			+ "), http://vowl.visualdataweb.org";
	private Map<String, Object> root;
	private Map<String, Object> header;
	private Map<String, Object> prefixList;
	private List<Object> gizmoAnnotations;
	private List<Object> namespace;
	private JsonGeneratorVisitor visitor;

	public JsonGenerator() {
		initialize();
	}

	private void initialize() {
		root = new LinkedHashMap<>();
		header = new LinkedHashMap<>();
		prefixList = new LinkedHashMap<>();
		gizmoAnnotations= new ArrayList<>();
		namespace = new ArrayList<>();

		root.put("_comment", VERSION_INFORMATION);
		root.put("header", header);
		root.put("namespace", namespace);
		root.put("gizmoAnnotations", gizmoAnnotations);
		
		namespace.add(new HashMap<>());
	}

	public void execute(VowlData vowlData) throws Exception {
		processHeader(vowlData);
		processGizmoAnnotations(vowlData);
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
		header.put("baseIris", vowlData.getBaseIris().stream().map(IRI::toString).collect(Collectors.toSet()));
		header.put("prefixList", prefixList);
		header.put("title", JsonGeneratorVisitorImpl.getLabelsFromAnnotations(ontologyInformation.getTitles()));
		header.put("iri", ontologyInformation.getIri());
		header.put("version", ontologyInformation.getVersion());
		header.put("author", ontologyInformation.getAuthors());
		header.put("description", JsonGeneratorVisitorImpl.getLabelsFromAnnotations(ontologyInformation.getAnnotations().getDescription()));
		header.put("labels", JsonGeneratorVisitorImpl.getLabelsFromAnnotations(ontologyInformation.getAnnotations().getLabels()));
		header.put("comments", JsonGeneratorVisitorImpl.getLabelsFromAnnotations(ontologyInformation.getAnnotations().getComments()));
		header.put("other", ontologyInformation.getAnnotations().getIdentifierToAnnotation());
		
		Map<String, String> map = vowlData.getPrefixMap();
		// adding prefix list to that thing;
	    for (Map.Entry<String,String> entry : map.entrySet()) {
	    	  String pr=entry.getKey();
	            pr= pr.substring(0, pr.length() - 1);
            prefixList.put(pr,entry.getValue());
	    }
	}
	
	
protected void processGizmoAnnotations(VowlData vowlData) {
		
		Map<String, Map<String,String> >  annotationMap = vowlData.getAnnotationMap();
		Map<String, String>   prefixMap = vowlData.getPrefixMap();
		String gizmoRep=prefixMap.get("gizmo:");
		if (gizmoRep==null) {
			gizmoRep="empty";
		}	
		for (Map.Entry<String, Map<String , String > > entry : annotationMap.entrySet())
		{
			Map<String, Object> object = new HashMap<>();
			object.put("iri", entry.getKey());
			
		    int numGizmoAnnotations=0;
		    Map<String , String > bcMap=entry.getValue();
		    Map<String, Object> renderingDescriptions= new HashMap<>();
		    for (Map.Entry<String, String > annotationPV: bcMap.entrySet()){
		    	IRI temp=IRI.create(annotationPV.getKey());
		    	String ns= temp.getNamespace();
		    	if (ns.compareTo(gizmoRep)==0) {
		    		// this is a gizmo annotation 
		    		String annotationKey=temp.getShortForm();
		    		// just making sure we write only that which belongs here;
		    		if (annotationPV.getValue().contains(";")) {
		    			String[] sep=annotationPV.getValue().split(";");
		    			Set<String> tempSet = new  HashSet<>();
		    			for (int i=0;i<sep.length;i++) {
		    				tempSet.add(sep[i]);
		    			}
		    			renderingDescriptions.put(annotationKey, tempSet);
		    		} else {
		    			renderingDescriptions.put(annotationKey, annotationPV.getValue());
		    		}
		    		numGizmoAnnotations++;
		    	}
		    }
		    if (numGizmoAnnotations>0) {
		    	object.put("annotations", renderingDescriptions);
		    	// only when we have > 0 annotations we add this to the object
		    	gizmoAnnotations.add(object);
		    }
		}
	}

	protected <V extends AbstractEntity> void convertEntities(Map<IRI, V> entityMap) {
		for (Map.Entry<IRI, V> irivEntry : entityMap.entrySet()) {
			V entity = irivEntry.getValue();

			if (!entity.isExportToJson()) {
				continue;
			}
			try {
				entity.accept(visitor);
			}
			catch (Exception e){
				IRI key =irivEntry.getKey();
				logger.info("WARNING! ");
				logger.info("*******************************");
				logger.info("Failed to Accept!");
				logger.info("Entity   "+entity);
				logger.info("Key      "+key);
				logger.info("Visitor  "+visitor);
				logger.info("Reason : "+e);
				logger.info("The type of the object is: " + entity.getClass().getName());
				logger.info("*** SKIPPING THIS ***");
				continue;
			}
		}
	}
	
//removed Metrics Process >> WebVOWL computes the statistics
//	public void processMetrics(VowlData vowlData) {
//		
//		OntologyMetric metrics = vowlData.getMetrics();
//		this.metrics.put("classCount", metrics.getClasses());
//		this.metrics.put("objectPropertyCount", metrics.getObjectProperties());
//		this.metrics.put("datatypePropertyCount", metrics.getDatatypeProperties());
//		this.metrics.put("individualCount", metrics.getIndividuals());
//	}


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
