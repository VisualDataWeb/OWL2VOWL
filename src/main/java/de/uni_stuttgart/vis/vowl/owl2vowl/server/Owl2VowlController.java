package de.uni_stuttgart.vis.vowl.owl2vowl.server;

import de.uni_stuttgart.vis.vowl.owl2vowl.Owl2Vowl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

@RestController
public class Owl2VowlController {

	private static final Logger conversionLogger = LogManager.getLogger("conversion");
	private static final String СONVERT_MAPPING = "/convert";

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Parameter not correct")
	@ExceptionHandler(IllegalArgumentException.class)
	public void parameterExceptionHandler(Exception e) {
		System.out.println("--- Parameter exception: " + e.getMessage());
		conversionLogger.error("Problems with parameters: " + e.getMessage());
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Ontology could not be created")
	@ExceptionHandler(OWLOntologyCreationException.class)
	public void ontologyCreationExceptionHandler(Exception e) {
		System.out.println("--- Ontology creation exception: " + e.getMessage());
		conversionLogger.error("Problems in ontology creation process: " + e.getMessage());
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Problems while generating uploaded file on server.")
	@ExceptionHandler(IOException.class)
	public void fileExceptionHandler(Exception e) {
		System.out.println("--- IO exception: " + e.getMessage());
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

		try {
			Owl2Vowl owl2Vowl = new Owl2Vowl(IRI.create(iri));
			jsonAsString = owl2Vowl.getJsonAsString();
		} catch (Exception e) {
			conversionLogger.info(iri + " " + 1);
			throw e;
		}

		conversionLogger.info(iri + " " + 0);
		return jsonAsString;
	}
}
