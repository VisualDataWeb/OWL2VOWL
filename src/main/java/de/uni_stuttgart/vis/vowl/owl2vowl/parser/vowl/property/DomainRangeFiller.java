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
import java.util.Collection;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.IRI;

/**
 * Class which is responsible to fill the Domain/Range of properties regarding the VOWL specification.
 *
 * @author Eduard
 */
public class DomainRangeFiller implements VowlPropertyVisitor {

	private static final Logger logger = LogManager.getLogger(DomainRangeFiller.class);

	private final VowlData vowlData;
	private final Collection<? extends AbstractProperty> values;

	public DomainRangeFiller(VowlData vowlData, Collection<? extends AbstractProperty> values) {
		this.vowlData = vowlData;
		this.values = values;
	}

	public void execute() {
		fillEmpty();
		mergeMulti();
		processInverseProperties();
	}

	private void fillEmpty() {
		values.stream()
				// Only process props which have empty domain/range
				.filter(property -> property.getDomains().isEmpty() || property.getRanges().isEmpty())
				// skip inverse properties
				.filter(property -> property.getInverse() == null)
				.forEach(this::processProperty);
	}

	private void processProperty(AbstractProperty property) {
		if (property instanceof HasReference) {
			//Ignore references cause they do not need generated Domain/Range
			return;
		}
		try {
			property.accept(this);
		} catch (Exception e) {
			logger.error("Exception during processing property: " + e + " with message: " + e.getMessage() + " | Skip");
		}
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

	private void processInverseProperties() {
		values.stream()
				.filter(property -> property.getInverse() != null)
				.filter(property -> property.getDomains().isEmpty() || property.getRanges().isEmpty())
				.peek(this::fillWithInverse)
				.filter(property -> property.getDomains().isEmpty() || property.getRanges().isEmpty())
				.forEach(this::processProperty);
	}

	private void fillWithInverse(AbstractProperty property) {
		AbstractProperty inverse = vowlData.getPropertyForIri(property.getInverse());

		if (property.getDomains().isEmpty() && inverse.getJsonRange() != null) {
			property.getDomains().add(inverse.getJsonRange());
			logger.debug("Filled inverse property domain " + property + " with " + inverse.getJsonRange());
		}

		if (property.getRanges().isEmpty() && inverse.getJsonDomain() != null) {
			property.getRanges().add(inverse.getJsonDomain());
			logger.debug("Filled inverse property range " + property + " with " + inverse.getJsonDomain());
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
			disconnectedThing=null;
			
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
			disconnectedThing=null;
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
