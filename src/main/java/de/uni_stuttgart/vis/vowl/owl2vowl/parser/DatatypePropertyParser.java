/*
 * DatatypePropertyParser.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Standard_Iris;
import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Vowl_Lang;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.Vowl_Prop_Attr;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties.OwlDatatypeProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.OwlThing;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.BaseDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.RdfsDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.RdfsLiteral;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.OntologyInformation;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper.ComparisonHelper;
import org.semanticweb.owlapi.model.*;

import java.util.*;

/**
 *
 */
public class DatatypePropertyParser extends GeneralPropertyParser {

	private Set<OWLDataProperty> dataProperties;

	public DatatypePropertyParser(OntologyInformation ontologyInformation, MapData mapData, Set<OWLDataProperty> dataProperties) {
		super(ontologyInformation, mapData);
		this.dataProperties = dataProperties;
	}

	protected void execute() {
		Map<String, OwlDatatypeProperty> propertyMap = mapData.getDatatypePropertyMap();
		Map<String, OWLDataProperty> owlDatatypeProperties = mapData.getOwlDatatypeProperties();

		for (OWLDataProperty currentProperty : dataProperties) {
			isDeprecated = false;
			rdfsIsDefinedBy = "";
			owlVersionInfo = "";
			rdfsRange = "";
			rdfsDomain = "";
			iri = currentProperty.getIRI().toString();

			parseAnnotations(currentProperty);

			logger.info("DatatypeProperty: " + currentProperty);
			for (OWLAxiom currentAxiom : currentProperty.getReferencingAxioms(ontology)) {
				logger.info("\tAxiom: " + currentAxiom);

				for (OWLClassExpression nestExpr : currentAxiom.getNestedClassExpressions()) {
					logger.info("\t\tNested: " + nestExpr);
				}
			}

			rdfsDomain = retrieveDomain(currentProperty);

			rdfsRange = retrieveRange(currentProperty);

			BaseNode domainNode = findNode(rdfsDomain);

			BaseDatatype rangeNode;
			String resourceName = extractNameFromIRI(rdfsRange);
			boolean isGeneric = false;

			logger.info(currentProperty);
			for (OWLAxiom currentAxiom : currentProperty.getReferencingAxioms(ontology)) {
				logger.info("\t" + currentAxiom);

				for (OWLClassExpression nestExpr : currentAxiom.getNestedClassExpressions()) {
					logger.info("\t\t" + nestExpr);
				}
			}

			if (resourceName.isEmpty()) {
				resourceName = "Literal";
				isGeneric = true;
			}

			if (rdfsRange.equals(Standard_Iris.GENERIC_LITERAL_URI)) {
				isGeneric = true;
			}

			if (isGeneric) {
				rangeNode = new RdfsLiteral();
			} else {
				rangeNode = new RdfsDatatype();
			}

			rangeNode.setName(resourceName);
			mapData.getDatatypeMap().put(rangeNode.getId(), rangeNode);

			if (domainNode == null) {
				domainNode = getDisconnectedThing();

				if (domainNode == null) {
					OwlThing newThing = new OwlThing();
					mapData.getThingMap().put(newThing.getId(), newThing);
					domainNode = newThing;
				}
			}

			OwlDatatypeProperty property = new OwlDatatypeProperty();

			if (ComparisonHelper.hasDifferentNameSpace(currentProperty, ontologyInformation)) {
				property.getAttributes().add(Vowl_Prop_Attr.PROP_ATTR_IMPORT);
			}

			if (isFuntionalDataProperty(currentProperty)) {
				property.getAttributes().add(Vowl_Prop_Attr.PROP_ATTR_FUNCT);
			}

			property.setDisjoints(retrieveDisjoints(currentProperty));
			property.setEquivalents(retrieveEquivalents(currentProperty));
			property.setSubProperties(retrieveSubProperties(currentProperty));
			property.setSuperProperties(retrieveSuperProperties(currentProperty));

			property.setLabels(languageToLabel);
			property.setComments(comments);
			property.setName(languageToLabel.get(Vowl_Lang.LANG_DEFAULT));
			property.setIri(iri);
			property.setDefinedBy(rdfsIsDefinedBy);
			property.setOwlVersion(owlVersionInfo);
			domainNode.getOutGoingEdges().add(property);
			rangeNode.getInGoingEdges().add(property);

			property.setDomain(domainNode);
			property.setRange(rangeNode);

			if (isDeprecated) {
				property.getAttributes().add(Vowl_Prop_Attr.PROP_ATTR_DEPR);
			}

			owlDatatypeProperties.put(currentProperty.getIRI().toString(), currentProperty);
			propertyMap.put(property.getIri(), property);
		}
	}

	/**
	 * Checks whether the given OWLDataProperty is a functional property.
	 *
	 * @param owlDataProp the specific OWLDataProperty
	 * @return true, if the given OWLDataProperty is a functional property
	 */
	public boolean isFuntionalDataProperty(OWLDataProperty owlDataProp) {
		return !ontology.getFunctionalDataPropertyAxioms(owlDataProp).isEmpty();
	}
}
