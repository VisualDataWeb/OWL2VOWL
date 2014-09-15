/*
 * Test.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import de.uni_stuttgart.vis.vowl.owl2vowl.export.JsonExporter;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.Constants;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.OntologyInfo;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * @author Eduard Marbach
 */
public class Main {
	private static final boolean DEBUG_EXPORT = true;
	private static MapData mapData;
	private static GeneralParser parser;

	private static OWLOntologyManager manager;
	private static OWLOntology ontology;
	private static OWLDataFactory factory;

	public static void main(String[] args) {
		File ont = new File(Constants.MUTO);
		manager = OWLManager.createOWLOntologyManager();
		factory = manager.getOWLDataFactory();
		mapData = new MapData();

		try {
			ontology = manager.loadOntologyFromOntologyDocument(IRI.create(ont));
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
			//parseOntoInfo();
			parseClasses(classes);
			//parseDatatypes(datatypes);
			parseObjectProperty(objectProperties);
			parseDatatypeProperties(dataProperties);

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
			String filePath = System.getProperty("user.dir") + "\\WebVOWL\\src\\js\\data\\benchmark.json";
			JsonExporter exporter =
					new JsonExporter(new File(filePath));

			try {
				exporter.processNamespace();
				exporter.processHeader();
				exporter.processClasses(mapData.getClassMap());
				exporter.processDatatypes(mapData.getDatatypeMap());
				exporter.processProperties(mapData.getObjectPropertyMap());
				exporter.processProperties(mapData.getDatatypePropertyMap());
				exporter.processThings(mapData.getThingMap());
				exporter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void parseOntoInfo() {
		OntologyInfo info = mapData.getOntologyInfo();

		info.setIri(ontology.getOntologyID().getOntologyIRI().toString());
		info.setVersion(ontology.getOntologyID().getVersionIRI().toString());
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
}
