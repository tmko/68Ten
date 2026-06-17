# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/4.0.6/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/4.0.6/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/4.0.6/reference/web/servlet.html)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/4.0.6/reference/actuator/index.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.


### Prompt
Write a spring boot web application.  
1. Use yaml configuration.  It has a forward url, forward port, listening filepath
2. It has 1 post request endpoint.  
2.1 Inside create a Httpclient and forward the request body to another another web service.  The forward url and forward port is in the configuration.
2.2 The body in the respond will be copied and send to the original request
3. It has 1 web socket end point
3.1 it will look into a cookie, named lastlinenumber.  set the value in an int lineToRead = lastlinenumber + 1. set to 1 if lastlinenumber is missing
3.2 Read the content in the listening filepath.  The listening filepath is in the configuration.  skip to line that is equal to lineToRead or the end of file
3.3 respond to the web socket with the file content, read from lineToRead to at most lineToRead + 10
3.2 it will put the line number in a cookie, named lastlinenumber to the new lineToRead