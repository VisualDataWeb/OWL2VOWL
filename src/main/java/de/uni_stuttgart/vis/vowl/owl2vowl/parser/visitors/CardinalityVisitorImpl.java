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
	public void visit(OWLDataMaxCardinality desc) {
		BaseProperty prop = getProperty(desc.getProperty().asOWLDataProperty().getIRI().toString());
		prop.setMinCardinality(desc.getCardinality());
		super.visit(desc);
		logger.info("Max Cardinality =[" + desc.getCardinality() + "]");
	}

	@Override
	public void visit(OWLDataMinCardinality desc) {
		BaseProperty prop = getProperty(desc.getProperty().asOWLDataProperty().getIRI().toString());
		prop.setMinCardinality(desc.getCardinality());
		super.visit(desc);
		logger.info("Min Cardinality =[" + desc.getCardinality() + "]");
	}

	@Override
	public void visit(OWLObjectMaxCardinality desc) {
		BaseProperty prop = getProperty(desc.getProperty().asOWLObjectProperty().getIRI().toString());
		prop.setMinCardinality(desc.getCardinality());
		super.visit(desc);
		logger.info("Object Max Cardinality =[" + desc.getCardinality() + "]");
	}

	@Override
	public void visit(OWLObjectMinCardinality desc) {
		BaseProperty prop = getProperty(desc.getProperty().asOWLObjectProperty().getIRI().toString());
		prop.setMinCardinality(desc.getCardinality());
		super.visit(desc);
		logger.info("Object Min Cardinality =[" + desc.getCardinality() + "]");
	}

	@Override
	public void visit(OWLDataExactCardinality desc) {
		BaseProperty prop = getProperty(desc.getProperty().asOWLDataProperty().getIRI().toString());
		prop.setMinCardinality(desc.getCardinality());
		super.visit(desc);
		logger.info("Exact Data Cardinality =[" + desc.getCardinality() + "]");
	}

	@Override
	public void visit(OWLObjectExactCardinality desc) {
		BaseProperty prop = getProperty(desc.getProperty().asOWLObjectProperty().getIRI().toString());
		prop.setMinCardinality(desc.getCardinality());
		super.visit(desc);
		logger.info("Exact Object Cardinality =[" + desc.getCardinality() + "]");
	}

	public BaseProperty getProperty(String iri) {
		return data.getMergedProperties().get(iri);
	}
}
