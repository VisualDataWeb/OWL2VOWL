package de.uni_stuttgart.vis.vowl.owl2vowl.parser.visitors;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties.BaseProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
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

	public CardinalityVisitorImpl(MapData mapData) {
		this.data = mapData;
	}

	@Override
	public void visit(OWLDataMaxCardinality dataMaxCardinality) {
		BaseProperty prop = getProperty(dataMaxCardinality.getProperty().asOWLDataProperty().getIRI().toString());
		prop.setMaxCardinality(dataMaxCardinality.getCardinality());
		super.visit(dataMaxCardinality);
		logger.info("Data Max Cardinality =[" + dataMaxCardinality.getCardinality() + "]");
	}

	@Override
	public void visit(OWLDataMinCardinality dataMinCardinality) {
		BaseProperty prop = getProperty(dataMinCardinality.getProperty().asOWLDataProperty().getIRI().toString());
		prop.setMinCardinality(dataMinCardinality.getCardinality());
		super.visit(dataMinCardinality);
		logger.info("Data Min Cardinality =[" + dataMinCardinality.getCardinality() + "]");
	}

	@Override
	public void visit(OWLDataExactCardinality dataExactCardinality) {
		BaseProperty prop = getProperty(dataExactCardinality.getProperty().asOWLDataProperty().getIRI().toString());
		prop.setExactCardinality(dataExactCardinality.getCardinality());
		super.visit(dataExactCardinality);
		logger.info("Data Exact Cardinality =[" + dataExactCardinality.getCardinality() + "]");
	}

	@Override
	public void visit(OWLObjectMaxCardinality objectMaxCardinality) {
		BaseProperty prop = getProperty(objectMaxCardinality.getProperty().asOWLObjectProperty().getIRI().toString());
		prop.setMaxCardinality(objectMaxCardinality.getCardinality());
		super.visit(objectMaxCardinality);
		logger.info("Object Max Cardinality =[" + objectMaxCardinality.getCardinality() + "]");
	}

	@Override
	public void visit(OWLObjectMinCardinality objectMinCardinality) {
		BaseProperty prop = getProperty(objectMinCardinality.getProperty().asOWLObjectProperty().getIRI().toString());
		prop.setMinCardinality(objectMinCardinality.getCardinality());
		super.visit(objectMinCardinality);
		logger.info("Object Min Cardinality =[" + objectMinCardinality.getCardinality() + "]");
	}

	@Override
	public void visit(OWLObjectExactCardinality objectExactCardinality) {
		BaseProperty prop = getProperty(objectExactCardinality.getProperty().asOWLObjectProperty().getIRI().toString());
		prop.setExactCardinality(objectExactCardinality.getCardinality());
		super.visit(objectExactCardinality);
		logger.info("Object Exact Cardinality =[" + objectExactCardinality.getCardinality() + "]");
	}

	public BaseProperty getProperty(String iri) {
		return data.getMergedProperties().get(iri);
	}
}
