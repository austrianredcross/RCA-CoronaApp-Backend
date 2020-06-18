# Introduction
The RCA-CoronaApp-Backend is based on the Exposure Notification Reference Server: https://github.com/google/exposure-notifications-server


# Getting Started
## Set up your development environment
To run this functions locally with Java, install the following software:
* [Java Developer Kit (JDK)](https://www.azul.com/downloads/zulu-community/?architecture=x86-64-bit&package=jdk), version 8
* [Apache Maven](https://maven.apache.org/), version 3.0 or higher
* (optional) [IntelliJ IDEA](https://www.jetbrains.com/idea/download/#section=windows)

## IntelliJ IDEA
### Run
1. Import changes manually or enable auto import.
2. Configure the application
3. Edit Configurations ... and set Spring Boot Profile (optional)
4. Run 'CovidappApplication'

### Debug
1. Import changes manually or enable auto import.
2. Configure the application
3. Edit Configurations ... and set Spring Boot Profile (optional)
4. Debug 'CovidappApplication'



## Configure the application
The application can be configured as any Spring boot application:
- in the environment
- in specific application-<profile> properties or yaml file
... please check the documentation [Externalized Configuration] (https://docs.spring.io/spring-boot/docs/2.3.0.RELEASE/reference/html/spring-boot-features.html#boot-features-external-config)

###Configuration Keys when running it on Azure
* _APPINSIGHTS_INSTRUMENTATIONKEY_ : instrumentation key used for logging with Azure App Insights
* _APPLICATION_EXPORT_BLOBSTORE-TYPE_ : Type of the blobstore used by the application (azure-cloud-storage | filesystem | none)
* _SPRING_DATASOURCE_URL_ : url of the database
* _SPRING_DATASOURCE_USERNAME_ : database user name
* _SPRING_DATASOURCE_PASSWORD_ : database password
* _EXTERNAL_PERSONAL_DATA_STORAGE_URL_: the url of the tan validation specific
* _EXTERNAL_PERSONAL_DATA_STORAGE_AUTHORIZATION_KEY_NAME_ :autorization header
* _EXTERNAL_PERSONAL_DATA_STORAGE_AUTHORIZATION_KEY_VALUE_ : value of teh authoriztaionheader
* _EXTERNAL_PERSONAL_DATA_STORAGE_SHA256_KEY_ : sha key used for hashing must be the same as the one used by the service, otherwise it won't match


### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.3.0.RELEASE/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.3.0.RELEASE/maven-plugin/reference/html/#build-image)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.3.0.RELEASE/reference/htmlsingle/#production-ready)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.3.0.RELEASE/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Spring Security](https://docs.spring.io/spring-boot/docs/2.3.0.RELEASE/reference/htmlsingle/#boot-features-security)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.3.0.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)
* [Validation](https://docs.spring.io/spring-boot/docs/2.3.0.RELEASE/reference/htmlsingle/#boot-features-validation)
* [Spring cache abstraction](https://docs.spring.io/spring-boot/docs/2.3.0.RELEASE/reference/htmlsingle/#boot-features-caching)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/2.3.0.RELEASE/reference/htmlsingle/#configuration-metadata-annotation-processor)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Caching Data with Spring](https://spring.io/guides/gs/caching/)
