/*
 * AnnotationParser.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Vowl_Lang;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.Annotation;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import org.semanticweb.owlapi.model.*;

import java.util.Map;

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

	// TODO
	public static Annotation getAnnotation(OWLDataPropertyAssertionAxiom axiom, MapData mapData) {
		System.out.println(axiom.getObject());
		System.out.println(axiom.getSubject());
		System.out.println(axiom.getProperty());

		System.out.println();

		return null;
	}
}
