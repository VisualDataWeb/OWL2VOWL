/*
 * OntologyInfoVisitorImpl.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.visitors;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.OntologyInfo;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper.AnnotationParser;
import org.semanticweb.owlapi.model.*;

/**
 *
 */
public class OntologyInfoVisitorImpl implements OWLAxiomVisitor {
	private OntologyInfo info;

	public OntologyInfoVisitorImpl(OntologyInfo info) {
		this.info = info;
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

	}

	@Override
	public void visit(OWLDataPropertyAssertionAxiom axiom) {
		AnnotationParser.getAnnotation(axiom, null);
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
}
