/*
 * AxiomParser.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.Constants;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.GeneralParser;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class AxiomParser extends GeneralParser {
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

	public Set<OWLClass> search(OWLEntity property, String axiom, boolean direction) {
		List<String> searchString = new ArrayList<>();

		if (direction) {
			searchString.add(Constants.AXIOM_OBJ_PROP_DOMAIN);
			searchString.add(Constants.AXIOM_DATA_PROP_DOMAIN);
		} else {
			searchString.add(Constants.AXIOM_OBJ_PROP_RANGE);
			searchString.add(Constants.AXIOM_DATA_PROP_RANGE);
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
}
