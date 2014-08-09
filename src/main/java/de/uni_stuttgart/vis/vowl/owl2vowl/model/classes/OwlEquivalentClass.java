package de.uni_stuttgart.vis.vowl.owl2vowl.model.classes;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.BaseEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.Constants;

import java.util.ArrayList;
import java.util.List;


public class OwlEquivalentClass extends BaseClass {
	private List<BaseEntity> equivalentClasses;

	public OwlEquivalentClass() {
		equivalentClasses = new ArrayList<>();
		setType(Constants.TYPE_EQUIVALENT);
	}

	public List<BaseEntity> getEquivalentClasses() {
		return equivalentClasses;
	}

	public void setEquivalentClasses(List<BaseEntity> equivalentClasses) {
		this.equivalentClasses = equivalentClasses;
	}

	@Override public void printClass() {
		super.printClass();
		System.out.println("   equivalents=" + equivalentClasses + ",");
	}
}
