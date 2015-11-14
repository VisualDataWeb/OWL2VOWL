/*
 * EquivalentSorter.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.AbstractEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.VowlClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.VowlThing;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.datatypes.VowlDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.datatypes.VowlLiteral;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.TypeOfProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.VowlDatatypeProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.VowlObjectProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.individuals.VowlIndividual;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.visitor.VowlElementVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper.ComparisonHelper;
import org.semanticweb.owlapi.model.IRI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class EquivalentSorter implements VowlElementVisitor {
	private IRI baseIri;
	private VowlData vowlData;

	public EquivalentSorter(IRI baseIri, VowlData vowlData) {
		this.baseIri = baseIri;
		this.vowlData = vowlData;
	}

	protected List<IRI> getSortedEquivalents(AbstractEntity entity) {
		List<IRI> iriToSort = new ArrayList<>(entity.getEquivalentElements());

		if (ComparisonHelper.hasDifferentNameSpace(entity.getIri().toString(), baseIri.toString())) {
			for (IRI iri : iriToSort) {
				if (!ComparisonHelper.hasDifferentNameSpace(iri.toString(), baseIri.toString())) {
					return Collections.emptyList();
				}
			}
		}

		for (IRI iri : iriToSort) {
			if (vowlData.getEntityForIri(iri).getEquivalentElements().size() > iriToSort.size()) {
				return Collections.emptyList();
			}
		}

		Collections.sort(iriToSort, (o1, o2) -> {
			if (o1.equals(baseIri)) {
				return 1;
			}

			return 0;
		});

		return iriToSort;
	}

	@Override
	public void visit(VowlThing vowlThing) {
	}

	@Override
	public void visit(VowlClass vowlClass) {
		if (vowlClass.getEquivalentElements().size() > 0) {
			vowlClass.setSortedEquivalents(getSortedEquivalents(vowlClass));
		}
	}

	@Override
	public void visit(VowlLiteral vowlLiteral) {
	}

	@Override
	public void visit(VowlDatatype vowlDatatype) {
		if (vowlDatatype.getEquivalentElements().size() > 0) {
			vowlDatatype.setSortedEquivalents(getSortedEquivalents(vowlDatatype));
		}
	}

	@Override
	public void visit(VowlObjectProperty vowlObjectProperty) {
		if (vowlObjectProperty.getEquivalentElements().size() > 0) {
			vowlObjectProperty.setSortedEquivalents(getSortedEquivalents(vowlObjectProperty));
		}
	}

	@Override
	public void visit(VowlDatatypeProperty vowlDatatypeProperty) {
		if (vowlDatatypeProperty.getEquivalentElements().size() > 0) {
			vowlDatatypeProperty.setSortedEquivalents(getSortedEquivalents(vowlDatatypeProperty));
		}
	}

	@Override
	public void visit(TypeOfProperty typeOfProperty) {
	}

	@Override
	public void visit(VowlIndividual vowlIndividual) {
	}
}
