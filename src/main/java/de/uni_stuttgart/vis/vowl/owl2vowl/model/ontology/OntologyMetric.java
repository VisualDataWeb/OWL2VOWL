/*
 * OntologyMetric.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model.ontology;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;
import org.semanticweb.owlapi.metrics.ReferencedClassCount;
import org.semanticweb.owlapi.metrics.ReferencedDataPropertyCount;
import org.semanticweb.owlapi.metrics.ReferencedIndividualCount;
import org.semanticweb.owlapi.metrics.ReferencedObjectPropertyCount;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Holds the metrics of the desired ontology.
 */
public class OntologyMetric {
    private OWLOntology ontology;
    private int classes;
    private int objectProperties;
    private int datatypeProperties;
    private int individuals;
    private int nodes;
    private int edges;

    public OntologyMetric(OWLOntology ontology) {
        this.ontology = ontology;
        calculateOntologyMetrics();
    }

    private void calculateOntologyMetrics() {
        classes = new ReferencedClassCount(ontology).getValue();
        objectProperties = new ReferencedObjectPropertyCount(ontology).getValue();
        datatypeProperties = new ReferencedDataPropertyCount(ontology).getValue();
        individuals = new ReferencedIndividualCount(ontology).getValue();
    }

    public void calculate(VowlData vowlData) {
    	System.out.println("this is never called and thats good so, -> Metrics calculated on the fly by visualization ");
        int classCount = vowlData.getClassMap().size();
        int datatypeCount = vowlData.getDatatypeMap().size();
        int objectPropertyCounts = vowlData.getObjectPropertyMap().size();
        int dataPropertyCount = vowlData.getDatatypePropertyMap().size();

        nodes = classCount + datatypeCount;
        edges = objectPropertyCounts + dataPropertyCount;
    }

    public int getClasses() {
        return classes;
    }

    public int getObjectProperties() {
        return objectProperties;
    }

    public int getDatatypeProperties() {
        return datatypeProperties;
    }

    public int getIndividuals() {
        return individuals;
    }

    public int getNodes() {
        return nodes;
    }

    public int getEdges() {
        return edges;
    }

    @Override
    public String toString() {
        return "OntologyMetric{" +
                "classes=" + classes +
                ", objectProperties=" + objectProperties +
                ", datatypeProperties=" + datatypeProperties +
                ", individuals=" + individuals +
                ", nodes=" + nodes +
                ", edges=" + edges +
                '}';
    }
}
