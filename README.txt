How to use the current version of OWL2VOWL converter
====================================================

Requirements
------------
*   at least Java 6

Instructions
------------
*   To convert an ontology by its IRI, run owl2vowl.jar with the -iri parameter:
		java -jar owl2vowl.jar -iri "[Ontology IRI]"
	The converted ontology will be written in a .json file in the working directory.

*   To convert an ontology from a local file, run owl2vowl.jar with the -file parameter:
		java -jar owl2vowl.jar -file path/to/local/ontology
	The converted ontology will be written in a .json file in the working directory.

*   To echo the converted ontology onto the console, use one of the input parameters together with the -echo flag:
        java -jar owl2vowl.jar -echo -iri "[Ontology IRI]"

Examples
--------
*   Convert OntoViBe from its IRI into a local file:
		java -jar owl2vowl.jar -iri "http://ontovibe.visualdataweb.org/1.0/"

*   Convert OntoViBe from its IRI and echo it onto the console:
		java -jar owl2vowl.jar -iri "http://ontovibe.visualdataweb.org/1.0/" -echo

*   Convert a local ontology like foaf.rdf and echo it on the console:
		java -jar owl2vowl.jar -file ontologies/foaf.rdf -echo
