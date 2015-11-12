/*
 * Main.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl;

import de.uni_stuttgart.vis.vowl.owl2vowl.export.types.ConsoleExporter;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.types.Exporter;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.types.FileExporter;
import org.apache.commons.cli.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.IRI;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
	private static final String IRI_OPTION_NAME = "iri";
	private static final String FILE_OPTION_NAME = "file";
	private static final String DEPENDENCIES_OPTION_NAME = "dependencies";
	private static final String HELP_OPTION_NAME = "h";
	private static final String ECHO_OPTION_NAME = "echo";
	private static final Logger logger = LogManager.getRootLogger();

	public static void main(String[] args) {
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
		List<IRI> dependencies = new ArrayList<>();

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
			Converter converter = new ConverterImpl(ontologyIri, dependencies);
			converter.convert();
			converter.export(createExporterFromOption(line, ontologyIri));
		} catch (Exception e) {
			logger.error("FAILED TO LOAD " + Arrays.toString(args));
			System.err.println(e.getClass().getName());
			System.err.println(e.getMessage());
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
		String filename = FilenameUtils.removeExtension(ontologyIri.getRemainder().get()) + ".json";
		return new FileExporter(new File(filename));
	}
}
