package de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.visitor.VowlElementVisitor;

/**
 * NullClass which has no functionality and should be just used for VisitorEx returns.
 *
 * @author Eduard
 */
public class NullClass extends AbstractClass {
	public NullClass() {
		super(null, null);
	}

	@Override
	public void accept(VowlElementVisitor visitor) {
		// DO NOTHING
	}
}
