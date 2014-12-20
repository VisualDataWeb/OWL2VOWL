/*
 * GeneralParsing.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Annotations;
import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Vowl_Lang;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.OwlThing;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import de.uni_stuttgart.vis.vowl.owl2vowl.pipes.FormatText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class GeneralParser {
	private static final boolean LOG_ANNOTATIONS = false;
	protected static final Logger logger = LogManager.getRootLogger();
	protected OWLOntology ontology;
	protected OWLDataFactory factory;
	protected MapData mapData;
	protected String rdfsLabel = "";
	protected String rdfsComment = "";
	protected String rdfsIsDefinedBy = "";
	protected String owlVersionInfo = "";
	protected Boolean isDeprecated = false;
	protected Map<String, String> comments;
	protected Map<String, String> languageToLabel;
	protected String iri;
	protected OWLOntologyManager ontologyManager;

	public GeneralParser(OWLOntology ontology, OWLDataFactory factory, MapData mapData, OWLOntologyManager ontologyManager) {
		this.ontology = ontology;
		this.factory = factory;
		this.mapData = mapData;
		this.ontologyManager = ontologyManager;
	}

	public Map<String, String> getLanguageToLabel() {
		return languageToLabel;
	}

	public void setLanguageToLabel(Map<String, String> languageToLabel) {
		this.languageToLabel = languageToLabel;
	}

	public String getIri() {
		return iri;
	}

	public void setIri(String iri) {
		this.iri = iri;
	}

	public String getRdfsLabel() {
		return rdfsLabel;
	}

	public void setRdfsLabel(String rdfsLabel) {
		this.rdfsLabel = rdfsLabel;
	}

	public String getRdfsComment() {
		return rdfsComment;
	}

	public void setRdfsComment(String rdfsComment) {
		this.rdfsComment = rdfsComment;
	}

	public String getRdfsIsDefinedBy() {
		return rdfsIsDefinedBy;
	}

	public void setRdfsIsDefinedBy(String rdfsIsDefinedBy) {
		this.rdfsIsDefinedBy = rdfsIsDefinedBy;
	}

	public String getOwlVersionInfo() {
		return owlVersionInfo;
	}

	public void setOwlVersionInfo(String owlVersionInfo) {
		this.owlVersionInfo = owlVersionInfo;
	}

	public Boolean getIsDeprecated() {
		return isDeprecated;
	}

	public void setIsDeprecated(Boolean isDeprecated) {
		this.isDeprecated = isDeprecated;
	}

	public void parseAnnotations(OWLEntity entity) {
		rdfsLabel = "";
		rdfsComment = "";
		isDeprecated = false;
		rdfsIsDefinedBy = "";
		owlVersionInfo = "";
		languageToLabel = new HashMap<String, String>();

		OWLAnnotationProperty labelProp = factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());
		OWLAnnotationProperty commentProp = factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI());

		languageToLabel = parseLanguage(entity, labelProp);
		languageToLabel.put(Vowl_Lang.LANG_DEFAULT, FormatText.cutQuote(extractNameFromIRI(entity.getIRI().toString())));
		comments = parseLanguage(entity, commentProp);

		for (OWLOntology currentOntology : ontologyManager.getOntologies()) {
			for (OWLAnnotation owlPropAno : entity.getAnnotations(currentOntology)) {
				OWLAnnotationProperty annotationProperty = owlPropAno.getProperty();
				OWLAnnotationValue annotationValue = owlPropAno.getValue();

				if (annotationProperty.isComment()) {
					rdfsComment = annotationValue.toString();
				} else if (annotationProperty.isDeprecated()) {
					isDeprecated = true;
				} else if (annotationProperty.isLabel()) {
					rdfsLabel = annotationValue.toString();
				} else if (annotationProperty.toString().equals(Annotations.RDFS_DEFINED_BY)) {
					rdfsIsDefinedBy = annotationValue.toString();
				} else if (annotationProperty.toString().equals(Annotations.OWL_VERSIONINFO)) {
					owlVersionInfo = annotationValue.toString();
				} else if (LOG_ANNOTATIONS) {
					System.out.println("Not used annotation: " + owlPropAno);
				}
			}
		}
	}

	/**
	 * Processes all available languages in the given rdf property of the owl entity.
	 *
	 * @param entity   The entity to search in,
	 * @param property The desired property like rdf:comment or rdfs:label
	 * @return A mapping of language to the value.
	 */
	protected Map<String, String> parseLanguage(OWLEntity entity, OWLAnnotationProperty property) {
		Map<String, String> workingMap = new HashMap<String, String>();

		for (OWLOntology owlOntology : ontologyManager.getOntologies()) {
			for (OWLAnnotation owlAnnotation : entity.getAnnotations(owlOntology, property)) {
				if (owlAnnotation.getValue() instanceof OWLLiteral) {
					OWLLiteral val = (OWLLiteral) owlAnnotation.getValue();

					if (val.isRDFPlainLiteral()) {
						if (val.getLang().isEmpty()) {
							workingMap.put(Vowl_Lang.LANG_UNSET, val.getLiteral());
							mapData.getAvailableLanguages().add(Vowl_Lang.LANG_UNSET);
						} else {
							workingMap.put(val.getLang(), val.getLiteral());
							mapData.getAvailableLanguages().add(val.getLang());
						}
					}
				}
			}
		}

		return workingMap;
	}

	protected String extractNameFromIRI(String iri) {
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
				name = "";
			}
		}

		return name;
	}

	protected BaseNode findNode(String nodeIRI) {
		return mapData.findNode(nodeIRI);
	}

	/**
	 * Searches for a thing which is not connected to any class. Only possible connections are:
	 * Things, Literal and Datatypes
	 * TODO vielleicht sofort neues erzeugen, wenn keins vorhanden?
	 *
	 * @return The id of a not connected thing. If not available null.
	 */
	protected BaseNode getDisconnectedThing() {
		for (Map.Entry<String, OwlThing> i : mapData.getThingMap().entrySet()) {
			if (i.getValue().isFree()) {
				return i.getValue();
			}
		}

		return null;
	}

	public void handleOntologyInfo() {
		OntoInfoParser parser = new OntoInfoParser(ontology, factory, mapData, ontologyManager);
		parser.execute();
	}

	public void handleClass(Set<OWLClass> classes) {
		GeneralParser parser = new ClassParser(classes, ontology, factory, mapData, ontologyManager);
		parser.execute();
	}

	public void handleDatatype(Set<OWLDatatype> datatypes) {
		GeneralParser parser = new DatatypeParser(datatypes, ontology, factory, mapData, ontologyManager);
		parser.execute();
	}

	public void handleObjectProperty(Set<OWLObjectProperty> objectProperties) {
		GeneralParser parser = new ObjectPropertyParser(objectProperties, ontology, factory, mapData, ontologyManager);
		parser.execute();
	}

	public void handleDatatypeProperty(Set<OWLDataProperty> dataProperties) {
		GeneralParser parser = new DatatypePropertyParser(dataProperties, ontology, factory, mapData, ontologyManager);
		parser.execute();
	}

	/**
	 * Should be implemented in inherited classes.
	 */
	protected void execute() {
	}
}
