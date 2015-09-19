/*
 * ConverterImpl.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Ontology_Path;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.types.ConsoleExporter;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.types.Exporter;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.types.JsonGenerator;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.AbstractEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.owlapi.OwlClassVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.AnnotationParser;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.OwlEquivalentsVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.TypeSetter;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
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
		preParsing(ontology, vowlData);
		parsing(ontology, vowlData);
		postParsing(vowlData, manager);

		testExport(vowlData);
	}

	private static void preParsing(OWLOntology ontology, VowlData vowlData) {
		OWLOntologyWalker walker = new OWLOntologyWalker(ontology.getImportsClosure());
		walker.walkStructure(new OwlClassVisitor(vowlData));
	}

	private static void parsing(OWLOntology ontology, VowlData vowlData) {
		processClasses(ontology, vowlData);
	}

	private static void postParsing(VowlData vowlData, OWLOntologyManager manager) {
		setCorrectType(vowlData.getEntityMap().values());
		parseAnnotations(vowlData, manager);
	}

	private static void testExport(VowlData vowlData) {
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
		for (OWLClass owlClass : ontology.getClassesInSignature()) {
			for (OWLClassAxiom owlClassAxiom : ontology.getAxioms(owlClass, Imports.INCLUDED)) {
				owlClassAxiom.accept(new OwlEquivalentsVisitor(vowlData, owlClass));
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
