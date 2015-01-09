/*
 * GeneralProperty.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Standard_Iris;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.BaseClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.OwlIntersectionOf;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.OwlThing;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.OwlUnionOf;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.BaseDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.OntologyInformation;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper.ComparisonHelper;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper.IntersectionParser;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper.UnionParser;
import org.semanticweb.owlapi.model.*;

import java.util.*;

/**
 *
 */
public abstract class GeneralPropertyParser extends GeneralParser {
	protected String rdfsDomain = "";
	protected String rdfsRange = "";
	protected String rdfsInversOf = "";
	private UnionParser unionParser;
	private IntersectionParser intersectionParser;

	public GeneralPropertyParser(OntologyInformation ontologyInformation, MapData mapData) {
		super(ontologyInformation, mapData);
		unionParser = new UnionParser(ontologyInformation, mapData);
		intersectionParser = new IntersectionParser(ontologyInformation, mapData);
		rdfsDomains = new ArrayList<String>();
		rdfsRanges = new ArrayList<String>();
	}

	protected void reset() {

	}

	protected String retrieveRange(OWLObjectProperty currentProperty) {
		for (OWLClassExpression range : currentProperty.getRanges(this.ontology)) {
			if (!range.isAnonymous()) {
				return range.asOWLClass().getIRI().toString();
			} else {
				// TODO
				String classExpressionType = range.getClassExpressionType().toString();
				Set<OWLClass> classesInSignature = range.getClassesInSignature();

				for (OWLClass classInSig : classesInSignature) {
				}
			}
		}

		BaseClass union = unionParser.searchUnion(currentProperty, false);
		BaseClass intersection = intersectionParser.searchIntersection(currentProperty, false);

		if (union != null) {
			return union.getId();
		} else if (intersection != null) {
			return intersection.getId();
		} else {
			return "";
		}
	}

	protected String retrieveRange(OWLDataProperty currentProperty) {
		String rangeIRI;

		for (OWLDataRange range : currentProperty.getRanges(ontology)) {
			rangeIRI = range.asOWLDatatype().getIRI().toString();

			if (!rangeIRI.isEmpty()) {
				return rangeIRI;
			}
		}

		BaseClass union = unionParser.searchUnion(currentProperty, false);
		BaseClass intersection = intersectionParser.searchIntersection(currentProperty, false);

		if (union != null) {
			return union.getId();
		} else if (intersection != null) {
			return intersection.getId();
		} else {
			return "";
		}
	}

	protected String retrieveDomain(OWLPropertyExpression currentProperty) {
		for (Object domainObject : currentProperty.getDomains(ontology)) {
			OWLClassExpression domain = (OWLClassExpression) domainObject;

			if (!domain.isAnonymous()) {
				return domain.asOWLClass().getIRI().toString();
			} else {
				// TODO
				String classExpressionType = domain.getClassExpressionType().toString();
				Set<OWLClass> classesInSignature = domain.getClassesInSignature();

				for (OWLClass classInSig : classesInSignature) {
				}
			}
		}

		BaseClass union = unionParser.searchUnion((OWLEntity) currentProperty, true);
		BaseClass intersection = intersectionParser.searchIntersection((OWLProperty) currentProperty, true);

		if (union != null) {
			return union.getId();
		} else if (intersection != null) {
			return intersection.getId();
		} else {
			return "";
		}
	}

	protected BaseNode findDomain(BaseNode rdfsDomain) {
		BaseClass classDomain = mapData.getClassMap().get(rdfsDomain.getIri());
		BaseDatatype datatypeDomain = mapData.getDatatypeMap().get(rdfsDomain.getIri());
		OwlThing thingDomain = mapData.getThingMap().get(rdfsDomain.getId());

		if (classDomain != null) {
			return classDomain;
		} else if (datatypeDomain != null) {
			return datatypeDomain;
		} else if (thingDomain != null) {
			return thingDomain;
		}

		return null;
	}

	protected BaseNode findRange(BaseNode rdfsRange) {
		BaseClass classRange = mapData.getClassMap().get(rdfsRange.getIri());
		BaseDatatype datatypeRange = mapData.getDatatypeMap().get(rdfsRange.getIri());
		OwlThing thingRange = mapData.getThingMap().get(rdfsRange.getId());

		if (classRange != null) {
			return classRange;
		} else if (datatypeRange != null) {
			return datatypeRange;
		} else if (thingRange != null) {
			return thingRange;
		}

		return null;
	}

	protected List<String> retrieveSubProperties(OWLPropertyExpression property) {
		Set subProperties = property.getSubProperties(ontology);
		List<String> iriList = new ArrayList<String>();

		if (subProperties.isEmpty()) {
			return iriList;
		}


		for (Object curSub : subProperties) {
			OWLNamedObject definedSub = (OWLNamedObject) curSub;
			iriList.add(definedSub.getIRI().toString());
		}

		return iriList;
	}

	protected List<String> retrieveSuperProperties(OWLPropertyExpression property) {
		Set subProperties = property.getSuperProperties(ontology);
		List<String> iriList = new ArrayList<String>();

		if (subProperties.isEmpty()) {
			return iriList;
		}


		for (Object curSub : subProperties) {
			OWLNamedObject definedSub = (OWLNamedObject) curSub;
			iriList.add(definedSub.getIRI().toString());
		}

		return iriList;
	}

	protected List<String> retrieveDisjoints(OWLPropertyExpression property) {
		Set subProperties = property.getDisjointProperties(ontology);
		List<String> iriList = new ArrayList<String>();

		if (subProperties.isEmpty()) {
			return iriList;
		}

		for (Object curSub : subProperties) {
			OWLNamedObject definedSub = (OWLNamedObject) curSub;
			iriList.add(definedSub.getIRI().toString());
		}

		return iriList;
	}

	protected List<String> retrieveEquivalents(OWLPropertyExpression property) {
		Set subProperties = property.getEquivalentProperties(ontology);
		List<String> iriList = new ArrayList<String>();

		// TODO Do not use external properties as base. But if there are equivalent external properties only?
		if (ComparisonHelper.hasDifferentNameSpace((OWLEntity) property, ontology, factory)) {
			return iriList;
		}

		for (Object curSub : subProperties) {
			OWLNamedObject definedSub = (OWLNamedObject) curSub;
			iriList.add(definedSub.getIRI().toString());
		}

		return iriList;
	}

	protected OwlUnionOf getUnionDomain(OWLEntity property) {
		return unionParser.searchUnion(property, true);
	}

	protected OwlUnionOf getUnionRange(OWLEntity property) {
		return unionParser.searchUnion(property, false);
	}
}
