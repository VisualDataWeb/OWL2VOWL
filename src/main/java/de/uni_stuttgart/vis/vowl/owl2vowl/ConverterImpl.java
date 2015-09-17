/*
 * ConverterImpl.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl;

import de.uni_stuttgart.vis.vowl.owl2vowl.export.types.ConsoleExporter;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.types.Exporter;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.types.JsonGenerator;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.owlapi.OwlClassVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.OwlEquivalentsVisitor;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.util.OWLOntologyWalker;

/**
 *
 */
public class ConverterImpl implements Converter {

	public ConverterImpl() {
	}

	public static void main(String[] args) throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager.loadOntology(IRI.create("http://ontovibe.visualdataweb.org/2.0"));

		VowlData vowlData = new VowlData();

		OWLOntologyWalker walker = new OWLOntologyWalker(ontology.getImportsClosure());
		walker.walkStructure(new OwlClassVisitor(vowlData));

		processClasses(ontology, vowlData);

		testExport(vowlData);
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

	public static void processClasses(OWLOntology ontology, VowlData vowlData) {
		for (OWLClass owlClass : ontology.getClassesInSignature()) {
			for (OWLClassAxiom owlClassAxiom : ontology.getAxioms(owlClass, Imports.INCLUDED)) {
				owlClassAxiom.accept(new OwlEquivalentsVisitor(vowlData, owlClass));
			}
		}
	}

	@Override
	public void convert() {

	}

	@Override
	public void export(Exporter exporter) {

	}
}
