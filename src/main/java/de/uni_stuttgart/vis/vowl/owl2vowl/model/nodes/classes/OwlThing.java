package de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.Constants;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties.BaseProperty;

public class OwlThing extends BaseClass {
	public OwlThing() {
		setType(Constants.TYPE_THING);
		setIri(Constants.OWL_THING_CLASS_URI);
		setName("Thing");
	}

	/**
	 * A free Thing is a thing which is not connected to any class.
	 *
	 * @return True if and only if no connection to a class exists.
	 */
	public boolean isFree() {
		for (BaseProperty out : getOutGoingEdges()) {
			String type = out.getRange().getType();
			boolean allowed = type.equals(Constants.TYPE_DATATYPE)
					|| type.equals(Constants.TYPE_LITERAL) || type.equals(Constants.TYPE_THING);

			if (!allowed) {
				return false;
			}
		}

		for (BaseProperty in : getInGoingEdges()) {
			String type = in.getDomain().getType();
			boolean allowed = type.equals(Constants.TYPE_DATATYPE)
					|| type.equals(Constants.TYPE_LITERAL) || type.equals(Constants.TYPE_THING);

			if (!allowed) {
				return false;
			}
		}

		return true;
	}
}
