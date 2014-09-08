package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class OwlEquivalentClass extends BaseClass {
	private List<OwlEquivalentClass> equivalentClasses;

	public OwlEquivalentClass() {
		super();
		equivalentClasses = new ArrayList<>();
		setType(Constants.TYPE_EQUIVALENT);
	}

	public List<OwlEquivalentClass> getEquivalentClasses() {
		return equivalentClasses;
	}

	public void setEquivalentClasses(Collection<OwlEquivalentClass> equivalentClasses) {
		List<OwlEquivalentClass> bufList = new ArrayList<>();

		for (OwlEquivalentClass node : equivalentClasses) {
			bufList.add(node);
		}

		this.equivalentClasses = bufList;
	}

	@Override
	public void printClass() {
		super.printClass();
		System.out.println("   equivalents=" + equivalentClasses + ",");
	}
}
