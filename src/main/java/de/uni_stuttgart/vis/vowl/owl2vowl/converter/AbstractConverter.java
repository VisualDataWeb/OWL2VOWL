package de.uni_stuttgart.vis.vowl.owl2vowl.converter;

import de.uni_stuttgart.vis.vowl.owl2vowl.export.types.Exporter;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.types.JsonGenerator;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.AbstractEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.ontology.OntologyMetric;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.owlapi.EntityCreationVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.owlapi.IndividualsVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.*;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.classes.GenericClassAxiomVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.classes.HasKeyAxiomParser;
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

import java.util.Collection;

/**
 * Abstract converter which processes the most part of the converting.
 * The sub classes can specify the source of the ontology or to some additional processing if necessary.
 */
public abstract class AbstractConverter implements Converter {
	private static final Logger logger = LogManager.getLogger(AbstractConverter.class);
	protected final JsonGenerator jsonGenerator = new JsonGenerator();
	protected String loadedOntologyPath;
	protected OWLOntologyManager manager;
	protected VowlData vowlData;
	protected OWLOntology ontology;
	protected boolean initialized = false;

	private void preLoadOntology() {
		initApi();

		try {
			loadOntology();
		} catch (OWLOntologyCreationException e) {
			throw new RuntimeException(e);
		}

		initialized = true;
	}

	protected abstract void loadOntology() throws OWLOntologyCreationException;

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
		processGenericAxioms();
	}

	private void processIndividuals(OWLOntology ontology, VowlData vowlData, OWLOntologyManager manager) {
		// TODO check all classes
		ontology.getClassesInSignature(Imports.INCLUDED).forEach(owlClass -> {
			for (OWLOntology owlOntology : manager.getOntologies()) {
				EntitySearcher.getIndividuals(owlClass, owlOntology).forEach(owlIndividual -> owlIndividual.accept(new IndividualsVisitor(vowlData,
						owlIndividual, owlClass, manager)));
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

			HasKeyAxiomParser.parse(ontology, owlClass, vowlData);
		}
	}

	private void processGenericAxioms() {
		ontology.getGeneralClassAxioms().forEach(owlClassAxiom -> owlClassAxiom.accept(new GenericClassAxiomVisitor(vowlData)));
	}

	private void parseAnnotations(VowlData vowlData, OWLOntologyManager manager) {
		AnnotationParser annotationParser = new AnnotationParser(vowlData, manager);
		annotationParser.parse();
	}

	private void setCorrectType(Collection<AbstractEntity> entities) {
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
		vowlData.getEntityMap().values().forEach(entity -> entity.accept(new EquivalentSorter(ontology.getOntologyID().getOntologyIRI().orElse(IRI
				.create(loadedOntologyPath)), vowlData)));
		new BaseIriCollector(vowlData).execute();
	}

	private void initApi() {
		manager = OWLManager.createOWLOntologyManager();
	}

	/**
	 * Executes the complete conversion to the webvowl compatible json format.
	 * Normally is only called ones during export. But if a new conversion is required just call this method before exporting.
	 * <p>
	 *     The parsing is separated in three steps: The pre parsing -> normal parsing -> post parsing.
	 *     This is necessary because we can access some properties of the entities only if we have the corresponding entity.
	 *     For this example the pre parsing can be used to retrieve all entities without any additional special components.
	 *     After that we can access the more special properties of the entity with helper classes like {@link EntitySearcher}.
	 * </p>
	 */
	public void convert() {
		if (!initialized) {
			preLoadOntology();
		}

		vowlData = new VowlData();
		vowlData.setOwlManager(manager);
		// TODO Probably the parsing could be automatized via class annotation and annotation parsing.
		// e.q. @PreParsing, @Parsing, @PostParsing just as an idea for improvement
		preParsing(ontology, vowlData, manager);
		parsing(ontology, vowlData, manager);
		postParsing(ontology, vowlData, manager);
		processMetrics();
	}

	private void processMetrics() {
		OntologyMetric metrics = new OntologyMetric(ontology);
		metrics.calculate(vowlData);
		vowlData.setMetrics(metrics);
	}

	/**
	 * Exports the generated data according to the implemented {@link Exporter}.
	 *
	 * @param exporter The exporter.
	 * @throws Exception Any exception during json generation.
	 */
	public void export(Exporter exporter) throws Exception {
		if (vowlData == null) {
			convert();
		}

		jsonGenerator.execute(vowlData);
		jsonGenerator.export(exporter);
	}
}
