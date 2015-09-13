/*
 * ClassObjectVisitor.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.visitors.classes;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.BaseClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.OwlClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.OwlIntersectionOf;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.OwlUnionOf;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.OntologyInformation;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper.ElementFinder;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.visitors.AbstractVowlVisitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.*;

/**
 *
 */
public class ClassObjectVisitor extends AbstractVowlVisitor implements OWLObjectVisitorEx<BaseClass> {
	Logger logger = LogManager.getLogger(ClassObjectVisitor.class);

	public ClassObjectVisitor(OWLObject topLevelElement, OWLObject upperStageCaller, OntologyInformation ontologyInformation, ElementFinder finder) {
		super(topLevelElement, upperStageCaller, ontologyInformation, finder);
	}

	protected String getUpperIri() {
		if (upperStageCaller instanceof OWLClass) {
			return ((OWLClass)upperStageCaller).getIRI().toString();
		}

		return null;
	}

	@Override
	public BaseClass visit(OWLAnnotation owlAnnotation) {
		return null;
	}

	@Override
	public BaseClass visit(IRI iri) {
		return null;
	}

	@Override
	public BaseClass visit(OWLAnonymousIndividual owlAnonymousIndividual) {
		return null;
	}

	@Override
	public BaseClass visit(OWLSubClassOfAxiom owlSubClassOfAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLNegativeObjectPropertyAssertionAxiom owlNegativeObjectPropertyAssertionAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLAsymmetricObjectPropertyAxiom owlAsymmetricObjectPropertyAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLReflexiveObjectPropertyAxiom owlReflexiveObjectPropertyAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLDisjointClassesAxiom owlDisjointClassesAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLDataPropertyDomainAxiom owlDataPropertyDomainAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLObjectPropertyDomainAxiom owlObjectPropertyDomainAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLEquivalentObjectPropertiesAxiom owlEquivalentObjectPropertiesAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLNegativeDataPropertyAssertionAxiom owlNegativeDataPropertyAssertionAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLDifferentIndividualsAxiom owlDifferentIndividualsAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLDisjointDataPropertiesAxiom owlDisjointDataPropertiesAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLDisjointObjectPropertiesAxiom owlDisjointObjectPropertiesAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLObjectPropertyRangeAxiom owlObjectPropertyRangeAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLObjectPropertyAssertionAxiom owlObjectPropertyAssertionAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLFunctionalObjectPropertyAxiom owlFunctionalObjectPropertyAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLSubObjectPropertyOfAxiom owlSubObjectPropertyOfAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLDisjointUnionAxiom owlDisjointUnionAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLDeclarationAxiom owlDeclarationAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLAnnotationAssertionAxiom owlAnnotationAssertionAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLSubAnnotationPropertyOfAxiom owlSubAnnotationPropertyOfAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLAnnotationPropertyDomainAxiom owlAnnotationPropertyDomainAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLAnnotationPropertyRangeAxiom owlAnnotationPropertyRangeAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLSymmetricObjectPropertyAxiom owlSymmetricObjectPropertyAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLDataPropertyRangeAxiom owlDataPropertyRangeAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLFunctionalDataPropertyAxiom owlFunctionalDataPropertyAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLEquivalentDataPropertiesAxiom owlEquivalentDataPropertiesAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLClassAssertionAxiom owlClassAssertionAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLEquivalentClassesAxiom owlEquivalentClassesAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLDataPropertyAssertionAxiom owlDataPropertyAssertionAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLTransitiveObjectPropertyAxiom owlTransitiveObjectPropertyAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLIrreflexiveObjectPropertyAxiom owlIrreflexiveObjectPropertyAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLSubDataPropertyOfAxiom owlSubDataPropertyOfAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLInverseFunctionalObjectPropertyAxiom owlInverseFunctionalObjectPropertyAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLSameIndividualAxiom owlSameIndividualAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLSubPropertyChainOfAxiom owlSubPropertyChainOfAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLInverseObjectPropertiesAxiom owlInverseObjectPropertiesAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLHasKeyAxiom owlHasKeyAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLDatatypeDefinitionAxiom owlDatatypeDefinitionAxiom) {
		return null;
	}

	@Override
	public BaseClass visit(SWRLRule swrlRule) {
		return null;
	}

	@Override
	public BaseClass visit(SWRLClassAtom swrlClassAtom) {
		return null;
	}

	@Override
	public BaseClass visit(SWRLDataRangeAtom swrlDataRangeAtom) {
		return null;
	}

	@Override
	public BaseClass visit(SWRLObjectPropertyAtom swrlObjectPropertyAtom) {
		return null;
	}

	@Override
	public BaseClass visit(SWRLDataPropertyAtom swrlDataPropertyAtom) {
		return null;
	}

	@Override
	public BaseClass visit(SWRLBuiltInAtom swrlBuiltInAtom) {
		return null;
	}

	@Override
	public BaseClass visit(SWRLVariable swrlVariable) {
		return null;
	}

	@Override
	public BaseClass visit(SWRLIndividualArgument swrlIndividualArgument) {
		return null;
	}

	@Override
	public BaseClass visit(SWRLLiteralArgument swrlLiteralArgument) {
		return null;
	}

