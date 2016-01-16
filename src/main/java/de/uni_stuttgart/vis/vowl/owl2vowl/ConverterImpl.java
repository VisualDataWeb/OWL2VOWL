/*
 * ConverterImpl.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Ontology_Path;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.types.Exporter;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.types.FileExporter;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.types.JsonGenerator;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.AbstractEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.owlapi.EntityCreationVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.owlapi.IndividualsVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.*;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.classes.OwlClassAxiomVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.property.DataPropertyVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.property.DomainRangeFiller;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.property.ObjectPropertyVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.property.VowlSubclassPropertyGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLOntologyWalker;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ConverterImpl implements Converter {
	private final JsonGenerator jsonGenerator = new JsonGenerator();
	protected String loadedOntologyPath;
	protected OWLOntologyManager manager;
	protected VowlData vowlData;
	private static final Logger logger = LogManager.getLogger(ConverterImpl.class);
	private OWLOntology ontology;

	public ConverterImpl(IRI ontologyIRI) throws OWLOntologyCreationException {
		this(ontologyIRI, Collections.<IRI>emptyList());
	}

	public ConverterImpl(IRI ontologyIRI, Collection<IRI> necessaryExternals) throws OWLOntologyCreationException {
		initApi();
		logger.info("Loading ontologies ... [" + ontologyIRI + ",  " + necessaryExternals + "]");

		if (!necessaryExternals.isEmpty()) {
			for (IRI externalIRI : necessaryExternals) {
				manager.loadOntology(externalIRI);
			}
			logger.info("External ontologies loaded!");
		}

		ontology = manager.loadOntology(ontologyIRI);
		String logOntoName = ontologyIRI.toString();
		loadedOntologyPath = ontologyIRI.toString();

		if (!ontology.isAnonymous()) {
			logOntoName = ontology.getOntologyID().getOntologyIRI().get().toString();
		} else {
			logger.info("Ontology IRI is anonymous. Use loaded URI/IRI instead.");
		}

		logger.info("Ontologies loaded! Main Ontology: " + logOntoName);
	}

	public ConverterImpl(OWLOntology ontology, String ontologyIRI) {
		initApi();
		this.ontology = ontology;
		loadedOntologyPath = ontologyIRI;
	}

	public ConverterImpl(OWLOntology ontology) {
		initApi();
		this.ontology = ontology;
	}

	private void preParsing(OWLOntology ontology, VowlData vowlData, OWLOntologyManager manager) {
		OWLOntologyWalker walker = new OWLOntologyWalker(ontology.getImportsClosure());
		walker.walkStructure(new EntityCreationVisitor(vowlData));
		new OntologyInformationParser(vowlData, ontology).execute();
	}

	private void parsing(OWLOntology ontology, VowlData vowlData, OWLOntologyManager manager) {
		processClasses(ontology, vowlData);
		processObjectProperties(ontology, vowlData);
		processDataProperties(ontology, vowlData);
		processIndividuals(ontology, vowlData, manager);
	}

	private void processIndividuals(OWLOntology ontology, VowlData vowlData, OWLOntologyManager manager) {
		// TODO check all classes
		ontology.getClassesInSignature(Imports.INCLUDED).forEach(owlClass -> {
			for (OWLOntology owlOntology : manager.getOntologies()) {
				EntitySearcher.getIndividuals(owlClass, owlOntology).forEach(owlIndividual -> owlIndividual.accept(new IndividualsVisitor(vowlData, owlIndividual, owlClass, manager)));
			}
		});
	}

	private void processObjectProperties(OWLOntology ontology, VowlData vowlData) {
		for (OWLObjectProperty owlObjectProperty : ontology.getObjectPropertiesInSignature(Imports.INCLUDED)) {
			for (OWLObjectPropertyAxiom owlObjectPropertyAxiom : ontology.getAxioms(owlObjectProperty, Imports.INCLUDED)) {
				owlObjectPropertyAxiom.accept(new ObjectPropertyVisitor(vowlData, owlObjectProperty));
			}
		}
	}

	private void processDataProperties(OWLOntology ontology, VowlData vowlData) {
		for (OWLDataProperty property : ontology.getDataPropertiesInSignature(Imports.INCLUDED)) {
			for (OWLDataPropertyAxiom propertyAxiom : ontology.getAxioms(property, Imports.INCLUDED)) {
				propertyAxiom.accept(new DataPropertyVisitor(vowlData, property));
			}
		}
	}

	public static void main(String[] args) throws Exception {
		List<IRI> dep = new ArrayList<>();
		dep.add(IRI.create(new File(Ontology_Path.BENCHMARK2)));
		Converter converter = new ConverterImpl(IRI.create(new File(Ontology_Path.BENCHMARK1)), dep);
		converter.convert();
		converter.export(new FileExporter(new File("export.json")));
	}

	private void createSubclassProperties(VowlData vowlData) {
		new VowlSubclassPropertyGenerator(vowlData).execute();
	}

	private void fillDomainRanges(VowlData vowlData) {
		new DomainRangeFiller(vowlData, vowlData.getProperties()).execute();
	}

	private void processClasses(OWLOntology ontology, VowlData vowlData) {
		for (OWLClass owlClass : ontology.getClassesInSignature(Imports.INCLUDED)) {
			for (OWLClassAxiom owlClassAxiom : ontology.getAxioms(owlClass, Imports.INCLUDED)) {
				owlClassAxiom.accept(new OwlClassAxiomVisitor(vowlData, owlClass));
			}
		}
	}

	private void parseAnnotations(VowlData vowlData, OWLOntologyManager manager) {
		AnnotationParser annotationParser = new AnnotationParser(vowlData, manager);
		annotationParser.parse();
	}

	public void setCorrectType(Collection<AbstractEntity> entities) {
		for (AbstractEntity entity : entities) {
			entity.accept(new TypeSetter());
		}
	}

	private void postParsing(OWLOntology loadedOntology, VowlData vowlData, OWLOntologyManager manager) {
		setCorrectType(vowlData.getEntityMap().values());
		parseAnnotations(vowlData, manager);
		fillDomainRanges(vowlData);
		createSubclassProperties(vowlData);
		new ImportedChecker(vowlData, manager, loadedOntology, loadedOntologyPath).execute();
		vowlData.getEntityMap().values().forEach(entity -> entity.accept(new EquivalentSorter(ontology.getOntologyID().getOntologyIRI().or(IRI.create(loadedOntologyPath)), vowlData)));
		new BaseIriCollector(vowlData).execute();
		System.out.println(vowlData.getBaseIris());
	}

	protected void initApi() {
		manager = OWLManager.createOWLOntologyManager();
	}

	/**
	 * Executes the complete conversion to the webvowl compatible json format.
	 */
	public void convert() {
		vowlData = new VowlData();

		// TODO Vielleicht mithilfe von Klassenannotationen Unterteilung schaffen und dann die on the fly die annotierten Klassen holen und ausführen
		preParsing(ontology, vowlData, manager);
		parsing(ontology, vowlData, manager);
		postParsing(ontology, vowlData, manager);
	}

	/**
	 * Exports the generated data according to the implemented {@link Exporter}.
	 *
	 * @param exporter The exporter.
	 * @throws Exception Any exception during json generation.
	 */
	public void export(Exporter exporter) throws Exception {
		if (vowlData == null) {
			throw new IllegalAccessException("Ontology has to be converted first");
		}

		jsonGenerator.execute(vowlData);
		jsonGenerator.export(exporter);
	}
}
