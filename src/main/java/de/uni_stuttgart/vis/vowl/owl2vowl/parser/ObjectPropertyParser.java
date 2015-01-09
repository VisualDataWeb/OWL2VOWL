/*
 * ObjectPropertyParser.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Standard_Iris;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.Vowl_Prop_Attr;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties.OwlObjectProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.OwlIntersectionOf;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.OwlThing;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.OntologyInformation;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper.ComparisonHelper;
import org.semanticweb.owlapi.model.*;

import java.util.*;

/**
 *
 */
public class ObjectPropertyParser extends GeneralPropertyParser {
	private Set<OWLObjectProperty> objectProperties;

	public ObjectPropertyParser(OntologyInformation ontologyInformation, MapData mapData, Set<OWLObjectProperty> objectProperties) {
		super(ontologyInformation, mapData);
		this.objectProperties = objectProperties;
	}

	public boolean isFuntionalObjectProperty(OWLOntology onto, OWLObjectProperty owlObjectProp) {
		return !onto.getFunctionalObjectPropertyAxioms(owlObjectProp).isEmpty();
	}

	/**
	 * Checks whether the given OWLObjectProperty is a transitive property.
	 *
	 * @param onto          the whole OWLOntology
	 * @param owlObjectProp the specific OWLObjectProperty
	 * @return true, if the given OWLObjectProperty is a transitive property
	 */
	public boolean isTransitiveObjectProperty(OWLOntology onto, OWLObjectProperty owlObjectProp) {
		return !onto.getTransitiveObjectPropertyAxioms(owlObjectProp).isEmpty();
	}

	/**
	 * checks wheaten the given OWLObjectPropertyExpression is a inverse functional object property
	 *
	 * @param onto   the whole OWLOntology
	 * @param owlOPE OWLObjectPropertyExpression
	 * @return boolean            true if the given OWLObjectProperty is a inverse functional object property
	 */
	public boolean isInverseFunctionalObjectProperty(OWLOntology onto, OWLObjectPropertyExpression owlOPE) {
		return !onto.getInverseFunctionalObjectPropertyAxioms(owlOPE).isEmpty();
	}

	/**
	 * checks wheaten the given OWLObjectPropertyExpression is a symmetric object property
	 *
	 * @param onto   the whole OWLOntology
	 * @param owlOPE OWLObjectPropertyExpression
	 * @return boolean            true if the given OWLObjectProperty is a symmetric object property
	 */
	public boolean isSymmetricObjectProperty(OWLOntology onto, OWLObjectPropertyExpression owlOPE) {
		return !onto.getSymmetricObjectPropertyAxioms(owlOPE).isEmpty();
	}

