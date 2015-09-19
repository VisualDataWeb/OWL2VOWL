package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.AbstractEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * @author Eduard
 */
public class AnnotationParser {
	private final VowlData vowlData;
	private final OWLOntologyManager manager;

	public AnnotationParser(VowlData vowlData, OWLOntologyManager manager) {
		this.vowlData = vowlData;
		this.manager = manager;
	}

	public void parse() {
		vowlData.getEntityMap().values().stream().forEach(this::parseForEntity);
	}

	protected void parseForEntity(AbstractEntity entity) {
		entity.accept(new AnnotationVisitor(vowlData, manager));
	}
}
