/*
 * ElementFinder.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.BaseEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.individuals.NamedIndividual;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.BaseClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.ObjectOneOf;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.OwlClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.*;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class ElementFinder {
	private MapData mapData;


	public ElementFinder(MapData mapData) {
		this.mapData = mapData;
	}

	public BaseNode findOrCreateClass(String iri, Class<? extends BaseNode> clazz) throws IllegalAccessException, InstantiationException {
		BaseNode foundClass = findClass(iri);

		if (foundClass == null) {
			foundClass = clazz.newInstance();
			foundClass.setIri(iri);

			mapData.getClassMap().put(iri, (BaseClass) foundClass);
		}

		return foundClass;
	}

	public BaseNode findClass(String iri) {
		return mapData.getMergedMap().get(iri);
	}

	public BaseEntity findObjectOneOf(List<NamedIndividual> oneOfList) {
		for (ObjectOneOf objectOneOf : mapData.getObjectOneOfMap().values()) {
			if (objectOneOf.matchesIndividualList(oneOfList)) {
				return objectOneOf;
			}
		}

		return null;
	}

	public BaseEntity findObjectOneOfWithIriList(List<String> oneOfList) {
		for (ObjectOneOf objectOneOf : mapData.getObjectOneOfMap().values()) {
			if (objectOneOf.matchesIriList(oneOfList)) {
				return objectOneOf;
			}
		}

		return null;
	}

	public BaseDatatype findOrCreateDatatype() {
		// TODO finding

		return new RdfsDatatype();
	}

	public DataIntersectionOf findOrCreateDataIntersection(List<BaseDatatype> intersectionTypes) {
		// TODO finding

		return new DataIntersectionOf();
	}

	public DataComplementOf findOrCreateDataComplementOf(String iri, String complementIri) {
		BaseNode node = mapData.getMergedMap().get(iri);

		if (node == null) {
			return new DataComplementOf();
		}

		if(! (node instanceof DataComplementOf)){
			throw new IllegalStateException("Wrong instance for: " + node.getIri());
		}

		return (DataComplementOf) node;
	}

	public DataUnionOf findOrCreateDataUnionOf(String iri) {
		BaseNode node = mapData.getMergedMap().get(iri);

		if (node == null) {
			return new DataUnionOf();
		}

		if(! (node instanceof DataUnionOf)){
			throw new IllegalStateException("Wrong instance for: " + node.getIri());
		}

		return (DataUnionOf) node;

	}

	public void addClass(String iri, BaseEntity owlClass1) {
		Map<String, Set<BaseEntity>> iriToEntity = mapData.getIriToEntity();

		if (!iriToEntity.containsKey(iri)) {
			iriToEntity.put(iri, new HashSet<BaseEntity>());
		}

		iriToEntity.get(iri).add(owlClass1);
	}
}
