package de.uni_stuttgart.vis.vowl.owl2vowl.model.data;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.NodeType;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.AbstractNode;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.VowlThing;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.AbstractProperty;
import org.semanticweb.owlapi.model.IRI;

/**
 * @author Eduard
 */
public class VowlThingProvider {
	private final VowlData vowlData;
	private final VowlSearcher searcher;
	private final VowlGenerator generator;

	public VowlThingProvider(VowlData vowlData, VowlSearcher searcher, VowlGenerator generator) {
		this.vowlData = vowlData;
		this.searcher = searcher;
		this.generator = generator;
	}

	/**
	 * Returns the owl:Thing which is already connected to the node.
	 *
	 * @return Owl:Thing which has connection to this node. Else null.
	 */
	public VowlThing getConnectedThing(IRI nodeIri) {
		AbstractNode node = vowlData.getNodeForIri(nodeIri);
		for (IRI out : node.getOutGoingProperties()) {
			AbstractProperty prop = vowlData.getPropertyForIri(out);

			if (prop.getRanges().size() != 1) {
				continue;
			}

			AbstractNode rangeNode = vowlData.getNodeForIri(prop.getRanges().iterator().next());

			if (rangeNode.getType().equals(NodeType.TYPE_THING)) {
				return (VowlThing) rangeNode;
			}
		}

		for (IRI out : node.getInGoingProperties()) {
			AbstractProperty prop = vowlData.getPropertyForIri(out);

			if (prop.getDomains().size() != 1) {
				continue;
			}

			AbstractNode domainNode = vowlData.getNodeForIri(prop.getDomains().iterator().next());

			if (domainNode.getType().equals(NodeType.TYPE_THING)) {
				return (VowlThing) domainNode;
			}
		}

		return null;
	}

	/**
	 * Check if the nodes contain an already connected thing.
	 *
	 * @return True if there exists at least one connected thing otherwise false.
	 */
	public boolean containsConnectedThing(IRI nodeIri1, IRI nodeIri2) {
		AbstractNode node1 = vowlData.getNodeForIri(nodeIri1);
		AbstractNode node2 = vowlData.getNodeForIri(nodeIri2);
		for (IRI out : node1.getOutGoingProperties()) {
			AbstractProperty prop = vowlData.getPropertyForIri(out);

			if (prop.getRanges().size() != 1) {
				continue;
			}

			AbstractNode rangeNode = vowlData.getNodeForIri(prop.getRanges().iterator().next());

			if (rangeNode.getType().equals(NodeType.TYPE_THING) && rangeNode == node2) {
				return true;
			}
		}

		for (IRI out : node1.getInGoingProperties()) {
			AbstractProperty prop = vowlData.getPropertyForIri(out);

			if (prop.getDomains().size() != 1) {
				continue;
			}

			AbstractNode domainNode = vowlData.getNodeForIri(prop.getDomains().iterator().next());

			if (domainNode.getType().equals(NodeType.TYPE_THING) && domainNode == node2) {
				return true;
			}
		}

		return false;
	}

	/**
	 * A free Thing is a thing which is not connected to any class.
	 *
	 * @return True if and only if no connection to a class exists.
	 */
	public boolean isFree(VowlThing thing) {
		for (IRI out : thing.getOutGoingProperties()) {
			AbstractProperty property = vowlData.getPropertyForIri(out);

			if (property.getRanges().size() != 1) {
				continue;
			}

			AbstractNode rangeNode = vowlData.getNodeForIri(property.getRanges().iterator().next());

			boolean allowed = rangeNode.getType().equals(NodeType.TYPE_DATATYPE)
					|| rangeNode.getType().equals(NodeType.TYPE_LITERAL)
					|| rangeNode.getType().equals(NodeType.TYPE_THING);

			if (!allowed) {
				return false;
			}
		}

		for (IRI out : thing.getInGoingProperties()) {
			AbstractProperty property = vowlData.getPropertyForIri(out);

			if (property.getDomains().size() != 1) {
				continue;
			}

			AbstractNode domainNode = vowlData.getNodeForIri(property.getDomains().iterator().next());

			boolean allowed = domainNode.getType().equals(NodeType.TYPE_DATATYPE)
					|| domainNode.getType().equals(NodeType.TYPE_LITERAL)
					|| domainNode.getType().equals(NodeType.TYPE_THING);

			if (!allowed) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Searches for a thing which is not connected to any class. Only possible connections are:
	 * Things, Literal and Datatypes
	 * TODO vielleicht sofort neues erzeugen, wenn keins vorhanden?
	 *
	 * @return The id of a not connected thing.
	 */
	public VowlThing getDisconnectedThing() {
		for (VowlThing thing : searcher.getThings()) {
			if (isFree(thing)) {
				return thing;
			}
		}

		return generator.generateThing();
	}
}
