/*
 * AxiomParser.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Axiom_Annotations;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.containerElements.DisjointUnion;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties.BaseProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.GeneralParser;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;

import java.util.*;

/**
 *
 */
public class AxiomParser extends GeneralParser {

	public AxiomParser(OWLOntology ontology, OWLDataFactory factory, MapData mapData, OWLOntologyManager ontologyManager) {
		super(ontology, factory, mapData, ontologyManager);
	}

	/**
	 * MUST BE CALLED FIRST!
	 * Parses the axioms of the given entity.
	 * Stores the extracted axioms directly in the global data map.
	 *
	 * @param entity The entity to extract the axioms from.
	 */
	public void parseAxioms(OWLEntity entity) {
		Map<String, Map<String, List<OWLAxiom>>> mapping = mapData.getEntityToAxiom();
		Map<String, List<OWLAxiom>> axioms = new HashMap<String, List<OWLAxiom>>();

		for (OWLAxiom currentAxiom : entity.getReferencingAxioms(ontology)) {
			List<OWLAxiom> items = axioms.get(currentAxiom.getAxiomType().toString());

			if (items == null) {
				items = new ArrayList<OWLAxiom>();
			}

			items.add(currentAxiom);
			axioms.put(currentAxiom.getAxiomType().toString(), items);
		}

		mapping.put(entity.getIRI().toString(), axioms);
	}

	/**
	 * Returns the disjoint class expressions.
	 *
	 * @param entity The entity to get the disjoint expressions from.
	 * @return The disjoint expressions set.
	 */
	public Set<OWLClassExpression> getDisjoints(OWLEntity entity) {
		Map<String, Map<String, List<OWLAxiom>>> mapping = mapData.getEntityToAxiom();
		Map<String, List<OWLAxiom>> i = mapping.get(entity.getIRI().toString());

		List<OWLAxiom> j = i.get(Axiom_Annotations.AXIOM_DISJOINT);

		Set<OWLClassExpression> disjoints = new HashSet<OWLClassExpression>();

		if (j != null) {
			for (OWLAxiom currentAxiom : j) {
				disjoints.addAll(currentAxiom.getNestedClassExpressions());
			}
		}

		return disjoints;
	}

	/**
	 * Returns a set of VOWL DisjointUnions.
	 * ATTENTION: It automatically adds this disjoint unions to the mapdata. Probably it shouldn't
	 * to that right away?
	 *
	 * @param entity The entity to get the disjoint unions from.
	 * @return A set with disjoint union classes.
	 */
	public Set<DisjointUnion> getDisjointUnions(OWLEntity entity) {
		Map<String, Map<String, List<OWLAxiom>>> mapping = mapData.getEntityToAxiom();
		Map<String, List<OWLAxiom>> i = mapping.get(entity.getIRI().toString());

		List<OWLAxiom> j = i.get(Axiom_Annotations.AXIOM_DISJOINTUNION);
		Set<DisjointUnion> elements = new HashSet<DisjointUnion>();

		if (j != null) {
			for (OWLAxiom currentAxiom : j) {
				OWLDisjointUnionAxiom theAxiom = (OWLDisjointUnionAxiom) currentAxiom;
				BaseNode base = mapData.getMergedMap().get(theAxiom.getOWLClass().getIRI().toString());
				Set<BaseNode> disjoints = new HashSet<BaseNode>();

				for (OWLClass test : theAxiom.getOWLDisjointClassesAxiom().getClassesInSignature()) {
					disjoints.add(mapData.getMergedMap().get(test.getIRI().toString()));
				}

				DisjointUnion disjointUnion = new DisjointUnion(base, disjoints);

				if (!mapData.getDisjointUnions().contains(disjointUnion)) {
					mapData.getDisjointUnions().add(disjointUnion);
					elements.add(disjointUnion);
				}
			}
		}

		return elements;
	}

	/**
	 * Searches for an union class of the property. If already exists take it an return else create
	 * a new node. If no union found return null.
	 *
	 * @param property The property to search in.
	 */
	public Set<OWLClass> search(OWLEntity property, String axiom) {
		for (OWLAxiom currentAxiom : property.getReferencingAxioms(ontology)) {

			for (OWLClassExpression nestExpr : currentAxiom.getNestedClassExpressions()) {
				if (nestExpr.getClassExpressionType().toString().equals(axiom)) {
					return nestExpr.getClassesInSignature();
				}
			}
		}

		return null;
	}

	/**
	 * TODO Think of what this really is doing.
	 * Searches in equivalent axioms for given axiom.
	 *
	 * @param entity The entity to search in.
	 * @param axiom  The axiom to search in the equivalent axioms.
	 * @return A set of owl classes.
	 */
	public List<Set<OWLClass>> searchInEquivalents(OWLEntity entity, String axiom) {
		List<Set<OWLClass>> listOfNested = new ArrayList<Set<OWLClass>>();

		for (OWLAxiom currentAxiom : entity.getReferencingAxioms(ontology)) {
			// TODO if directly axiom is a logical axiom.

			for (OWLClassExpression nestExpr : currentAxiom.getNestedClassExpressions()) {
				if (nestExpr.getClassExpressionType().toString().equals(axiom)) {
					listOfNested.add(nestExpr.getClassesInSignature());
				}
			}
		}

		return listOfNested;
	}

