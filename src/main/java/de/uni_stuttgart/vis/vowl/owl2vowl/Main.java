/*
 * Test.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Ontology_Path;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.ConsoleExporter;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.Exporter;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.FileExporter;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper.ExportNames;
import org.apache.commons.cli.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.IRI;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 */
public class Main {
	private static final boolean CONVERT_ONE = false;
	private static final boolean CONVERT_REQUIRED_ONTOLOGIES = false;
	private static final String IRI_OPTION_NAME = "iri";
	private static final String FILE_OPTION_NAME = "file";
	private static final String DEPENDENCIES_OPTION_NAME = "dependencies";
	private static final String HELP_OPTION_NAME = "h";
	private static final String ECHO_OPTION_NAME = "echo";
	private static final Logger logger = LogManager.getRootLogger();

	public static void main(String[] args) {
		if (CONVERT_ONE) {
			convertOneOntology();
			return;
		}
		if (CONVERT_REQUIRED_ONTOLOGIES) {
			convertAllOntologies();
			return;
		}

		parseCommandLine(args);
	}

	private static void parseCommandLine(String[] args) {
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

		IRI ontologyIri;
		List<IRI> dependencies = new ArrayList<IRI>();

		if (line.hasOption(FILE_OPTION_NAME)) {
			ontologyIri = IRI.create(new File(line.getOptionValue(FILE_OPTION_NAME)));
		} else {
			ontologyIri = IRI.create(line.getOptionValue(IRI_OPTION_NAME));
		}

		if (line.hasOption(DEPENDENCIES_OPTION_NAME)) {
			for (String path : line.getOptionValues(DEPENDENCIES_OPTION_NAME)) {
				File dependency = new File(path);
				dependencies.add(IRI.create(dependency));
			}
		}

		try {
			Converter converter = new Converter(ontologyIri, dependencies);
			converter.convert();

			converter.export(createExporterFromOption(line, ontologyIri));
		} catch (Exception e) {
			logger.error("FAILED TO LOAD " + Arrays.toString(args));
			System.exit(1);
		}
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

		options.addOption(OptionBuilder.withArgName("PATHS")
				.hasArgs()
				.withDescription("paths to dependencies of a local ontology")
				.create(DEPENDENCIES_OPTION_NAME));

		OptionGroup outputOptions = new OptionGroup();
		outputOptions.addOption(new Option(ECHO_OPTION_NAME, "prints the converted ontology on the console"));

		options.addOptionGroup(inputOptions);
		options.addOptionGroup(outputOptions);

		return options;
	}

	private static void printHelpMenuAndExit(Options options) {
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp("java -jar owl2vowl.jar", options);
		System.exit(0);
	}

	private static Exporter createExporterFromOption(CommandLine line, IRI ontologyIri) {
		if (line.hasOption(ECHO_OPTION_NAME)) {
			return new ConsoleExporter();
		} else {
			return generateFileExporter(ontologyIri);
		}
	}

	private static FileExporter generateFileExporter(IRI ontologyIri) {
		File exportFile;

		String filename = searchExportFilename(ontologyIri) + ".json";

		if (CONVERT_REQUIRED_ONTOLOGIES) {
			String filePath = System.getProperty("user.dir") + "/WebVOWL/src/js/data/";
			exportFile = new File(filePath, filename);
		} else {
			exportFile = new File(filename);
		}

		return new FileExporter(exportFile);
	}

	private static String searchExportFilename(IRI ontologyIri) {
		String iri = ontologyIri.toString();

		if (iri.equals(ExportNames.EXPORT_FOAF)) {
			return ExportNames.FILENAME_FOAF;
		} else if (iri.equals(ExportNames.EXPORT_GEONAMES)) {
			return ExportNames.FILENAME_GEONAMES;
		} else if (iri.equals(ExportNames.EXPORT_GOOD_RELATIONS)) {
			return ExportNames.FILENAME_GOOD_RELATIONS;
		} else if (iri.equals(ExportNames.EXPORT_GS1)) {
			return ExportNames.FILENAME_GS1;
		} else if (iri.equals(ExportNames.EXPORT_MARINE)) {
			return ExportNames.FILENAME_MARINE;
		} else if (iri.equals(ExportNames.EXPORT_MARINE2)) {
			return ExportNames.FILENAME_MARINE2;
		} else if (iri.equals(ExportNames.EXPORT_MUTO)) {
			return ExportNames.FILENAME_MUTO;
		} else if (iri.equals(ExportNames.EXPORT_ONTOVIBE)) {
			return ExportNames.FILENAME_ONTOVIBE;
		} else if (iri.equals(ExportNames.EXPORT_ONTOVIBE_2)) {
			return ExportNames.FILENAME_ONTOVIBE_2;
		} else if (iri.equals(ExportNames.EXPORT_PERSONAS)) {
			return ExportNames.FILENAME_PERSONAS;
		} else if (iri.equals(ExportNames.EXPORT_PROV)) {
			return ExportNames.FILENAME_PROV;
		} else if (iri.equals(ExportNames.EXPORT_SIOC)) {
			return ExportNames.FILENAME_SIOC;
		} else if (iri.equals(ExportNames.EXPORT_RELATIONS)) {
			return ExportNames.FILENAME_RELATIONS;
		}

		return FilenameUtils.removeExtension(ontologyIri.getFragment());
	}

	private static void convertAllOntologies() {
		List<String> externalOntologies = new ArrayList<String>();
		externalOntologies.add(ExportNames.EXPORT_FOAF);
		externalOntologies.add(ExportNames.EXPORT_MUTO);
		externalOntologies.add(ExportNames.EXPORT_PERSONAS);
		externalOntologies.add(ExportNames.EXPORT_SIOC);
		externalOntologies.add(ExportNames.EXPORT_ONTOVIBE);

		for (String externalOntology : externalOntologies) {
			try {
				IRI ontologyIri = IRI.create(externalOntology);
				Converter converter = new Converter(ontologyIri);

				System.out.println("Ontology: " + externalOntology + " loaded!");
				converter.convert();
				converter.export(generateFileExporter(ontologyIri));
				System.out.println();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.out.println("FAILED TO LOAD " + externalOntology);
				logger.error("FAILED TO LOAD " + externalOntology);
			}
		}
	}

	private static void convertOneOntology() {
		IRI fileIri = IRI.create(new File(Ontology_Path.BENCHMARK1));
		IRI requiredIri = IRI.create(new File(Ontology_Path.BENCHMARK2));

		try {
			Converter converter = new Converter(fileIri, Arrays.asList(requiredIri));
			converter.convert();
			converter.export(generateFileExporter(converter.getOntologyIri()));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

}
