/*
 * Constants.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.model;

/**
 * @author Vincent Link, Eduard Marbach
 */
public class Constants {

	/* NODE TYPES */
	public static final String TYPE_CLASS = "owl:Class";
	public static final String TYPE_RDFSCLASS = "rdfs:Class";
	public static final String TYPE_EQUIVALENT = "owl:equivalentClass";
	public static final String TYPE_EXTERNALCLASS = "externalclass";
	public static final String TYPE_DEPRECTAEDCLASS = "owl:DeprecatedClass";
	public static final String TYPE_THING = "owl:Thing";
	public static final String TYPE_NOTHING = "owl:Nothing";
	public static final String TYPE_RDFSRESOURCE = "rdfs:Resource";
	public static final String TYPE_UNION = "owl:unionOf";
	public static final String TYPE_INTERSECTION = "owl:intersectionOf";
	public static final String TYPE_COMPLEMENT = "owl:complementOf";
	public static final String TYPE_DATATYPE = "rdfs:Datatype";
	public static final String TYPE_LITERAL = "rdfs:Literal";

	/* PROPERTY TYPES */
	public static final String PROP_TYPE_SUBCLASS = "rdfs:SubClassOf";

	/* SAMPLE ONTOLOGIES */
	public static final String PATH_VARIABLE =
			System.getProperty("user.dir") + "/OWL2VOWL/ontologies/";
	public static final String IMARINE = PATH_VARIABLE + "marinetloimarine.owl";
	public static final String MARINE = PATH_VARIABLE + "marinetlo.owl";
	public static final String MUSIC = PATH_VARIABLE + "musicontology.rdfs";
	public static final String SIOC = PATH_VARIABLE + "sioc.rdf";
	public static final String GR = PATH_VARIABLE + "v1.owl";
	public static final String MUTO = PATH_VARIABLE + "muto.rdf";
	public static final String FOAF = PATH_VARIABLE + "foaf.rdf";
	public static final String PERSONAS = PATH_VARIABLE + "personasonto.owl";
	public static final String GEONAMES = PATH_VARIABLE + "geonames.rdf";
	public static final String BENCHMARK1 = PATH_VARIABLE + "BenchmarkOntology.ttl";
	public static final String BENCHMARK2 = PATH_VARIABLE + "BenchmarkOntologyModule.ttl";
	public static final String PROV = PATH_VARIABLE + "prov.owl";

	/* ANNOTATIONS */
	public static final String RDFS_COMMENT = "rdfs:comment";
	public static final String RDFS_LABEL = "rdfs:label";
	public static final String RDFS_DEFINED_BY = "rdfs:isDefinedBy";
	public static final String OWL_VERSIONINFO = "owl:versionInfo";
	public static final String RDFS_SUBCLASS = "rdfs:subClassOf";
	public static final String RDFS_DOMAIN = "rdfs:domain";
	public static final String RDFS_RANGE = "rdfs:range";
	public static final String OWL_DEPRECATED = "owl:deprecated";

	/* STANDARD IRIS */
	public static final String GENERIC_LITERAL_URI =
			"http://www.w3.org/2000/01/rdf-schema#Literal";
	public static final String OWL_THING_CLASS_URI = "http://www.w3.org/2002/07/owl#Thing";

	/* PROPERTY ATTRIBUTES */
	public static final String PROP_ATTR_FUNCT = "functional";
	public static final String PROP_ATTR_OBJ = "object";
	public static final String PROP_ATTR_DATA = "datatype";
	public static final String PROP_ATTR_DEPR = "deprecated";
	public static final String PROP_ATTR_RDF = "rdf";
	public static final String PROP_ATTR_TRANS = "transitive";
	public static final String PROP_ATTR_INV_FUNCT = "inverse functional";
	public static final String PROP_ATTR_SUB = "subclass";
	public static final String PROP_ATTR_DISJOINT = "disjoint";
	public static final String PROP_ATTR_SYM = "symmetric";
	public static final String PROP_ATTR_IMPORT = "external";

	/* Ontology info annotations */
	public static final String INFO_TITLE = "<http://purl.org/dc/elements/1.1/title>";
	public static final String INFO_SEE_ALSO = "rdfs:seeAlso";
	public static final String INFO_ISSUED = "<http://purl.org/dc/terms/issued>";
	public static final String INFO_CREATOR = "<http://purl.org/dc/elements/1.1/creator>";
	public static final String INFO_LICENSE = "<http://purl.org/dc/terms/licence>";
	public static final String INFO_DESCRIPTION = "<http://purl.org/dc/elements/1.1/description>";
	public static final String INFO_VERSION_INFO = "owl:versionInfo";
	public static final String INFO_RDFS_LABEL = "rdfs:label";

}
