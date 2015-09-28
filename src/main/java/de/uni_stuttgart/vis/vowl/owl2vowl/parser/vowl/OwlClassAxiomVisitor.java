package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.AbstractClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;

import java.util.Set;

public class OwlClassAxiomVisitor extends OWLObjectVisitorAdapter {

	private VowlData vowlData;
	private OWLClass owlClass;
	private Logger logger = LogManager.getLogger(OwlClassAxiomVisitor.class);

	public OwlClassAxiomVisitor(VowlData vowlData, OWLClass owlClass) {
		this.vowlData = vowlData;
		this.owlClass = owlClass;
	}

	@Override
	protected void handleDefault(OWLObject axiom) {
		System.out.println(owlClass);
		System.out.println("\t" + axiom);
	}

	@Override
	public void visit(OWLEquivalentClassesAxiom axiom) {
		// TODO NamedClasses size != 1 means either
		if (axiom.getNamedClasses().size() != 1) {
			createEquivalentClass(axiom);
		}

		OWLClass referencedClass = axiom.getNamedClasses().iterator().next();

		Set<OWLClassExpression> expressionsWithoutRefClass = axiom.getClassExpressionsMinus(referencedClass);
		for (OWLClassExpression anonymExpressions : expressionsWithoutRefClass) {
			anonymExpressions.accept(new VowlClassVisitor(vowlData, referencedClass));
		}
	}

	protected void createEquivalentClass(OWLEquivalentClassesAxiom axiom) {

	}

	@Override
	public void visit(OWLSubClassOfAxiom axiom) {
		if (axiom.isGCI()) {
			// TODO anonym subclass behaviour
			logger.info("Anonym subclass: " + axiom);
			return;
		}

		OWLClass subClass = axiom.getSubClass().asOWLClass();
		AbstractClass vowlSubclass = vowlData.getClassForIri(subClass.getIRI());

		if (axiom.getSuperClass().isAnonymous()) {
			axiom.getSuperClass().accept(new OwlSubclassAnonymVisitor(vowlData, owlClass));
		} else {
			OWLClass superClass = axiom.getSuperClass().asOWLClass();
			AbstractClass vowlSuperClass = vowlData.getClassForIri(superClass.getIRI());
			vowlSubclass.addSuperEntity(vowlSuperClass.getIri());
			vowlSuperClass.addSubEntity(vowlSubclass.getIri());
		}
	}
}
