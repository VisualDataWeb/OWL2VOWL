/*
 * IndividualVisitorImpl.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.visitors;

import de.uni_stuttgart.vis.vowl.owl2vowl.parser.container.MapData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.*;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class IndividualVisitorImpl implements OWLIndividualVisitor {

	private static final Logger logger = LogManager.getRootLogger();
	private int setSize;
	private MapData mapData;
	private OWLOntology ontology;
	private Set<OWLClass> instances;

	public IndividualVisitorImpl(int size, MapData mapData, OWLOntology ontology) {
		setSize = size;
		this.mapData = mapData;
		this.ontology = ontology;
		instances = new HashSet<OWLClass>();
	}

	public Set<OWLClass> getInstances() {
		return instances;
	}

	@Override
	public void visit(OWLNamedIndividual owlNamedIndividual) {
		OWLClass owlClass = mapData.getOwlClasses().get(owlNamedIndividual.getIRI().toString());

		if (owlClass != null) {
			addInstance(owlClass);
		}
	}

	private void addInstance(OWLClass instance) {
		if (instance == null) {
			return;
		}

		setSize--;
		instances.add(instance);
	}

	@Override
	public void visit(OWLAnonymousIndividual owlAnonymousIndividual) {
		logger.info("Anonym Individual: " + owlAnonymousIndividual);
		// TODO implement what to do if exists.
	}

	public int getSetSize() {
		return setSize;
	}
}
