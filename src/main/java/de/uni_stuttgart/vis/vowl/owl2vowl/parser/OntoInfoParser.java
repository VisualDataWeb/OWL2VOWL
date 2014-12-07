package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.Constants;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.OntologyInfo;
import de.uni_stuttgart.vis.vowl.owl2vowl.pipes.FormatText;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLLiteral;

import java.util.Map;

/**
 * Parser for the ontology information.
 */
public class OntoInfoParser extends GeneralParser {
	public void execute() {
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
		for (OWLAnnotation annotation : ontology.getAnnotations()) {
			if (annotation.getProperty().toString().equals(Constants.INFO_CREATOR)) {
				info.setAuthor(FormatText.cutQuote(annotation.getValue().toString()));
			}
			if (annotation.getProperty().toString().equals(Constants.INFO_DESCRIPTION)) {
				addLanguage(info.getLanguageToDescription(), annotation);
				info.setDescription(FormatText.cutQuote(annotation.getValue().toString()));
			}
			if (annotation.getProperty().toString().equals(Constants.INFO_ISSUED)) {
				info.setIssued(FormatText.cutQuote(annotation.getValue().toString()));
			}
			if (annotation.getProperty().toString().equals(Constants.INFO_LICENSE)) {
				info.setLicense(FormatText.cutQuote(annotation.getValue().toString()));
			}
			if (annotation.getProperty().toString().equals(Constants.INFO_RDFS_LABEL)) {
				addLanguage(info.getLanguageToLabel(), annotation);
				info.setRdfsLabel(FormatText.cutQuote(annotation.getValue().toString()));
			}
			if (annotation.getProperty().toString().equals(Constants.INFO_SEE_ALSO)) {
				info.setSeeAlso(FormatText.cutQuote(annotation.getValue().toString()));
			}
			if (annotation.getProperty().toString().equals(Constants.INFO_TITLE)) {
				addLanguage(info.getLanguageToTitle(), annotation);
				info.setTitle(FormatText.cutQuote(annotation.getValue().toString()));
			}
			if (annotation.getProperty().toString().equals(Constants.INFO_VERSION_INFO)) {
				info.setVersion(FormatText.cutQuote(annotation.getValue().toString()));
			}
		}
	}

	/**
	 * Adds a language found in the owl annotation into the given map.
	 *
	 * @param mapToAdd      Map where the language should be added.
	 * @param owlAnnotation The desired annotation which should contain a language.
	 */
	private void addLanguage(Map<String, String> mapToAdd, OWLAnnotation owlAnnotation) {
		if (owlAnnotation.getValue() instanceof OWLLiteral) {
			OWLLiteral val = (OWLLiteral) owlAnnotation.getValue();

			if (val.isRDFPlainLiteral()) {
				if (val.getLang().isEmpty()) {
					mapToAdd.put(Constants.LANG_UNSET, val.getLiteral());
					mapData.getAvailableLanguages().add(Constants.LANG_UNSET);
				} else {
					mapToAdd.put(val.getLang(), val.getLiteral());
					mapData.getAvailableLanguages().add(val.getLang());
				}
			}
		}
	}
}
