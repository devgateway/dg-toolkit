# dg-toolkit web module

This module provides REST endpoints for the services needed, as well as basic security. It depends on the **persistence** module.

It also provides full authentication and security using Spring Security.
The module is packaged as a jar and can be deployed as a [fat jar](http://docs.spring.io/spring-boot/docs/current/reference/html/howto-build.html).

# Building

The web module is part of its larger dg-toolkit parent build, so you need to build on the parent first.

# Starting the app

Because it gets packaged as a fat jar, starting it is piece of cake:

`java -jar target/web-0.0.1-SNAPSHOT.jar`

This will start everything, including an embedded Tomcat Web server and all the services attached it.

# Using Spring Boot Developer Tools

This spring add-on allows automatic context reload of beans when resource changes detected 
(classes recompiled, other files in classpath changed). This means you do not have to restart your
application when you recompile classes, nor you need paid tools like jrebel for it.

Read more about it [here](https://docs.spring.io/spring-boot/docs/2.1.9.RELEASE/reference/html/using-boot-devtools.html)
A good howto can be found [here](https://www.baeldung.com/spring-boot-devtools)

To enable devtools you need to start the application using java startup property
`spring.devtools.restart.enabled=true`

