/*
 * UnionParser.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.Constants;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.OwlUnionOf;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.GeneralParser;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class UnionParser extends GeneralParser{
	Map<String, OwlUnionOf> unionNodes = mapData.getUnionMap();

	/**
	 * Searches for an union class of the property. If already exists take it an return else create
	 * a new node. If no union found return null.
	 * @param property The property to search in.
	 * @param direction True for domain else range.
	 */
	public OwlUnionOf searchUnion(OWLProperty property, boolean direction) {
		final String searchString;

		if(direction){
			searchString = Constants.AXIOM_OBJ_PROP_DOMAIN;
		}   else {
			searchString = Constants.AXIOM_OBJ_PROP_RANGE;
		}

		for(OWLAxiom currentAxiom : property.getReferencingAxioms(ontology)){
			if(!currentAxiom.getAxiomType().toString().equals(searchString)){
				continue;
			}

			for(OWLClassExpression nestExpr : currentAxiom.getNestedClassExpressions()) {
				if(nestExpr.getClassExpressionType().toString().equals(Constants.AXIOM_OBJ_UNION)){
					OwlUnionOf unionOf = getExistingUnionClass(nestExpr.getClassesInSignature());

					if(unionOf == null){
						unionOf = createUnionClass(nestExpr.getClassesInSignature());
					}

					return unionOf;
				}
			}
		}

		return null;
	}

	private OwlUnionOf createUnionClass(Set<OWLClass> classesInSignature) {
		OwlUnionOf unionOf = new OwlUnionOf();

		for(OWLClass currentClass : classesInSignature){
			BaseNode theNode = findNode(currentClass.getIRI().toString());

			if(theNode != null) {
				unionOf.addUnion(theNode);
			}
		}

		unionNodes.put(unionOf.getId(), unionOf);
		return unionOf;
	}

	private OwlUnionOf getExistingUnionClass(Set<OWLClass> classes) {
		for(Map.Entry<String, OwlUnionOf> entry : unionNodes.entrySet()) {
			OwlUnionOf currentClass = entry.getValue();
			List<String> test = new ArrayList<>();

			for(OWLClass curIri : classes){
				test.add(curIri.getIRI().toString());
			}

			if(currentClass.equalUnionIris(test)){
				return currentClass;
			}
		}

		return null;
	}
}
