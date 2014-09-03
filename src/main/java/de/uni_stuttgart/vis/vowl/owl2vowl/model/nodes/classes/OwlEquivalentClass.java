package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.Constants;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;

import java.util.ArrayList;
import java.util.List;


public class OwlEquivalentClass extends BaseClass {
	private List<BaseNode> equivalentClasses;

	public OwlEquivalentClass() {
		super();
		equivalentClasses = new ArrayList<>();
		setType(Constants.TYPE_EQUIVALENT);
	}

	public List<BaseNode> getEquivalentClasses() {
		return equivalentClasses;
	}

	public void setEquivalentClasses(List<BaseNode> equivalentClasses) {
		this.equivalentClasses = equivalentClasses;
	}

	@Override
	public void printClass() {
		super.printClass();
		System.out.println("   equivalents=" + equivalentClasses + ",");
	}
}
