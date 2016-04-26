package de.uni_stuttgart.vis.vowl.owl2vowl.constants;

public class Ontology_Path {

	// Local
	public static final String PATH_VARIABLE =
			System.getProperty("user.dir") + "/ontologies/";
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
	public static final String ONTO_CARD = PATH_VARIABLE + "ontovibe/ontovibe_cardinalities.ttl";
	public static final String LOCAL_TESTING = PATH_VARIABLE + "testing.owl";
	public static final String STACK = PATH_VARIABLE + "StackExchange.ttl";
	public static final String TAGONT = PATH_VARIABLE + "tagont.owl";
	public static final String DRAMMAR = PATH_VARIABLE + "Drammar_NunnaryScene_Optimized_Rules.owl";
	public static final String DISJOINT_NULL_POINTER = PATH_VARIABLE + "temp_simple.ttl";
	public static final String TEST = PATH_VARIABLE + "test.ttl";
	public static final String TEST_FULL_ONTOBENCH = PATH_VARIABLE + "fullontobench.ttl";
	public static final String TEST_ALLVALUES = PATH_VARIABLE + "allvalues.ttl";

	// External
	public static final String EXT_GEONAMES = "http://www.geonames.org/ontology/ontology_v3.1.rdf";
	public static final String EXT_PROV = "http://www.w3.org/ns/prov-o";
	public static final String EXT_PIZZA = "http://130.88.198.11/co-ode-files/ontologies/pizza.owl";
	public static final String EXT_ONTOVIBE = "http://ontovibe.visualdataweb.org/2.1";
	public static final String EXT_ONTO_CARD = "http://ontovibe.visualdataweb.org/cardinalities/";
	public static final String EXT_NICETAG = "http://ns.inria.fr/nicetag/2010/09/09/voc";
	public static final String EXT_SIOC = "http://rdfs.org/sioc/ns#";
	public static final String EXT_FOAF = "http://xmlns.com/foaf/0.1/";
	public static final String EXT_MUTO = "http://purl.org/muto/core";
	public static final String EXT_GOODRELATIONS = "http://purl.org/goodrelations/v1";
	public static final String EXT_MUSIC = "http://purl.org/ontology/mo/";
	public static final String EXT_MARINE = "http://www.ics.forth.gr/isl/MarineTLO/v3/marinetlo.owl";
	public static final String EXT_MARINE_TLO = "http://www.ics.forth.gr/isl/MarineTLO/v3/marinetloimarine.owl";
	public static final String EXT_DC_TERMS = "http://purl.org/dc/terms/";
	public static final String EXT_HOLY = "http://www.holygoat.co.uk/owl/redwood/0.1/tags/";
	public static final String EXT_PERSONAS = "http://blankdots.com/open/personasonto.owl";
	// NOT WORKING FOR ONTOLOGY PARSERS
	public static final String EXT_HP_NOT_WORKING = "http://purl.org/voc/hp/";
	public static final String EXT_HP = "https://raw.githubusercontent.com/AKSW/vocab.hp/master/schema.ttl";
	// Label Datatype construct. Time out?
	public static final String EXT_ENVO = "http://envo.googlecode.com/svn/trunk/src/envo/envo.owl";
	public static final String EXT_MLHIM = "http://www.mlhim.org/xmlns/mlhim2/mlhim246.owl";
}
