package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.Constants;

public class RdfsLiteral extends BaseDatatype {

	public RdfsLiteral() {
		setType(Constants.TYPE_LITERAL);
		setID();
		getLabels().put(Constants.LANG_DEFAULT, "Literal");
	}

	@Override
	protected void setID() {
		id = "literal" + counterObjects;
		counterObjects++;
	}
}
