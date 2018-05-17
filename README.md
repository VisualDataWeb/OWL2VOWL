[![Build Status](https://travis-ci.org/VisualDataWeb/OWL2VOWL.svg)](https://travis-ci.org/VisualDataWeb/OWL2VOWL)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.visualdataweb.vowl.owl2vowl/OWL2VOWL/badge.svg)](https://mvnrepository.com/artifact/org.visualdataweb.vowl.owl2vowl/OWL2VOWL)
[![](https://jitpack.io/v/VisualDataWeb/OWL2VOWL.svg)](https://jitpack.io/#VisualDataWeb/OWL2VOWL)

OWL2VOWL converter 
==================

Dev Dependency
--------------
We have a release repo in maven central but the version there is and will not be updated frequently.
Therefore i suggest to use [Jitpack OWL2VOWL](https://jitpack.io/#VisualDataWeb/OWL2VOWL) in order to pull the latest release version (in example master branch or the available tags).

For instructions how to include the dependency click the above link or as a example for maven projects:

```
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
       ....
       <dependency>
	    <groupId>com.github.VisualDataWeb</groupId>
	    <artifactId>OWL2VOWL</artifactId>
	    <version>master-SNAPSHOT</version>
	</dependency>
       
```


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
To build a jar file that can be executed standalone, you have to use the package option with parameter: `mvn package -Denv=standalone` or `mvn package -P standalone-release`. This will build the file `...-shaded.jar` containing all dependencies needed to be executable.

### Build the war
To build the war file, simply execute `mvn package -P war-release`. This will generate a war file.
This war can be execute like a jar file to start a local server.

### Running in IDE
It would be a pain always building the jar only to test some new implemented stuff. We included a possiblity to run the conversion directly with the IDE. For this you need to change the `ConverterImpl.java` class.

To run a Spring Server directly from the IDE you have to start the `ServerMain.java` class.

### Building docker image
With the provided `Dockerfile` you can easily build the docker image of the server version on the exposed port `8080`.
Just run `docker build -t owl2vowl/owl2vowl .`
After that you can run the image `docker run --rm -d -p 8080:8080 owlv2vowl/owl2vowl`.
OWL2VOWL is now reachable on `localhost:8080`.


FAQ
==================

## I want to log information, errors, etc to files. Is there a default configuration?
If you want to use logging to files there is log4j2 configuration provided under ```src/main/resources/log4j2-spring-file.xml```.

- JAR/IDE: To load it you have to provide a JVM argument. For example if you have the JAR you have to exeecute:
       ```java -Dlog4j.configurationFile=path/to/log4j2-spring-file.xml -jar ...```
- WAR: Uncomment the line in ```src/main/resources/application.properties```        
  
