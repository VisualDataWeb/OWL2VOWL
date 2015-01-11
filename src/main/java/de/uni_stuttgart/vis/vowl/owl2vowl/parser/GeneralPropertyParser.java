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
	protected List<String> rdfsDomains;
	protected List<String> rdfsRanges;
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
		super.reset();
		rdfsDomains = null;
		rdfsRanges = null;
		rdfsInversOf = "";
	}

	protected List<String> retrieveRanges(OWLObjectProperty currentProperty) {
		List<String> ranges = new ArrayList<String>();

		for (OWLClassExpression range : currentProperty.getRanges(this.ontology)) {
			if (!range.isAnonymous()) {
				ranges.add(range.asOWLClass().getIRI().toString());
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
			ranges.add(union.getId());
		} else if (intersection != null) {
			ranges.add(intersection.getId());
		}

		return ranges;
	}

	protected List<String> retrieveRanges(OWLDataProperty currentProperty) {
		List<String> ranges = new ArrayList<String>();

		for (OWLDataRange range : currentProperty.getRanges(ontology)) {
			ranges.add(range.asOWLDatatype().getIRI().toString());
		}

		BaseClass union = unionParser.searchUnion(currentProperty, false);
		BaseClass intersection = intersectionParser.searchIntersection(currentProperty, false);

		if (union != null) {
			ranges.add(union.getId());
		} else if (intersection != null) {
			ranges.add(intersection.getId());
		}

		return ranges;
	}

	protected List<String> retrieveDomains(OWLPropertyExpression currentProperty) {
		List<String> domains = new ArrayList<String>();

		for (Object domainObject : currentProperty.getDomains(ontology)) {
			OWLClassExpression domain = (OWLClassExpression) domainObject;

			if (!domain.isAnonymous()) {
				domains.add(domain.asOWLClass().getIRI().toString());
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
			domains.add(union.getId());
		} else if (intersection != null) {
			domains.add(intersection.getId());
		}

		return domains;
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
		if (ComparisonHelper.hasDifferentNameSpace((OWLEntity) property, ontologyInformation)) {
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

	protected BaseNode mergeTargets(Collection<? extends BaseNode> domains) {
		if (domains.size() == 1) {
			return domains.iterator().next();
		}

		OwlIntersectionOf intersectionOf = new OwlIntersectionOf();
		mapData.getIntersectionMap().put(intersectionOf.getId(), intersectionOf);
		intersectionOf.addIntersections(domains);

		return intersectionOf;
	}

	protected boolean hasThingConstruct(List<String> rdfsDomains, List<String> rdfsRanges) {
		return hasThingConstruct(rdfsDomains) || hasThingConstruct(rdfsRanges);
	}

	protected boolean hasThingConstruct(List<String> iriList) {
		int size = iriList.size();

		if (size == 0 || iriList.contains(Standard_Iris.OWL_THING_CLASS_URI)) {
			return true;
		}

		return false;
	}

	protected Collection<? extends BaseNode> getNodesWithThings(List<String> iriList, Collection<? extends BaseNode> targetNodes) {
		List<BaseNode> nodes = findNodes(iriList);
		boolean oneNodeThing = iriList.size() == 0 || iriList.size() == 1 && iriList.contains(Standard_Iris.OWL_THING_CLASS_URI);

		if (oneNodeThing) {
			boolean createNew = false;
			BaseNode thing = targetNodes.iterator().next().getConnectedThing();

			for (BaseNode targetNode : targetNodes) {
				if (!targetNode.containsConnectedThing(thing)) {
					createNew = true;
					break;
				}
			}

			if (createNew) {
				OwlThing created = new OwlThing();
				mapData.getThingMap().put(created.getId(), created);
				thing = created;
			}

			return Arrays.asList(thing);
		}

		if (nodes.size() == iriList.size()) {
			return nodes;
		}

		OwlThing created = new OwlThing();
		mapData.getThingMap().put(created.getId(), created);
		nodes.add(created);

		return nodes;

	}

	protected Collection<? extends BaseNode> getDatatypeDomainNodes(List<String> iriList, Collection<? extends BaseNode> targetNodes) {
		List<BaseNode> nodes = findNodes(iriList);
		boolean oneNodeThing = iriList.size() == 0 || iriList.size() == 1 && iriList.contains(Standard_Iris.OWL_THING_CLASS_URI);

		if (oneNodeThing) {
			BaseNode thing = getDisconnectedThing();

			return Arrays.asList(thing);
		}

		if (nodes.size() == iriList.size()) {
			return nodes;
		}

		OwlThing created = new OwlThing();
		mapData.getThingMap().put(created.getId(), created);
		nodes.add(created);

		return nodes;

	}

	/**
	 * Checks wether the domains and ranges contains any classes, datatypes or literals.
	 * @return True if is completely free. Else false.
	 * @param rdfsDomains
	 * @param rdfsRanges
	 */
	protected boolean isClassLess(List<String> rdfsDomains, List<String> rdfsRanges) {
		int sizeDom = rdfsDomains.size();
		int sizeRan = rdfsRanges.size();
		boolean domainConstruct = sizeDom == 0 || rdfsDomains.contains(Standard_Iris.OWL_THING_CLASS_URI);
		boolean rangeConstruct = sizeRan == 0 || rdfsRanges.contains(Standard_Iris.OWL_THING_CLASS_URI);


		if (sizeDom > 1 || sizeRan > 1) {
			return false;
		}

		if (domainConstruct && rangeConstruct) {
		   return true;
		}

		return false;
	}
}
