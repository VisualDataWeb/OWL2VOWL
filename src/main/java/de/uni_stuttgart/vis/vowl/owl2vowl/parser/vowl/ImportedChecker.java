/*
 * ImportedChecker.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.VowlAttribute;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 *
 */
public class ImportedChecker implements OWLNamedObjectVisitor {
	private final VowlData vowlData;
	private final OWLOntologyManager manager;
	private OWLOntology loadedOntology;

	public ImportedChecker(VowlData vowlData, OWLOntologyManager manager, OWLOntology loadedOntology) {
		this.vowlData = vowlData;
		this.manager = manager;
		this.loadedOntology = loadedOntology;
	}

	public void execute() {
		Set<OWLOntology> imports = manager.getImports(loadedOntology);
		imports.forEach(owlOntology -> {
			owlOntology.getSignature().forEach(element -> {
				element.accept(this);
			});
		});
		/*
		loadedOntology.getSignature().forEach(element -> {
			element.accept(this);
			//new OWLOntologyWalker(imports).walkStructure(this);
		});
		*/
	}

	protected void addImportedAttribute(IRI iri) {
		vowlData.getEntityForIri(iri).addAttribute(VowlAttribute.IMPORTED);
	}

	@Override
	public void visit(@Nonnull OWLClass owlClass) {
		addImportedAttribute(owlClass.getIRI());
	}

	@Override
	public void visit(@Nonnull OWLObjectProperty owlObjectProperty) {
		addImportedAttribute(owlObjectProperty.getIRI());
	}

	@Override
	public void visit(@Nonnull OWLDataProperty owlDataProperty) {
		addImportedAttribute(owlDataProperty.getIRI());
	}

	@Override
	public void visit(@Nonnull OWLNamedIndividual owlNamedIndividual) {
		addImportedAttribute(owlNamedIndividual.getIRI());
	}

	@Override
	public void visit(@Nonnull OWLOntology owlOntology) {
	}

	@Override
	public void visit(@Nonnull OWLDatatype owlDatatype) {
		addImportedAttribute(owlDatatype.getIRI());
	}

	@Override
	public void visit(@Nonnull OWLAnnotationProperty owlAnnotationProperty) {
	}
}
