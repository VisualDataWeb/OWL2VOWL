package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.property;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.VowlClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.VowlThing;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.datatypes.VowlLiteral;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.AbstractProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.TypeOfProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.VowlDatatypeProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.VowlObjectProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.visitor.VowlPropertyVisitor;
import org.semanticweb.owlapi.model.IRI;

import java.util.Collection;
import java.util.Set;

/**
 * @author Eduard
 */
public class DomainRangeFiller implements VowlPropertyVisitor {

	private final VowlData vowlData;
	private final Collection<? extends AbstractProperty> values;

	public DomainRangeFiller(VowlData vowlData, Collection<? extends AbstractProperty> values) {
		this.vowlData = vowlData;
		this.values = values;
	}

	public void execute() {
		fillEmpty();
		mergeMulti();
	}

	protected void fillEmpty() {
		values.forEach(element -> element.accept(this));
	}

	protected VowlThing searchForConnectedThing(Set<IRI> value) {
		if (value.size() != 1) {
			return null;
		}

		return vowlData.getThingProvider().getConnectedThing(value.iterator().next());
	}

	protected void mergeMulti() {
		for (AbstractProperty value : values) {
			// TODO rethink validity of in/out going edges in the nodes! Probably remap
			if (value.getRanges().size() > 1) {
				VowlClass rangeUnion = vowlData.getGenerator().generateUnion(value.getRanges());
				value.setMergedRange(rangeUnion.getIri());
			}

			if (value.getDomains().size() > 1) {
				VowlClass domainUnion = vowlData.getGenerator().generateUnion(value.getDomains());
				value.setMergedDomain(domainUnion.getIri());
			}
		}
	}

	@Override
	public void visit(VowlObjectProperty vowlObjectProperty) {
		classBehaviour(vowlObjectProperty);
	}

	@Override
	public void visit(VowlDatatypeProperty vowlDatatypeProperty) {
		if (vowlDatatypeProperty.getDomains().isEmpty() && vowlDatatypeProperty.getRanges().isEmpty()) {
			VowlThing disconnectedThing = vowlData.getThingProvider().getDisconnectedThing();
			VowlLiteral vowlLiteral = vowlData.getGenerator().generateLiteral();
			disconnectedThing.addOutGoingProperty(vowlDatatypeProperty.getIri());
			vowlLiteral.addInGoingProperty(vowlDatatypeProperty.getIri());
			vowlDatatypeProperty.addDomain(disconnectedThing.getIri());
			vowlDatatypeProperty.addRange(vowlLiteral.getIri());
		} else if (vowlDatatypeProperty.getDomains().isEmpty()) {
			VowlThing connectedThing = searchForConnectedThing(vowlDatatypeProperty.getRanges());
			if (connectedThing == null) {
				connectedThing = vowlData.getThingProvider().getDisconnectedThing();
			}
			connectedThing.addOutGoingProperty(vowlDatatypeProperty.getIri());
			vowlDatatypeProperty.addDomain(connectedThing.getIri());
		} else if (vowlDatatypeProperty.getRanges().isEmpty()) {
			VowlLiteral vowlLiteral = vowlData.getGenerator().generateLiteral();
			vowlLiteral.addInGoingProperty(vowlDatatypeProperty.getIri());
			vowlDatatypeProperty.addRange(vowlLiteral.getIri());
		}
	}

	@Override
	public void visit(TypeOfProperty typeOfProperty) {
		classBehaviour(typeOfProperty);
	}

	protected void classBehaviour(AbstractProperty property) {
		if (property.getDomains().isEmpty() && property.getRanges().isEmpty()) {
			VowlThing disconnectedThing = vowlData.getThingProvider().getDisconnectedThing();
			disconnectedThing.addOutGoingProperty(property.getIri());
			disconnectedThing.addInGoingProperty(property.getIri());
			property.addDomain(disconnectedThing.getIri());
			property.addRange(disconnectedThing.getIri());
		} else if (property.getDomains().isEmpty()) {
			VowlThing connectedThing = searchForConnectedThing(property.getRanges());
			if (connectedThing == null) {
				connectedThing = vowlData.getGenerator().generateThing();
			}
			connectedThing.addOutGoingProperty(property.getIri());
			property.addDomain(connectedThing.getIri());
		} else if (property.getRanges().isEmpty()) {
			VowlThing connectedThing = searchForConnectedThing(property.getDomains());
			if (connectedThing == null) {
				connectedThing = vowlData.getGenerator().generateThing();
			}
			connectedThing.addInGoingProperty(property.getIri());
			property.addRange(connectedThing.getIri());
		}
	}
}
