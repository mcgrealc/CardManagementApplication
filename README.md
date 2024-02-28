# Card Management Application

RESTful API Secure Credit Card Management Application

## Description

This is a Spring Boot (version 3.1.8) Application which provides RESTful APIs for a secure Credit
Card Account Management System.

### Database

The Application uses an embedded H2 Database which provides Tables for User and Credit Card
Management. This Database can be accessed using a browser console, using the link provided below,
and using the credentials which are stored in the applications.properties in the /src/main/resources
folder.

<http://localhost:8080/creditcardapp/v1/h2-console>

### Payment Gateway Simulator

An Express.js application is provided in the /payment-gateway-sim folder. This application has been
tested with Node.js version 21.2.0. This application can be set up by changing directory to
/payment-gateway-sim in a terminal and running the following commands:

`npm install` (Note - only need to run this once to install the required node modules)

`node server.js`

The application should start on port 5000 (if there is something else running on this port please
edit the server.js file line 3). Note that if changing the port from the default that the Spring
Boot application.properties file must be updated accordingly, by changing the following property:

`payment.gateway.url=http://localhost:5000/PaymentGateway`

### Spring Boot Application

This application provides the APIs and API documentation. The application requires Java 17 and Maven to be
installed on the system where it is expected to run.

To install the dependencies for the application execute the following command from the root folder:

`mvn clean install`

As part of the installation the unit tests are executed and a coverage report is avialable in:

`root-folder/target/site/jacoco/index.html`

To start the application run the following command from the root folder:

`mvn spring-boot run.`

This will start the application on port 8080. If a different port is required then please edit the
application.properties in the /src/main/resources folder.

Note that the application will log to the console where it has been started.

### Usage

The application APIs are both documented and accessible at the following browser location:

<http://localhost:8080/creditcardapp/v1/swagger-ui/index.html>

#### Authentication APIs

In order to use the credit-card APIs you must first signup as a User by using the Auth Controller
/api/auth/signup API. You can us ethe preloaded parameters or new ones by selecting the 'Try it out'
button for the API and the 'Execute' button.

If the Signup is successful then use the Signin API to obtain a JWT which can be used for the
remaining credit-card flows. Again the Swagger Document has preloaded parameters which can be
overriden for evaluation purposes, and again using 'Try it out' and 'Execute' buttons to obtain
a JWT.

Once the JWT is returned use the 'Authorize' button at the top of the Swagger page and paste the
JWT (without quotes) into the 'Value' input and then press the 'Authorize' button.

#### Credit Card APIs

The credit card APIs are per the exercise specification at follows:

* POST   /credit-cards to create a new credit card account.
* GET    /credit-cards to retrieve all credit card accounts.
* GET    /credit-cards/{id} to retrieve a specific account by its unique identifier.
* PUT    /credit-cards/{id} to update the credit limit of a specified credit card account.
* DELETE /credit-cards/{id} to delete a specified credit card account.
* POST   /credit-cards/{originalCardNumber}/charge to charge a credit card account.
* POST   /credit-cards/{originalCardNumber}/credit to credit a credit card account.

#### Test Cards

The following cards will produce negative responses:

4444 3333 2222 1111 - Card has been declined.

5555 4444 3333 1111 - Fraud detected.

All other test generated cards will return a successful charge/credit outcome.

## Testing

The Application is Unit Tested using a combination of JUnit and Spring Integration tests. The
Spring tests should be executed when the application is not running (to avoid cross-context issues).
