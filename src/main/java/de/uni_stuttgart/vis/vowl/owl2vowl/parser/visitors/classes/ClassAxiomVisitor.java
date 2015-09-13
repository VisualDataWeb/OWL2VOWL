/*
 * ClassAxiomVisitor.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.visitors.classes;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.BaseClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.OntologyInformation;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper.ElementFinder;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.visitors.AbstractVowlVisitor;
import org.semanticweb.owlapi.model.*;

import java.util.Set;

/**
 *
 */
public class ClassAxiomVisitor extends AbstractVowlVisitor implements OWLAxiomVisitorEx<BaseClass> {
	public ClassAxiomVisitor(OWLObject topLevel, OWLObject entity, OntologyInformation ontologyInformation, ElementFinder finder) {
		super(topLevel, entity, ontologyInformation, finder);
	}

	@Override
	public BaseClass visit(OWLSubClassOfAxiom owlSubClassOfAxiom) {
		OWLClassExpression superClass = owlSubClassOfAxiom.getSuperClass();
		return superClass.accept(new ClassObjectVisitor(topLevelElement, superClass, ontologyInformation, finder));
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
		Set<OWLClass> namedClasses = owlEquivalentClassesAxiom.getNamedClasses();
		if (namedClasses.size() != 1) {
			// TODO
			return null;
		}

		for (OWLClassExpression owlClassExpression : owlEquivalentClassesAxiom.getClassExpressions()) {
			owlClassExpression.accept(new ClassObjectVisitor(topLevelElement, namedClasses.iterator().next(), ontologyInformation, finder));
		}

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
}
