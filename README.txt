-- How to use the current version of OWL2VOWL converter --

Requirements:
- Java 6 (1.6_u18)

Instructions:
Run owl2vowl.jar from the command line:

java -jar owl2vowl.jar "[Ontology IRI]"

Example:
java -jar owl2vowl.jar "http://ontovibe.visualdataweb.org/1.0/"

The JSON file is created next to the .jar file.
If possible, the name of the JSON file is extracted from the ontology IRI.
If not, the name will be >null.json<

Notes:
A log file is created for debugging purposes.
