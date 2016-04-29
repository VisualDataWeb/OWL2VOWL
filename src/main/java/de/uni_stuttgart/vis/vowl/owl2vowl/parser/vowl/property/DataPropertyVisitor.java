package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.property;

import de.uni_stuttgart.vis.vowl.owl2vowl.constants.VowlAttribute;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.AbstractNode;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.NullClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.datatypes.DatatypeReference;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.AbstractProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.properties.VowlDatatypeProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.*;

public class DataPropertyVisitor extends PropertyVisitor {
	private Logger logger = LogManager.getLogger(DataPropertyVisitor.class);

	public DataPropertyVisitor(VowlData vowlData, OWLProperty owlObjectProperty) {
		super(vowlData, owlObjectProperty);
	}

	@Override
	public void visit(OWLFunctionalDataPropertyAxiom axiom) {
		VowlDatatypeProperty prop = vowlData.getDatatypePropertyForIri(owlObjectProperty.getIRI());
		prop.addAttribute(VowlAttribute.FUNCTIONAL);
	}

	@Override
	public void visit(OWLDataPropertyRangeAxiom axiom) {
		VowlDatatypeProperty prop = vowlData.getDatatypePropertyForIri(owlObjectProperty.getIRI());

		if (!axiom.getRange().isDatatype()) {
			logger.info("DataPropertyRange is no datatype: " + axiom);
			return;
		}

		AbstractNode node = axiom.getRange().accept(new DomainRangeVisitor(owlObjectProperty, vowlData));

		if ((node instanceof NullClass)) {
			logger.info("Skipped anonymous data range: " + axiom.getRange());
			return;
		}

		DatatypeReference reference = (DatatypeReference) node;

		prop.addRange(reference.getIri());
		reference.addInGoingProperty(prop.getIri());
	}

	@Override
	public void visit(OWLDataPropertyDomainAxiom axiom) {
		VowlDatatypeProperty prop = vowlData.getDatatypePropertyForIri(owlObjectProperty.getIRI());
		IRI domainIri;

		if (axiom.getDomain().isAnonymous()){
			AbstractNode anonymClass = axiom.getDomain().accept(new DomainRangeVisitor(owlObjectProperty, vowlData));
			if (!(anonymClass instanceof NullClass)) {
				domainIri = anonymClass.getIri();
			} else {
				logger.info("Skipped anonymous object domain: " + axiom.getDomain());
				return;
			}
		} else {
			if (axiom.getDomain().asOWLClass().isOWLThing()) {
				return;
			}

			domainIri = axiom.getDomain().asOWLClass().getIRI();
		}

		prop.addDomain(domainIri);
		vowlData.getClassForIri(domainIri).addOutGoingProperty(prop.getIri());
	}

	@Override
	public void visit(OWLSubDataPropertyOfAxiom axiom) {
		logger.info("Sub Data property axiom not supported yet.");
	}

	@Override
	public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
		AbstractProperty base = vowlData.getPropertyForIri(owlObjectProperty.getIRI());

		for (OWLDataPropertyExpression expr : axiom.getProperties()) {
			if (expr.isAnonymous()) {
				// TODO anonymous behaviour
				logger.info("Anonysmous equivalent prop: " + expr);
				continue;
			}

			if (expr.asOWLDataProperty().getIRI().equals(base.getIri())) {
				continue;
			}

			base.addEquivalentElement(expr.asOWLDataProperty().getIRI());
		}
	}
}
