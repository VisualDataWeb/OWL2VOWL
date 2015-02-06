package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Ontology_Info;
import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Vowl_Lang;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.OntologyInfo;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.Annotation;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.OntologyInformation;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.visitors.OntologyInfoVisitorImpl;
import de.uni_stuttgart.vis.vowl.owl2vowl.pipes.FormatText;
import org.semanticweb.owlapi.model.*;

import java.util.Arrays;
import java.util.Map;

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
		} else {
			info.setIri(ontologyInformation.getLoadedIri().toString());
		}

		if (versionIri != null) {
			info.setVersion(versionIri.toString());
		}

		/* The way to get the ontology axioms for SIOC
		for (OWLAxiom owlAxiom : ontology.getABoxAxioms(false)) {
			owlAxiom.accept(new OntologyInfoVisitorImpl(info));
		}
		*/

		/* Save available annotations */
		for (OWLAnnotation annotation : ontology.getAnnotations()) {
			OWLAnnotationProperty prop = annotation.getProperty();
			OWLAnnotationValue val = annotation.getValue();

			switch (PROPMAP.getValue(prop.toString())) {
				case CREATOR:
					info.addAuthor(FormatText.cutQuote(val.toString()));
					break;
				case DESCRIPTION:
					addLanguage(info.getLanguageToDescription(), annotation);
					info.setDescription(FormatText.cutQuote(val.toString()));
					break;
				case ISSUED:
					info.addIssued(FormatText.cutQuote(val.toString()));
					break;
				case LICENSE:
					info.addLicense(FormatText.cutQuote(val.toString()));
					break;
				case LABEL:
					addLanguage(info.getLanguageToLabel(), annotation);
					info.addLabel(FormatText.cutQuote(val.toString()));
					break;
				case SEE_ALSO:
					info.addSeeAlso(FormatText.cutQuote(val.toString()));
					break;
				case TITLE:
					addLanguage(info.getLanguageToTitle(), annotation);
					info.setTitle(FormatText.cutQuote(val.toString()));
					break;
				case VERSION:
					info.setVersion(FormatText.cutQuote(val.toString()));
					break;
				default:
					Annotation theAnno = getAnnotation(annotation);
					info.addAnnotatin(theAnno.getIdentifier(), theAnno);
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

	private Annotation getAnnotation(OWLAnnotation annotation) {
		Annotation anno;

		if (annotation.getValue() instanceof OWLLiteral) {
			OWLLiteral val = (OWLLiteral) annotation.getValue();
			String language;

			if (val.isRDFPlainLiteral()) {
				if (val.getLang().isEmpty()) {
					language = Vowl_Lang.LANG_UNSET;
					mapData.getAvailableLanguages().add(Vowl_Lang.LANG_UNSET);
				} else {
					language = val.getLang();
					mapData.getAvailableLanguages().add(val.getLang());
				}
				anno = new Annotation(annotation.getProperty().toString(), val.getLiteral());
				anno.setLanguage(language);
				anno.setType(Annotation.TYPE_LABEL);
			} else {
				anno = new Annotation(annotation.getProperty().toString(), val.getLiteral());
				anno.setType(Annotation.TYPE_LABEL);
			}
		} else if (annotation.getValue() instanceof IRI) {
			anno = new Annotation(annotation.getProperty().toString(), annotation.getValue().toString());
			anno.setType(Annotation.TYPE_IRI);
		} else {
			anno = new Annotation(annotation.getProperty().toString(), annotation.getValue().toString());
			anno.setType(Annotation.TYPE_LABEL);
		}

		return anno;
	}
}
