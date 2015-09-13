/*
 * TestDataObjectVisitor.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.visitors.datatypes;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.*;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.OntologyInformation;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper.ElementFinder;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.visitors.AbstractVowlVisitor;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLDatatypeDefinitionAxiomImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class DataObjectVisitor extends AbstractVowlVisitor implements OWLDataRangeVisitorEx<BaseDatatype> {

	public DataObjectVisitor(OWLObject topLevel, OWLObject entity, OntologyInformation ontologyInformation, ElementFinder finder) {
		super(topLevel, entity, ontologyInformation, finder);
	}

	@Override
	public BaseDatatype visit(OWLDatatype node) {
		if (node.isBuiltIn()) {
			RdfsDatatype datatype = new RdfsDatatype();
			datatype.setIri(node.getIRI().toString());
			datatype.setName(node.getIRI().getFragment());
			return datatype;
		}

		BaseDatatype datatype = finder.findOrCreateDatatype();

		// TODO currently overriding probably existing attributes
		datatype.setIri(node.getIRI().toString());
		datatype.setName(node.getIRI().getFragment());

		return datatype;
	}

	@Override
	public BaseDatatype visit(OWLDataOneOf node) {
		DataOneOf dataOneOf = new DataOneOf();

		for (OWLLiteral owlLiteral : node.getValues()) {
			RdfsLiteral literal = new RdfsLiteral();
			literal.setName(owlLiteral.getLiteral());
			dataOneOf.getOneOf().add(literal);
		}

		return dataOneOf;
	}

	@Override
	public BaseDatatype visit(OWLDataComplementOf node) {
		OWLDatatype datatype = ((OWLDatatypeDefinitionAxiomImpl) upperStageCaller).getDatatype();

		if (datatype == null || datatype.getIRI() == null) {
			System.err.println("Calling entity is no datatype or contains no iri: " + upperStageCaller);
			return null;
		}

		String iri = datatype.getIRI().toString();
		String complementIri = node.getDataRange().getDataRangeType().getIRI().toString();
		DataComplementOf dataComplementOf = finder.findOrCreateDataComplementOf(iri, complementIri);
		dataComplementOf.setIri(iri);
		dataComplementOf.getStringComplementOf().add(complementIri);

		return dataComplementOf;
	}

	@Override
	public BaseDatatype visit(OWLDataIntersectionOf node) {
		List<BaseDatatype> intersectionTypes = new ArrayList<BaseDatatype>();
		OWLDatatype datatype = ((OWLDatatypeDefinitionAxiomImpl) upperStageCaller).getDatatype();

		for (OWLDataRange owlDataRange : node.getOperands()) {
			intersectionTypes.add(owlDataRange.accept(new DataObjectVisitor(topLevelElement, upperStageCaller, ontologyInformation, finder)));
		}

		DataIntersectionOf dataIntersection = finder.findOrCreateDataIntersection(intersectionTypes);
		dataIntersection.setIri(datatype.getIRI().toString());
		dataIntersection.getIntersectionOf().clear();
		dataIntersection.getIntersectionOf().addAll(intersectionTypes);

		return dataIntersection;
	}

	@Override
	public BaseDatatype visit(OWLDataUnionOf node) {
		Set<OWLDataRange> operands = node.getOperands();
		OWLDatatype datatype = ((OWLDatatypeDefinitionAxiomImpl) upperStageCaller).getDatatype();

		if (datatype == null || datatype.getIRI() == null) {
			System.err.println("Calling entity is no datatype or contains no iri: " + upperStageCaller);
			return null;
		}

		String iri = datatype.getIRI().toString();
		DataUnionOf dataUnionOf = finder.findOrCreateDataUnionOf(iri);
		dataUnionOf.setIri(iri);

		for (OWLDataRange operand : operands) {
			dataUnionOf.getStringUnionOf().add(operand.getDataRangeType().getIRI().toString());
		}

		return dataUnionOf;
	}

	@Override
	public BaseDatatype visit(OWLDatatypeRestriction node) {
		System.err.println(OWLDatatypeRestriction.class + " not supported yet. In entity " + upperStageCaller);
		return null;
	}
}
