# package self Service

This service is responsible for creating a new package to be sent and to get status of the package created. It also gives functinality to query individual package detail based on package id and also to query packages by the sender.

## Endpoints

- `GET /api/packages/receivers`: Gets a list of receivers
- `POST /api/packages`: Submit package for sending. This will call shipping-service internally and submits the package.
- `GET /api/packages/{packageId}` : Gets detail of an individual package by passing packageId.
- `GET /api/packages/sender/{senderEmployeeId}` : Gets detail of list of packages send by a particular sender   by passing employeeId of the sender. this can also be used to filter packages based on status

## Running the Application Locally

1. Ensure you have [JDK 17] and [Maven 3] installed.
2. Clone the repository to your local machine.
3. There is a main module package-self-service which has 2 sub modules. self-service(Assesment scope)  &      shipping-service(Backend stub service). Navigate to each module(self-service & shipping-service) directory and run `mvn spring-boot:run`.

IntelliJ Setup:
Import the Project: Open IntelliJ IDEA, click on File -> New -> Project from Existing Sources..., navigate to the directory where you cloned the repository, and select the pom.xml file. Click OK and follow the prompts to import the project.

## Tesing with postman
1. start both self-service (assessment-scope) & shipping-service(backend stub) in local.
2. below senderEmployeeIds &  packageIds can be used for testing the application.

Available Id's
list of senderEmployeeIds:
1. 20001
2. 20002
3. 20003

list of packageIds:
1. 3f6c794b-2c96-491e-81fb-a2f9731d02c4
2. 4a7d794b-3d97-492e-82fc-b3f9731d03d5
3. 5b8e794b-4e98-493e-83fd-c4f9731d04e6
4. 6c9f794b-5f99-494e-84fe-d5f9731d05f7
5. 7d0g794b-6g00-495e-85ff-e6f9731d06g8
6. 8e1h794b-7h01-496e-86gg-f7f9731d07h9
7. 9f2i794b-8i02-497e-87hh-g8f9731d08i0
8. 0g3j794b-9j03-498e-88ii-h9f9731d09j1
9. 1h4k794b-0k04-499e-89jj-i0f9731d00k2
10. 2i5l794b-1l05-400e-90kk-j1f9731d01l3

## Running Tests

1. Navigate to the self-service project directory.
2. Run `mvn test` to execute the unit tests.
3. Run `mvn verify` to execute the integration tests.


## Data Base
1. I did not use any database. for receivers I have a hardcoded the list of receivers for sake of simplicity.
2. shipping-service backend service also doesn't have database configured. So the list of package data are hardcoded

## Test Cases
The test cases are located in the `src/test/java` directory. 


##  Basic Authorization

The Basic Authorization is automatically applied to all incoming HTTP requests, so you do not need to do anything to use it. Just make sure to include the Basic Authorization header in your requests.For example, I have kept below credentails 

username: user 
password: userPass

## API Documentation

API documentation self-service is available at http://localhost:8200/swagger-ui.html when the application is running. The documentation includes information about the request and response formats, as well as the possible error codes for each endpoint.


## Create Docker Image
docker buildx build -t self-service:latest .

docker buildx build -t shipping-service:latest .

# Run the self-service image
docker run -d -p 8200:8200 self-service:latest

# Run the shipping-service image
docker run -d -p 8100:8100 shipping-service:latest

### In addition:
1. The registration date is calculated as 7 days prior to the expectedDelivery date of packageDetail. This assumes that the expected delivery date is based on the package creation date. If a database were implemented, the registration date would be stored.  
2. Given more time, I would have implemented a database to retrieve employee and package details from database itself instead of hard coded values.  
3. Given more time, I would have implemented circuit breaker and rate limiting mechanisms.

