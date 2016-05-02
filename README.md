OWL2VOWL converter [![Build Status](https://travis-ci.org/VisualDataWeb/OWL2VOWL.svg)](https://travis-ci.org/VisualDataWeb/OWL2VOWL)
==================

Requirements
------------
*   Java 8 (or higher)
*   Maven


Instructions for using OWL2VOWL JAR
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
OWL2VOWL has been developed to work with Java 8, so `JDK 8` has to be used for compiling.

### Build the jar
To build the jar file, simply execute `mvn package`. The built `jar` only contains the compiled source code. 
To build a jar file that can be executed standalone, you have to use the package option with parameter: `mvn package -Denv=standalone` or `mvn package -P standalone-release`. This will build the file `owl2vowl.jar` containing all dependencies needed to be executable. There will also be a `zip` file created containing the `owl2vowl.jar`, README and the license files.

### Build the war
To build the war file, simply execute `mvn package -P war-release`. This will generate a war file.
This war can be execute like a jar file to start a local server.

### Running in IDE
It would be a pain always building the jar only to test some new implemented stuff. We included a possiblity to run the conversion directly with the IDE. For this you need to change the `ConverterImpl.java` class.

To run a Spring Server directly from the IDE you have to start the `ServerMain.java` class.