	@Override
	public BaseClass visit(SWRLSameIndividualAtom swrlSameIndividualAtom) {
		return null;
	}

	@Override
	public BaseClass visit(SWRLDifferentIndividualsAtom swrlDifferentIndividualsAtom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLClass owlClass) {
		OwlClass owlClass1 = new OwlClass();
		owlClass1.setIri(owlClass.getIRI().toString());
		finder.addClass(owlClass1.getIri(), owlClass1);
		return null;
	}

	@Override
	public BaseClass visit(OWLNamedIndividual owlNamedIndividual) {
		return null;
	}

	@Override
	public BaseClass visit(OWLOntology owlOntology) {
		return null;
	}

	@Override
	public BaseClass visit(OWLObjectIntersectionOf owlObjectIntersectionOf) {
		OwlIntersectionOf intersectionOf = new OwlIntersectionOf();
		intersectionOf.setIri(getUpperIri());
		for (OWLClassExpression owlClassExpression : owlObjectIntersectionOf.getOperands()) {
			if (!owlClassExpression.isAnonymous()) {
				OWLClass owlClass = owlClassExpression.asOWLClass();
				intersectionOf.getStringIntersectionOf().add(owlClass.getIRI().toString());
			} else {
				// TODO
			}
		}

		finder.addClass(intersectionOf.getIri(), intersectionOf);
		return null;
	}

	@Override
	public BaseClass visit(OWLObjectUnionOf owlObjectUnionOf) {
		OwlUnionOf unionOf = new OwlUnionOf();
		unionOf.setIri(getUpperIri());
		for (OWLClassExpression owlClassExpression : owlObjectUnionOf.getOperands()) {
			if (!owlClassExpression.isAnonymous()) {
				OWLClass owlClass = owlClassExpression.asOWLClass();
				unionOf.getStringUnionOf().add(owlClass.getIRI().toString());
			} else {
				// TODO
			}
		}

		finder.addClass(unionOf.getIri(), unionOf);
		return null;
	}

	@Override
	public BaseClass visit(OWLObjectComplementOf owlObjectComplementOf) {
		return null;
	}

	@Override
	public BaseClass visit(OWLObjectSomeValuesFrom owlObjectSomeValuesFrom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLObjectAllValuesFrom owlObjectAllValuesFrom) {
		logger.debug("AllValuesFrom not working yet.");
		return null;
	}

	@Override
	public BaseClass visit(OWLObjectHasValue owlObjectHasValue) {
		return null;
	}

	@Override
	public BaseClass visit(OWLObjectMinCardinality owlObjectMinCardinality) {
		return null;
	}

	@Override
	public BaseClass visit(OWLObjectExactCardinality owlObjectExactCardinality) {
		return null;
	}

	@Override
	public BaseClass visit(OWLObjectMaxCardinality owlObjectMaxCardinality) {
		return null;
	}

	@Override
	public BaseClass visit(OWLObjectHasSelf owlObjectHasSelf) {
		return null;
	}

	@Override
	public BaseClass visit(OWLObjectOneOf owlObjectOneOf) {
		return null;
	}

	@Override
	public BaseClass visit(OWLDataSomeValuesFrom owlDataSomeValuesFrom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLDataAllValuesFrom owlDataAllValuesFrom) {
		return null;
	}

	@Override
	public BaseClass visit(OWLDataHasValue owlDataHasValue) {
		return null;
	}

	@Override
	public BaseClass visit(OWLDataMinCardinality owlDataMinCardinality) {
		return null;
	}

	@Override
	public BaseClass visit(OWLDataExactCardinality owlDataExactCardinality) {
		return null;
	}

	@Override
	public BaseClass visit(OWLDataMaxCardinality owlDataMaxCardinality) {
		return null;
	}

	@Override
	public BaseClass visit(OWLDatatype owlDatatype) {
		return null;
	}

	@Override
	public BaseClass visit(OWLAnnotationProperty owlAnnotationProperty) {
		return null;
	}

	@Override
	public BaseClass visit(OWLDataComplementOf owlDataComplementOf) {
		return null;
	}

	@Override
	public BaseClass visit(OWLDataOneOf owlDataOneOf) {
		return null;
	}

	@Override
	public BaseClass visit(OWLDataIntersectionOf owlDataIntersectionOf) {
		return null;
	}

	@Override
	public BaseClass visit(OWLDataUnionOf owlDataUnionOf) {
		return null;
	}

	@Override
	public BaseClass visit(OWLDatatypeRestriction owlDatatypeRestriction) {
		return null;
	}

	@Override
	public BaseClass visit(OWLLiteral owlLiteral) {
		return null;
	}

	@Override
	public BaseClass visit(OWLFacetRestriction owlFacetRestriction) {
		return null;
	}

	@Override
	public BaseClass visit(OWLObjectProperty owlObjectProperty) {
		return null;
	}

	@Override
	public BaseClass visit(OWLObjectInverseOf owlObjectInverseOf) {
		return null;
	}

	@Override
	public BaseClass visit(OWLDataProperty owlDataProperty) {
		return null;
	}
}
