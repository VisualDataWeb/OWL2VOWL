package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.property;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.VowlAttribute;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.AbstractNode;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.NullClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.AbstractProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.VowlObjectProperty;

import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.*;

public class ObjectPropertyVisitor extends PropertyVisitor {
	private Logger logger = LogManager.getLogger(ObjectPropertyVisitor.class);

	public ObjectPropertyVisitor(VowlData vowlData, OWLProperty owlObjectProperty) {
		super(vowlData, owlObjectProperty);
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
			AbstractNode anonymClass = axiom.getDomain().accept(new DomainRangeVisitor(owlObjectProperty, vowlData));
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

		prop.addDomain(domainIri);
		vowlData.getClassForIri(domainIri).addOutGoingProperty(prop.getIri());
	}

	@Override
	public void visit(OWLObjectPropertyRangeAxiom axiom) {
		VowlObjectProperty prop = vowlData.getObjectPropertyForIri(owlObjectProperty.getIRI());
		IRI rangeIri;

		if (axiom.getRange().isAnonymous()){
			AbstractNode anonymClass = axiom.getRange().accept(new DomainRangeVisitor(owlObjectProperty, vowlData));
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

		prop.addRange(rangeIri);
		vowlData.getClassForIri(rangeIri).addInGoingProperty(prop.getIri());
	}

	@Override
	public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
		logger.info("Disjoint object properties not supported: " + axiom);
	}

	@Override
	public void visit(OWLInverseObjectPropertiesAxiom axiom) {
		OWLObjectPropertyExpression firstProperty = axiom.getFirstProperty();
		OWLObjectPropertyExpression secondProperty = axiom.getSecondProperty();

		if (firstProperty.isAnonymous()) {
			// TODO anonymous behavior
			logger.info("Anonym first property:" + firstProperty);
			return;
		}

		if (secondProperty.isAnonymous()) {
			// TODO anonymous behavior
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
			// TODO anonymous behavior
			logger.info("Anonym sub property:" + subProperty);
			return;
		}

		if (superProperty.isAnonymous()) {
			// TODO anonymous behavior
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

		for (OWLObjectPropertyExpression expr : axiom.properties().collect(Collectors.toSet())) {
			if (expr.isAnonymous()) {
				// TODO anonymous behavior
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
