## About

The following are the features of **Form App** project.
* Provides the following REST APIs required to manage forms in a [**Form Modeler**](https://git.techsophy.com/techsophy-platform/tp-modeler-form/blob/dev/README.md)
    * ***Create*** - To create a form
    * ***Update*** - To update a form
    * ***Delete*** - To delete a from
    * ***FetchForms*** - To fetch all the forms, search for a form, pagination and sorting
    * ***FetchFormByFormKey*** - To fecth a form
* Provides the following REST APIs required to manage components in a Form Modeler
    * ***Create*** - To create a component
    * ***Update*** - To update a component
    * ***Delete*** - To delete a component
    * ***FetchComponents*** - To fetch all the components, search for a component, pagination and sorting
    * ***FetchComponentsByComponentKey*** - To fecth a form
* Supports **versioning** of forms/components and provides REST APIs to fetch the versions of forms/components
* Records the **history/audit** of actions performed on forms/components

## REST API Documentation
Click [here](https://techsophysol.sharepoint.com/sites/TechsophyDeveloperNetwork/augment/SitePages/TP-APP-FORM.aspx?Mode=Edit) for more details on API documentation

## Postman Collection
click [here](url) for postman collection to test the REST APIs

## Built With
This section contains list of frameworks,libraries and tools used to bootstrap this project.
- Java 11
- Spring boot 2.4.5
- Gradle 7.0 or newer
- Mongodb 4.4
- keycloak 11.0.2
- IntelliJ Idea


## Environment Variables

Set the below environment variables to bootstrap this project.

| Name | Example Value |
| ------ | ------ |
| GATEWAY_URI | https://api-gateway.techsophy.com |
| KEYCLOAK_URL_AUTH | https://keycloak.techsophy.com/auth | 
| TP_MODELER_APP_MONGO_URI | mongodb://localhost:27017/techsophy-platform (Setup replica set)|


Click [here](https://git.techsophy.com/techsophy-platform/tp-cloud-config/blob/dev/tp-app-form-dev.yaml)  to view all application properties.

## Getting Started
In order to start working on this project follow below steps:

### Prerequisites
- Download and install [JDK 11](https://www.oracle.com/in/java/technologies/javase/jdk11-archive-downloads.html)
- Download and install [IntelliJ Idea](https://www.jetbrains.com/idea/download/#section=linux)
- Download and Install [Keycloak](https://www.keycloak.org/archive/downloads-11.0.2.html) and set **KEYCLOAK_URL_AUTH**
- Download and install [MongoDB]() and setup [replicaset]().
- Run [tp-cloud-config-server](https://git.techsophy.com/techsophy-platform/tp-cloud-config-server/blob/dev/README.md)  
- Run [tp-api-gateway](https://git.techsophy.com/techsophy-platform/tp-api-gateway)
- Download and install Postman - Click [here](https://www.postman.com/downloads/) to test the REST APIs

### Configure Keycloak
Click [here](url) to configure keycloak

### Run
The following instructions are useful to run the projet.
- Open terminal and run following cammand.
- clone this git repo using below url
>git clone https://git.techsophy.com/techsophy-platform/tp-app-form
- Open the created folder in intellij idea
- Set the [environment variables] 
- Start the application

### Test
Test the REST APIs by importing the postman collection from postman or [swagger ui](url).

## Acknowledgments

## Contributions

## License

## Contact




