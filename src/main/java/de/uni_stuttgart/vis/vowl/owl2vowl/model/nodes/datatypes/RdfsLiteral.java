package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Node_Types;
import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Vowl_Lang;

public class RdfsLiteral extends BaseDatatype {

	public RdfsLiteral() {
		setType(Node_Types.TYPE_LITERAL);
		setID();
		getLabels().put(Vowl_Lang.LANG_DEFAULT, "Literal");
	}

	@Override
	protected void setID() {
		id = "literal" + counterObjects;
		counterObjects++;
	}
}
