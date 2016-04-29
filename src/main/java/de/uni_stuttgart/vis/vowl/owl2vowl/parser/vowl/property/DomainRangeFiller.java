package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.property;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.HasReference;
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
 * Class which is responsible to fill the Domain/Range of properties regarding the VOWL specification.
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

	private void fillEmpty() {
		values.forEach(element -> {
			if (element instanceof HasReference) {
				//Ignore references cause they do not need generated Domain/Range
				return;
			}

			element.accept(this);
		});
	}

	private VowlThing searchForConnectedThing(Set<IRI> value) {
		if (value.size() != 1) {
			return null;
		}

		return vowlData.getThingProvider().getConnectedThing(value.iterator().next());
	}

	private void mergeMulti() {
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
	public void visit(VowlDatatypeProperty property) {
		if (property.getDomains().isEmpty() && property.getRanges().isEmpty()) {
			if (!property.getReferencedIris().isEmpty()) {
				property.setExportToJson(false);
				return;
			}

			VowlThing disconnectedThing = vowlData.getThingProvider().getDisconnectedThing();
			VowlLiteral vowlLiteral = vowlData.getGenerator().generateLiteral();
			disconnectedThing.addOutGoingProperty(property.getIri());
			vowlLiteral.addInGoingProperty(property.getIri());
			property.addDomain(disconnectedThing.getIri());
			property.addRange(vowlLiteral.getIri());
		} else if (property.getDomains().isEmpty()) {
			VowlThing connectedThing = searchForConnectedThing(property.getRanges());
			if (connectedThing == null) {
				connectedThing = vowlData.getThingProvider().getDisconnectedThing();
			}
			connectedThing.addOutGoingProperty(property.getIri());
			property.addDomain(connectedThing.getIri());
		} else if (property.getRanges().isEmpty()) {
			VowlLiteral vowlLiteral = vowlData.getGenerator().generateLiteral();
			vowlLiteral.addInGoingProperty(property.getIri());
			property.addRange(vowlLiteral.getIri());
		}
	}

	@Override
	public void visit(TypeOfProperty typeOfProperty) {
		classBehaviour(typeOfProperty);
	}

	private void classBehaviour(AbstractProperty property) {
		if (property.getDomains().isEmpty() && property.getRanges().isEmpty()) {
			if (!property.getReferencedIris().isEmpty()) {
				property.setExportToJson(false);
				return;
			}

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
