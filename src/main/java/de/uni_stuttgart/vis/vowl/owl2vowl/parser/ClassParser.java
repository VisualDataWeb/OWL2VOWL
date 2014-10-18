/*
 * ClassParser.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.Constants;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.BaseClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.pipes.FormatText;
import org.semanticweb.owlapi.model.*;

import java.util.Map;
import java.util.Set;

/**
 *
 */
public class ClassParser extends GeneralNodeParser {
	private Set<OWLClass> classes;

	public ClassParser(Set<OWLClass> classes) {
		this.classes = classes;
	}

	protected void execute() {
		Map<String, BaseClass> classMap = mapData.getClassMap();
		Map<String, OWLClass> owlClasses = mapData.getOwlClasses();

		int indexCounter = classMap.size();

		for (OWLClass currentClass : classes) {
			rdfsLabel = "";
			rdfsComment = "";
			isDeprecated = false;
			rdfsIsDefinedBy = "";
			owlVersionInfo = "";
			iri = currentClass.getIRI().toString();

			// If thing is found skip it! It only gets created where necessary.
			if (iri.equals(Constants.OWL_THING_CLASS_URI)) {
				continue;
			}

			Main.logger.info("Class: " + currentClass);
			for(OWLAxiom currentAxiom : currentClass.getReferencingAxioms(ontology)){
				Main.logger.info("\tAxiom: " + currentAxiom);

				for(OWLClassExpression nestExpr : currentAxiom.getNestedClassExpressions()) {
					Main.logger.info("\t\tNested: " + nestExpr);
				}
			}

			Set<OWLAnnotation> currentClassAnnotations = currentClass.getAnnotations(GeneralParser.ontology);

			TypeFinder finder = new TypeFinder(GeneralParser.ontology, GeneralParser.factory);
			BaseClass theClass = finder.findVowlClass(currentClass);

			parseAnnotations(currentClass);

			if (rdfsLabel.isEmpty()) {
				rdfsLabel = extractNameFromIRI(iri);
			}


			// Setting data in VOWLClass
			theClass.setName(FormatText.cutQuote(rdfsLabel));
			theClass.setComment(FormatText.cutQuote(rdfsComment));
			theClass.setIri(iri);
			theClass.setDefinedBy(FormatText.cutQuote(rdfsIsDefinedBy));
			theClass.setOwlVersion(FormatText.cutQuote(owlVersionInfo));

			if (isDeprecated) {
				theClass.getAttributes().add(Constants.PROP_ATTR_DEPR);
			}

			owlClasses.put(currentClass.getIRI().toString(), currentClass);
			classMap.put(theClass.getIri(), theClass);

			indexCounter++;
		}
	}
}
