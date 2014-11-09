/*
 * UnionParser.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.Constants;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.OwlUnionOf;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class UnionParser extends AxiomParser {
	Map<String, OwlUnionOf> unionNodes = mapData.getUnionMap();

	/**
	 * Searches for an union class of the property. If already exists take it an return else create
	 * a new node. If no union found return null.
	 * @param property The property to search in.
	 * @param direction True for domain else range.
	 */
	public OwlUnionOf searchUnion(OWLEntity property, boolean direction) {
		Set<OWLClass> classes = search(property, Constants.AXIOM_OBJ_UNION, direction);

		if (classes == null) {
			return null;
		}

		OwlUnionOf unionOf = getExistingUnionClass(classes);

		if (unionOf == null) {
			unionOf = createUnionClass(classes);
		}

		return unionOf;
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
			List<String> test = new ArrayList<String>();

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
