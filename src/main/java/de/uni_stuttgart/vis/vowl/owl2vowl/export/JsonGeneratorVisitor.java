/*
 * JsonGeneratorVisitor.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.export;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.BaseEntity;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.BaseEdge;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.edges.properties.*;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.BaseNode;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.classes.*;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.BaseDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.RdfsDatatype;
import de.uni_stuttgart.vis.vowl.owl2vowl.model.nodes.datatypes.RdfsLiteral;
import de.uni_stuttgart.vis.vowl.owl2vowl.parser.visitors.classes.Owl2VowlClassVisitor;

import java.util.Map;

/**
 *
 */
public interface JsonGeneratorVisitor extends Owl2VowlClassVisitor {
	public Map<String, Object> getEntityJson();

	public Map<String, Object> getEntityAttributes();

	public boolean isFailure();
}
