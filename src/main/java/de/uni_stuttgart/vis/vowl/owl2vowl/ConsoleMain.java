package de.uni_stuttgart.vis.vowl.owl2vowl;

import de.uni_stuttgart.vis.vowl.owl2vowl.converter.Converter;
import de.uni_stuttgart.vis.vowl.owl2vowl.converter.IRIConverter;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.types.ConsoleExporter;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.types.Exporter;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.types.FileExporter;
import org.apache.commons.cli.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.IRI;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ConsoleMain {
	private static final String IRI_OPTION_NAME = "iri";
	private static final String FILE_OPTION_NAME = "file";
	private static final String OUTPUT_OPTION_NAME = "output";
	private static final String DEPENDENCIES_OPTION_NAME = "dependencies";
	private static final String HELP_OPTION_NAME = "h";
	private static final String ECHO_OPTION_NAME = "echo";
	private static final Logger logger = LogManager.getLogger(ConsoleMain.class);

	public static void main(String[] args) {
		new ConsoleMain().parseCommandLine(args);
	}

	protected void parseCommandLine(String[] args) {
		CommandLine line = null;
		Options options = createOptions();

		try {
			line = new DefaultParser().parse(options, args);
		} catch (ParseException exp) {
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
			printHelpMenu(options);
			System.exit(0);
		}

		if (line.hasOption(HELP_OPTION_NAME)) {
			printHelpMenu(options);
			System.exit(0);
		}

		IRI ontologyIri;
		List<IRI> dependencies = new ArrayList<>();

		if (line.hasOption(FILE_OPTION_NAME)) {
			ontologyIri = IRI.create(new File(line.getOptionValue(FILE_OPTION_NAME)));
		} else {
			// rename the file here
			String iriName=line.getOptionValue(IRI_OPTION_NAME);
			String out=iriName.replace(" ","%20");
			//ontologyIri = IRI.create(line.getOptionValue(IRI_OPTION_NAME));
			ontologyIri = IRI.create(out);
		}

		if (line.hasOption(DEPENDENCIES_OPTION_NAME)) {
			for (String path : line.getOptionValues(DEPENDENCIES_OPTION_NAME)) {
				File dependency = new File(path);
				dependencies.add(IRI.create(dependency));
			}
		}

		try {
			Converter converter = new IRIConverter(ontologyIri, dependencies);
			converter.convert();
			converter.export(createExporterFromOption(line, ontologyIri));
			converter=null;
			line=null;
			ontologyIri=null;
			dependencies.clear();
			dependencies=null;
		} catch (Exception e) {
			logger.error("FAILED TO LOAD " + Arrays.toString(args));
			System.err.println(e.getClass().getName());
			System.err.println(e.getMessage());
			System.exit(1);
		}
		Runtime runtime = Runtime.getRuntime();
		NumberFormat format = NumberFormat.getInstance();
		long allocatedMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();
	
		System.gc();
		System.runFinalization();
		System.out.println("Cleaning up Memory for session of console converseion  << closed Session ");
		System.out.println("Conversion Finished ");
	
		// create some statistics
		format = NumberFormat.getInstance();
		long after_allocatedMemory = runtime.totalMemory();
		long after_freeMemory = runtime.freeMemory();
		System.out.println("-> USED MEMORY " + format.format((allocatedMemory - freeMemory) / 1024)      + "  ->    "+format.format( (after_allocatedMemory- after_freeMemory) / 1024)+"  ");
	}

	protected Options createOptions() {
		Options options = new Options();
		options.addOption(Option.builder(HELP_OPTION_NAME).desc("views this help text").build());

		OptionGroup inputOptions = new OptionGroup();
		inputOptions.setRequired(true);
		inputOptions.addOption(Option.builder(IRI_OPTION_NAME).argName("IRI").hasArg().desc("the iri of an ontology").build());
		inputOptions.addOption(Option.builder(FILE_OPTION_NAME).argName("PATH").hasArg().desc("the local path to an ontology").build());
		options.addOption(Option.builder(DEPENDENCIES_OPTION_NAME).argName("PATHS").hasArgs().desc("paths to dependencies of a local ontology").build());

		OptionGroup outputOptions = new OptionGroup();
		outputOptions.addOption(Option.builder(ECHO_OPTION_NAME).desc("prints the converted ontology on the console").build());
		outputOptions.addOption(Option.builder(OUTPUT_OPTION_NAME).argName("PATH").hasArg().desc("specify the path for the desired output location").build());

		options.addOptionGroup(inputOptions);
		options.addOptionGroup(outputOptions);

		return options;
	}

	protected void printHelpMenu(Options options) {
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp("java -jar owl2vowl.jar", options);
	}

	protected Exporter createExporterFromOption(CommandLine line, IRI ontologyIri) {
		if (line.hasOption(ECHO_OPTION_NAME)) {
			return new ConsoleExporter();
		} else {
			String exportPath = null;

			if (line.hasOption(OUTPUT_OPTION_NAME)) {
				exportPath = line.getOptionValue(OUTPUT_OPTION_NAME);
			}

			return generateFileExporter(ontologyIri, exportPath);
		}
	}

	protected FileExporter generateFileExporter(IRI ontologyIri, String filePath) {
		String filename;

		if (filePath != null) {
			filename = filePath;
		} else {
			// catch empty remainder
			try {
				filename = FilenameUtils.removeExtension(ontologyIri.getRemainder().get()) + ".json";
			} catch (Exception e){
				System.out.println("Failed to extract filename from iri");
				System.out.println("Reason: "+e);
				String defaultName="default.json";
				System.out.println("Writing to '"+defaultName+"'");
				filename=defaultName;
			}
		}

		return new FileExporter(new File(filename));
	}
}
