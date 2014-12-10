-- How to use initial version of OWL2VOWL-Converter --

Requirements:
- Java 6 (1.6_u18)

How-To:
You have to run this .jar out of command line tool.
Then you type: java -jar owl2vowl.jar "[Here you type in your desired ontology URI]"
The output file will be created next to the .jar directory. If possible the name of the jar
will be extracted out of the ontology IRI. If not the name will be >null.json<

Example:
java -jar owl2vowl.jar "http://ontovibe.visualdataweb.org/1.0/"
The output file is next to the jar with the name null.json

Notes:
- There will be created a log file for the ontology structure. This is just for debug purpose
and only in the initial release of OWL2VOWL.


Log:
- Updated to run with Java 6_u18
- Improvements in conversion of further constructs.