/*
 * Test.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import de.uni_stuttgart.vis.vowl.owl2vowl.export.JsonExporter;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.Constants;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.OntologyInfo;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.OntologyMetric;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import de.uni_stuttgart.vis.vowl.owl2vowl.pipes.FormatText;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.metrics.*;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * @author Eduard Marbach
 */
public class Main {
	private static final boolean DEBUG_EXPORT = true;
	public static org.apache.logging.log4j.Logger logger = LogManager.getRootLogger();
	private static MapData mapData;
	private static GeneralParser parser;

	private static OWLOntologyManager manager;
	private static OWLOntology ontology;
	private static OWLDataFactory factory;

	public static void main(String[] args) {
		File quickExport = new File(Constants.BENCHMARK1);
		File nec = new File(Constants.BENCHMARK2);
		File prov = new File(Constants.PERSONAS);

		new Main().startConvertion(prov, null);
		System.exit(0);

		new Main().startConvertion(quickExport, nec);

		System.exit(0);
		File test = new File(Constants.PATH_VARIABLE);
		File[] children = test.listFiles();

		if(children == null){
			throw new IllegalStateException("Directory doesn't contain files.");
		}

		for(File file : children){
			new Main().startConvertion(file, null);
		}
	}

	private static void parseOntoInfo() {
		OntologyInfo info = mapData.getOntologyInfo();

		IRI ontoIri = ontology.getOntologyID().getOntologyIRI();
		IRI versionIri = ontology.getOntologyID().getVersionIRI();

		if (ontoIri != null) {
			info.setIri(ontoIri.toString());
		}

		if (versionIri != null) {
			info.setVersion(versionIri.toString());
		}

		/* Save available annotations */
		for (OWLAnnotation i : ontology.getAnnotations()) {
			if (i.getProperty().toString().equals(Constants.INFO_CREATOR)) {
				info.setAuthor(FormatText.cutQuote(i.getValue().toString()));
			}
			if (i.getProperty().toString().equals(Constants.INFO_DESCRIPTION)) {
				info.setDescription(FormatText.cutQuote(i.getValue().toString()));
			}
			if (i.getProperty().toString().equals(Constants.INFO_ISSUED)) {
				info.setIssued(FormatText.cutQuote(i.getValue().toString()));
			}
			if (i.getProperty().toString().equals(Constants.INFO_LICENSE)) {
				info.setLicense(FormatText.cutQuote(i.getValue().toString()));
			}
			if (i.getProperty().toString().equals(Constants.INFO_RDFS_LABEL)) {
				info.setRdfsLabel(FormatText.cutQuote(i.getValue().toString()));
			}
			if (i.getProperty().toString().equals(Constants.INFO_SEE_ALSO)) {
				info.setSeeAlso(FormatText.cutQuote(i.getValue().toString()));
			}
			if (i.getProperty().toString().equals(Constants.INFO_TITLE)) {
				info.setTitle(FormatText.cutQuote(i.getValue().toString()));
			}
			if (i.getProperty().toString().equals(Constants.INFO_VERSION_INFO)) {
				info.setVersion(FormatText.cutQuote(i.getValue().toString()));
			}
		}


	}

	private static void parseDatatypeProperties(Set<OWLDataProperty> dataProperties) {
		parser.handleDatatypeProperty(dataProperties);
	}

	private static void parseObjectProperty(Set<OWLObjectProperty> objectProperties) {
		parser.handleObjectProperty(objectProperties);
	}

	private static void parseDatatypes(Set<OWLDatatype> datatypes) {
		parser.handleDatatype(datatypes);
	}

	private static void parseClasses(Set<OWLClass> classes) {
		parser.handleClass(classes);
	}

	/**
	 * Save the metrics of the current loaded ontology.
	 */
	public static void parseMetrics() {
		OntologyMetric ontologyMetric = mapData.getOntologyMetric();

		OWLMetric metric = new ReferencedClassCount(ontology.getOWLOntologyManager());
		metric.setOntology(ontology);
		ontologyMetric.setClassCount(Integer.parseInt(metric.getValue().toString()));

		metric = new ReferencedObjectPropertyCount(ontology.getOWLOntologyManager());
		metric.setOntology(ontology);
		ontologyMetric.setObjectPropertyCount(Integer.parseInt(metric.getValue().toString()));

		metric = new ReferencedDataPropertyCount(ontology.getOWLOntologyManager());
		metric.setOntology(ontology);
		ontologyMetric.setDataPropertyCount(Integer.parseInt(metric.getValue().toString()));

		metric = new ReferencedIndividualCount(ontology.getOWLOntologyManager());
		metric.setOntology(ontology);
		ontologyMetric.setIndividualCount(Integer.parseInt(metric.getValue().toString()));

		metric = new AxiomCount(ontology.getOWLOntologyManager());
		metric.setOntology(ontology);
		ontologyMetric.setAxiomCount(Integer.parseInt(metric.getValue().toString()));

		// metric = new DLExpressivity(ontology.getOWLOntologyManager());
		// metric.setOntology(ontology);

		ontologyMetric.calculateSums();
	}

	public void startConvertion(File theOntology, File furtherOntologies) {
		manager = OWLManager.createOWLOntologyManager();
		factory = manager.getOWLDataFactory();
		mapData = new MapData();

		try {
			if(furtherOntologies != null) {
				manager.loadOntologyFromOntologyDocument(furtherOntologies);
			}
			//ontology = manager.loadOntology(IRI.create("http://ontovibe.visualdataweb.org/ontovibe.ttl"));
			ontology = manager.loadOntologyFromOntologyDocument(IRI.create(theOntology));
			Set<OWLClass> classes = ontology.getClassesInSignature();
			Set<OWLDatatype> datatypes = ontology.getDatatypesInSignature();
			Set<OWLObjectProperty> objectProperties = ontology.getObjectPropertiesInSignature();
			Set<OWLDataProperty> dataProperties = ontology.getDataPropertiesInSignature();

			ProcessUnit processor = new ProcessUnit(ontology, factory, mapData);
			parser = new GeneralParser(ontology, factory, mapData);

			/*
			Parsing of the raw data gained from the OWL API. Will be transformed to useable data
			for WebVOWL.
			 */
			parseOntoInfo();
			parseClasses(classes);
			//parseDatatypes(datatypes);
			parseObjectProperty(objectProperties);
			parseDatatypeProperties(dataProperties);
			parseMetrics();

			/*
			Further processing of the gained data. Eq. IRIs will be transformed to IDs where necessary
			 */
			processor.processClasses();
			//processor.processDatatypes();
			processor.processProperties();

		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}

		if (DEBUG_EXPORT) {
			String filePath = System.getProperty("user.dir") + "\\WebVOWL\\src\\js\\data\\";
			File exportFile = new File(filePath, FilenameUtils.removeExtension(theOntology.getName()) + ".json");
			JsonExporter exporter = new JsonExporter(exportFile);

			try {
				exporter.execute(mapData);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
