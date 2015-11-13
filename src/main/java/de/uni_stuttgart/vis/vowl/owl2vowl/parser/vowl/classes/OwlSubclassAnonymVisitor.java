package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.classes;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.AbstractProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;

/**
 * @author Eduard
 */
public class OwlSubclassAnonymVisitor extends OWLObjectVisitorAdapter {
	private VowlData vowlData;
	private OWLClass subclass;
	private Logger logger = LogManager.getLogger(OwlSubclassAnonymVisitor.class);

	public OwlSubclassAnonymVisitor(VowlData vowlData, OWLClass subclass) {
		this.vowlData = vowlData;
		this.subclass = subclass;
	}

	@Override
	protected void handleDefault(OWLObject axiom) {
		logger.info("Missed implementation for: " + axiom);
	}

	@Override
	public void visit(OWLObjectMinCardinality ce) {
		if (!ce.getFiller().isOWLThing() && !ce.getFiller().isOWLNothing()) {
			// TODO specification of a filler class
			logger.info("Specification of cardinalities not supported yet: " + ce);
			return;
		}

		OWLObjectProperty property = ce.getProperty().getNamedProperty();
		AbstractProperty vowlProperty = vowlData.getPropertyForIri(property.getIRI());
		vowlProperty.setMinCardinality(ce.getCardinality());
	}

	@Override
	public void visit(OWLDataAllValuesFrom ce) {
		logger.info(ce + " not supported yet.");
	}

	@Override
	public void visit(OWLDataExactCardinality ce) {
		logger.info(ce + " not supported yet.");
	}

	@Override
	public void visit(OWLDataMaxCardinality ce) {
		logger.info(ce + " not supported yet.");
	}

	@Override
	public void visit(OWLDataMinCardinality ce) {
		logger.info(ce + " not supported yet.");
	}

	@Override
	public void visit(OWLDataSomeValuesFrom ce) {
		logger.info(ce + " not supported yet.");
	}

	@Override
	public void visit(OWLDataHasValue ce) {
		logger.info(ce + " not supported yet.");
	}

	@Override
	public void visit(OWLObjectAllValuesFrom ce) {
		logger.info(ce + " not supported yet.");
	}

	@Override
	public void visit(OWLObjectSomeValuesFrom ce) {
		logger.info(ce + " not supported yet.");
	}

	@Override
	public void visit(OWLObjectMaxCardinality ce) {
		if (!ce.getFiller().isOWLThing() && !ce.getFiller().isOWLNothing()) {
			// TODO specification of a filler class
			logger.info("Specification of cardinalities not supported yet: " + ce);
			return;
		}

		OWLObjectProperty property = ce.getProperty().getNamedProperty();
		AbstractProperty vowlProperty = vowlData.getPropertyForIri(property.getIRI());
		vowlProperty.setMaxCardinality(ce.getCardinality());
	}

	@Override
	public void visit(OWLObjectHasSelf ce) {
		logger.info(ce + " not supported yet.");
	}

	@Override
	public void visit(OWLObjectHasValue ce) {
		logger.info(ce + " not supported yet.");
	}

	@Override
	public void visit(OWLObjectExactCardinality ce) {
		if (!ce.getFiller().isOWLThing() && !ce.getFiller().isOWLNothing()) {
			// TODO specification of a filler class
			logger.info("Specification of cardinalities not supported yet: " + ce);
			return;
		}

		OWLObjectProperty property = ce.getProperty().getNamedProperty();
		AbstractProperty vowlProperty = vowlData.getPropertyForIri(property.getIRI());
		vowlProperty.setExactCardinality(ce.getCardinality());
	}
}
