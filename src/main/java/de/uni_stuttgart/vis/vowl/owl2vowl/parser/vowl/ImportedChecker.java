/*
 * ImportedChecker.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.VowlAttribute;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.helper.ComparisonHelper;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;

/**
 *
 */
public class ImportedChecker implements OWLNamedObjectVisitor {

//	private static final Logger logger = LogManager.getLogger(ImportedChecker.class);

	private final VowlData vowlData;
	@SuppressWarnings("unused")
	private final OWLOntologyManager manager;
	private OWLOntology loadedOntology;
	private String loadPath;

	public ImportedChecker(VowlData vowlData, OWLOntologyManager manager, OWLOntology loadedOntology, String loadPath) {
		this.vowlData = vowlData;
		this.manager = manager;
		this.loadedOntology = loadedOntology;
		this.loadPath = loadPath;
	}

	public void execute() {
//		Set<OWLOntology> imports = manager.getImports(loadedOntology);
//		imports.forEach(owlOntology -> {
//			owlOntology.getSignature().forEach(element -> {
//				element.accept(this);
//			});
//		});

//		Stream<OWLOntology> imports = manager.imports(loadedOntology);
//		logger.info("THE STREAM \n "+imports);
//		List<String> strings = imports.map(Object::toString)
//				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
//		logger.info("Strings \n "+strings);

		vowlData.getEntityMap().values().forEach(abstractEntity -> {
			IRI entityIri = abstractEntity.getIri();
			if (entityIri.toString().contains("http://owl2vowl.de#")) {
				return;
			}

			if (ComparisonHelper.hasDifferentNameSpace(entityIri.toString(), loadedOntology, loadPath)) {
				addImportedAttribute(entityIri);
			}
		});
	}

	protected void addImportedAttribute(IRI iri) {
		vowlData.getEntityForIri(iri).addAttribute(VowlAttribute.IMPORTED);
	}

	@Override
	public void visit(@Nonnull OWLClass owlClass) {
		if (owlClass.isOWLThing() || owlClass.isOWLNothing()) {
			// Ignore imported things or owl things
			return;
		}
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
		// TODO Do we need to mark individuals as imported?
		//addImportedAttribute(owlNamedIndividual.getIRI());
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
