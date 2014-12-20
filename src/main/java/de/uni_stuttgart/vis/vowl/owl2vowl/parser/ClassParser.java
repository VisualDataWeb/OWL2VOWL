/*
 * ClassParser.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import de.uni_stuttgart.vis.vowl.owl2vowl.Main;
import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Standard_Iris;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.Vowl_Prop_Attr;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.BaseClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import de.uni_stuttgart.vis.vowl.owl2vowl.pipes.FormatText;
import org.semanticweb.owlapi.model.*;

import java.util.Map;
import java.util.Set;

/**
 *
 */
public class ClassParser extends GeneralNodeParser {
	private Set<OWLClass> classes;

	public ClassParser(Set<OWLClass> classes, OWLOntology ontology, OWLDataFactory factory, MapData mapData, OWLOntologyManager ontologyManager) {
		super(ontology, factory, mapData, ontologyManager);
		this.classes = classes;
	}

	protected void execute() {
		Map<String, BaseClass> classMap = mapData.getClassMap();
		Map<String, OWLClass> owlClasses = mapData.getOwlClasses();

		for (OWLClass currentClass : classes) {
			isDeprecated = false;
			rdfsIsDefinedBy = "";
			owlVersionInfo = "";
			iri = currentClass.getIRI().toString();

			// If thing is found skip it! It only gets created where necessary.
			if (iri.equals(Standard_Iris.OWL_THING_CLASS_URI)) {
				continue;
			}

			Main.logger.info("Class: " + currentClass);
			for (OWLAxiom currentAxiom : currentClass.getReferencingAxioms(ontology)) {
				Main.logger.info("\tAxiom: " + currentAxiom);

				for (OWLClassExpression nestExpr : currentAxiom.getNestedClassExpressions()) {
					Main.logger.info("\t\tNested: " + nestExpr);
				}
			}

			TypeFinder finder = new TypeFinder(ontology, factory);
			BaseClass theClass = finder.findVowlClass(currentClass);

			parseAnnotations(currentClass);

			// Setting data in VOWLClass
			theClass.setLabels(languageToLabel);
			theClass.setComments(comments);
			theClass.setName(languageToLabel.get("default"));
			theClass.setIri(iri);
			theClass.setDefinedBy(FormatText.cutQuote(rdfsIsDefinedBy));
			theClass.setOwlVersion(FormatText.cutQuote(owlVersionInfo));

			setIndividuals(theClass, currentClass);

			if (isDeprecated) {
				theClass.getAttributes().add(Vowl_Prop_Attr.PROP_ATTR_DEPR);
			}

			owlClasses.put(currentClass.getIRI().toString(), currentClass);
			classMap.put(theClass.getIri(), theClass);
		}
	}

	private void setIndividuals(BaseClass theClass, OWLClass clazz) {
		int size = 0;

		for (OWLOntology owlOntology : ontologyManager.getOntologies()) {
			Set<OWLIndividual> individuals = clazz.getIndividuals(owlOntology);
			size += individuals.size();
		}

		theClass.setNumberOfIndividuals(size);
	}
}
