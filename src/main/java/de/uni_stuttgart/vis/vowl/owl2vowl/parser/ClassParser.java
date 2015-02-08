/*
 * ClassParser.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Standard_Iris;
import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Vowl_Lang;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.Vowl_Prop_Attr;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.BaseClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.OntologyInformation;
import de.uni_stuttgart.vis.vowl.owl2vowl.pipes.FormatText;
import org.semanticweb.owlapi.model.*;

import java.util.Map;
import java.util.Set;

/**
 *
 */
public class ClassParser extends GeneralNodeParser {
	private Set<OWLClass> classes;

	public ClassParser(OntologyInformation ontologyInformation, MapData mapData, Set<OWLClass> classes) {
		super(ontologyInformation, mapData);
		this.classes = classes;
	}

	@Override
	protected void reset() {
		super.reset();
	}

	protected void execute() {
		Map<String, BaseClass> classMap = mapData.getClassMap();
		Map<String, OWLClass> owlClasses = mapData.getOwlClasses();

		for (OWLClass currentClass : classes) {
			reset();
			iri = currentClass.getIRI().toString();

			// If thing is found skip it! It only gets created where necessary.
			if (currentClass.isOWLThing() || iri.equals(Standard_Iris.OWL_THING_CLASS_URI)) {
				continue;
			}

			TypeFinder finder = new TypeFinder(ontologyInformation);
			BaseClass theClass = finder.findVowlClass(currentClass);

			parseAnnotations(currentClass);

			// Setting data in VOWLClass
			theClass.setLabels(languageToLabel);
			theClass.setComments(comments);
			theClass.setName(languageToLabel.get(Vowl_Lang.LANG_DEFAULT));
			theClass.setIri(iri);
			theClass.setDefinedBy(FormatText.cutQuote(rdfsIsDefinedBy));
			theClass.setOwlVersion(FormatText.cutQuote(owlVersionInfo));
			theClass.setAnnotations(annotations);

			if (isDeprecated) {
				theClass.getAttributes().add(Vowl_Prop_Attr.PROP_ATTR_DEPR);
			}

			owlClasses.put(currentClass.getIRI().toString(), currentClass);
			classMap.put(theClass.getIri(), theClass);

			logAxioms(currentClass);
		}
	}
}
