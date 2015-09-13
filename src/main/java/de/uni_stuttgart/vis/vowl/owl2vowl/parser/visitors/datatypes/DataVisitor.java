/*
 * TestDataVisitor.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.visitors.datatypes;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Standard_Iris;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.*;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.OntologyInformation;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper.ElementFinder;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.visitors.AbstractVowlVisitor;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class DataVisitor extends AbstractVowlVisitor implements OWLDataVisitorEx<BaseDatatype> {

	public DataVisitor(OWLObject topLevel, OWLObject entity, OntologyInformation ontologyInformation, ElementFinder finder) {
		super(topLevel, entity, ontologyInformation, finder);
	}

	protected BaseDatatype findCorrectTopDatatype(Set<BaseDatatype> datatypes) {
		BaseDatatype currentBase = null;

		for (BaseDatatype datatype : datatypes) {
			if (currentBase == null || highClass(datatype)) {
				currentBase = datatype;
			}
		}

		return currentBase;
	}

	protected boolean highClass(BaseDatatype higher) {
		List<Class<? extends BaseDatatype>> higherClassList = new ArrayList<Class<? extends BaseDatatype>>();
		higherClassList.add(DataIntersectionOf.class);
		higherClassList.add(DataOneOf.class);
		higherClassList.add(DataComplementOf.class);
		higherClassList.add(DataUnionOf.class);


		if (higherClassList.contains(higher.getClass())) {
			return true;
		}

		return false;
	}

	@Override
	public BaseDatatype visit(OWLDatatype node) {
		if (node.isBuiltIn() || node.getIRI().getNamespace().equals(Standard_Iris.TYPE_XML_2001)) {
			RdfsDatatype datatype = new RdfsDatatype();
			datatype.setIri(node.getIRI().toString());
			datatype.setName(node.getIRI().getFragment());
			return datatype;
		}

		Set<BaseDatatype> test = new HashSet<BaseDatatype>();

		for (OWLAxiom owlAxiom : node.getReferencingAxioms(ontologyInformation.getOntology())) {
			BaseDatatype visited = owlAxiom.accept(new DataAxiomVisitor(topLevelElement, owlAxiom, ontologyInformation, finder));

			if (visited == null) {
				continue;
			}

			test.add(visited);
		}

		// TODO element rausfiltern, dessen IRI nicht die benötigte ist!!!
		// Beziehungsweise, sauberere Lösung überlegen
		BaseDatatype base = findCorrectTopDatatype(test);
		base.setIri(node.getIRI().toString());
		base.setName(node.getIRI().getNamespace());

		return base;
	}

	@Override
	public BaseDatatype visit(OWLDataComplementOf node) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLDataOneOf node) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLDataIntersectionOf node) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLDataUnionOf node) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLDatatypeRestriction node) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLLiteral node) {
		return null;
	}

	@Override
	public BaseDatatype visit(OWLFacetRestriction node) {
		return null;
	}
}
