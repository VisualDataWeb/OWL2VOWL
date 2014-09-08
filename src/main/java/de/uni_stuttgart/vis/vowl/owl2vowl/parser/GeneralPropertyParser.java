/*
 * GeneralProperty.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.BaseClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.OwlThing;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.BaseDatatype;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 */
public abstract class GeneralPropertyParser extends GeneralParser {
	protected String rdfsDomain = "";
	protected String rdfsRange = "";
	protected String rdfsInversOf = "";

	protected String retrieveRange(OWLObjectProperty currentProperty) {
		for (OWLClassExpression range : currentProperty.getRanges(GeneralParser.ontology)) {
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

		return "";
	}

	protected String retrieveRange(OWLDataProperty currentProperty) {
		String rangeIRI;

		for (OWLDataRange range : currentProperty.getRanges(GeneralParser.ontology)) {
			rangeIRI = range.asOWLDatatype().getIRI().toString();

			if (!rangeIRI.isEmpty()) {
				return rangeIRI;
			}
		}

		return "";
	}

	protected String retrieveDomain(OWLPropertyExpression currentProperty) {
		for (Object domainObject : currentProperty.getDomains(GeneralParser.ontology)) {
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

		return "";
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
		List<String> iriList = new ArrayList<>();

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
		List<String> iriList = new ArrayList<>();

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
		List<String> iriList = new ArrayList<>();

		if (subProperties.isEmpty()) {
			return iriList;
		}


		for (Object curSub : subProperties) {
			OWLNamedObject definedSub = (OWLNamedObject) curSub;
			iriList.add(definedSub.getIRI().toString());
		}

		return iriList;
	}
}
