# AddressBook

Section 1: Address Book App Setup

UC 1
Create a Address Book Spring Project to
cater to REST Request from Address
Book UI - Make sure every UC is in its own branch and then
merged to Main Branch. The GIT Version History will be
looked during Code Review.
- Here we will reuse the Address Book Frontend UI we
have already developed
- Instead of making REST call to JSONServer will now
make server calls to Address Book Spring App
- Also test the REST API using CURL Commands



UC 2
Create a Rest Controller to demonstrate
the various HTTP Methods
- Before starting set application.properties to set properties
of MySQL Driver and MySQL Credentials
- Use ResponseEntity to return JSON Response
- Test the REST Calls using CURL.
- At this stage the interest is to establish the connectivity and
ensure data is transmitted in REST Calls
- Make sure to test all CURL Calls – GET, GET by ID, POST, PUT
to Update by ID, and DELETE



Section 2: Handling AddressBook DTO
and Model in AddressBook Service Layer

S2-UC3
Introducing DTO and Model to
AddressBook App
- Note that you can keep the DTO and Model fairly
simplistic with only few fields
- This is because you are still in the setup of the Layers and
once all layers are set up you can make AddressBook DTO
and Models as full-fledged Object for Front End to make
REST Call.
- Use ResponseEntity to return JSON Response
- Currently we will do CURL Call to ensure the REST Calls
are working




S2-UC4
Introducing Services Layer in
AddressBook App - Note that Controller in UC1 was creating the
Model and returning the Model on the REST Calls.
This is the job of services layer to manage the
Model.
- Autowired Annotation will be used to do
Dependency Injection of the Services Object to
the Controller.




S2-UC5
Ability for the Services Layer to
store the AddressBook Data - Note that Services Layer is not storing, updating
or deleting Employee Payroll Data in UC 2.
- In this use case the Services Layer will store this
Data in a Memory as a List. As we go further
down we will persist this data in DB.
- Make sure to test all CURL Calls
– GET, GET by ID,
POST, PUT to Update by ID, and DELETE


S3-UC1
Implement User Registration & Login
Create User Model (or Entity) & DTO(for response)
Implement Password Hashing (BCrypt)
Generate JWT Token on successful login
Store User Data in MY SQL Database
Endpoints:
POST /api/auth/register
POST /api/auth/login
Return the Response Accordingly


S3-UC2
Implement Forgot & Reset Password
Generate Reset Token
Send Password Reset Email (SMTP)
Verify token & allow password reset
Endpoints:
POST /api/auth/forgot-password
POST /api/auth/reset-password




