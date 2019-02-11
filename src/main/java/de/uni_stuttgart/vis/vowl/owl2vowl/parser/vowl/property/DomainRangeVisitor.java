package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.property;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.AbstractNode;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.NullClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.VowlClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.entities.nodes.classes.VowlThing;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.owlapi.IndividualsVisitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Eduard
 */
public class DomainRangeVisitor implements OWLObjectVisitorEx<AbstractNode> {
	@SuppressWarnings("unused")
	private final OWLProperty owlObjectProperty;
	private final VowlData vowlData;
	private Logger logger = LogManager.getLogger(DomainRangeVisitor.class);

	public DomainRangeVisitor(OWLProperty owlObjectProperty, VowlData vowlData) {
		this.owlObjectProperty = owlObjectProperty;
		this.vowlData = vowlData;
	}

	@Nonnull
	@Override
	public AbstractNode doDefault(@Nonnull Object object) {
		logger.info("Missed object for domain/range: " + object);
		return (AbstractNode) object;
	}

	@Nonnull
	@Override
	public AbstractNode visit(@Nonnull OWLObjectIntersectionOf ce) {
		Set<OWLClassExpression> operands = ce.operands().collect(Collectors.toSet());
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

		
		// memCleaner;
		iriList.clear();
		iriList=null;
		operands=null;
		return intersection;
	}

	@Nonnull
	@Override
	public AbstractNode visit(OWLObjectUnionOf ce) {
		Set<OWLClassExpression> operands = ce.operands().collect(Collectors.toSet());
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

		// memCleaners
		iriList.clear();
		iriList=null;
		operands=null;
		
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

		ce.individuals().collect(Collectors.toSet()).forEach(owlIndividual -> {
			owlIndividual.accept(new IndividualsVisitor(vowlData, vowlData.getOwlManager(), oneOfClass.getIri()));
		});

		return oneOfClass;
	}

}
