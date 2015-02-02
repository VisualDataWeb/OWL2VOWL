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

	@Override
	public void reset() {
		super.reset();
	}

	public void execute() {
		Map<String, OwlObjectProperty> objectPropertyMap = mapData.getObjectPropertyMap();
		Map<String, OWLObjectProperty> owlObjectProperties = mapData.getOwlObjectProperties();

		for (OWLObjectProperty currentProperty : objectProperties) {
			iri = currentProperty.getIRI().toString();
			OwlObjectProperty theProperty = new OwlObjectProperty();

			reset();
			parseAnnotations(currentProperty);

			// get the domain of the property
			rdfsDomains = retrieveDomains(currentProperty);

			// get the range of the property
			rdfsRanges = retrieveRanges(currentProperty);

			// get the IRI of the object property which is inverse to 'this' object property
			// TODO Mehrfache Inversen speichern
			Set<OWLObjectPropertyExpression> inversesExpressions = currentProperty.getInverses(ontology);

			for (OWLObjectPropertyExpression owlOPE : inversesExpressions) {
				rdfsInversOf = owlOPE.asOWLObjectProperty().getIRI().toString();

				// maybe we need to extract range or domain data from the inverse object property
				for (OWLObjectProperty owlProp2 : objectProperties) {
					if (owlProp2.getIRI().toString().equals(rdfsInversOf)) {

						if (rdfsRanges.isEmpty()) {
							rdfsRanges = retrieveDomains(owlProp2);
						}

						if (rdfsDomains.isEmpty()) {
							rdfsDomains = retrieveRanges(owlProp2);
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

			List<BaseNode> domains = new ArrayList<BaseNode>();
			List<BaseNode> ranges = new ArrayList<BaseNode>();

			// Not any class detected
			if (isClassLess(rdfsDomains, rdfsRanges)) {
				BaseNode thing = getDisconnectedThing();
				domains.add(thing);
				ranges.add(thing);
			}
			// Check if there exists a thing construct.
			else if(hasThingConstruct(rdfsDomains, rdfsRanges)){
				if (hasThingConstruct(rdfsDomains)) {
					ranges.addAll(findNodes(rdfsRanges));
					domains.addAll(getNodesWithThings(rdfsDomains, ranges));
				} else {
					domains.addAll(findNodes(rdfsDomains));
					ranges.addAll(getNodesWithThings(rdfsRanges, domains));
				}
			}
			// No things detected!
			else {
				domains.addAll(findNodes(rdfsDomains));
				ranges.addAll(findNodes(rdfsRanges));
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
			theProperty.setAnnotations(annotations);

			BaseNode domain = mergeTargets(domains);
			BaseNode range = mergeTargets(ranges);

			domain.getOutGoingEdges().add(theProperty);
			theProperty.setDomain(domain);

			range.getInGoingEdges().add(theProperty);
			theProperty.setRange(range);

			if (isDeprecated) {
				theProperty.getAttributes().add(Vowl_Prop_Attr.PROP_ATTR_DEPR);
			}

			owlObjectProperties.put(currentProperty.getIRI().toString(), currentProperty);
			objectPropertyMap.put(theProperty.getIri(), theProperty);

			logAxioms(currentProperty);
		}
	}

}
