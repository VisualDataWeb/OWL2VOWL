package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.property;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.VowlAttribute;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.AbstractClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.NullClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.properties.VowlObjectProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;

/**
 * @author Eduard
 */
public class PropertyVisitor extends OWLObjectVisitorAdapter {
	private Logger logger = LogManager.getLogger(PropertyVisitor.class);
	private final VowlData vowlData;
	private final OWLObjectProperty owlObjectProperty;

	public PropertyVisitor(VowlData vowlData, OWLObjectProperty owlObjectProperty) {
		this.vowlData = vowlData;
		this.owlObjectProperty = owlObjectProperty;
	}

	@Override
	protected void handleDefault(OWLObject axiom) {
		super.handleDefault(axiom);
		logger.info("Missing axiom: " + axiom);
	}

	@Override
	public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
		VowlObjectProperty prop = vowlData.getObjectPropertyForIri(owlObjectProperty.getIRI());
		prop.addAttribute(VowlAttribute.INVERSE_FUNCTIONAL);
	}

	@Override
	public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
		VowlObjectProperty prop = vowlData.getObjectPropertyForIri(owlObjectProperty.getIRI());
		prop.addAttribute(VowlAttribute.FUNCTIONAL);
	}

	@Override
	public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
		VowlObjectProperty prop = vowlData.getObjectPropertyForIri(owlObjectProperty.getIRI());
		prop.addAttribute(VowlAttribute.IRREFLEXIVE);
	}

	@Override
	public void visit(OWLObjectPropertyDomainAxiom axiom) {
		VowlObjectProperty prop = vowlData.getObjectPropertyForIri(owlObjectProperty.getIRI());

		if (axiom.getDomain().isAnonymous()){
			AbstractClass anonymClass = axiom.getDomain().accept(new DomainRangeVisitor(owlObjectProperty, vowlData));
			if (!(anonymClass instanceof NullClass)) {
				prop.setDomain(anonymClass.getIri());
			} else {
				logger.info("Skipped anonymous object domain: " + axiom.getDomain());
			}
		} else {
			prop.setDomain(axiom.getDomain().asOWLClass().getIRI());
		}
	}

	@Override
	public void visit(OWLObjectPropertyRangeAxiom axiom) {
		VowlObjectProperty prop = vowlData.getObjectPropertyForIri(owlObjectProperty.getIRI());

		if (axiom.getRange().isAnonymous()){
			AbstractClass anonymClass = axiom.getRange().accept(new DomainRangeVisitor(owlObjectProperty, vowlData));
			if (!(anonymClass instanceof NullClass)) {
				prop.setRange(anonymClass.getIri());
			} else {
				logger.info("Skipped anonymous object range: " + axiom.getRange());
			}
		} else {
			prop.setRange(axiom.getRange().asOWLClass().getIRI());
		}
	}

	@Override
	public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
	}

	@Override
	public void visit(OWLInverseObjectPropertiesAxiom axiom) {
	}

	@Override
	public void visit(OWLSubObjectPropertyOfAxiom axiom) {
	}

	@Override
	public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
	}

	@Override
	public void visit(OWLAsymmetricObjectPropertyAxiom axiom) {
		VowlObjectProperty prop = vowlData.getObjectPropertyForIri(owlObjectProperty.getIRI());
		prop.addAttribute(VowlAttribute.ASYMMETRIC);
	}

	@Override
	public void visit(OWLSymmetricObjectPropertyAxiom axiom) {
		VowlObjectProperty prop = vowlData.getObjectPropertyForIri(owlObjectProperty.getIRI());
		prop.addAttribute(VowlAttribute.SYMMETRIC);
	}

	@Override
	public void visit(OWLReflexiveObjectPropertyAxiom axiom) {
		VowlObjectProperty prop = vowlData.getObjectPropertyForIri(owlObjectProperty.getIRI());
		prop.addAttribute(VowlAttribute.REFLEXIVE);
	}
}
