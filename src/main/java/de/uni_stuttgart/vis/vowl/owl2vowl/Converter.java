/*
 * Converter.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl;

import de.uni_stuttgart.vis.vowl.owl2vowl.export.types.Exporter;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.JsonGenerator;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.OntologyMetric;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.GeneralParser;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.ProcessUnit;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.OntologyInformation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.metrics.*;
import org.semanticweb.owlapi.model.*;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * The {@link de.uni_stuttgart.vis.vowl.owl2vowl.Converter} loads the passed ontologies and has
 * methods to start the conversion and the export.
 */
public class Converter {

	private static final Logger logger = LogManager.getRootLogger();
	private static MapData mapData;
	private final JsonGenerator jsonGenerator = new JsonGenerator();
	private OntologyInformation ontologyInformation;
	private OWLOntologyManager ontologyManager;
	private OWLOntology ontology;
	private OWLDataFactory dataFactory;

	public Converter(IRI ontologyIRI) throws OWLOntologyCreationException {
		this(ontologyIRI, Collections.<IRI>emptyList());
	}

	public Converter(IRI ontologyIRI, List<IRI> necessaryExternals) throws OWLOntologyCreationException {
		initializeAPI();

		logger.info("Loading ontologies ... [" + ontologyIRI + ",  " + necessaryExternals + "]");

		if (!necessaryExternals.isEmpty()) {
			for (IRI externalIRI : necessaryExternals) {
				ontologyManager.loadOntology(externalIRI);
			}
			logger.info("External ontologies loaded!");
		}

		ontology = ontologyManager.loadOntology(ontologyIRI);
		String logOntoName = ontologyIRI.toString();

		ontologyInformation = new OntologyInformation(ontology, ontologyIRI);

		if (!ontology.isAnonymous()) {
			logOntoName = ontology.getOntologyID().getOntologyIRI().getFragment();
		} else {
			logger.info("Ontology IRI is anonymous. Use loaded URI/IRI instead.");
		}

		logger.info("Ontologies loaded! Main Ontology: " + logOntoName);
	}

	public Converter(OWLOntology ontology, String ontologyIRI) {
		this.ontology = ontology;

		if (ontologyIRI == null) {
			ontologyInformation = new OntologyInformation(ontology, IRI.create(URI.create("")));
		} else {
			ontologyInformation = new OntologyInformation(ontology, IRI.create(URI.create(ontologyIRI)));
		}
	}

	public Converter(OWLOntology ontology) {
		String iri = "";

		if (!ontology.isAnonymous()) {
			iri = ontology.getOntologyID().getOntologyIRI().getFragment();
		} else {
			logger.info("Ontology IRI is anonymous. Use loaded URI/IRI instead.");
		}

		this.ontology = ontology;
		ontologyInformation = new OntologyInformation(ontology, IRI.create(URI.create(iri)));

	}

	private void initializeAPI() {
		logger.info("Initializing OWL API ...");
		ontologyManager = OWLManager.createOWLOntologyManager();
		dataFactory = ontologyManager.getOWLDataFactory();
		logger.info("OWL API initialized!");
	}

	/**
	 * Executes the complete conversion to the webvowl compatible json format.
	 */
	public void convert() {
		mapData = new MapData();

		Set<OWLClass> classes = ontology.getClassesInSignature();
		Set<OWLDatatype> datatypes = ontology.getDatatypesInSignature();
		Set<OWLObjectProperty> objectProperties = ontology.getObjectPropertiesInSignature();
		Set<OWLDataProperty> dataProperties = ontology.getDataPropertiesInSignature();

		ProcessUnit processor = new ProcessUnit(ontologyInformation, mapData);
		GeneralParser parser = new GeneralParser(ontologyInformation, mapData);

		/*
		Parsing of the raw data gained from the OWL API. Will be transformed to useable data
		for WebVOWL.
		*/
		parser.handleOntologyInfo();

		parser.handleClass(classes);
		processor.processClasses();

		//parseDatatypes(datatypes);
		parser.handleObjectProperty(objectProperties);
		parser.handleDatatypeProperty(dataProperties);
		parseMetrics();
		//processor.processDatatypes();
		processor.processProperties();
		processor.processAxioms();
	}

	/**
	 * Exports the generated data according to the implemented {@link Exporter}.
	 * @param exporter The exporter.
	 * @throws Exception Any exception during json generation.
	 */
	public void export(Exporter exporter) throws Exception {
		jsonGenerator.execute(mapData);
		jsonGenerator.export(exporter);
	}

	/**
	 * Save the metrics of the current loaded ontology.
	 * TODO Should be optimized probalby...
	 */
	public void parseMetrics() {
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

	public IRI getOntologyIri() {
		return ontology.getOntologyID().getOntologyIRI();
	}
}