	public void execute() {
		Map<String, OwlObjectProperty> objectPropertyMap = mapData.getObjectPropertyMap();
		Map<String, OwlThing> thingMap = mapData.getThingMap();
		Map<String, OWLObjectProperty> owlObjectProperties = mapData.getOwlObjectProperties();

		int indexCounter = objectPropertyMap.size() + 1;

		for (OWLObjectProperty currentProperty : objectProperties) {
			isDeprecated = false;
			rdfsIsDefinedBy = "";
			owlVersionInfo = "";
			rdfsRange = "";
			rdfsDomain = "";
			rdfsInversOf = "";
			iri = currentProperty.getIRI().toString();

			OwlObjectProperty theProperty = new OwlObjectProperty();

			parseAnnotations(currentProperty);

			logger.info("ObjectProperty: " + currentProperty);
			for (OWLAxiom currentAxiom : currentProperty.getReferencingAxioms(ontology)) {
				logger.info("\tAxiom: " + currentAxiom);

				for (OWLClassExpression nestExpr : currentAxiom.getNestedClassExpressions()) {
					logger.info("\t\tNested: " + nestExpr);
				}
			}

			// get the domain of the property
			rdfsDomain = retrieveDomain(currentProperty);

			// get the range of the property
			rdfsRange = retrieveRange(currentProperty);

			// get the IRI of the object property which is inverse to 'this' object property
			Set<OWLObjectPropertyExpression> inversesExpressions = currentProperty.getInverses(ontology);

			for (OWLObjectPropertyExpression owlOPE : inversesExpressions) {
				rdfsInversOf = owlOPE.asOWLObjectProperty().getIRI().toString();

				// maybe we need to extract range or domain data from the inverse object property
				for (OWLObjectProperty owlProp2 : objectProperties) {
					if (owlProp2.getIRI().toString().equals(rdfsInversOf)) {

						if (rdfsRange.isEmpty()) {
							rdfsRange = retrieveDomain(owlProp2);
						}

						if (rdfsDomain.isEmpty()) {
							rdfsDomain = retrieveRange(owlProp2);
						}
					}
				}
			}

			if (ComparisonHelper.hasDifferentNameSpace(currentProperty, ontologyInformation)) {
				theProperty.getAttributes().add(Vowl_Prop_Attr.PROP_ATTR_IMPORT);
			}
			if (isFuntionalObjectProperty(ontology, currentProperty)) {
				theProperty.getAttributes().add(Vowl_Prop_Attr.PROP_ATTR_FUNCT);
			}
			if (isSymmetricObjectProperty(ontology, currentProperty)) {
				theProperty.getAttributes().add(Vowl_Prop_Attr.PROP_ATTR_SYM);
			}
			if (isTransitiveObjectProperty(ontology, currentProperty)) {
				theProperty.getAttributes().add(Vowl_Prop_Attr.PROP_ATTR_TRANS);
			}
			if (isInverseFunctionalObjectProperty(ontology, currentProperty)) {
				theProperty.getAttributes().add(Vowl_Prop_Attr.PROP_ATTR_INV_FUNCT);
			}

			// TODO
			BaseNode sourceNodeID = findNode(rdfsDomain);
			BaseNode targetNodeID = findNode(rdfsRange);

			if (Standard_Iris.OWL_THING_CLASS_URI.equals(rdfsDomain)) {
				sourceNodeID = null;
			}
			if (Standard_Iris.OWL_THING_CLASS_URI.equals(rdfsRange)) {
				targetNodeID = null;
			}

			// If domain AND range are not owl:Thing
			if (sourceNodeID == null || targetNodeID == null) {
				// If domain AND range are owl:Thing
				if (sourceNodeID == null && targetNodeID == null) {
					// neither source nor target is given
					sourceNodeID = getDisconnectedThing();

					if (sourceNodeID == null) {
						OwlThing newThing = new OwlThing();
						thingMap.put(newThing.getId(), newThing);
						sourceNodeID = newThing;
					}

					targetNodeID = sourceNodeID;
				} else {
					// no sourceID for object property? Take OWLClassThing as source
					if (sourceNodeID == null) {
						// check if OWLClassThing exists already.
						sourceNodeID = targetNodeID.getConnectedThing();

						// no OWLClassThing connected to this class foudn, we have to create one
						if (sourceNodeID == null) {
							OwlThing newThing = new OwlThing();
							thingMap.put(newThing.getId(), newThing);
							sourceNodeID = newThing;
						}
					}

					// no sourceID for object property? Take OWLClassThing as source
					if (targetNodeID == null) {
						// check if OWLClassThing exists already. If not create one
						targetNodeID = sourceNodeID.getConnectedThing();

						if (targetNodeID == null) {
							OwlThing newThing = new OwlThing();
							thingMap.put(newThing.getId(), newThing);
							targetNodeID = newThing;
						}
					}
				}
			}

			theProperty.setDisjoints(retrieveDisjoints(currentProperty));
			theProperty.setEquivalents(retrieveEquivalents(currentProperty));
			theProperty.setSubProperties(retrieveSubProperties(currentProperty));
			theProperty.setSuperProperties(retrieveSuperProperties(currentProperty));

			theProperty.setLabels(languageToLabel);
			theProperty.setComments(comments);
			theProperty.setName(languageToLabel.get("default"));
			theProperty.setIri(iri);
			theProperty.setDefinedBy(rdfsIsDefinedBy);
			theProperty.setOwlVersion(owlVersionInfo);
			theProperty.setInverseIRI(rdfsInversOf);

			BaseNode domain = sourceNodeID;
			BaseNode range = targetNodeID;

			domain.getOutGoingEdges().add(theProperty);
			theProperty.setDomain(domain);

			range.getInGoingEdges().add(theProperty);
			theProperty.setRange(range);

			if (isDeprecated) {
				theProperty.getAttributes().add(Vowl_Prop_Attr.PROP_ATTR_DEPR);
			}

			indexCounter++;

			owlObjectProperties.put(currentProperty.getIRI().toString(), currentProperty);
			objectPropertyMap.put(theProperty.getIri(), theProperty);
		}
	}
}
