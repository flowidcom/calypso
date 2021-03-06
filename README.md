# Calypso

Calypso started from the idea that part of the programming craft is building an assorted set of tools and techniques that make easier to develop new applications.

Some of the ideas shown in this project:

* Schema-first development of web-services - check calypso/src/main/resources/schemas for a set of schemas used in the application. These schemas are used to generate Java code using the CXF framework and the wsdl2java maven plugin. We also generate the REST service skeletons with the wadl2java plugin. See the mvn profile wadl in calypso-ws/pom.xml

* Use the same schema files to create both SOAP/XML messages and JSON messages - JacksonJaxbJsonProvider.

* UI development using a combination of Javascript/Typescript + Spring MVC/Tiles. See calypso-ws/src/main/webapp for the overall application. Javascript is a powerfull language, but is also hard to develop large-scale applications. All the JS code in Calypso is built using Typescript - a superset of Javascript with type annotations. Check calypso-ws/src/main/webapp/WEB-INF/ts/*.ts. JetBrain WebStorm is a great IDE for developing Typescript + Javascript applications. See http://flowid.com/typescript for more details on using TypeScript for large scale web-applications.

* Maven (mvn) is at the core of the build system. An application may have multiple components, all sharing the same dependencies and versioning scheme. Look at the root pom.xml and then at the calypso-parent/pom.xml.

* Use Bower and Grunt for building Javascript applications. Check the files calypso-ws/bower.json and calypso-ws/Gruntfile.js. The external js libraries are checked in but they can be generated based on these build scripts.

* Use Docker to create images for the application running on the cloud. See the file src/main/docker/tomcat.dockerfile.

* Find bugs with ... findbugs. See calypso-parent/find-bugs and the mvn-profile findbugs in calypso-parent/pom.xml file.
 
* Testing coverage with emma. See the mvn-profile emma in calypso-parent/pom.xml
 
... and many other neat tools and configurations.
