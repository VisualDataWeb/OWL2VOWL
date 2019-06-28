package de.uni_stuttgart.vis.vowl.owl2vowl.converter;

import de.uni_stuttgart.vis.vowl.owl2vowl.export.types.Exporter;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.types.JsonGenerator;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.AbstractEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.owlapi.EntityCreationVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.owlapi.IndividualsVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.AnnotationParser;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.BaseIriCollector;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.EquivalentSorter;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.ImportedChecker;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.OntologyInformationParser;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.TypeSetter;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.classes.GenericClassAxiomVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.classes.HasKeyAxiomParser;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.classes.OwlClassAxiomVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.property.DataPropertyVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.property.DomainRangeFiller;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.property.ObjectPropertyVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.property.VowlSubclassPropertyGenerator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;

import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLOntologyWalker;

import java.util.Collection;
import java.util.Set;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Abstract converter which processes the most part of the converting. The sub
 * classes can specify the source of the ontology or to some additional
 * processing if necessary.
 */
public abstract class AbstractConverter implements Converter {

	private static final Logger logger = LogManager.getLogger(AbstractConverter.class);

	protected final JsonGenerator jsonGenerator = new JsonGenerator();
	protected String loadedOntologyPath;
	protected String loadingInfoString = "";
	protected boolean missingImports = false;
	protected OWLOntologyManager manager;
	protected VowlData vowlData;
	protected OWLOntology ontology;
	protected boolean initialized = false;
	protected OWLAPI_MissingImportsListener missingListener = null;
	protected boolean currentlyLoading = false;
	protected String parentLine = "";

	public void setCurrentlyLoadingFlag(boolean val) {
		this.parentLine = "";
		currentlyLoading = val;
	}

	public void releaseMemory() {
		manager.removeMissingImportListener(missingListener);
		manager.removeOntology(ontology);
		vowlData.Destructore();
		vowlData = null;
		missingListener = null;
		manager = null;
		ontology = null;
	}

	public void setCurrentlyLoadingFlag(String parentLine, boolean val) {
		// Line where to append to
		this.parentLine = parentLine;
		currentlyLoading = val;
	}

	public boolean getCurrentlyLoadingFlag() {
		return currentlyLoading;
	}

	public void clearLoadingMsg() {
		loadingInfoString = "";
	}

	public String msgForWebVOWL(String stackTrace) {
		// converts the < and > tags to html string so no html injection is created
		String s1 = stackTrace.replaceAll("<", "&lt;");
		String s2 = s1.replaceAll(">", "&gt;");
		return s2;
	}

	public boolean ontologyHasMissingImports() {
		return missingImports;
	}

	public void setOntologyHasMissingImports(boolean val) {
		missingImports = val;
	}

	public String getLoadingInfoString() {
		return loadingInfoString;
	}

	public void addLoadingInfo(String msg) {
		loadingInfoString += msg;
	}

	public void addLoadingInfoToLine(String parent, String msg) {
		// find parent line in msg;
		if (loadingInfoString.length() > 0) {
			String tokens[] = loadingInfoString.split("\n");
			for (int i = 0; i < tokens.length; i++) {
				if (tokens[i].contains(parent)) {
					tokens[i] += msg;
				}
			}
			// replace loading msg;
			loadingInfoString = "";
			for (int i = 0; i < tokens.length - 1; i++) {
				if (tokens[i].length() > 0) {
					loadingInfoString += tokens[i] + "\n";
				}
			}
			loadingInfoString += tokens[tokens.length - 1] + "\n";
		}
	};

	public void addLoadingInfoToParentLine(String msg) {
		if (this.parentLine.length() > 0) {
			addLoadingInfoToLine(this.parentLine, msg);
		}
	}

	private void preLoadOntology() {
		try {
			loadOntology();
		} catch (OWLOntologyCreationException e) {
			throw new RuntimeException(e);
		}

		initialized = true;
	}

	protected abstract void loadOntology() throws OWLOntologyCreationException;

