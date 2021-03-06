# Domo

Domo is a SPA Building Management System intended to facilitate the responsibilities of a Property manager.

The application follows the standard Spring MVC architecture. 

## Environment: 
>Java 14, Spring Framework, JUnit, Maven, Spring Data, Hibernate, Spring Security, MySQL, Angular, Intellij IDEA, VSCode, MySQL Workbench, Docker, Git, Postman/Insomnia, AWS

# How does Domo work?
>Domo provides easy and hassle free way for building managers to supervise janitorial and
maintenance services, monitor fees collection, staff payroll and tasks completion, 
organize online meetings with the residents, assign tasks to staff. 
Domo presents convenient means for residents to pay their monthly fees/rent 
and receive notifications for overdue payments or building related news. 

# Who uses Domo?

Domo is a perfect solution for property administration managers and building residents.


This text you see here is *actually* written in Markdown! To get a feel for Markdown's syntax, type some text into the left window and watch the results in the right.

### What has been done:

- Designed the data model for the system. Defined JPA entities.
- Used Spring Data JPA with Hibernate to create DAO layer in order to connect and perform CRUD operations on a MySQL data source.
- Developed business logic using Spring Service layer annotations and configuration files.
- Provided REST API by creating a web layer with @RestControllers annotated classes. 
- Used @RestControllerAdvice as a global exception handler. 
- Performed unit and integration testing. (Hamcrest, JUnit, Spring MockMvc)
- Implemented an asynchronous notification service with JavaMailSender.
- Used Git and GitHub for Version control
- Configured and scheduled cron tasks with the Spring @Scheduled annotation.

### Work in progress:

- Building the application front-end with Angular, TypeScript, HTML, CSS, Bootstrap, JavaScript, DOM.
- To be done: adding a Bcrypt password encryption, Securing the application with Spring Security, Deploying the application to AWS Elastic Beanstalk. 

### Setup
Tested on Windows 10 x64

### Versions:
Java 14
Maven 3.6.3
MySQL with mysql-connector-java 8.0.22

### Other tools:

ModelMapper
Hibernate Validator
Hamcrest
H2 Database
JUnit

### System and IDE should be configured to use:
Java 14 - IntelliJ
Maven 3.6.3- IntelliJ
mysql-connector-java 8.0.22 - IntelliJ

