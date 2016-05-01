package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.property;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLProperty;

/**
 * Top class for property visitors.
 * @author Eduard
 */
public class PropertyVisitor implements OWLObjectVisitor {
	private Logger logger = LogManager.getLogger(PropertyVisitor.class);
	protected final VowlData vowlData;
	protected final OWLProperty owlObjectProperty;

	public PropertyVisitor(VowlData vowlData, OWLProperty owlObjectProperty) {
		this.vowlData = vowlData;
		this.owlObjectProperty = owlObjectProperty;
	}

	@Override
	public void doDefault(Object object) {
		logger.info("Missing axiom: " + object);
	}
}
