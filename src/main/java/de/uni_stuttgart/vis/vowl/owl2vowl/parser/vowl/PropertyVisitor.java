package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.VowlAttribute;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.properties.VowlObjectProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.property.DomainRangeVisitor;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;

/**
 * @author Eduard
 */
public class PropertyVisitor extends OWLObjectVisitorAdapter {
	private final VowlData vowlData;
	private final OWLObjectProperty owlObjectProperty;

	public PropertyVisitor(VowlData vowlData, OWLObjectProperty owlObjectProperty) {
		this.vowlData = vowlData;
		this.owlObjectProperty = owlObjectProperty;
	}

	@Override
	protected void handleDefault(OWLObject axiom) {
		super.handleDefault(axiom);
		System.out.println("Missing axiom: " + axiom);
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
		if (axiom.getDomain().isAnonymous()){
			axiom.getDomain().accept(new DomainRangeVisitor(owlObjectProperty, vowlData));
		} else {
			VowlObjectProperty prop = vowlData.getObjectPropertyForIri(owlObjectProperty.getIRI());
			prop.addDomain(axiom.getDomain().asOWLClass().getIRI());
		}
	}

	@Override
	public void visit(OWLObjectPropertyRangeAxiom axiom) {
		if (axiom.getRange().isAnonymous()){
			axiom.getRange().accept(new DomainRangeVisitor(owlObjectProperty, vowlData));
		} else {
			VowlObjectProperty prop = vowlData.getObjectPropertyForIri(owlObjectProperty.getIRI());
			prop.addRange(axiom.getRange().asOWLClass().getIRI());
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
