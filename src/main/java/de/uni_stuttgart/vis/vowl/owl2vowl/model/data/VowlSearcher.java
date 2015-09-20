package de.uni_stuttgart.vis.vowl.owl2vowl.model.data;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.AbstractEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.VowlClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.VowlThing;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.VowlDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.VowlLiteral;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.properties.VowlDatatypeProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.properties.VowlObjectProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.VowlElementVisitor;
import org.semanticweb.owlapi.model.IRI;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Eduard
 */
public class VowlSearcher implements VowlElementVisitor {

	private final VowlData data;
	private int dataSize = 0;
	private Set<VowlClass> intersections = new HashSet<>();
	private Set<VowlClass> unions = new HashSet<>();
	private Set<VowlThing> things = new HashSet<>();

	public VowlSearcher(VowlData data) {
		this.data = data;
		dataSize = data.getEntityMap().keySet().size();
	}

	public Set<VowlThing> getThings() {
		checkConsistenty();
		return Collections.unmodifiableSet(things);
	}

	protected void refresh() {
		intersections.clear();
		unions.clear();
		things.clear();

		for (AbstractEntity abstractEntity : data.getEntityMap().values()) {
			abstractEntity.accept(this);
		}
	}

	protected void checkConsistenty() {
		int currentSize = data.getEntityMap().keySet().size();
		if (currentSize != dataSize) {
			refresh();
		}
	}

	@Override
	public void visit(VowlThing vowlThing) {
		things.add(vowlThing);
	}

	@Override
	public void visit(VowlClass vowlClass) {
		if (vowlClass.getElementOfIntersection().size() != 0) {
			intersections.add(vowlClass);
		}

		if (vowlClass.getElementsOfUnion().size() != 0) {
			unions.add(vowlClass);
		}
	}

	@Override
	public void visit(VowlLiteral vowlLiteral) {

	}

	@Override
	public void visit(VowlDatatype vowlDatatype) {

	}

	@Override
	public void visit(VowlObjectProperty vowlObjectProperty) {

	}

	@Override
	public void visit(VowlDatatypeProperty vowlDatatypeProperty) {

	}

	public VowlClass getIntersection(Collection<IRI> intersectionIris) {
		checkConsistenty();
		for (VowlClass intersection : intersections) {
			if (intersection.getElementOfIntersection().containsAll(intersectionIris)) {
				return intersection;
			}
		}

		return null;
	}

	public VowlClass getUnion(Set<IRI> iriList) {
		checkConsistenty();

		for (VowlClass union : unions) {
			if (union.getElementOfIntersection().containsAll(iriList)) {
				return union;
			}
		}

		return null;
	}
}
