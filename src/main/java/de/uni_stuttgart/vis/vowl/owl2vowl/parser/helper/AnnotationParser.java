/*
 * AnnotationParser.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Vowl_Lang;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.Annotation;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;

import java.util.*;

/**
 *
 */
public class AnnotationParser {

	public static Annotation getAnnotation(OWLAnnotation annotation, MapData mapData) {
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

	public static Map<String, List<Annotation>> processAndGet(Collection<OWLAnnotation> set, MapData mapData) {
		Map<String, List<Annotation>> result = new LinkedHashMap<String, List<Annotation>>();

		for (OWLAnnotation owlAnnotation : set) {
			Annotation current = getAnnotation(owlAnnotation, mapData);
			String key = current.getIdentifier();

			if (!result.containsKey(key)) {
				result.put(key, new ArrayList<Annotation>());
			}

			result.get(key).add(current);
		}

		return result;
	}

	// TODO
	public static Annotation getAnnotation(OWLDataPropertyAssertionAxiom axiom, MapData mapData) {
		System.out.println(axiom.getObject());
		System.out.println(axiom.getSubject());
		System.out.println(axiom.getProperty());

		System.out.println();

		return null;
	}
}