	/**
	 * Search for a given axiom in the direction false = range, true = domain of the property.
	 *
	 * @param property  The property to search in.
	 * @param axiom     The axiom to search for.
	 * @param direction The desired direction. False = Range, True = Domain.
	 * @return A set of owl classes.
	 */
	public Set<OWLClass> search(OWLEntity property, String axiom, boolean direction) {
		List<String> searchString = new ArrayList<String>();

		if (direction) {
			searchString.add(Axiom_Annotations.AXIOM_OBJ_PROP_DOMAIN);
			searchString.add(Axiom_Annotations.AXIOM_DATA_PROP_DOMAIN);
		} else {
			searchString.add(Axiom_Annotations.AXIOM_OBJ_PROP_RANGE);
			searchString.add(Axiom_Annotations.AXIOM_DATA_PROP_RANGE);
		}

		for (OWLAxiom currentAxiom : property.getReferencingAxioms(ontology)) {
			if (!searchString.contains(currentAxiom.getAxiomType().toString())) {
				continue;
			}

			for (OWLClassExpression nestExpr : currentAxiom.getNestedClassExpressions()) {
				if (nestExpr.getClassExpressionType().toString().equals(axiom)) {
					return nestExpr.getClassesInSignature();
				}
			}
		}

		return null;
	}

	public void processAxioms(OWLOntology ontology) {
		OWLAxiomVisitor objectVisitor = new OWLObjectVisitor(mapData);

		if (ontology != null) {
			Set<OWLAxiom> axiomSet = ontology.getAxioms();
			HashMap<String, Integer> axiomsMap = new HashMap<String, Integer>();

			if (axiomSet != null && axiomSet.size() > 0) {
				Iterator<OWLAxiom> setIter = axiomSet.iterator();
				OWLAxiom axiom = null;

				while (setIter.hasNext()) {
					axiom = setIter.next();
					axiom.accept(objectVisitor);
					if (axiom.getNestedClassExpressions().size() > 0) {
						for (OWLClassExpression owlClassExpression : axiom.getNestedClassExpressions()) {
							owlClassExpression.accept((OWLClassExpressionVisitor) objectVisitor);
						}
					}
				}
			}

		}

	}
}

class OWLObjectVisitor extends OWLObjectVisitorAdapter {
	private static final Logger logger = LogManager.getRootLogger();
	MapData data;

	public OWLObjectVisitor(MapData data) {
		this.data = data;
	}

	@Override
	public void visit(OWLDataMaxCardinality desc) {
		BaseProperty prop = getProperty(desc.getProperty().asOWLDataProperty().getIRI().toString());
		prop.setMinCardinality(desc.getCardinality());
		super.visit(desc);
		logger.info("Max Cardinality =[" + desc.getCardinality() + "]");
	}

	@Override
	public void visit(OWLDataMinCardinality desc) {
		BaseProperty prop = getProperty(desc.getProperty().asOWLDataProperty().getIRI().toString());
		prop.setMinCardinality(desc.getCardinality());
		super.visit(desc);
		logger.info("Min Cardinality =[" + desc.getCardinality() + "]");
	}

	@Override
	public void visit(OWLObjectMaxCardinality desc) {
		BaseProperty prop = getProperty(desc.getProperty().asOWLObjectProperty().getIRI().toString());
		prop.setMinCardinality(desc.getCardinality());
		super.visit(desc);
		logger.info("Object Max Cardinality =[" + desc.getCardinality() + "]");
	}

	@Override
	public void visit(OWLObjectMinCardinality desc) {
		BaseProperty prop = getProperty(desc.getProperty().asOWLObjectProperty().getIRI().toString());
		prop.setMinCardinality(desc.getCardinality());
		super.visit(desc);
		logger.info("Object Min Cardinality =[" + desc.getCardinality() + "]");
	}

	@Override
	public void visit(OWLDataExactCardinality desc) {
		BaseProperty prop = getProperty(desc.getProperty().asOWLDataProperty().getIRI().toString());
		prop.setMinCardinality(desc.getCardinality());
		super.visit(desc);
		logger.info("Exact Data Cardinality =[" + desc.getCardinality() + "]");
	}

	@Override
	public void visit(OWLObjectExactCardinality desc) {
		BaseProperty prop = getProperty(desc.getProperty().asOWLObjectProperty().getIRI().toString());
		prop.setMinCardinality(desc.getCardinality());
		super.visit(desc);
		logger.info("Exact Object Cardinality =[" + desc.getCardinality() + "]");
	}

	private BaseProperty getProperty(String iri) {
		return data.getMergedProperties().get(iri);
	}
}
