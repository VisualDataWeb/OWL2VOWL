/*
 * Test.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import de.uni_stuttgart.vis.vowl.owl2vowl.export.JsonExporter;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.BaseEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.Constants;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.classes.BaseClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.datatypes.BaseDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.properties.OwlDatatypeProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.properties.OwlObjectProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.pipes.FormatText;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Eduard Marbach
 */
public class Main {
	private static final boolean DEBUG_EXPORT = true;
	private static Map<String, BaseClass> classMap = new HashMap<>();
	private static Map<String, BaseDatatype> datatypeMap = new HashMap<>();
	private static Map<String, OwlObjectProperty> objectPropertyMap = new HashMap<>();
	private static Map<String, OwlDatatypeProperty> datatypePropertyMap = new HashMap<>();

	private static Map<String, OWLClass> owlClasses = new HashMap<>();
	private static Map<String, OWLDatatype> owlDatatypes = new HashMap<>();
	private static Map<String, OWLObjectProperty> owlObjectProperties = new HashMap<>();
	private static Map<String, OWLDataProperty> owlDatatypeProperties = new HashMap<>();

	private static OWLOntologyManager manager;
	private static OWLOntology ontology;
	private static OWLDataFactory factory;

	public static void main(String[] args) {
		File ont = new File(Constants.MUTO);
		manager = OWLManager.createOWLOntologyManager();
		factory = manager.getOWLDataFactory();

		try {
			ontology = manager.loadOntologyFromOntologyDocument(IRI.create(ont));
			Set<OWLClass> classes = ontology.getClassesInSignature();
			Set<OWLDatatype> datatypes = ontology.getDatatypesInSignature();
			Set<OWLObjectProperty> objectProperties = ontology.getObjectPropertiesInSignature();
			Set<OWLDataProperty> dataProperties = ontology.getDataPropertiesInSignature();

			ProcessUnit processor = new ProcessUnit(ontology, factory);

			parseClasses(classes);
			parseDatatypes(datatypes);
			parseObjectProperty(objectProperties);
			parseDatatypeProperties(dataProperties);

			processor.processClasses(classMap, owlClasses);
			processor.processDatatypes(datatypeMap, owlDatatypes);

			// printClasses(classMap);

		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}


		if (DEBUG_EXPORT) {
			JsonExporter exporter =
				new JsonExporter(new File("C:\\Users\\Eduard\\Documents\\test.json"));

			try {
				exporter.processNamespace();
				exporter.processHeader();
				exporter.processClasses(classMap);
				exporter.processDatatypes(datatypeMap);
				exporter.processObjectProperties(objectPropertyMap);
				exporter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void parseDatatypeProperties(Set<OWLDataProperty> dataProperties) {
		// TODO
	}

	private static void parseObjectProperty(Set<OWLObjectProperty> objectProperties) {
		int indexCounter = objectPropertyMap.size() + 1;

		for (OWLObjectProperty currentProperty : objectProperties) {
			Set<OWLAnnotation> owlPropAnoSet = currentProperty.getAnnotations(ontology);
			String rdfsDomain = "";
			String rdfsRange = "";
			String rdfsInversOf = "";
			String rdfsLabel = "";
			String rdfsComment = "";
			String rdfsIsDefinedBy = "";
			String owlVersionInfo = "";
			String objectPropertyIRI = currentProperty.getIRI().toString();
			Boolean isDeprecated = false;

			OwlObjectProperty theProperty = new OwlObjectProperty();

			// Proceed the annotations.
			for (OWLAnnotation owlPropAno : owlPropAnoSet) {
				OWLAnnotationProperty annotationProperty = owlPropAno.getProperty();
				OWLAnnotationValue annotationValue = owlPropAno.getValue();

				if (annotationProperty.isComment()) {
					rdfsComment = annotationValue.toString();
				} else if (annotationProperty.isDeprecated()) {
					isDeprecated = true;
				} else if (annotationProperty.isLabel()) {
					rdfsLabel = annotationValue.toString();
				} else if (annotationProperty.toString().equals(Constants.RDFS_DEFINED_BY)) {
					rdfsIsDefinedBy = annotationValue.toString();
				} else if (annotationProperty.toString().equals(Constants.OWL_VERSIONINFO)) {
					owlVersionInfo = annotationValue.toString();
				} else {
					System.out.println("Not used annotation: " + owlPropAno);
				}
			}

			// get the domain of the property
			for (OWLClassExpression domain : currentProperty.getDomains(ontology)) {
				if (!domain.isAnonymous()) {
					rdfsDomain = domain.asOWLClass().getIRI().toString();
				} else {
					// TODO
					String classExpressionType = domain.getClassExpressionType().toString();
					Set<OWLClass> classesInSignature = domain.getClassesInSignature();

					for (OWLClass classInSig : classesInSignature) {
					}
				}
			}

			// get the range of the property
			for (OWLClassExpression domain : currentProperty.getRanges(ontology)) {
				if (!domain.isAnonymous()) {
					rdfsRange = domain.asOWLClass().getIRI().toString();
				} else {
					// TODO
					String classExpressionType = domain.getClassExpressionType().toString();
					Set<OWLClass> classesInSignature = domain.getClassesInSignature();

					for (OWLClass classInSig : classesInSignature) {
					}
				}
			}

			if (rdfsLabel.isEmpty()) {
				rdfsLabel = extractNameFromIRI(objectPropertyIRI);
			}

			theProperty.setName(rdfsLabel);
			theProperty.setComment(rdfsComment);
			theProperty.setIri(objectPropertyIRI);
			theProperty.setDefinedBy(rdfsIsDefinedBy);
			theProperty.setOwlVersion(owlVersionInfo);
			theProperty.setId("objectProperty" + indexCounter);

			BaseClass classDomain = classMap.get(rdfsDomain);
			BaseClass classRange = classMap.get(rdfsRange);
			BaseDatatype datatypeDomain = datatypeMap.get(rdfsDomain);
			BaseDatatype datatypeRange = datatypeMap.get(rdfsRange);

			if(classDomain != null) {
				theProperty.setDomain(classDomain.getId());
			} else if(datatypeDomain != null) {
				theProperty.setDomain(datatypeDomain.getId());
			} else {
					System.out.println("Unknown domain: " + rdfsDomain);
			}

			if(classRange != null) {
				theProperty.setRange(classRange.getId());
			}  else if(datatypeRange != null) {
				theProperty.setRange(datatypeRange.getId());
			} else {
				System.out.println("Unknown range: " + rdfsRange);
			}

			indexCounter++;

			owlObjectProperties.put(currentProperty.getIRI().toString(), currentProperty);
			objectPropertyMap.put(theProperty.getIri(), theProperty);
		}
	}

	private static void parseDatatypes(Set<OWLDatatype> datatypes) {
		int indexCounter = 1;

		for (OWLDatatype currentDatatype : datatypes) {
			String className = "";
			String classIRI = currentDatatype.getIRI().toString();
			String classComment = "";
			String definedBy = "";
			String owlVersion = "";
			Boolean isDeprecated = false;
			Set<OWLAnnotation> currentClassAnnotations = currentDatatype.getAnnotations(ontology);

			TypeFinder finder = new TypeFinder(ontology, factory);

			BaseDatatype theDatatype = finder.findVowlDatatype(currentDatatype);

			// Work over all annotations.
			for (OWLAnnotation annotation : currentClassAnnotations) {
				OWLAnnotationProperty annotationProperty = annotation.getProperty();
				OWLAnnotationValue annotationValue = annotation.getValue();

				if (annotationProperty.isComment()) {
					classComment = annotationValue.toString();
				} else if (annotationProperty.isDeprecated()) {
					isDeprecated = true;
				} else if (annotationProperty.isLabel()) {
					className = annotationValue.toString();
				} else if (annotationProperty.toString().equals(Constants.RDFS_DEFINED_BY)) {
					definedBy = annotationValue.toString();
				} else if (annotationProperty.toString().equals(Constants.OWL_VERSIONINFO)) {
					owlVersion = annotationValue.toString();
				} else {
					System.out.println("Not used annotation: " + annotation);
				}
			}

			if (className.isEmpty()) {
				className = extractNameFromIRI(classIRI);
			}

			theDatatype.setName(className);
			theDatatype.setComment(classComment);
			theDatatype.setIri(classIRI);
			theDatatype.setDefinedBy(definedBy);
			theDatatype.setOwlVersion(owlVersion);
			theDatatype.setId("datatype" + indexCounter);

			indexCounter++;

			owlDatatypes.put(currentDatatype.getIRI().toString(), currentDatatype);
			datatypeMap.put(theDatatype.getIri(), theDatatype);
		}
	}

	private static void parseClasses(Set<OWLClass> classes) {
		int indexCounter = 1;

		for (OWLClass currentClass : classes) {
			String className = "";
			String classIRI = currentClass.getIRI().toString();
			String classComment = "";
			String definedBy = "";
			String owlVersion = "";
			Boolean isDeprecated = false;
			String type = "";
			Set<OWLAnnotation> currentClassAnnotations = currentClass.getAnnotations(ontology);

			TypeFinder finder = new TypeFinder(ontology, factory);
			BaseClass theClass = finder.findVowlClass(currentClass);

			// Work over all annotations.
			for (OWLAnnotation annotation : currentClassAnnotations) {
				OWLAnnotationProperty annotationProperty = annotation.getProperty();
				OWLAnnotationValue annotationValue = annotation.getValue();

				if (annotationProperty.isComment()) {
					classComment = annotationValue.toString();
				} else if (annotationProperty.isDeprecated()) {
					isDeprecated = true;
				} else if (annotationProperty.isLabel()) {
					className = annotationValue.toString();
				} else if (annotationProperty.toString().equals(Constants.RDFS_DEFINED_BY)) {
					definedBy = annotationValue.toString();
				} else if (annotationProperty.toString().equals(Constants.OWL_VERSIONINFO)) {
					owlVersion = annotationValue.toString();
				} else {
					System.out.println("Not used annotation: " + annotation);
				}
			}

			if (className.isEmpty()) {
				className = extractNameFromIRI(classIRI);
			}


			// Setting data in VOWLClass
			theClass.setName(FormatText.cutQuote(className));
			theClass.setComment(FormatText.cutQuote(classComment));
			theClass.setIri(classIRI);
			theClass.setId("class" + indexCounter);
			theClass.setDefinedBy(FormatText.cutQuote(definedBy));
			theClass.setOwlVersion(FormatText.cutQuote(owlVersion));

			if (isDeprecated) {
				theClass.getAttributes().add(Constants.TYPE_DEPRECTAEDCLASS);
			}

			owlClasses.put(currentClass.getIRI().toString(), currentClass);
			classMap.put(theClass.getIri(), theClass);

			indexCounter++;
		}
	}

	public static String extractValue(OWLAnnotation annotation, String annotationName) {
		String returnValue = "";

		if (annotationName.equals(annotationName)) {
			returnValue = annotation.getValue().toString();
		}

		return returnValue;
	}

	private static boolean hasDifferentNamespace(String elementNamespace, IRI ontologyNamespace) {
		if (elementNamespace == null || ontologyNamespace == null) {
			return false;
		}

		return !(elementNamespace.contains(ontologyNamespace));
	}

	private static String extractNameFromIRI(String iri) {
		String name;

		if (iri.contains("#")) {
			// IRI contains a # -> take name behind #
			name = iri.substring(iri.indexOf("#") + 1);
		} else {
			if (iri.contains("/")) {
				// IRI contains / -> take name behind the last /
				name = iri.substring(iri.lastIndexOf("/") + 1);
			} else {
				// No suitable name found.
				name = iri;
			}
		}

		return name;
	}
}
