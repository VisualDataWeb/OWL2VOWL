OWL2VOWL converter
==================

Requirements
------------
*   at least Java 6


Instructions
------------

By default the converted ontology will be written in a .json file in the working directory.

### Convert an ontology by its IRI
Run owl2vowl.jar with the `-iri` parameter: `java -jar owl2vowl.jar -iri "[Ontology IRI]"`

**Example**: `java -jar owl2vowl.jar -iri "http://ontovibe.visualdataweb.org"`


### Convert an ontology from a local file
Run owl2vowl.jar with the `-file` parameter: `java -jar owl2vowl.jar -file path/to/local/ontology`

**Example**: `java -jar owl2vowl.jar -file ontologies/foaf.rdf`


### Convert an ontology with dependencies from a local file
Run owl2vowl with the additional `-dependencies` parameter: `java -jar owl2vowl.jar -file path/to/local/ontology -dependencies path/to/dependency1 path/to/dependency2 ...`

**Example**: `java -jar owl2vowl.jar -file ontovibe.ttl -dependencies ontovibe_imported.ttl`


### Echo the converted ontology on the console
Use any of the input parameters together with the `-echo` flag: `java -jar owl2vowl.jar -echo -iri "[Ontology IRI]"`

**Example**: `java -jar owl2vowl.jar -iri "http://ontovibe.visualdataweb.org" -echo`
