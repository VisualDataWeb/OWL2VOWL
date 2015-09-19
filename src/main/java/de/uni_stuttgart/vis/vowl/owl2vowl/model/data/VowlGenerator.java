package de.uni_stuttgart.vis.vowl.owl2vowl.model.data;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.VowlAttribute;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.VowlClass;
import org.semanticweb.owlapi.model.IRI;

import java.util.Collection;
import java.util.List;

/**
 * @author Eduard
 */
public class VowlGenerator {
	private final VowlData vowlData;

	public VowlGenerator(VowlData vowlData) {
		this.vowlData = vowlData;
	}

	public VowlClass generateIntersection(Collection<IRI> iriList) {
		VowlClass element = new VowlClass(vowlData.getNewIri());
		element.setGenerated(VowlGenerationEnum.MANUALLY);
		iriList.stream().forEach(element::addElementToIntersection);
		element.addAttribute(VowlAttribute.INTERSECTION);
		vowlData.addClass(element);
		return element;
	}

	public VowlClass generateUnion(Collection<IRI> iriList) {
		VowlClass element = new VowlClass(vowlData.getNewIri());
		element.setGenerated(VowlGenerationEnum.MANUALLY);
		iriList.stream().forEach(element::addElementToUnion);
		element.addAttribute(VowlAttribute.UNION);
		vowlData.addClass(element);
		return element;
	}

	public VowlClass generateComplement(IRI iri) {
		VowlClass element = new VowlClass(vowlData.getNewIri());
		element.setGenerated(VowlGenerationEnum.MANUALLY);
		element.setComplement(iri);
		element.addAttribute(VowlAttribute.COMPLEMENT);
		vowlData.addClass(element);
		return element;
	}
}