	private void preParsing(OWLOntology ontology, VowlData vowlData, OWLOntologyManager manager) {
		OWLOntologyWalker walker = new OWLOntologyWalker(ontology.importsClosure().collect(Collectors.toSet()));
		EntityCreationVisitor ecv = new EntityCreationVisitor(vowlData);
		// this is a set of annotation assertions;
		Set<OWLAxiom> ax = ontology.axioms(Imports.INCLUDED).collect(Collectors.toSet());
		for (OWLAxiom entry : ax) {
			try {
				// try to cast this as assertion axiom and add ot to VOWL Data
				OWLAnnotationAssertionAxiom castedEntry = (OWLAnnotationAssertionAxiom) entry;
				String subject = castedEntry.getSubject().asIRI().get().toString();
				String property = castedEntry.getProperty().getIRI().toString();
				String value = "";
				if (castedEntry.getProperty().getIRI().getShortForm().toString().compareTo("targetElement") == 0
						|| castedEntry.getProperty().getIRI().getShortForm().toString().compareTo("subjectElement") == 0
						|| castedEntry.getProperty().getIRI().getShortForm().toString()
								.compareTo("predicateElement") == 0
						|| castedEntry.getProperty().getIRI().getShortForm().toString()
								.compareTo("providesNotation") == 0
						||

						castedEntry.getProperty().getIRI().getShortForm().toString()
								.compareTo("subjectDescription") == 0
						|| castedEntry.getProperty().getIRI().getShortForm().toString()
								.compareTo("predicateDescription") == 0
						|| castedEntry.getProperty().getIRI().getShortForm().toString()
								.compareTo("objectDescription") == 0
						||

						castedEntry.getProperty().getIRI().getShortForm().toString().compareTo("providesView") == 0
						|| castedEntry.getProperty().getIRI().getShortForm().toString()
								.compareTo("viewUsesNotation") == 0
						|| castedEntry.getProperty().getIRI().getShortForm().toString().compareTo("objectElement") == 0
						|| castedEntry.getProperty().getIRI().getShortForm().toString().compareTo("isTypeOf") == 0) {
					value = castedEntry.getValue().toString();
				} else {

					OWLAnnotationValue le_val = castedEntry.getValue();
					OWLLiteral le_literal = le_val.asLiteral().get();
					value = le_literal.getLiteral();

				}
				vowlData.addBaseConstructorAnnotation(subject, property, value);
			} catch (Exception e) {
				// we dont care
				// this is a declaration axiom and not an assertion axiom , we are interessted
				// in assertion to baseConstructors
			}
		}

		try {
			walker.walkStructure(ecv);
			logger.info("WalkStructure Success!");
		} catch (Exception e) {
			logger.info("@WORKAROUND WalkStructure Failed!");
			logger.info("Exception: " + e);
		}
		// vowlData.showBaseConstructorAnnotations();

		new OntologyInformationParser(vowlData, ontology).execute();
	}

	private void parsing(OWLOntology ontology, VowlData vowlData, OWLOntologyManager manager) {
		processAnnotationProperties(ontology, vowlData);
		processClasses(ontology, vowlData);
		processObjectProperties(ontology, vowlData);
		processDataProperties(ontology, vowlData);
		processIndividuals(ontology, vowlData, manager);

		processGenericAxioms();
	}

	private void processIndividuals(OWLOntology ontology, VowlData vowlData, OWLOntologyManager manager) {
		ontology.classesInSignature(Imports.INCLUDED).collect(Collectors.toSet()).forEach(owlClass -> {
			for (OWLOntology owlOntology : manager.ontologies().collect(Collectors.toSet())) {
				try {
					EntitySearcher.getIndividuals(owlClass, owlOntology).forEach(owlIndividual -> owlIndividual
							.accept(new IndividualsVisitor(vowlData, owlIndividual, owlClass, manager)));
				} catch (Exception e) {
					logger.info("@WORKAROUND: Failed to accept some individuals ... SKIPPING THIS");
					logger.info("Exception: " + e);
					logger.info("----------- Continue Process --------");
				}

			}
		});
	}

