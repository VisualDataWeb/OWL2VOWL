package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.AbstractEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.VowlClass;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.VowlThing;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.VowlDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.VowlLiteral;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.properties.VowlDatatypeProperty;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.properties.VowlObjectProperty;
import org.semanticweb.owlapi.model.IRI;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Eduard
 */
public class VowlSubclassPropertyGenerator implements VowlElementVisitor {
	private VowlData vowlData;

	public VowlSubclassPropertyGenerator(VowlData vowlData) {
		this.vowlData = vowlData;
	}

	public void execute() {
		Set<AbstractEntity> entities = new HashSet<>(vowlData.getEntityMap().values());

		for (AbstractEntity abstractEntity : entities) {
			abstractEntity.accept(this);
		}
	}

	@Override
	public void visit(VowlThing vowlThing) {

	}

	@Override
	public void visit(VowlClass vowlClass) {
		if (vowlClass.getSubEntities().size() != 0) {
			for (IRI iri : vowlClass.getSubEntities()) {
				vowlData.getGenerator().generateSubclassProperty(iri, vowlClass.getIri());
			}
		}
	}

	@Override
	public void visit(VowlLiteral vowlLiteral) {

	}

	@Override
	public void visit(VowlDatatype vowlDatatype) {

	}

	@Override
	public void visit(VowlObjectProperty vowlObjectProperty) {

	}

	@Override
	public void visit(VowlDatatypeProperty vowlDatatypeProperty) {

	}
}
