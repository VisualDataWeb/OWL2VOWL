package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Ontology_Info;
import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Vowl_Lang;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.OntologyInfo;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.OntologyInformation;
import de.uni_stuttgart.vis.vowl.owl2vowl.pipes.FormatText;
import org.semanticweb.owlapi.model.*;

import java.util.Arrays;
import java.util.Map;

/**
 * Parser for the ontology information.
 */
public class OntoInfoParser extends GeneralParser {
	public OntoInfoParser(OntologyInformation ontologyInformation, MapData mapData) {
		super(ontologyInformation, mapData);
	}

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
			switch (PROPMAP.getValue(annotation.getProperty().toString())) {
				case CREATOR:
					info.setAuthor(FormatText.cutQuote(annotation.getValue().toString()));
					break;
				case DESCRIPTION:
					addLanguage(info.getLanguageToDescription(), annotation);
					info.setDescription(FormatText.cutQuote(annotation.getValue().toString()));
					break;
				case ISSUED:
					info.setIssued(FormatText.cutQuote(annotation.getValue().toString()));
					break;
				case LICENSE:
					info.setLicense(FormatText.cutQuote(annotation.getValue().toString()));
					break;
				case LABEL:
					addLanguage(info.getLanguageToLabel(), annotation);
					info.setRdfsLabel(FormatText.cutQuote(annotation.getValue().toString()));
					break;
				case SEE_ALSO:
					info.setSeeAlso(FormatText.cutQuote(annotation.getValue().toString()));
					break;
				case TITLE:
					addLanguage(info.getLanguageToTitle(), annotation);
					info.setTitle(FormatText.cutQuote(annotation.getValue().toString()));
					break;
				case VERSION:
					info.setVersion(FormatText.cutQuote(annotation.getValue().toString()));
					break;
				default:
					// Nothing
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
					mapToAdd.put(Vowl_Lang.LANG_UNSET, val.getLiteral());
					mapData.getAvailableLanguages().add(Vowl_Lang.LANG_UNSET);
				} else {
					mapToAdd.put(val.getLang(), val.getLiteral());
					mapData.getAvailableLanguages().add(val.getLang());
				}
			}
		}
	}
}

enum PROPMAP {
	CREATOR(new String[]{Ontology_Info.INFO_CREATOR_DC, Ontology_Info.INFO_CREATOR_DCTERMS}),
	DESCRIPTION(new String[]{Ontology_Info.INFO_DESCRIPTION_DC, Ontology_Info.INFO_DESCRIPTION_DCTERMS}),
	ISSUED(new String[]{Ontology_Info.INFO_ISSUED_DCTERMS}),
	LICENSE(new String[]{Ontology_Info.INFO_LICENSE_DCTERMS}),
	LABEL(new String[]{Ontology_Info.INFO_RDFS_LABEL}),
	SEE_ALSO(new String[]{Ontology_Info.INFO_SEE_ALSO}),
	TITLE(new String[]{Ontology_Info.INFO_TITLE_DC, Ontology_Info.INFO_TITLE_DCTERMS}),
	VERSION(new String[]{Ontology_Info.INFO_VERSION_INFO}),
	EMPTY(new String[0]);

	private final String[] values;

	/**
	 * @param values All matching strings.
	 */
	private PROPMAP(final String[] values) {
		this.values = values;
	}

	/**
	 * Returns the enum value if found.
	 *
	 * @param searchProp The search string.
	 * @return The matching enum or EMPTY if not found.
	 */
	public static PROPMAP getValue(String searchProp) {
		for (PROPMAP propmap : PROPMAP.values()) {
			if (propmap.contains(searchProp)) {
				return propmap;
			}
		}

		return EMPTY;
	}

	@Override
	public String toString() {
		return Arrays.toString(values);
	}

	/**
	 * Checks if the string array contains the search string.
	 *
	 * @param search String to search for.
	 * @return True if found else false.
	 */
	public boolean contains(String search) {
		for (String value : values) {
			if (value.equals(search)) {
				return true;
			}
		}

		return false;
	}
}
