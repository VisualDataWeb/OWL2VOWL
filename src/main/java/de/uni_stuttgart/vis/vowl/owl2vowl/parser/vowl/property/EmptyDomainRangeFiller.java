package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.property;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.VowlThing;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.properties.AbstractProperty;

import java.util.Collection;

/**
 * @author Eduard
 */
public class EmptyDomainRangeFiller {

	private final VowlData vowlData;
	private final Collection<? extends AbstractProperty> values;

	public EmptyDomainRangeFiller(VowlData vowlData, Collection<? extends AbstractProperty> values) {
		this.vowlData = vowlData;
		this.values = values;
	}

	public void execute() {
		for (AbstractProperty value : values) {
			if (value.getDomain() == null && value.getRange() == null) {
				VowlThing disconnectedThing = vowlData.getThingProvider().getDisconnectedThing();
				disconnectedThing.addOutGoingProperty(value.getIri());
				disconnectedThing.addInGoingProperty(value.getIri());
				value.setDomain(disconnectedThing.getIri());
				value.setRange(disconnectedThing.getIri());
			} else if (value.getDomain() == null) {
				VowlThing connectedThing = vowlData.getThingProvider().getConnectedThing(value.getRange());
				if (connectedThing == null) {
					connectedThing = vowlData.getGenerator().generateThing();
				}
				connectedThing.addOutGoingProperty(value.getIri());
				value.setDomain(connectedThing.getIri());
			} else if (value.getRange() == null) {
				VowlThing connectedThing = vowlData.getThingProvider().getConnectedThing(value.getDomain());
				if (connectedThing == null) {
					connectedThing = vowlData.getGenerator().generateThing();
				}
				connectedThing.addInGoingProperty(value.getIri());
				value.setRange(connectedThing.getIri());
			}
		}
	}
}
