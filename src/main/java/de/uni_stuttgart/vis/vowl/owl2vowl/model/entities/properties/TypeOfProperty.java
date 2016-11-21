package de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.PropertyType;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.annotation.Annotation;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.visitor.VowlElementVisitor;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.visitor.VowlPropertyVisitor;
import org.semanticweb.owlapi.model.IRI;

/**
 * Special property in VOWL which is colored in purple color.
 */
public class TypeOfProperty extends AbstractProperty {
	public TypeOfProperty(IRI iri) {
		super(iri, PropertyType.TYPEOF);
		getAnnotations().addLabel(new Annotation("label", "is a"));
	}

	@Override
	public void accept(VowlElementVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void accept(VowlPropertyVisitor visitor) {
		visitor.visit(this);
	}
}
