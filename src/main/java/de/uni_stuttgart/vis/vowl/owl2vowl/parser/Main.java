/*
 * Test.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import de.uni_stuttgart.vis.vowl.owl2vowl.export.JsonExporter;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.Constants;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.OntologyInfo;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.OntologyMetric;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import de.uni_stuttgart.vis.vowl.owl2vowl.pipes.FormatText;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.metrics.*;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author Eduard Marbach
 */
public class Main {
	private static final boolean DEBUG_EXPORT = true;
	public static org.apache.logging.log4j.Logger logger = LogManager.getRootLogger();
	public static OWLOntologyManager manager;
	private static MapData mapData;
	private static GeneralParser parser;
	private static OWLOntology ontology;
	private static OWLDataFactory factory;

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Please call this program with a URI as parameter!");
			return;
		}

		Main mainO = new Main();
		mainO.initializeAPI();
		System.out.println("API loaded ...");

		try {
			mainO.loadOntologies(args[0]);
			System.out.println("Ontologie >" + args[0] + "< loaded! Starting convertion ...");
			mainO.startConvertion();
			mainO.reset();
		} catch (OWLOntologyCreationException e) {
			//e.printStackTrace();
			System.out.println("FAILED TO LOAD " + Arrays.toString(args));
			logger.error("FAILED TO LOAD " + Arrays.toString(args));
		}
	}

	public static void debugLoading() {
		Main mainO = new Main();
		mainO.initializeAPI();

		File quickExport = new File(Constants.BENCHMARK1);
		File nec = new File(Constants.BENCHMARK2);
		File prov = new File(Constants.PERSONAS);

		File test = new File(Constants.PATH_VARIABLE);
		File[] children = test.listFiles();

		if (children == null) {
			throw new IllegalStateException("Directory doesn't contain files.");
		}

		for (File curr : children) {
			if (curr.isDirectory()) {
				continue;
			}

			try {
				//mainO.loadOntologies(curr);
				//mainO.loadOntologies(new File(Constants.SIOC));
				//mainO.loadOntologies(quickExport, Arrays.asList(nec));
				mainO.loadOntologies(Constants.EXT_ONTOVIBE);
				mainO.startConvertion();
				mainO.reset();
				break;
			} catch (OWLOntologyCreationException e) {
				//e.printStackTrace();
				System.out.println("FAILED TO LOAD " + curr.getName());
				logger.error("FAILED TO LOAD " + curr.getName());
			}
		}

	}

	private static void parseOntoInfo() {
		OntologyInfo info = mapData.getOntologyInfo();

		IRI ontoIri = ontology.getOntologyID().getOntologyIRI();
		IRI versionIri = ontology.getOntologyID().getVersionIRI();

		if (ontoIri != null) {
			info.setIri(ontoIri.toString());
		}

		if (versionIri != null) {
			info.setVersion(versionIri.toString());
		}

		/* Save available annotations */
		for (OWLAnnotation i : ontology.getAnnotations()) {
			if (i.getProperty().toString().equals(Constants.INFO_CREATOR)) {
				info.setAuthor(FormatText.cutQuote(i.getValue().toString()));
			}
			if (i.getProperty().toString().equals(Constants.INFO_DESCRIPTION)) {
				info.setDescription(FormatText.cutQuote(i.getValue().toString()));
			}
			if (i.getProperty().toString().equals(Constants.INFO_ISSUED)) {
				info.setIssued(FormatText.cutQuote(i.getValue().toString()));
			}
			if (i.getProperty().toString().equals(Constants.INFO_LICENSE)) {
				info.setLicense(FormatText.cutQuote(i.getValue().toString()));
			}
			if (i.getProperty().toString().equals(Constants.INFO_RDFS_LABEL)) {
				info.setRdfsLabel(FormatText.cutQuote(i.getValue().toString()));
			}
			if (i.getProperty().toString().equals(Constants.INFO_SEE_ALSO)) {
				info.setSeeAlso(FormatText.cutQuote(i.getValue().toString()));
			}
			if (i.getProperty().toString().equals(Constants.INFO_TITLE)) {
				info.setTitle(FormatText.cutQuote(i.getValue().toString()));
			}
			if (i.getProperty().toString().equals(Constants.INFO_VERSION_INFO)) {
				info.setVersion(FormatText.cutQuote(i.getValue().toString()));
			}
		}


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

	/**
	 * Save the metrics of the current loaded ontology.
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

	public void startConvertion() {
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
		parseOntoInfo();
		parseClasses(classes);
		//parseDatatypes(datatypes);
		parseObjectProperty(objectProperties);
		parseDatatypeProperties(dataProperties);
		parseMetrics();

		/*
		Further processing of the gained data. Eq. IRIs will be transformed to IDs where necessary
		*/
		processor.processClasses();
		//processor.processDatatypes();
		processor.processProperties();

		System.out.println("Ontology data parsed!");

		if (DEBUG_EXPORT) {
			File location = getJarDir(this.getClass());
			File exportFile = new File(location, FilenameUtils.removeExtension(ontology.getOntologyID().getOntologyIRI().getFragment()) + ".json");
			JsonExporter exporter = new JsonExporter(exportFile);

			try {
				exporter.execute(mapData);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
