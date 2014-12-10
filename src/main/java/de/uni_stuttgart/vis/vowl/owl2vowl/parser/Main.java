/*
 * Test.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import de.uni_stuttgart.vis.vowl.owl2vowl.export.ConsoleExporter;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.FileExporter;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.JsonGenerator;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.Constants;
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
 * @author Eduard Marbach
 */
public class Main {
	private static final boolean DEBUG_EXPORT = false;
	private static final boolean CONVERSION = false;
	private static final String IRI_OPTION_NAME = "iri";
	private static final String HELP_OPTION_NAME = "h";
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

		if (DEBUG_EXPORT) {
			debugLoading();
			return;
		}

		CommandLine line = null;
		Options options = createOptions();

		String ontologyIri = null;

		try {
			line = new BasicParser().parse(options, args);
		} catch (ParseException exp) {
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
			System.exit(1);
		}
		if (line.hasOption(HELP_OPTION_NAME) || line.getOptions().length == 0) {
			HelpFormatter helpFormatter = new HelpFormatter();
			helpFormatter.printHelp("OWL2VOWL", options);
			System.exit(0);
		}
		ontologyIri = line.getOptionValue(IRI_OPTION_NAME);


		Main mainO = new Main();
		mainO.initializeAPI();
//		System.out.println("API loaded ...");

		try {
			mainO.loadOntologies(ontologyIri);
//			System.out.println("Ontologie >" + ontologyIri + "< loaded! Starting convertion ...");
			mainO.startConvertion(true);
			mainO.reset();
		} catch (OWLOntologyCreationException e) {
			//e.printStackTrace();
			logger.error("FAILED TO LOAD " + Arrays.toString(args));
			System.exit(1);
		}
	}

	private static Options createOptions() {
		Options options = new Options();

		Option helpOption = new Option(HELP_OPTION_NAME, "views this help text");

		Option iriOption = OptionBuilder.withArgName("IRI")
				.isRequired()
				.hasArg()
				.withDescription("the iri of an ontology")
				.create(IRI_OPTION_NAME);

		options.addOption(helpOption);
		options.addOption(iriOption);

		return options;
	}

	public static void debugLoading() {
		Main mainO = new Main();
		mainO.initializeAPI();
		try {
			mainO.loadOntologies(Constants.EXT_NICETAG);
			//mainO.loadOntologies(new File(Constants.WINE));
			mainO.startConvertion(false);
			mainO.reset();
		} catch (OWLOntologyCreationException e) {
			//e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	public static void convertAllOntologies() {
		Main mainO = new Main();
		mainO.initializeAPI();

		File filePath = new File(Constants.PATH_VARIABLE);
		File[] children = filePath.listFiles();

		List<String> externalOntologies = new ArrayList<String>();
		externalOntologies.add(Constants.EXT_GEONAMES);
		externalOntologies.add(Constants.EXT_ONTOVIBE);
		externalOntologies.add(Constants.EXT_PIZZA);
		externalOntologies.add(Constants.EXT_PROV);

		if (children == null) {
			throw new IllegalStateException("Directory doesn't contain files.");
		}

		for (File curr : children) {
			if (curr.isDirectory()) {
				continue;
			}

			try {
				mainO.loadOntologies(curr);
				System.out.println("Ontology: " + curr + " loaded!");
				mainO.startConvertion(false);
				mainO.reset();
				System.out.println();
			} catch (OWLOntologyCreationException e) {
				//e.printStackTrace();
				System.out.println(e.getMessage());
				System.out.println("FAILED TO LOAD " + curr.getName());
				logger.error("FAILED TO LOAD " + curr.getName());
			}
		}

		for (String externalOntology : externalOntologies) {
			try {
				mainO.loadOntologies(externalOntology);
				System.out.println("Ontology: " + externalOntology + " loaded!");
				mainO.startConvertion(false);
				mainO.reset();
				System.out.println();
			} catch (OWLOntologyCreationException e) {
				//e.printStackTrace();
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

		logger.info("Ontologies loaded! Main Ontolgy: " + ontology.getOntologyID().getOntologyIRI());
	}

	public void loadOntologies(File mainOnto) throws OWLOntologyCreationException {
		logger.info("Loading ontology ... " + mainOnto);

		ontology = manager.loadOntologyFromOntologyDocument(mainOnto);

		logger.info("Ontology loaded! Main Ontolgy: " + ontology.getOntologyID().getOntologyIRI());
	}

	public void loadOntologies(String linkToOntology) throws OWLOntologyCreationException {
		logger.info("Loading ontologies ... " + linkToOntology);
		ontology = manager.loadOntology(IRI.create(linkToOntology));
		logger.info("Ontologies loaded! Main Ontolgy: " + ontology.getOntologyID().getOntologyIRI().getFragment());
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

//		System.out.println("Ontology data parsed!");

		JsonGenerator exporter;

		if (exportToConsole) {
			exporter = new JsonGenerator(new ConsoleExporter());
		} else {
			File exportFile;

			if (DEBUG_EXPORT) {
				String exportName = getDebugExportName(ontology);

				if(exportName != null) {
					String filePath = System.getProperty("user.dir") + "\\WebVOWL\\src\\js\\data\\";
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
