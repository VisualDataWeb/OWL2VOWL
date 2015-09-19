package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.property;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;

/**
 * @author Eduard
 */
public class DomainRangeVisitor extends OWLObjectVisitorAdapter {
	private final OWLObjectProperty owlObjectProperty;
	private final VowlData vowlData;

	public DomainRangeVisitor(OWLObjectProperty owlObjectProperty, VowlData vowlData) {
		this.owlObjectProperty = owlObjectProperty;
		this.vowlData = vowlData;
	}

	@Override
	protected void handleDefault(OWLObject axiom) {
		System.out.println("Missing handler for: " + axiom);
	}


}
