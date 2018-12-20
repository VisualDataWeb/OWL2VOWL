package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.classes;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.VowlAttribute;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.AbstractClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.VowlDatatypeProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.VowlObjectProperty;

import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.*;

public class HasKeyAxiomParser {

	private static final Logger logger = LogManager.getLogger(HasKeyAxiomParser.class);

	public static void parse(OWLOntology ontology, OWLClass owlClass, VowlData vowlData) {
		for (OWLHasKeyAxiom owlHasKeyAxiom : ontology.hasKeyAxioms(owlClass).collect(Collectors.toSet())) {
			AbstractClass hasKeysClass = vowlData.getClassForIri(owlClass.getIRI());

			for (OWLObjectPropertyExpression propertyExpression : owlHasKeyAxiom.objectPropertyExpressions().collect(Collectors.toSet())) {
				if (propertyExpression.isAnonymous()) {
					logger.info("Anonymous has key object property expression " + propertyExpression);
					continue;
				}

				VowlObjectProperty property = vowlData.getObjectPropertyForIri(propertyExpression.asOWLObjectProperty().getIRI());
				property.addAttribute(VowlAttribute.KEY);
				hasKeysClass.addKey(property.getIri());
			}

			for (OWLDataPropertyExpression propertyExpression : owlHasKeyAxiom.dataPropertyExpressions().collect(Collectors.toSet())) {
				if (propertyExpression.isAnonymous()) {
					logger.info("Anonymous has key data property expression " + propertyExpression);
					continue;
				}

				VowlDatatypeProperty property = vowlData.getDatatypePropertyForIri(propertyExpression.asOWLDataProperty().getIRI());
				property.addAttribute(VowlAttribute.KEY);
				hasKeysClass.addKey(property.getIri());
			}
		}
	}
}
