package de.uni_stuttgart.vis.vowl.owl2vowl.model.data;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.PropertyAllSomeValue;
import de.uni_stuttgart.vis.vowl.owl2vowl.constants.PropertyType;
import de.uni_stuttgart.vis.vowl.owl2vowl.constants.VowlAttribute;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.annotation.Annotation;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.AbstractEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.VowlClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.VowlThing;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.datatypes.DatatypeReference;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.datatypes.LiteralReference;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.*;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.datatypes.VowlLiteral;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.individuals.VowlIndividual;
import org.semanticweb.owlapi.model.IRI;

import java.util.Collection;

/**
 * @author Eduard
 */
public class VowlGenerator {
	private final VowlData vowlData;

	public VowlGenerator(VowlData vowlData) {
		this.vowlData = vowlData;
	}

	protected void applyAnonymous(AbstractEntity entity) {
		entity.setGenerated(VowlGenerationEnum.MANUALLY);
		entity.addAttribute(VowlAttribute.ANONYMOUS);
	}

	public VowlClass generateIntersection(Collection<IRI> iriList) {
		VowlClass element = new VowlClass(vowlData.getNewIri());
		applyAnonymous(element);
		iriList.stream().forEach(element::addElementToIntersection);
		element.addAttribute(VowlAttribute.INTERSECTION);
		vowlData.addClass(element);
		return element;
	}

	public VowlClass generateUnion(Collection<IRI> iriList) {
		VowlClass element = new VowlClass(vowlData.getNewIri());
		applyAnonymous(element);
		iriList.stream().forEach(element::addElementToUnion);
		element.addAttribute(VowlAttribute.UNION);
		vowlData.addClass(element);
		return element;
	}

	public VowlClass generateComplement(IRI iri) {
		VowlClass element = new VowlClass(vowlData.getNewIri());
		applyAnonymous(element);
		element.addComplement(iri);
		element.addAttribute(VowlAttribute.COMPLEMENT);
		vowlData.addClass(element);
		return element;
	}

	public VowlClass generateAnonymousClass() {
		VowlClass element = new VowlClass(vowlData.getNewIri());
		applyAnonymous(element);
		vowlData.addClass(element);
		return element;
	}

	public VowlThing generateThing() {
		VowlThing thing = new VowlThing(vowlData.getNewIri());
		thing.setGenerated(VowlGenerationEnum.MANUALLY);
		vowlData.addClass(thing);
		return thing;
	}

	public void generateDisjointProperty(IRI domain, IRI range) {
		if (domain == null || range == null) {
			throw new IllegalArgumentException("Parameters should not be null!");
		}

		VowlObjectProperty property = new VowlObjectProperty(vowlData.getNewIri());
		applyAnonymous(property);
		property.addDomain(domain);
		property.addRange(range);
		property.setType(PropertyType.DISJOINT);
		vowlData.addObjectProperty(property);

		return;
	}

	public AbstractProperty generateSubclassProperty(IRI domain, IRI range) {
		if (domain == null || range == null) {
			throw new IllegalArgumentException("Parameters should not be null!");
		}

		VowlObjectProperty property = new VowlObjectProperty(vowlData.getNewIri());
		applyAnonymous(property);
		property.addDomain(domain);
		property.addRange(range);
		property.setType(PropertyType.SUBCLASS);
		property.getAnnotations().addLabel(new Annotation("label", "SubClassOf"));
		vowlData.addObjectProperty(property);

		return property;
	}

	public VowlIndividual generateIndividual(IRI iri) {
		VowlIndividual individual = new VowlIndividual(iri);
		vowlData.addIndividual(individual);

		return individual;
	}

	public TypeOfProperty generateTypeOf(IRI domain, IRI range) {
		if (domain == null || range == null) {
			throw new IllegalArgumentException("Parameters should not be null!");
		}

		TypeOfProperty property = new TypeOfProperty(vowlData.getNewIri());
		property.setGenerated(VowlGenerationEnum.MANUALLY);
		property.addDomain(domain);
		property.addRange(range);
		vowlData.addTypeOfProperty(property);

		return property;
	}

	public DatatypeReference generateDatatypeReference(IRI reference) {
		if (reference == null) {
			throw new IllegalArgumentException("Parameters should not be null!");
		}

		DatatypeReference reference1 = new DatatypeReference(vowlData.getNewIri(), reference);
		reference1.setGenerated(VowlGenerationEnum.MANUALLY);
		vowlData.addDatatype(reference1);

		return reference1;
	}

	public ObjectValueReference generateObjectValueReference(IRI reference, PropertyAllSomeValue value) {
		if (reference == null) {
			throw new IllegalArgumentException("Parameters should not be null!");
		}

		ObjectValueReference reference1 = new ObjectValueReference(vowlData.getNewIri(), reference);
		reference1.setGenerated(VowlGenerationEnum.MANUALLY);
		reference1.setValue(value);
		vowlData.addObjectProperty(reference1);
		vowlData.getPropertyForIri(reference).addReferencedProperty(reference1.getIri());

		return reference1;
	}

	public DatatypeValueReference generateDatatypeValueReference(IRI reference, PropertyAllSomeValue value) {
		if (reference == null) {
			throw new IllegalArgumentException("Parameters should not be null!");
		}

		DatatypeValueReference reference1 = new DatatypeValueReference(vowlData.getNewIri(), reference);
		reference1.setGenerated(VowlGenerationEnum.MANUALLY);
		reference1.setValue(value);
		vowlData.addDatatypeProperty(reference1);
		vowlData.getPropertyForIri(reference).addReferencedProperty(reference1.getIri());

		return reference1;
	}

	public VowlLiteral generateLiteral() {
		VowlLiteral vowlLiteral = new LiteralReference(vowlData.getNewIri(), vowlData.getGenericLiteral().getIri());
		vowlLiteral.setGenerated(VowlGenerationEnum.MANUALLY);
		vowlData.addDatatype(vowlLiteral);

		return vowlLiteral;
	}
}
