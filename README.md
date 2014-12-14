# Calypso

Calypso started from the idea that part of the programming craft is building an assorted set of tools and techniques that will assist in quickly developing complex/powerful applications.

Some of the ideas shown in this project:

* Schema-first development of web-services - check calypso/src/main/resources/schemas for a set of schemas used in the application. These schemas are used to generate Java code using the CXF framework and the wsdl2java maven plugin. We also generate the REST service skeletons with the wadl2java plugin. See the mvn profile wadl in calypso-ws/pom.xml

* Use the same schema files to create both SOAP/XML messages and JSON messages - JacksonJaxbJsonProvider.

* UI development using a combination of Javascript/Typescript + Spring MVC/Tiles. See calypso-ws/src/main/webapp for the overall application. Javascript is a very powerfull language, but is also hard to develop large-scale applications. All the JS code in Calypso is built using Typescript - a superset of Javascript with type annotations. Check calypso-ws/src/main/webapp/WEB-INF/ts/*.ts. JetBrain WebStorm is a great IDE for developing Typescript + Javascript applications.

* Maven (mvn) is at the core of the build system. An application may have multiple components, all sharing the same dependencies and versioning scheme. Look at the root pom.xml and then at the calypso-parent/pom.xml.

* Use bower and grunt for building Javascript applications. Check the files calypso-ws/bower.json and calypso-ws/Gruntfile.js. The external js libraries are checked in but they can be generated based on these build scripts.

* Unit testing with JUnit plus some extensions to make regression testing simpler and support integration testing. Check calypso/src/test/java/**/JTest... .

* Find bugs with ... findbugs. See calypso-parent/find-bugs and the mvn-profile findbugs in calypso-parent/pom.xml file.
 
* Testing coverage with emma. See the mvn-profile emma in calypso-parent/pom.xml
 
... and many other neat tools and configurations.
