# wikipathways-api-client-java (Deprecated)

**⚠️ This repository is deprecated and will no longer receive updates. Please use the new repository: [wikipathways-java-client](https://github.com/wikipathways/wikipathways-java-client).**

For continued support and updates, migrate to the new [WikiPathways Java Client](https://github.com/wikipathways/wikipathways-java-client), which serves as the replacement for this repository.

Java library for WikiPathways webservice

Currently under development and testing.

With this library you can access the pathway database WikiPathways from Java through the REST webservice API (http://webservice.wikipathways.org).
The old SOAP webservice will be discontinued in the near future (spring 2015) and all functionality (also updating and uploading information) have been implemented in the REST API. 

Be aware that update and upload functionality provide special permissions that need to be requested before using those webservice functions. 

Compile the code and run the tests with:

```
cd org.wikipathways.client
mvn clean install
```

If you want to use a different web service, use (for example):

```
cd org.wikipathways.client
mvn -Dwp.webserver=http://otherservice.wikipathways.org clean test
```

## OSGi bundle

Create the OSGi bundle with:

```
cd org.wikipathways.client
mvn bundle:bundle
```
