/*
 * DatatypeParser.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.Constants;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.BaseDatatype;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDatatype;

import java.util.Map;
import java.util.Set;

/**
 *
 */
public class DatatypeParser extends GeneralNodeParser {
	private Set<OWLDatatype> datatypes;

	public DatatypeParser(Set<OWLDatatype> datatypes) {
		this.datatypes = datatypes;
	}

	public void execute() {
		Map<String, BaseDatatype> datatypeMap = mapData.getDatatypeMap();
		Map<String, OWLDatatype> owlDatatypes = mapData.getOwlDatatypes();

		int indexCounter = datatypeMap.size() + 1;

		for (OWLDatatype currentDatatype : datatypes) {
			rdfsLabel = "";
			rdfsComment = "";
			isDeprecated = false;
			rdfsIsDefinedBy = "";
			owlVersionInfo = "";
			iri = currentDatatype.getIRI().toString();
			Set<OWLAnnotation> currentClassAnnotations = currentDatatype.getAnnotations(GeneralParser.ontology);
			TypeFinder finder = new TypeFinder(GeneralParser.ontology, GeneralParser.factory);
			BaseDatatype theDatatype = finder.findVowlDatatype(currentDatatype);


			Main.logger.info("Datatype: " + currentDatatype);
			for(OWLAxiom currentAxiom : currentDatatype.getReferencingAxioms(ontology)){
				Main.logger.info("\tAxiom: " + currentAxiom);

				for(OWLClassExpression nestExpr : currentAxiom.getNestedClassExpressions()) {
					Main.logger.info("\t\tNested: " + nestExpr);
				}
			}

			parseAnnotations(currentClassAnnotations);

			if (rdfsLabel.isEmpty()) {
				rdfsLabel = extractNameFromIRI(iri);
			}

			theDatatype.setName(rdfsLabel);
			theDatatype.setComment(rdfsComment);
			theDatatype.setIri(iri);
			theDatatype.setDefinedBy(rdfsIsDefinedBy);
			theDatatype.setOwlVersion(owlVersionInfo);

			if (isDeprecated) {
				theDatatype.getAttributes().add(Constants.PROP_ATTR_DEPR);
			}

			indexCounter++;

			owlDatatypes.put(currentDatatype.getIRI().toString(), currentDatatype);
			datatypeMap.put(theDatatype.getIri(), theDatatype);
		}
	}
}
