package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.property;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.VowlAttribute;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.AbstractClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.NullClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.properties.AbstractProperty;
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
		IRI domainIri;

		if (axiom.getDomain().isAnonymous()){
			AbstractClass anonymClass = axiom.getDomain().accept(new DomainRangeVisitor(owlObjectProperty, vowlData));
			if (!(anonymClass instanceof NullClass)) {
				domainIri = anonymClass.getIri();
			} else {
				logger.info("Skipped anonymous object domain: " + axiom.getDomain());
				return;
			}
		} else {
			if (axiom.getDomain().asOWLClass().isOWLThing()) {
				return;
			}

			domainIri = axiom.getDomain().asOWLClass().getIRI();
		}

		prop.setDomain(domainIri);
		vowlData.getClassForIri(domainIri).addOutGoingProperty(prop.getIri());
	}

	@Override
	public void visit(OWLObjectPropertyRangeAxiom axiom) {
		VowlObjectProperty prop = vowlData.getObjectPropertyForIri(owlObjectProperty.getIRI());
		IRI rangeIri;

		if (axiom.getRange().isAnonymous()){
			AbstractClass anonymClass = axiom.getRange().accept(new DomainRangeVisitor(owlObjectProperty, vowlData));
			if (!(anonymClass instanceof NullClass)) {
				rangeIri = anonymClass.getIri();
			} else {
				logger.info("Skipped anonymous object range: " + axiom.getRange());
				return;
			}
		} else {
			if (axiom.getRange().asOWLClass().isOWLThing()) {
				return;
			}
			rangeIri = axiom.getRange().asOWLClass().getIRI();
		}

		prop.setRange(rangeIri);
		vowlData.getClassForIri(rangeIri).addInGoingProperty(prop.getIri());
	}

	@Override
	public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
		logger.info("Disjoint object properties not supported yet.");
	}

	@Override
	public void visit(OWLInverseObjectPropertiesAxiom axiom) {
		OWLObjectPropertyExpression firstProperty = axiom.getFirstProperty();
		OWLObjectPropertyExpression secondProperty = axiom.getSecondProperty();

		if (firstProperty.isAnonymous()) {
			// TODO anonym behaviour
			logger.info("Anonym first property:" + firstProperty);
			return;
		}

		if (secondProperty.isAnonymous()) {
			// TODO anonym behaviour
			logger.info("Anonym second property:" + secondProperty);
			return;
		}

		OWLObjectProperty inverseProperty = firstProperty.asOWLObjectProperty();
		OWLObjectProperty baseProperty = secondProperty.asOWLObjectProperty();

		AbstractProperty inverseVowlProp = vowlData.getPropertyForIri(inverseProperty.getIRI());
		inverseVowlProp.addInverse(baseProperty.getIRI());
	}

	@Override
	public void visit(OWLSubObjectPropertyOfAxiom axiom) {
		OWLObjectPropertyExpression subProperty = axiom.getSubProperty();
		OWLObjectPropertyExpression superProperty = axiom.getSuperProperty();

		if (subProperty.isAnonymous()) {
			// TODO anonym behaviour
			logger.info("Anonym sub property:" + subProperty);
			return;
		}

		if (superProperty.isAnonymous()) {
			// TODO anonym behaviour
			logger.info("Anonym super property:" + superProperty);
			return;
		}

		AbstractProperty subVowl = vowlData.getPropertyForIri(subProperty.asOWLObjectProperty().getIRI());
		AbstractProperty superVowl = vowlData.getPropertyForIri(superProperty.asOWLObjectProperty().getIRI());
		subVowl.addSuperEntity(superVowl.getIri());
		superVowl.addSubEntity(subVowl.getIri());
	}

	@Override
	public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
		AbstractProperty base = vowlData.getPropertyForIri(owlObjectProperty.getIRI());

		for (OWLObjectPropertyExpression expr : axiom.getProperties()) {
			if (expr.isAnonymous()) {
				// TODO anonymous behaviour
				logger.info("Anonysmous equivalent prop: " + expr);
				continue;
			}

			if (expr.asOWLObjectProperty().getIRI().equals(base.getIri())) {
				continue;
			}

			base.addEquivalentElement(expr.asOWLObjectProperty().getIRI());
		}
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

	@Override
	public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
		VowlObjectProperty prop = vowlData.getObjectPropertyForIri(owlObjectProperty.getIRI());
		prop.addAttribute(VowlAttribute.TRANSITIVE);
	}
}
