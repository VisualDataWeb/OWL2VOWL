package de.uni_stuttgart.vis.vowl.owl2vowl.parser.visitors;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties.BaseProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.OntologyInformation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;

/**
 * Visitor implementation for the cardinalities.
 */
public class CardinalityVisitorImpl extends OWLObjectVisitorAdapter {
	private static final Logger logger = LogManager.getRootLogger();
	private MapData data;
	private OntologyInformation information;

	public CardinalityVisitorImpl(MapData mapData, OntologyInformation information) {
		this.data = mapData;
		this.information = information;
	}

	@Override
	public void visit(OWLDataMaxCardinality dataMaxCardinality) {
		BaseProperty property = getProperty(dataMaxCardinality);
		property.setMaxCardinality(dataMaxCardinality.getCardinality());
		super.visit(dataMaxCardinality);
		logger.info("Data Max Cardinality =[" + dataMaxCardinality.getCardinality() + "]");
	}

	@Override
	public void visit(OWLDataMinCardinality dataMinCardinality) {
		BaseProperty property = getProperty(dataMinCardinality);
		property.setMinCardinality(dataMinCardinality.getCardinality());
		super.visit(dataMinCardinality);
		logger.info("Data Min Cardinality =[" + dataMinCardinality.getCardinality() + "]");
	}

	@Override
	public void visit(OWLDataExactCardinality dataExactCardinality) {
		BaseProperty property = getProperty(dataExactCardinality);
		property.setExactCardinality(dataExactCardinality.getCardinality());
		super.visit(dataExactCardinality);
		logger.info("Data Exact Cardinality =[" + dataExactCardinality.getCardinality() + "]");
	}

	@Override
	public void visit(OWLObjectMaxCardinality objectMaxCardinality) {
		BaseProperty property = getProperty(objectMaxCardinality);
		property.setMaxCardinality(objectMaxCardinality.getCardinality());
		super.visit(objectMaxCardinality);
		logger.info("Object Max Cardinality =[" + objectMaxCardinality.getCardinality() + "]");
	}

	@Override
	public void visit(OWLObjectMinCardinality objectMinCardinality) {
		BaseProperty property = getProperty(objectMinCardinality);
		property.setMinCardinality(objectMinCardinality.getCardinality());
		super.visit(objectMinCardinality);
		logger.info("Object Min Cardinality =[" + objectMinCardinality.getCardinality() + "]");
	}

	@Override
	public void visit(OWLObjectExactCardinality objectExactCardinality) {
		BaseProperty property = getProperty(objectExactCardinality);
		property.setExactCardinality(objectExactCardinality.getCardinality());
		super.visit(objectExactCardinality);
		logger.info("Object Exact Cardinality =[" + objectExactCardinality.getCardinality() + "]");
	}

	public BaseProperty getProperty(OWLObjectCardinalityRestriction card) {
		OWLObjectPropertyExpression prop = card.getProperty();
		BaseProperty returnValue;

		if (prop.isAnonymous()) {
			returnValue = data.getMergedProperties().get(prop.getNamedProperty().getIRI().toString());
		} else {
			returnValue = data.getMergedProperties().get(prop.asOWLObjectProperty().getIRI().toString());
		}

		return returnValue;
	}

	public BaseProperty getProperty(OWLDataCardinalityRestriction card) {
		OWLDataPropertyExpression prop = card.getProperty();
		BaseProperty returnValue;

		if (prop.isAnonymous()) {
			//TODO
			returnValue = data.getMergedProperties().get(prop.asOWLDataProperty().getIRI().toString());
		} else {
			returnValue = data.getMergedProperties().get(prop.asOWLDataProperty().getIRI().toString());
		}

		return returnValue;
	}
}
