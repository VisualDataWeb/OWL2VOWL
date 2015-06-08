OWL2VOWL converter [![Build Status](https://travis-ci.org/VisualDataWeb/OWL2VOWL.svg)](https://travis-ci.org/VisualDataWeb/OWL2VOWL)
==================

Requirements
------------
*   Java 6 (or higher)
*   Maven


Instructions for using OWL2VOWL
------------

The converted ontology will be written in a .json file in the working directory by default.

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

Instructions for developing with OWL2VOWL
-----------

### First steps
`Maven` is required to develop with OWL2VOWL and compile the code. It will load all dependencies automatically. If there occur any problems, refreshing the dependencies might solve them.
OWL2VOWL has been developed to work with Java 6, so `JDK 6` has to be used for compiling.

### Build the jar
To build the jar file, simply execute `mvn package`. The built `jar` only contains the compiled source code. 
To build a jar file that can be executed standalone, you have to use the package option with parameter: `mvn package -Denv=standalone`. This will build the file `owl2vowl.jar` containing all dependencies needed to be executable. There will also be a `zip` file created containing the `owl2vowl.jar`, README and the license files.

### Running in IDE
It would be a pain always building the jar only to test some new implemented stuff. We included a possiblity to run the conversion directly with the IDE. For this you need to change the `Main.java` class.

Change the field `CONVERT_ONE = false` to `CONVERT_ONE = true`. Now you can work with the `convertOneOntology`-method to convert those ontologies you would like to test with. Some ontologies for testing can also be found in the `Ontology_Path.java`.

BUT if you want to create the `jar`, you have to change the field back to `CONVERT_ONE = false` - otherwise it will still only execute the `convertOneOntology`-method.
