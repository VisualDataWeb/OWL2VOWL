/*
 * Test.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import de.uni_stuttgart.vis.vowl.owl2vowl.export.ConsoleExporter;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.FileExporter;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.JsonGenerator;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.OntologyMetric;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper.ExportNames;
import org.apache.commons.cli.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.metrics.*;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author Vincent Link
 * @author Eduard Marbach
 */
public class Main {
	private static final boolean DEBUG_EXPORT = false;
	private static final boolean CONVERSION = false;
	private static final String IRI_OPTION_NAME = "iri";
	private static final String FILE_OPTION_NAME = "file";
	private static final String HELP_OPTION_NAME = "h";
	private static final String ECHO_OPTION_NAME = "echo";
	public static org.apache.logging.log4j.Logger logger = LogManager.getRootLogger();
	public static OWLOntologyManager manager;
	public static OWLDataFactory factory;
	private static MapData mapData;
	private static GeneralParser parser;
	private static OWLOntology ontology;

	public static void main(String[] args) {
		if (CONVERSION) {
			convertAllOntologies();
			return;
		}

		CommandLine line = null;
		Options options = createOptions();


		try {
			line = new BasicParser().parse(options, args);
		} catch (ParseException exp) {
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
			printHelpMenuAndExit(options);
		}
		if (line.hasOption(HELP_OPTION_NAME)) {
			printHelpMenuAndExit(options);
		}

		Main mainO = new Main();
		mainO.initializeAPI();

		try {
			if (line.hasOption(FILE_OPTION_NAME)) {
				mainO.loadOntologies(new File(line.getOptionValue(FILE_OPTION_NAME)));
			} else {
				mainO.loadOntologies(line.getOptionValue(IRI_OPTION_NAME));
			}
			mainO.startConvertion(line.hasOption(ECHO_OPTION_NAME));
			mainO.reset();
		} catch (OWLOntologyCreationException e) {
			logger.error("FAILED TO LOAD " + Arrays.toString(args));
			System.exit(1);
		}
	}

	private static void printHelpMenuAndExit(Options options) {
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp("java -jar owl2vowl.jar", options);
		System.exit(0);
	}

	private static Options createOptions() {
		Options options = new Options();

		options.addOption(new Option(HELP_OPTION_NAME, "views this help text"));

		OptionGroup inputOptions = new OptionGroup();
		inputOptions.setRequired(true);
		inputOptions.addOption(OptionBuilder.withArgName("IRI")
				.hasArg()
				.withDescription("the iri of an ontology")
				.create(IRI_OPTION_NAME));
		inputOptions.addOption(OptionBuilder.withArgName("PATH")
				.hasArg()
				.withDescription("the local path to an ontology")
				.create(FILE_OPTION_NAME));

		OptionGroup outputOptions = new OptionGroup();
		outputOptions.addOption(new Option(ECHO_OPTION_NAME, "prints the converted ontology on the console"));

		options.addOptionGroup(inputOptions);
		options.addOptionGroup(outputOptions);

		return options;
	}

	public static void convertAllOntologies() {
		Main mainO = new Main();
		mainO.initializeAPI();

		List<String> externalOntologies = new ArrayList<String>();
		externalOntologies.add(ExportNames.EXPORT_FOAF);
		externalOntologies.add(ExportNames.EXPORT_MUTO);
		externalOntologies.add(ExportNames.EXPORT_PERSONAS);
		externalOntologies.add(ExportNames.EXPORT_SIOC);
		externalOntologies.add(ExportNames.EXPORT_ONTOVIVBE);

		for (String externalOntology : externalOntologies) {
			try {
				mainO.loadOntologies(externalOntology);
				System.out.println("Ontology: " + externalOntology + " loaded!");
				mainO.startConvertion(false);
				mainO.reset();
				System.out.println();
			} catch (OWLOntologyCreationException e) {
				System.out.println(e.getMessage());
				System.out.println("FAILED TO LOAD " + externalOntology);
				logger.error("FAILED TO LOAD " + externalOntology);
			}
		}
	}

	/**
	 * Save the metrics of the current loaded ontology.
	 * TODO Should be optimized probalby...
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

	/**
	 * Compute the absolute file path to the jar file.
	 * The framework is based on http://stackoverflow.com/a/12733172/1614775
	 * But that gets it right for only one of the four cases.
	 *
	 * @param aclass A class residing in the required jar.
	 * @return A File object for the directory in which the jar file resides.
	 * During testing with NetBeans, the result is ./build/classes/,
	 * which is the directory containing what will be in the jar.
	 */
	public static File getJarDir(Class aclass) {
		URL url;
		String extURL;      //  url.toExternalForm();

		// get an url
		try {
			url = aclass.getProtectionDomain().getCodeSource().getLocation();
			// url is in one of two forms
			//        ./build/classes/   NetBeans test
			//        jardir/JarName.jar  froma jar
		} catch (SecurityException ex) {
			url = aclass.getResource(aclass.getSimpleName() + ".class");
			// url is in one of two forms, both ending "/com/physpics/tools/ui/PropNode.class"
			//          file:/U:/Fred/java/Tools/UI/build/classes
			//          jar:file:/U:/Fred/java/Tools/UI/dist/UI.jar!
		}

		// convert to external form
		extURL = url.toExternalForm();

		// prune for various cases
		if (extURL.endsWith(".jar"))   // from getCodeSource
			extURL = extURL.substring(0, extURL.lastIndexOf("/"));
		else {  // from getResource
			String suffix = "/" + (aclass.getName()).replace(".", "/") + ".class";
			extURL = extURL.replace(suffix, "");
			if (extURL.startsWith("jar:") && extURL.endsWith(".jar!"))
				extURL = extURL.substring(4, extURL.lastIndexOf("/"));
		}

		// convert back to url
		try {
			url = new URL(extURL);
		} catch (MalformedURLException mux) {
			// leave url unchanged; probably does not happen
		}

		// convert url to File
		try {
			return new File(url.toURI());
		} catch (URISyntaxException ex) {
			return new File(url.getPath());
		}
	}

