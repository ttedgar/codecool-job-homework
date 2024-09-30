# Exam Management Application

This is a Spring Boot application built with Java that handles data synchronization and analysis for Codecool exam results. It interacts with two PostgreSQL databases: **source** and **target**.

## Features

1. **Data Synchronization**
    - The application reads exam result data stored as JSON strings in the source database.
    - These JSON objects are mapped to Exam entities and stored in the target database.
    - Manual triggering of the synchronization feature is achieved via sending a request to the `POST /api/target/exam/sync` endpoint.
    - To ensure fault tolerance, exceptions are caught and summarized in a data transfer report, which is provided in the responsebody upon completion of the exam synchronization process.

2. **Student Module Averages**
    - Fetches the average results of a student's most recent exams per module.
    - Endpoint: `GET /api/target/codecooler/averages/{studentId}`

3. **Mentor Statistics**
    - Provides statistics on the strictness of mentors, specifically the number of passes and fails they give on exams on each attempt.
    - Endpoint: `GET /api/target/codecooler/mentorstats/{mentorId}`

4. **Data Initialization**
    - The application initializes sample data (sources, exam results, students, mentors) during setup.

## Technology Stack

- **Spring Boot**: A powerful framework for building Java applications quickly and efficiently. It provides built-in features such as dependency injection, configuration management, and support for RESTful APIs, enabling rapid development and deployment of microservices.

- **Java 21**: The application is developed using Java 21, which offers new features and enhancements, ensuring improved performance, security, and modern programming paradigms.

- **PostgreSQL**: An advanced open-source relational database management system used for both the source and target databases. It provides robust features like ACID compliance, complex queries, and support for JSON data types, making it suitable for handling structured and semi-structured data.

- **JPA (Java Persistence API)**: A specification for accessing, persisting, and managing data between Java objects and relational databases. JPA is used in this application through Spring Data JPA, which simplifies database interactions and allows for the use of repositories to manage data entities.

- **Maven**: A build automation tool that manages project dependencies and builds the application using the defined lifecycle, ensuring that the application is packaged and ready for deployment.

- **Jackson**: A library used for processing JSON data, providing powerful data-binding capabilities to convert Java objects to and from JSON.