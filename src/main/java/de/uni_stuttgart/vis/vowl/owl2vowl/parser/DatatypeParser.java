/*
 * DatatypeParser.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.Vowl_Prop_Attr;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.BaseDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.OntologyInformation;
import org.semanticweb.owlapi.model.*;

import java.util.Map;
import java.util.Set;

/**
 *
 */
public class DatatypeParser extends GeneralNodeParser {
	private Set<OWLDatatype> datatypes;

	public DatatypeParser(OntologyInformation ontologyInformation, MapData mapData, Set<OWLDatatype> datatypes) {
		super(ontologyInformation, mapData);
		this.datatypes = datatypes;
	}

	public void reset() {
		super.reset();
	}

	public void execute() {
		Map<String, BaseDatatype> datatypeMap = mapData.getDatatypeMap();
		Map<String, OWLDatatype> owlDatatypes = mapData.getOwlDatatypes();

		for (OWLDatatype currentDatatype : datatypes) {
			iri = currentDatatype.getIRI().toString();
			TypeFinder finder = new TypeFinder(ontologyInformation);
			BaseDatatype theDatatype = finder.findVowlDatatype(currentDatatype);

			parseAnnotations(currentDatatype);

			theDatatype.setLabels(languageToLabel);
			theDatatype.setComments(comments);
			theDatatype.setName(languageToLabel.get("default"));
			theDatatype.setIri(iri);
			theDatatype.setDefinedBy(rdfsIsDefinedBy);
			theDatatype.setOwlVersion(owlVersionInfo);

			if (isDeprecated) {
				theDatatype.getAttributes().add(Vowl_Prop_Attr.PROP_ATTR_DEPR);
			}

			owlDatatypes.put(currentDatatype.getIRI().toString(), currentDatatype);
			datatypeMap.put(theDatatype.getIri(), theDatatype);
			logAxioms(currentDatatype);
		}
	}
}
