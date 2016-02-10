/*
 * Owl2VowlController.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.server;

import de.uni_stuttgart.vis.vowl.owl2vowl.Owl2Vowl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 */
@RestController
public class Owl2VowlController {

	private static final Logger conversionLogger = LogManager.getLogger("conversion");

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Parameter not correct")
	@ExceptionHandler(IllegalArgumentException.class)
	public void parameterException(Exception e) {
		System.out.println("--- Parameter exception: " + e.getMessage());
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Ontology could not be created")
	@ExceptionHandler(OWLOntologyCreationException.class)
	public void ontologyCreationException(Exception e) {
		System.out.println("--- Parameter exception: " + e.getMessage());
	}

	@RequestMapping(value = {"/owl2vowl", "/converter.php"}, method = RequestMethod.POST)
	public String uploadOntology(@RequestParam("ontology") MultipartFile[] files) throws IOException, OWLOntologyCreationException {
		if (files == null || files.length == 0) {
			throw new IllegalArgumentException("No file uploaded!");
		}

		List<File> createdFiles = new ArrayList<>();

		for (MultipartFile file : files) {
			byte[] bytes = file.getBytes();
			File serverFile = new File(UUID.randomUUID().toString().replace(" ", "%20"));

			while (serverFile.exists()) {
				serverFile = new File(UUID.randomUUID().toString().replace(" ", "%20"));
			}

			try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile))) {
				stream.write(bytes);
				createdFiles.add(serverFile);
			} catch (Exception e) {
				System.err.println("Error creating file: External file name <" + file.getName() + "> | local file name <" + serverFile.getName() + ">. Reason: " + e.getMessage());
				throw new IOException();
			}
		}

		IRI mainIri = IRI.create(createdFiles.get(0));
		List<IRI> dependencies = new ArrayList<>();

		if (createdFiles.size() > 1) {
			for (int i = 1; i < createdFiles.size(); i++) {
				File dependency = createdFiles.get(i);
				dependencies.add(IRI.create(dependency));
			}
		}

		String jsonAsString;

		try {
			Owl2Vowl owl2Vowl = new Owl2Vowl(mainIri, dependencies);
			jsonAsString = owl2Vowl.getJsonAsString();
		} catch (Exception e) {
			conversionLogger.info(mainIri + " " + 0);
			throw e;
		}

		return jsonAsString;
	}

	@RequestMapping(value = {"/owl2vowl", "/converter.php"}, method = RequestMethod.GET)
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
