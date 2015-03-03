/*
 * AxiomVisitor.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.visitors;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.SpecialClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.OntologyInformation;
import org.semanticweb.owlapi.model.*;

/**
 *
 */
public class AxiomVisitor implements OWLObjectVisitor {
	private MapData information;
	private OWLEntity entity;
	private SpecialClass target;

	public AxiomVisitor(MapData information, OWLEntity entity, SpecialClass target) {
		this.information = information;
		this.entity = entity;
		this.target = target;
	}

	@Override
	public void visit(OWLAnnotation node) {

	}

	@Override
	public void visit(IRI iri) {

	}

	@Override
	public void visit(OWLDeclarationAxiom axiom) {

	}

	@Override
	public void visit(OWLSubClassOfAxiom axiom) {

	}

	@Override
	public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {

	}

	@Override
	public void visit(OWLAsymmetricObjectPropertyAxiom axiom) {

	}

	@Override
	public void visit(OWLReflexiveObjectPropertyAxiom axiom) {

	}

	@Override
	public void visit(OWLDisjointClassesAxiom axiom) {

	}

	@Override
	public void visit(OWLDataPropertyDomainAxiom axiom) {

	}

	@Override
	public void visit(OWLObjectPropertyDomainAxiom axiom) {

	}

	@Override
	public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {

	}

	@Override
	public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {

	}

	@Override
	public void visit(OWLDifferentIndividualsAxiom axiom) {

	}

	@Override
	public void visit(OWLDisjointDataPropertiesAxiom axiom) {

	}

	@Override
	public void visit(OWLDisjointObjectPropertiesAxiom axiom) {

	}

	@Override
	public void visit(OWLObjectPropertyRangeAxiom axiom) {

	}

	@Override
	public void visit(OWLObjectPropertyAssertionAxiom axiom) {

	}

	@Override
	public void visit(OWLFunctionalObjectPropertyAxiom axiom) {

	}

	@Override
	public void visit(OWLSubObjectPropertyOfAxiom axiom) {

	}

	@Override
	public void visit(OWLDisjointUnionAxiom axiom) {

	}

	@Override
	public void visit(OWLSymmetricObjectPropertyAxiom axiom) {

	}

	@Override
	public void visit(OWLDataPropertyRangeAxiom axiom) {

	}

	@Override
	public void visit(OWLFunctionalDataPropertyAxiom axiom) {

	}

	@Override
	public void visit(OWLEquivalentDataPropertiesAxiom axiom) {

	}

	@Override
	public void visit(OWLClassAssertionAxiom axiom) {

	}

	@Override
	public void visit(OWLEquivalentClassesAxiom axiom) {
		for (OWLClassExpression owlClassExpression : axiom.getNestedClassExpressions()) {
			owlClassExpression.accept(new ClassExpressionVisitor(information, entity, target));
		}
	}

	@Override
	public void visit(OWLDataPropertyAssertionAxiom axiom) {

	}

	@Override
	public void visit(OWLTransitiveObjectPropertyAxiom axiom) {

	}

	@Override
	public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) {

	}

	@Override
	public void visit(OWLSubDataPropertyOfAxiom axiom) {

	}

	@Override
	public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {

	}

	@Override
	public void visit(OWLSameIndividualAxiom axiom) {

	}

	@Override
	public void visit(OWLSubPropertyChainOfAxiom axiom) {

	}

	@Override
	public void visit(OWLInverseObjectPropertiesAxiom axiom) {

	}

	@Override
	public void visit(OWLHasKeyAxiom axiom) {

	}

	@Override
	public void visit(OWLDatatypeDefinitionAxiom axiom) {

	}

	@Override
	public void visit(SWRLRule rule) {

	}

	@Override
	public void visit(SWRLClassAtom node) {

	}

	@Override
	public void visit(SWRLDataRangeAtom node) {

	}

	@Override
	public void visit(SWRLObjectPropertyAtom node) {

	}

	@Override
	public void visit(SWRLDataPropertyAtom node) {

	}

	@Override
	public void visit(SWRLBuiltInAtom node) {

	}

	@Override
	public void visit(SWRLVariable node) {

	}

	@Override
	public void visit(SWRLIndividualArgument node) {

	}

	@Override
	public void visit(SWRLLiteralArgument node) {

	}

	@Override
	public void visit(SWRLSameIndividualAtom node) {

	}

	@Override
	public void visit(SWRLDifferentIndividualsAtom node) {

	}

	@Override
	public void visit(OWLAnnotationAssertionAxiom axiom) {

	}

	@Override
	public void visit(OWLSubAnnotationPropertyOfAxiom axiom) {

	}

	@Override
	public void visit(OWLAnnotationPropertyDomainAxiom axiom) {

	}

	@Override
	public void visit(OWLAnnotationPropertyRangeAxiom axiom) {

	}

	@Override
	public void visit(OWLClass ce) {

	}

	@Override
	public void visit(OWLNamedIndividual individual) {

	}

	@Override
	public void visit(OWLOntology ontology) {

	}

	@Override
	public void visit(OWLAnonymousIndividual individual) {

	}

	@Override
	public void visit(OWLObjectIntersectionOf ce) {

	}

	@Override
	public void visit(OWLObjectUnionOf ce) {

	}

	@Override
	public void visit(OWLObjectComplementOf ce) {

	}

	@Override
	public void visit(OWLObjectSomeValuesFrom ce) {

	}

	@Override
	public void visit(OWLObjectAllValuesFrom ce) {

	}

	@Override
	public void visit(OWLObjectHasValue ce) {

	}

	@Override
	public void visit(OWLObjectMinCardinality ce) {

	}

	@Override
	public void visit(OWLObjectExactCardinality ce) {

	}

	@Override
	public void visit(OWLObjectMaxCardinality ce) {

	}

	@Override
	public void visit(OWLObjectHasSelf ce) {

	}

	@Override
	public void visit(OWLObjectOneOf ce) {

	}

	@Override
	public void visit(OWLDataSomeValuesFrom ce) {

	}

	@Override
	public void visit(OWLDataAllValuesFrom ce) {

	}

	@Override
	public void visit(OWLDataHasValue ce) {

	}

	@Override
	public void visit(OWLDataMinCardinality ce) {

	}

	@Override
	public void visit(OWLDataExactCardinality ce) {

	}

	@Override
	public void visit(OWLDataMaxCardinality ce) {

	}

	@Override
	public void visit(OWLLiteral node) {

	}

	@Override
	public void visit(OWLFacetRestriction node) {

	}

	@Override
	public void visit(OWLDatatype node) {

	}

	@Override
	public void visit(OWLAnnotationProperty property) {

	}

	@Override
	public void visit(OWLDataOneOf node) {

	}

	@Override
	public void visit(OWLDataComplementOf node) {

	}

	@Override
	public void visit(OWLDataIntersectionOf node) {

	}

	@Override
	public void visit(OWLDataUnionOf node) {

	}

	@Override
	public void visit(OWLDatatypeRestriction node) {

	}

	@Override
	public void visit(OWLObjectProperty property) {

	}

	@Override
	public void visit(OWLObjectInverseOf property) {

	}

	@Override
	public void visit(OWLDataProperty property) {

	}
}