	private void processObjectProperties(OWLOntology ontology, VowlData vowlData) {
		for (OWLObjectProperty owlObjectProperty : ontology.objectPropertiesInSignature(Imports.INCLUDED)
				.collect(Collectors.toSet())) {
			for (OWLObjectPropertyAxiom owlObjectPropertyAxiom : ontology.axioms(owlObjectProperty, Imports.INCLUDED)
					.collect(Collectors.toSet())) {
				try {
					owlObjectPropertyAxiom.accept(new ObjectPropertyVisitor(vowlData, owlObjectProperty));
				} catch (Exception e) {
					logger.info(
							"          @WORKAROUND: Failed to accept property with HAS_VALUE OR  SubObjectPropertyOf ... SKIPPING THIS");
					logger.info("          propertyName: " + owlObjectProperty);
					logger.info("          propertyAxiom: " + owlObjectPropertyAxiom);
				}
			}
		}
	}

	private void processDataProperties(OWLOntology ontology, VowlData vowlData) {
		for (OWLDataProperty property : ontology.dataPropertiesInSignature(Imports.INCLUDED)
				.collect(Collectors.toSet())) {
			for (OWLDataPropertyAxiom propertyAxiom : ontology.axioms(property, Imports.INCLUDED)
					.collect(Collectors.toSet())) {
				try {
					propertyAxiom.accept(new DataPropertyVisitor(vowlData, property));
				} catch (Exception e) {
					logger.info("DataPropertyVisitor : Failed to accept OWLDataPropertyAxiom -> Skipping");
				}
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
		// use classExpressions;

		for (OWLClass owlClass : ontology.classesInSignature(Imports.INCLUDED).collect(Collectors.toSet())) {
			for (OWLClassAxiom owlClassAxiom : ontology.axioms(owlClass, Imports.INCLUDED)
					.collect(Collectors.toSet())) {
				OwlClassAxiomVisitor temp = new OwlClassAxiomVisitor(vowlData, owlClass);
				try {
					owlClassAxiom.accept(temp);
					temp.Destrucotre();
					temp = null;
				} catch (Exception e) {
					logger.info("ProcessClasses : Failed to accept owlClassAxiom -> Skipping");
				}
			}
			HasKeyAxiomParser.parse(ontology, owlClass, vowlData);
			owlClass = null;
		}
	}

	private void processAnnotationProperties(OWLOntology ontology, VowlData vowlData) {
		for (OWLAnnotationProperty owlAnnotationProperty : ontology.annotationPropertiesInSignature(Imports.INCLUDED)
				.collect(Collectors.toSet())) {
			for (OWLAnnotationAxiom owlAnnotationAxiom : ontology.axioms(owlAnnotationProperty, Imports.INCLUDED)
					.collect(Collectors.toSet())) {
				try {
					String subject = owlAnnotationProperty.getIRI().toString();
					String property = "http://visualdataweb.org/ontologies/gizmo-core#assertionDatatypeValue";
					OWLAnnotationPropertyRangeAxiom ra = (OWLAnnotationPropertyRangeAxiom) owlAnnotationAxiom;
					String value = ra.getRange().toString();
					vowlData.addBaseConstructorAnnotation(subject, property, value);
				} catch (Exception e) {
					// not interesting
				}
			}
		}
	}

	private void processGenericAxioms() {
		ontology.generalClassAxioms().collect(Collectors.toSet())
				.forEach(owlClassAxiom -> owlClassAxiom.accept(new GenericClassAxiomVisitor(vowlData)));
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
		vowlData.getEntityMap().values().forEach(entity -> entity.accept(new EquivalentSorter(
				ontology.getOntologyID().getOntologyIRI().orElse(IRI.create(loadedOntologyPath)), vowlData)));
		new BaseIriCollector(vowlData).execute();

		PrefixDocumentFormat pm = (PrefixDocumentFormat) manager.getOntologyFormat(ontology);
		Map<String, String> prefixName2PrefixMap = pm.getPrefixName2PrefixMap();
		// adding prefix list to that thing;
		for (Map.Entry<String, String> entry : prefixName2PrefixMap.entrySet()) {
			vowlData.addPrefix(entry.getKey(), entry.getValue());
		}

		// get the importedOntologies;
		Stream<OWLOntology> imported = ontology.imports();
		for (OWLOntology importedOntology : imported.collect(Collectors.toSet())) {
			PrefixDocumentFormat i_pm = (PrefixDocumentFormat) manager.getOntologyFormat(importedOntology);
			Map<String, String> i_prefixName2PrefixMap = i_pm.getPrefixName2PrefixMap();
			// adding prefix list to that thing;
			for (Map.Entry<String, String> entry : i_prefixName2PrefixMap.entrySet()) {
				vowlData.addPrefix(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * Executes the complete conversion to the webvowl compatible json format.
	 * Normally is only called ones during export. But if a new conversion is
	 * required just call this method before exporting.
	 * <p>
	 * The parsing is separated in three steps: The pre parsing -> normal parsing ->
	 * post parsing. This is necessary because we can access some properties of the
	 * entities only if we have the corresponding entity. For this example the pre
	 * parsing can be used to retrieve all entities without any additional special
	 * components. After that we can access the more special properties of the
	 * entity with helper classes like {@link EntitySearcher}.
	 * </p>
	 */
	public void convert() {
		// adding some debug hints for identifying crashes in the public version
		
		System.out.println("Converting Ontology:");
		if (!initialized) {
			System.out.println("  -> preLoadOntontology()"+new java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Calendar.getInstance().getTime()));
			preLoadOntology();
		}
		vowlData = new VowlData();
		vowlData.setOwlManager(manager);
		// TODO Probably the parsing could be automated via class annotation and
		// annotation parsing.
		// e.q. @PreParsing, @Parsing, @PostParsing just as an idea for improvement

		this.addLoadingInfo("* Generating ontology graph ");
		this.setCurrentlyLoadingFlag("* Generating ontology graph ", true);
		try {
			System.out.println("  -> preParsing() "+new java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Calendar.getInstance().getTime()));
			preParsing(ontology, vowlData, manager);

			System.out.println("  -> parsing()"+new java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Calendar.getInstance().getTime()));
			parsing(ontology, vowlData, manager);

			System.out.println("  -> postParsing() "+new java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Calendar.getInstance().getTime()));
			postParsing(ontology, vowlData, manager);

			this.addLoadingInfoToParentLine("... done");
			System.out.println("--- Finished --- "+new java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Calendar.getInstance().getTime()));

			this.setCurrentlyLoadingFlag(false);
		} catch (Exception e) {
			this.addLoadingInfoToParentLine("... <span style='color:red;'>failed</span>");
			this.addLoadingInfoToParentLine("\n  <span style='color:red;'>Error :</span>\n");
			this.addLoadingInfoToParentLine(msgForWebVOWL(e.getMessage()));
			this.setCurrentlyLoadingFlag(false);
		}
	}

	// removed Metrics Process >> WebVOWL computes the statistics
	// private void processMetrics() {
	// OntologyMetric metrics = new OntologyMetric(ontology);
	// metrics.calculate(vowlData);
	// vowlData.setMetrics(metrics);
	// }

	/**
	 * Exports the generated data according to the implemented {@link Exporter}.
	 *
	 * @param exporter
	 *            The exporter.
	 * @throws Exception
	 *             Any exception during json generation.
	 */
	public void export(Exporter exporter) throws Exception {
		if (vowlData == null) {
			convert();
		}
		jsonGenerator.execute(vowlData);
		jsonGenerator.export(exporter);
		releaseMemory();
	}
}
