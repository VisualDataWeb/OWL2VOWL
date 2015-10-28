package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.property;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.VowlClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.VowlThing;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.properties.AbstractProperty;
import org.semanticweb.owlapi.model.IRI;

import java.util.Collection;
import java.util.Set;

/**
 * @author Eduard
 */
public class DomainRangeFiller {

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
		for (AbstractProperty value : values) {
			if (value.getDomains().isEmpty() && value.getRanges().isEmpty()) {
				VowlThing disconnectedThing = vowlData.getThingProvider().getDisconnectedThing();
				disconnectedThing.addOutGoingProperty(value.getIri());
				disconnectedThing.addInGoingProperty(value.getIri());
				value.addDomain(disconnectedThing.getIri());
				value.addRange(disconnectedThing.getIri());
			} else if (value.getDomains().isEmpty()) {
				VowlThing connectedThing = searchForConnectedThing(value.getRanges());
				if (connectedThing == null) {
					connectedThing = vowlData.getGenerator().generateThing();
				}
				connectedThing.addOutGoingProperty(value.getIri());
				value.addDomain(connectedThing.getIri());
			} else if (value.getRanges().isEmpty()) {
				VowlThing connectedThing = searchForConnectedThing(value.getDomains());
				if (connectedThing == null) {
					connectedThing = vowlData.getGenerator().generateThing();
				}
				connectedThing.addInGoingProperty(value.getIri());
				value.addRange(connectedThing.getIri());
			}
		}
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
				value.setMergedRange(domainUnion.getIri());
			}
		}
	}
}
