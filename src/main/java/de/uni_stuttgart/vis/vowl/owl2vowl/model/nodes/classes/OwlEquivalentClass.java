package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class OwlEquivalentClass extends BaseClass {
	private List<BaseClass> equivalentClasses;

	public OwlEquivalentClass() {
		super();
		equivalentClasses = new ArrayList<>();
		setType(Constants.TYPE_EQUIVALENT);
	}

	public List<BaseClass> getEquivalentClasses() {
		return equivalentClasses;
	}

	public void setEquivalentClasses(Collection<BaseClass> equivalentClasses) {
		List<BaseClass> bufList = new ArrayList<>();

		for (BaseClass node : equivalentClasses) {
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
