/*
 * TestDataRangeVisitor.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.visitors.datatypes;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.BaseDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.RdfsDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.OntologyInformation;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper.ElementFinder;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.visitors.AbstractVowlVisitor;
import org.semanticweb.owlapi.model.*;

/**
 *
 */
public class DataAxiomVisitor extends AbstractVowlVisitor implements OWLAxiomVisitorEx<BaseDatatype> {

	public DataAxiomVisitor(OWLObject topLevel, OWLObject entity, OntologyInformation ontologyInformation, ElementFinder finder) {
		super(topLevel, entity, ontologyInformation, finder);
	}

	@Override
	public BaseDatatype visit(OWLSubClassOfAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLAsymmetricObjectPropertyAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLReflexiveObjectPropertyAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLDisjointClassesAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLDataPropertyDomainAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLObjectPropertyDomainAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLEquivalentObjectPropertiesAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLDifferentIndividualsAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLDisjointDataPropertiesAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLDisjointObjectPropertiesAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLObjectPropertyRangeAxiom axiom) {
		System.out.println(axiom);
		return null;
	}

	@Override
	public BaseDatatype visit(OWLObjectPropertyAssertionAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLFunctionalObjectPropertyAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLSubObjectPropertyOfAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLDisjointUnionAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLDeclarationAxiom axiom) {
		OWLEntity entity = axiom.getEntity();

		if (!entity.isOWLDatatype()) {
			return null;
		}

		OWLDatatype owlDatatype = entity.asOWLDatatype();

		RdfsDatatype datatype = new RdfsDatatype();
		datatype.setName(owlDatatype.getIRI().getFragment());
		datatype.setIri(owlDatatype.getIRI().toString());

		return datatype;
	}

	@Override
	public BaseDatatype visit(OWLAnnotationAssertionAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLSubAnnotationPropertyOfAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLAnnotationPropertyDomainAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLAnnotationPropertyRangeAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLSymmetricObjectPropertyAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLDataPropertyRangeAxiom axiom) {
		return axiom.getRange().accept(new DataObjectVisitor(topLevelElement, upperStageCaller, ontologyInformation, finder));
	}

	@Override
	public BaseDatatype visit(OWLFunctionalDataPropertyAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLEquivalentDataPropertiesAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLClassAssertionAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLEquivalentClassesAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLDataPropertyAssertionAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLTransitiveObjectPropertyAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLSubDataPropertyOfAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLSameIndividualAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLSubPropertyChainOfAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLInverseObjectPropertiesAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLHasKeyAxiom axiom) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLDatatypeDefinitionAxiom axiom) {
		BaseDatatype data = axiom.getDataRange().accept(new DataObjectVisitor(topLevelElement, axiom, ontologyInformation, finder));

		return data;
	}

	@Override
	public BaseDatatype visit(SWRLRule rule) {
		return null;
	}
}
