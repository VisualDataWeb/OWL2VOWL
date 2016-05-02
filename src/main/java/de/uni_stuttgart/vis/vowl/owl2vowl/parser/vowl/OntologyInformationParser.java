package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.OntologyInformationEnum;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.annotation.Annotation;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.ontology.OntologyInformation;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 *
 */
public class OntologyInformationParser {
	private VowlData vowlData;
	private OWLOntology ontology;

	public OntologyInformationParser(VowlData vowlData, OWLOntology ontology) {
		this.vowlData = vowlData;
		this.ontology = ontology;
	}

	public void execute() {
		Set<Annotation> ontologyAnnotations = new AnnotationVisitor(vowlData, null).getOntologyAnnotations(ontology);
		OntologyInformation ontologyInformation = vowlData.getOntologyInformation();
		ontologyInformation.getAnnotations().fillAnnotations(ontologyAnnotations);

		Optional<IRI> ontologyIRI = ontology.getOntologyID().getOntologyIRI();
		if (ontologyIRI.isPresent()) {
			ontologyInformation.setIri(ontologyIRI.get().toString());
		} else {
			ontologyInformation.setIri("No IRI set");
		}

		Map<String, List<Annotation>> identifierMap = ontologyInformation.getAnnotations().getIdentifierToAnnotation();

		if (identifierMap.containsKey(OntologyInformationEnum.VERSION.getValue())) {
			// Take the first available versions
			ontologyInformation.setVersion(identifierMap.get(OntologyInformationEnum.VERSION.getValue()).get(0).getValue());
		}

		if (identifierMap.containsKey(OntologyInformationEnum.TITLE.getValue())) {
			ontologyInformation.setTitles(identifierMap.get(OntologyInformationEnum.TITLE.getValue()));
		}

		if (identifierMap.containsKey(OntologyInformationEnum.AUTHOR.getValue())) {
			identifierMap.get(OntologyInformationEnum.AUTHOR.getValue()).forEach(annotation -> {
				ontologyInformation.addAuthor(annotation.getValue());
			});
		}
	}
}
