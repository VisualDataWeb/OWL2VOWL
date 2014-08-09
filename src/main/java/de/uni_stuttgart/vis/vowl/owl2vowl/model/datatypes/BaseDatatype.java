package de.uni_stuttgart.vis.vowl.owl2vowl.model.datatypes;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.BaseEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.Constants;

public abstract class BaseDatatype extends BaseEntity {

	protected BaseDatatype() {
		setType(Constants.TYPE_DATATYPE);
	}
}
