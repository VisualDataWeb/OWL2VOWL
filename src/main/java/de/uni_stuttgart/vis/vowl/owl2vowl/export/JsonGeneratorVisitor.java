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

import java.util.Map;

/**
 *
 */
public interface JsonGeneratorVisitor {
	public Map<String, Object> getEntityJson();

	public Map<String, Object> getEntityAttributes();

	public boolean isFailure();

	public void visit(BaseEntity entity);

	public void visit(BaseEdge entity);

	public void visit(BaseProperty entity);

	public void visit(TypeOfProperty entity);

	public void visit(DisjointProperty entity);

	public void visit(OwlDatatypeProperty entity);

	public void visit(OwlObjectProperty entity);

	public void visit(SubClassProperty entity);

	public void visit(BaseNode entity);

	public void visit(BaseClass entity);

	public void visit(ExternalClass entity);

	public void visit(OwlClass entity);

	public void visit(OwlComplementOf entity);

	public void visit(OwlDeprecatedClass entity);

	public void visit(OwlEquivalentClass entity);

	public void visit(OwlIntersectionOf entity);

	public void visit(OwlThing entity);

	public void visit(OwlUnionOf entity);

	public void visit(RdfsClass entity);

	public void visit(RdfsResource entity);

	public void visit(SpecialClass entity);

	public void visit(BaseDatatype entity);

	public void visit(RdfsDatatype entity);

	public void visit(RdfsLiteral entity);
}
