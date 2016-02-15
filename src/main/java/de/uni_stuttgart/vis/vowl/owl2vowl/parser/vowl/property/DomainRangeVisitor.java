package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.property;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.AbstractNode;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.AbstractClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.NullClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.VowlClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.VowlThing;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.datatypes.AbstractDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.datatypes.DatatypeReference;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.datatypes.VowlDatatype;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLObjectVisitorExAdapter;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Eduard
 */
public class DomainRangeVisitor extends OWLObjectVisitorExAdapter<AbstractNode> {
	private final OWLProperty owlObjectProperty;
	private final VowlData vowlData;
	private Logger logger = LogManager.getLogger(DomainRangeVisitor.class);

	public DomainRangeVisitor(OWLProperty owlObjectProperty, VowlData vowlData) {
		super(new NullClass());
		this.owlObjectProperty = owlObjectProperty;
		this.vowlData = vowlData;
	}

	@Nonnull
	@Override
	protected AbstractNode doDefault(@Nonnull OWLObject object) {
		logger.info("Missed object for domain/range: " + object);
		return super.doDefault(object);
	}

	@Nonnull
	@Override
	public AbstractNode visit(@Nonnull OWLObjectIntersectionOf ce) {
		Set<OWLClassExpression> operands = ce.getOperands();
		Set<IRI> iriList = new HashSet<>();

		for (OWLClassExpression operand : operands) {
			if (operand.isAnonymous()) {
				logger.info("Anonymous operand in intersections");
				return new NullClass();
			}
			iriList.add(operand.asOWLClass().getIRI());
		}

		if (iriList.contains(VowlThing.GENERIC_THING_IRI)) {
			iriList.remove(VowlThing.GENERIC_THING_IRI);
		}

		VowlClass intersection = vowlData.getSearcher().getIntersection(iriList);

		if (intersection == null) {
			intersection = vowlData.getGenerator().generateIntersection(iriList);
		}

		return intersection;
	}

	@Nonnull
	@Override
	public AbstractNode visit(OWLObjectUnionOf ce) {
		Set<OWLClassExpression> operands = ce.getOperands();
		Set<IRI> iriList = new HashSet<>();

		for (OWLClassExpression operand : operands) {
			if (operand.isAnonymous()) {
				logger.info("Anonymous operand in intersections");
				return new NullClass();
			}

			iriList.add(operand.asOWLClass().getIRI());
		}

		if (iriList.contains(VowlThing.GENERIC_THING_IRI)) {
			iriList.remove(VowlThing.GENERIC_THING_IRI);
		}

		VowlClass intersection = vowlData.getSearcher().getUnion(iriList);

		if (intersection == null) {
			intersection = vowlData.getGenerator().generateUnion(iriList);
		}

		return intersection;
	}

	@Nonnull
	@Override
	public AbstractNode visit(OWLObjectComplementOf ce) {
		OWLClassExpression baseClass = ce.getOperand();
		if (baseClass.isAnonymous()) {
			logger.info("Complement base is anonym:" + baseClass);
			return new NullClass();
		}

		return vowlData.getGenerator().generateComplement(baseClass.asOWLClass().getIRI());
	}

	@Nonnull
	@Override
	public AbstractNode visit(@Nonnull OWLDatatype node) {
		return vowlData.getGenerator().generateDatatypeReference(node.getIRI());
	}

	@Nonnull
	@Override
	public AbstractNode visit(OWLObjectOneOf ce) {
		VowlClass oneOfClass = vowlData.getGenerator().generateAnonymousClass();
		ce.getIndividuals().forEach(individual -> individual.accept(new OWLIndividualVisitor() {
			@Override
			public void visit(@Nonnull OWLNamedIndividual owlNamedIndividual) {
				oneOfClass.addIndividual(owlNamedIndividual.getIRI());
			}

			@Override
			public void visit(@Nonnull OWLAnonymousIndividual owlAnonymousIndividual) {
				// TODO anonymous behaviour of individuals
			}
		}));

		return oneOfClass;
	}
}
