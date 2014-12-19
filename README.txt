How to use the current version of OWL2VOWL converter
====================================================

Requirements
------------
*   at least Java 6

Instructions
------------
*   To convert an ontology by its IRI, run owl2vowl.jar with the -iri parameter:
		java -jar owl2vowl.jar -iri "[Ontology IRI]"
	By default the converted ontology will be written in a .json file in the working directory.

*   To convert an ontology from a local file, run owl2vowl.jar with the -file parameter:
		java -jar owl2vowl.jar -file path/to/local/ontology

*   To convert an ontology with dependencies from a local file, run owl2vowl with the additional -dependencies parameter:
		java -jar owl2vowl.jar -file path/to/local/ontology -dependencies path/to/dependency1 path/to/dependency2 ...

*   To echo the converted ontology onto the console, use any of the input parameters together with the -echo flag:
		java -jar owl2vowl.jar -echo -iri "[Ontology IRI]"

Examples
--------
*   Convert OntoViBe from its IRI into a local file:
		java -jar owl2vowl.jar -iri "http://ontovibe.visualdataweb.org/1.0/"

*   Convert OntoViBe with its dependencies from a local file:
		java -jar owl2vowl.jar -file BenchmarkOntology.ttl -dependencies BenchmarkOntologyModule.ttl

*   Convert OntoViBe from its IRI and echo it onto the console:
		java -jar owl2vowl.jar -iri "http://ontovibe.visualdataweb.org/1.0/" -echo

*   Convert a local ontology like foaf.rdf and echo it on the console:
		java -jar owl2vowl.jar -file ontologies/foaf.rdf -echo
