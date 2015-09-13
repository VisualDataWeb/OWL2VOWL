package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Node_Types;
import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Vowl_Lang;
import de.uni_stuttgart.vis.vowl.owl2vowl.export.JsonGeneratorVisitor;

public class RdfsDatatype extends BaseDatatype {
	public RdfsDatatype() {
		setType(Node_Types.TYPE_DATATYPE);
	}

	@Override
	protected void setID() {
		id = "datatype" + counterObjects;
		counterObjects++;
	}

	@Override
	public void setName(String name) {
		getLabels().put(Vowl_Lang.LANG_DEFAULT, name);
		super.setName(name);
	}

	@Override
	public void accept(JsonGeneratorVisitor visitor) {
		super.accept(visitor);
		visitor.visit(this);
	}
}
