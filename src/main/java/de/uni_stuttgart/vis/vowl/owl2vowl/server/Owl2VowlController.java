package de.uni_stuttgart.vis.vowl.owl2vowl.server;

import de.uni_stuttgart.vis.vowl.owl2vowl.Owl2Vowl;
import de.uni_stuttgart.vis.vowl.owl2vowl.constants.LoggingConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.io.IOUtils;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.io.StringWriter;

@RestController
public class Owl2VowlController {

	private static final Logger logger = LogManager.getLogger(Owl2VowlController.class);

	private static final Logger conversionLogger = LogManager.getLogger(LoggingConstants.CONVERSION_LOGGER);
	private static final String СONVERT_MAPPING = "/convert";
	private static final String READ_JSON = "/read";

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Parameter not correct")
	@ExceptionHandler(IllegalArgumentException.class)
	public void parameterExceptionHandler(Exception e) {
		logger.info("--- Parameter exception: " + e.getMessage());
		conversionLogger.error("Problems with parameters: " + e.getMessage());
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Ontology could not be created")
	@ExceptionHandler(OWLOntologyCreationException.class)
	public void ontologyCreationExceptionHandler(Exception e) {
		logger.info("--- Ontology creation exception: " + e.getMessage());
		conversionLogger.error("Problems in ontology creation process: " + e.getMessage());
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Problems while generating uploaded file on server.")
	@ExceptionHandler(IOException.class)
	public void fileExceptionHandler(Exception e) {
		logger.info("--- IO exception: " + e.getMessage());
		conversionLogger.error("IO exception while generating file on server: " + e.getMessage());
	}

	@RequestMapping(value = СONVERT_MAPPING, method = RequestMethod.POST)
	public String uploadOntology(@RequestParam("ontology") MultipartFile[] files) throws IOException,
			OWLOntologyCreationException {
		if (files == null || files.length == 0) {
			throw new IllegalArgumentException("No file uploaded!");
		}

		if (files.length > 1) {
			throw new IllegalArgumentException("Please upload only the main ontology!");
		}

		List<InputStream> inputStreams = new ArrayList<>();

		for (MultipartFile file : files) {
			inputStreams.add(file.getInputStream());
		}

		String jsonAsString;

		try {
			Owl2Vowl owl2Vowl = new Owl2Vowl(inputStreams.get(0));
			jsonAsString = owl2Vowl.getJsonAsString();
		} catch (Exception e) {
			conversionLogger.info("Uploaded files " + files[0].getName() + ": " + 0);
			throw e;
		}

		return jsonAsString;
	}

	@RequestMapping(value = СONVERT_MAPPING, method = RequestMethod.GET)
	public String convertIRI(@RequestParam("iri") String iri) throws IOException, OWLOntologyCreationException {
		String jsonAsString;
		String out=iri.replace(" ","%20");
		try {
			Owl2Vowl owl2Vowl = new Owl2Vowl(IRI.create(out));
			jsonAsString = owl2Vowl.getJsonAsString();
		} catch (Exception e) {
			conversionLogger.info(out + " " + 1);
			throw e;
		}

		conversionLogger.info(iri + " " + 0);
		return jsonAsString;
	}

	// adding some proxy functionallity

	@RequestMapping(value = READ_JSON, method = RequestMethod.POST)
	public String uploadJSON(@RequestParam("ontology") MultipartFile[] files) throws IOException,
			OWLOntologyCreationException {
		if (files == null || files.length == 0) {
			throw new IllegalArgumentException("No file uploaded!");
		}

		if (files.length > 1) {
			throw new IllegalArgumentException("Please upload only the main ontology!");
		}

		List<InputStream> inputStreams = new ArrayList<>();

		String jsonAsString="";
		for (MultipartFile file : files) {
			inputStreams.add(file.getInputStream());
		}

		try {
			jsonAsString=IOUtils.toString(inputStreams.get(0));
		}
		catch (Exception e){
			logger.info("Something went wrong");
			conversionLogger.error("Something went wrong " + e.getMessage());
		}

		return jsonAsString;
	}

	@RequestMapping(value = READ_JSON, method = RequestMethod.GET)
	public String readJsons(@RequestParam("json") String json) throws IOException, OWLOntologyCreationException {
		String jsonAsString="";
		try {
			InputStream input = new URL(json).openStream();
			try {
				jsonAsString=IOUtils.toString(input);
			} finally {
				IOUtils.closeQuietly(input);
			}
		}
		catch (Exception e){
			logger.info("Something went wrong");
			conversionLogger.error("Something went wrong " + e.getMessage());
		}
		return jsonAsString;
	}

}
