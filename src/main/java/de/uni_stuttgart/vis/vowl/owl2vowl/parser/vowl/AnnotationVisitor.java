package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.VowlAttribute;
import de.uni_stuttgart.vis.vowl.owl2vowl.constants.Vowl_Lang;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.AbstractEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.annotation.Annotation;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.VowlClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.VowlThing;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.datatypes.VowlDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.datatypes.VowlLiteral;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.TypeOfProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.VowlDatatypeProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.VowlObjectProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.individuals.VowlIndividual;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.visitor.VowlElementVisitor;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Eduard
 */
public class AnnotationVisitor implements VowlElementVisitor {
	private final VowlData vowlData;
	private final OWLOntologyManager manager;

	public AnnotationVisitor(VowlData vowlData, OWLOntologyManager manager) {
		this.vowlData = vowlData;
		this.manager = manager;
	}

	public Set<Annotation> getOntologyAnnotations(OWLOntology ontology) {
		return ontology.annotations().map(this::getVowlAnnotation).collect(Collectors.toSet());
	}

	protected Set<Annotation> getAnnotations(OWLEntity entity) {
		Set<Annotation> allAnnotations = new HashSet<>();

		for (OWLOntology ontology : manager.ontologies().collect(Collectors.toSet())) {
			Set<Annotation> currentAnnotations = mapOwlAnnotationsToVowlAnnotations(EntitySearcher.getAnnotations(entity, ontology));
			allAnnotations.addAll(currentAnnotations);
		}

		return allAnnotations;
	}

	protected Set<Annotation> mapOwlAnnotationsToVowlAnnotations(Stream<OWLAnnotation> annotations) {
		return annotations.map(this::getVowlAnnotation).collect(Collectors.toSet());
	}

	protected Annotation getVowlAnnotation(OWLAnnotation annotation) {
		Annotation anno;

		if (annotation.getValue() instanceof OWLLiteral) {
			OWLLiteral val = (OWLLiteral) annotation.getValue();
			String language;

			if (val.hasLang()) {
				language = val.getLang();
				vowlData.addLanguage(val.getLang());
			} else {
				language = Vowl_Lang.LANG_UNSET;
				vowlData.addLanguage(Vowl_Lang.LANG_UNSET);
			}

			anno = new Annotation(annotation.getProperty().toString(), val.getLiteral());
			anno.setType(Annotation.TYPE_LABEL);
			anno.setLanguage(language);
		} else if (annotation.getValue() instanceof IRI) {
			anno = new Annotation(annotation.getProperty().toString(), annotation.getValue().toString());
			anno.setType(Annotation.TYPE_IRI);
		} else {
			anno = new Annotation(annotation.getProperty().toString(), annotation.getValue().toString());
			anno.setType(Annotation.TYPE_LABEL);
		}

		return anno;
	}

	protected void checkDeprecation(AbstractEntity vowlEntity) {
		if (vowlEntity.getAnnotations().isDeprecated()) {
			vowlEntity.addAttribute(VowlAttribute.DEPRECATED);
		}
	}

	@Override
	public void visit(VowlThing vowlThing) {
		// Generic class has not annotations
	}

	@Override
	public void visit(VowlClass vowlClass) {
		OWLClass owlClass = manager.getOWLDataFactory().getOWLClass(vowlClass.getIri());
		vowlClass.getAnnotations().fillAnnotations(getAnnotations(owlClass));
		checkDeprecation(vowlClass);
	}

	@Override
	public void visit(VowlLiteral vowlLiteral) {
		// Generic class has no annotations
	}

	@Override
	public void visit(VowlDatatype vowlDatatype) {
		OWLDatatype owlDatatype = manager.getOWLDataFactory().getOWLDatatype(vowlDatatype.getIri());
		vowlDatatype.getAnnotations().fillAnnotations(getAnnotations(owlDatatype));
		checkDeprecation(vowlDatatype);
	}

	@Override
	public void visit(VowlObjectProperty vowlObjectProperty) {
		OWLObjectProperty owlCowlObjectPropertyass = manager.getOWLDataFactory().getOWLObjectProperty(vowlObjectProperty.getIri());
		vowlObjectProperty.getAnnotations().fillAnnotations(getAnnotations(owlCowlObjectPropertyass));
		checkDeprecation(vowlObjectProperty);
	}

	@Override
	public void visit(VowlDatatypeProperty vowlDatatypeProperty) {
		OWLDataProperty owlDataProperty = manager.getOWLDataFactory().getOWLDataProperty(vowlDatatypeProperty.getIri());
		vowlDatatypeProperty.getAnnotations().fillAnnotations(getAnnotations(owlDataProperty));
		checkDeprecation(vowlDatatypeProperty);
	}

	@Override
	public void visit(VowlIndividual vowlIndividual) {
		OWLNamedIndividual owlNamedIndividual = manager.getOWLDataFactory().getOWLNamedIndividual(vowlIndividual.getIri());
		vowlIndividual.getAnnotations().fillAnnotations(getAnnotations(owlNamedIndividual));
	}

	@Override
	public void visit(TypeOfProperty typeOfProperty) {
		// Nothing needed
	}
}
