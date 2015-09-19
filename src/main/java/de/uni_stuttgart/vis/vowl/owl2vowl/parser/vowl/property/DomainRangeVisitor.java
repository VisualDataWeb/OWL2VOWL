package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl.property;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlGenerationEnum;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.AbstractClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.NullClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.VowlClass;
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
public class DomainRangeVisitor extends OWLObjectVisitorExAdapter<AbstractClass> {
	private final OWLObjectProperty owlObjectProperty;
	private final VowlData vowlData;
	private Logger logger = LogManager.getLogger(DomainRangeVisitor.class);

	public DomainRangeVisitor(OWLObjectProperty owlObjectProperty, VowlData vowlData) {
		super(new NullClass());
		this.owlObjectProperty = owlObjectProperty;
		this.vowlData = vowlData;
	}

	@Nonnull
	@Override
	protected AbstractClass doDefault(@Nonnull OWLObject object) {
		logger.info("Missed object for domain/range: " + object);
		return super.doDefault(object);
	}

	@Nonnull
	@Override
	public AbstractClass visit(@Nonnull OWLObjectIntersectionOf ce) {
		Set<OWLClassExpression> operands = ce.getOperands();
		Set<IRI> iriList = new HashSet<>();

		for (OWLClassExpression operand : operands) {
			if (operand.isAnonymous()) {
				logger.info("Anonymous operand in intersections");
				return new NullClass();
			}
			iriList.add(operand.asOWLClass().getIRI());
		}

		VowlClass intersection = vowlData.getSearcher().getIntersection(iriList);

		if (intersection == null) {
			intersection = vowlData.getGenerator().generateIntersection(iriList);
		}

		return intersection;
	}

	@Nonnull
	@Override
	public AbstractClass visit(OWLObjectUnionOf ce) {
		Set<OWLClassExpression> operands = ce.getOperands();
		Set<IRI> iriList = new HashSet<>();

		for (OWLClassExpression operand : operands) {
			if (operand.isAnonymous()) {
				logger.info("Anonymous operand in intersections");
				return new NullClass();
			}
			iriList.add(operand.asOWLClass().getIRI());
		}

		VowlClass intersection = vowlData.getSearcher().getUnion(iriList);

		if (intersection == null) {
			intersection = vowlData.getGenerator().generateUnion(iriList);
		}

		return intersection;
	}

	@Nonnull
	@Override
	public AbstractClass visit(OWLObjectComplementOf ce) {
		OWLClassExpression baseClass = ce.getOperand();
		if (baseClass.isAnonymous()) {
			logger.info("Complement base is anonym:" + baseClass);
			return new NullClass();
		}

		AbstractClass clazz = vowlData.getClassForIri(baseClass.asOWLClass().getIRI());
		IRI complementIri = clazz.getComplement();
		AbstractClass complementClass;

		if (complementIri == null) {
			complementClass = vowlData.getGenerator().generateComplement(clazz.getIri());
		} else {
			complementClass = vowlData.getClassForIri(complementIri);
		}

		return complementClass;
	}
}
