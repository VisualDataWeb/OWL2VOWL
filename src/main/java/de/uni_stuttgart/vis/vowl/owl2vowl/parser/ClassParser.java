/*
 * ClassParser.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.Constants;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.BaseClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.pipes.FormatText;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLClass;

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

			Set<OWLAnnotation> currentClassAnnotations = currentClass.getAnnotations(GeneralParser.ontology);

			TypeFinder finder = new TypeFinder(GeneralParser.ontology, GeneralParser.factory);
			BaseClass theClass = finder.findVowlClass(currentClass);

			parseAnnotations(currentClassAnnotations);

			if (rdfsLabel.isEmpty()) {
				rdfsLabel = extractNameFromIRI(iri);
			}


			// Setting data in VOWLClass
			theClass.setName(FormatText.cutQuote(rdfsLabel));
			theClass.setComment(FormatText.cutQuote(rdfsComment));
			theClass.setIri(iri);
			theClass.setId("class" + indexCounter);
			theClass.setDefinedBy(FormatText.cutQuote(rdfsIsDefinedBy));
			theClass.setOwlVersion(FormatText.cutQuote(owlVersionInfo));

			if (isDeprecated) {
				theClass.getAttributes().add(Constants.TYPE_DEPRECTAEDCLASS);
			}

			owlClasses.put(currentClass.getIRI().toString(), currentClass);
			classMap.put(theClass.getIri(), theClass);

			indexCounter++;
		}
	}
}
