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
	public static final String PROP_TYPE_DISJOINT = "owl:disjointWith";

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
	public static final String WINE = PATH_VARIABLE + "wine.rdf";
	public static final String PERSONAS = PATH_VARIABLE + "personasonto.owl";
	public static final String BENCHMARK1 = PATH_VARIABLE + "ontovibe/BenchmarkOntology.ttl";
	public static final String BENCHMARK2 = PATH_VARIABLE + "ontovibe/BenchmarkOntologyModule.ttl";
	public static final String EXT_GEONAMES = "http://www.geonames.org/ontology/ontology_v3.1.rdf";
	public static final String EXT_PROV = "http://www.w3.org/ns/prov-o";
	public static final String EXT_PIZZA = "http://130.88.198.11/co-ode-files/ontologies/pizza.owl";
	public static final String EXT_ONTOVIBE = "http://ontovibe.visualdataweb.org/1.0";
	public static final String EXT_NICETAG = "http://ns.inria.fr/nicetag/2010/09/09/voc";
	public static final String EXT_SIOC = "http://rdfs.org/sioc/ns#";
	public static final String EXT_FOAF = "http://xmlns.com/foaf/0.1/";
	public static final String EXT_MUTO = "http://purl.org/muto/core";
	public static final String EXT_GOODRELATIONS = "http://purl.org/goodrelations/v1";
	public static final String EXT_MUSIC = "http://purl.org/ontology/mo/";
	public static final String EXT_MARINE = "http://www.ics.forth.gr/isl/MarineTLO/v3/marinetlo.owl";
	public static final String EXT_MARINE_TLO = "http://www.ics.forth.gr/isl/MarineTLO/v3/marinetloimarine.owl";


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
	public static final String INFO_TITLE_DC = "<http://purl.org/dc/elements/1.1/title>";
	public static final String INFO_TITLE_DCTERMS = "<http://purl.org/dc/terms/title>";
	public static final String INFO_SEE_ALSO = "rdfs:seeAlso";
	public static final String INFO_ISSUED_DCTERMS = "<http://purl.org/dc/terms/issued>";
	public static final String INFO_CREATOR_DC = "<http://purl.org/dc/elements/1.1/creator>";
	public static final String INFO_CREATOR_DCTERMS = "<http://purl.org/dc/terms/creator>";
	public static final String INFO_CONTRIBUTOR_DCTERMS = "<http://purl.org/dc/terms/contributor>";
	public static final String INFO_PUBLISHER_DCTERMS = "<http://purl.org/dc/terms/publisher>";
	public static final String INFO_LICENSE_DCTERMS = "<http://purl.org/dc/terms/licence>";
	public static final String INFO_DESCRIPTION_DC = "<http://purl.org/dc/elements/1.1/description>";
	public static final String INFO_DESCRIPTION_DCTERMS = "<http://purl.org/dc/terms/description>";
	public static final String INFO_VERSION_INFO = "owl:versionInfo";
	public static final String INFO_RDFS_LABEL = "rdfs:label";
	public static final String INFO_RIGHTS_DCTERMS = "<http://purl.org/dc/terms/rights>";
	public static final String INFO_MODIFIED_DCTERMS = "<http://purl.org/dc/terms/modified>";

	/* Annotations used for axioms. */
	public static final String AXIOM_OBJ_PROP_DOMAIN = "ObjectPropertyDomain";
	public static final String AXIOM_OBJ_PROP_RANGE = "ObjectPropertyRange";
	public static final String AXIOM_DATA_PROP_DOMAIN = "DataPropertyDomain";
	public static final String AXIOM_DATA_PROP_RANGE = "DataPropertyRange";
	public static final String AXIOM_OBJ_UNION = "ObjectUnionOf";
	public static final String AXIOM_OBJ_INTERSECTION = "ObjectIntersectionOf";
	public static final String AXIOM_OBJ_COMPLEMENT = "ObjectComplementOf";
	public static final String AXIOM_DISJOINT = "DisjointClasses";
	public static final String AXIOM_DISJOINTUNION = "DisjointUnion";
	public static final String AXIOM_SUBCLASS = "SubClassOf";
	public static final String AXIOM_CARD_MIN = "ObjectMinCardinality";
	public static final String AXIOM_CARD_EXACT = "ObjectExactCardinality";

	/* Languages */
	public static final String LANG_UNSET = "undefined";
	public static final String LANG_DEFAULT = "IRI-based";
}