	private void reset() {
		manager.removeOntology(ontology);
	}

	public void initializeAPI() {
		logger.info("Initializing OWL API ...");
		manager = OWLManager.createOWLOntologyManager();
		factory = manager.getOWLDataFactory();
		logger.info("OWL API initialized!");
	}

	public void loadOntologies(File mainOnto, List<File> necessaryExternals) throws OWLOntologyCreationException {
		logger.info("Loading ontologies ... [" + mainOnto + ",  " + necessaryExternals + "]");

		for (File current : necessaryExternals) {
			manager.loadOntologyFromOntologyDocument(current);
		}

		logger.info("External ontologies loaded!");

		ontology = manager.loadOntologyFromOntologyDocument(mainOnto);

		logger.info("Ontologies loaded! Main Ontology: " + ontology.getOntologyID().getOntologyIRI());
	}

	public void loadOntologies(File mainOnto) throws OWLOntologyCreationException {
		logger.info("Loading ontology ... " + mainOnto);

		ontology = manager.loadOntologyFromOntologyDocument(mainOnto);

		logger.info("Ontology loaded! Main Ontology: " + ontology.getOntologyID().getOntologyIRI());
	}

	public void loadOntologies(String linkToOntology) throws OWLOntologyCreationException {
		logger.info("Loading ontologies ... " + linkToOntology);
		ontology = manager.loadOntology(IRI.create(linkToOntology));
		logger.info("Ontologies loaded! Main Ontology: " + ontology.getOntologyID().getOntologyIRI().getFragment());
	}

	public void startConvertion(boolean exportToConsole) {
		mapData = new MapData();

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
		parser.handleOntologyInfo();
		parser.handleClass(classes);
		//parseDatatypes(datatypes);
		parser.handleObjectProperty(objectProperties);
		parser.handleDatatypeProperty(dataProperties);
		parseMetrics();

		/*
		Further processing of the gained data. Eq. IRIs will be transformed to IDs where necessary
		*/
		processor.processClasses();
		//processor.processDatatypes();
		processor.processProperties();

		JsonGenerator exporter;

		if (exportToConsole) {
			exporter = new JsonGenerator(new ConsoleExporter());
		} else {
			File exportFile;

			if (DEBUG_EXPORT) {
				String exportName = getDebugExportName(ontology);

				if (exportName != null) {
					String filePath = System.getProperty("user.dir") + "/WebVOWL/src/js/data/";
					exportFile = new File(filePath, exportName + ".json");
				} else {
					logger.error("No suitable export name found to export: " + ontology.getOntologyID().getOntologyIRI().toString());
					return;
				}
			} else {
				File location = getJarDir(this.getClass());
				exportFile = new File(location, FilenameUtils.removeExtension(ontology.getOntologyID().getOntologyIRI().getFragment()) + ".json");
			}
			exporter = new JsonGenerator(new FileExporter(exportFile));
		}

		try {
			exporter.execute(mapData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getDebugExportName(OWLOntology ontology) {
		String iri = ontology.getOntologyID().getOntologyIRI().toString();

		if (iri.equals(ExportNames.EXPORT_FOAF)) {
			return ExportNames.FILENAME_FOAF;
		} else if (iri.equals(ExportNames.EXPORT_GEONAMES)) {
			return ExportNames.FILENAME_GEONAMES;
		} else if (iri.equals(ExportNames.EXPORT_MARINE)) {
			return ExportNames.FILENAME_MARINE;
		} else if (iri.equals(ExportNames.EXPORT_MARINE2)) {
			return ExportNames.FILENAME_MARINE2;
		} else if (iri.equals(ExportNames.EXPORT_MUTO)) {
			return ExportNames.FILENAME_MUTO;
		} else if (iri.equals(ExportNames.EXPORT_ONTOVIVBE)) {
			return ExportNames.FILENAME_ONTOVIVBE;
		} else if (iri.equals(ExportNames.EXPORT_PERSONAS)) {
			return ExportNames.FILENAME_PERSONAS;
		} else if (iri.equals(ExportNames.EXPORT_PROV)) {
			return ExportNames.FILENAME_PROV;
		} else if (iri.equals(ExportNames.EXPORT_SIOC)) {
			return ExportNames.FILENAME_SIOC;
		} else if (iri.equals(ExportNames.EXPORT_RELATIONS)) {
			return ExportNames.FILENAME_RELATIONS;
		}

		return null;
	}
}
