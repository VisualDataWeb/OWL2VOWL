/*
 * ConverterImpl.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Ontology_Path;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.types.ConsoleExporter;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.types.Exporter;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.types.FileExporter;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.types.JsonGenerator;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.AbstractEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.owlapi.EntityCreationVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.owlapi.IndividualsVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.AnnotationParser;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.ImportedChecker;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.OntologyInformationParser;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.TypeSetter;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.classes.OwlClassAxiomVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.property.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLOntologyWalker;

import java.io.File;
import java.util.Collection;

public class ConverterImpl implements Converter {

	public ConverterImpl() {
	}

	public static void main(String[] args) throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		manager.loadOntologyFromOntologyDocument(new File(Ontology_Path.BENCHMARK2));
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File(Ontology_Path.BENCHMARK1));
		VowlData vowlData = new VowlData();

		// TODO Vielleicht mithilfe von Klassenannotationen Unterteilung schaffen und dann die on the fly die annotierten Klassen holen und ausführen
		preParsing(ontology, vowlData, manager);
		parsing(ontology, vowlData, manager);
		postParsing(ontology, vowlData, manager);

		//exportToConsole(vowlData);
		exportToFile(vowlData);
	}

	private static void preParsing(OWLOntology ontology, VowlData vowlData, OWLOntologyManager manager) {
		OWLOntologyWalker walker = new OWLOntologyWalker(ontology.getImportsClosure());
		walker.walkStructure(new EntityCreationVisitor(vowlData));
		new OntologyInformationParser(vowlData, ontology).execute();
	}

	private static void parsing(OWLOntology ontology, VowlData vowlData, OWLOntologyManager manager) {
		processClasses(ontology, vowlData);
		processObjectProperties(ontology, vowlData);
		processDataProperties(ontology, vowlData);
		processIndividuals(ontology, vowlData, manager);
	}

	private static void processIndividuals(OWLOntology ontology, VowlData vowlData, OWLOntologyManager manager) {
		// TODO check all classes
		ontology.getClassesInSignature(Imports.INCLUDED).forEach(owlClass -> {
			// TODO Probably collection of ontologies?
			EntitySearcher.getIndividuals(owlClass, ontology).forEach(owlIndividual -> owlIndividual.accept(new IndividualsVisitor(vowlData, owlIndividual, owlClass, manager)));
		});
	}

	private static void processObjectProperties(OWLOntology ontology, VowlData vowlData) {
		for (OWLObjectProperty owlObjectProperty : ontology.getObjectPropertiesInSignature(Imports.INCLUDED)) {
			for (OWLObjectPropertyAxiom owlObjectPropertyAxiom : ontology.getAxioms(owlObjectProperty, Imports.INCLUDED)) {
				owlObjectPropertyAxiom.accept(new ObjectPropertyVisitor(vowlData, owlObjectProperty));
			}
		}
	}

	private static void processDataProperties(OWLOntology ontology, VowlData vowlData) {
		for (OWLDataProperty property : ontology.getDataPropertiesInSignature(Imports.INCLUDED)) {
			for (OWLDataPropertyAxiom propertyAxiom : ontology.getAxioms(property, Imports.INCLUDED)) {
				propertyAxiom.accept(new DataPropertyVisitor(vowlData, property));
			}
		}
	}

	private static void postParsing(OWLOntology loadedOntology, VowlData vowlData, OWLOntologyManager manager) {
		setCorrectType(vowlData.getEntityMap().values());
		parseAnnotations(vowlData, manager);
		fillDomainRanges(vowlData);
		createSubclassProperties(vowlData);
		new ImportedChecker(vowlData, manager, loadedOntology).execute();
	}

	private static void createSubclassProperties(VowlData vowlData) {
		new VowlSubclassPropertyGenerator(vowlData).execute();
	}

	private static void fillDomainRanges(VowlData vowlData) {
		new DomainRangeFiller(vowlData, vowlData.getProperties()).execute();
	}

	private static void exportToFile(VowlData vowlData) {
		Exporter exporter = new FileExporter(new File("export.json"));
		JsonGenerator generator = new JsonGenerator();
		try {
			generator.execute(vowlData);
			generator.export(exporter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void exportToConsole(VowlData vowlData) {
		Exporter exporter = new ConsoleExporter();
		JsonGenerator generator = new JsonGenerator();
		try {
			generator.execute(vowlData);
			generator.export(exporter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void processClasses(OWLOntology ontology, VowlData vowlData) {
		for (OWLClass owlClass : ontology.getClassesInSignature(Imports.INCLUDED)) {
			for (OWLClassAxiom owlClassAxiom : ontology.getAxioms(owlClass, Imports.INCLUDED)) {
				owlClassAxiom.accept(new OwlClassAxiomVisitor(vowlData, owlClass));
			}
		}
	}

	private static void parseAnnotations(VowlData vowlData, OWLOntologyManager manager) {
		AnnotationParser annotationParser = new AnnotationParser(vowlData, manager);
		annotationParser.parse();
	}

	public static void setCorrectType(Collection<AbstractEntity> entities) {
		for (AbstractEntity entity : entities) {
			entity.accept(new TypeSetter());
		}
	}

	@Override
	public void convert() {

	}

	@Override
	public void export(Exporter exporter) {

	}
}
