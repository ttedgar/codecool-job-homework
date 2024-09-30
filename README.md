# Exam Management Application

This is a Spring Boot application built with Java that handles data synchronization and analysis for Codecool exam results. It interacts with two PostgreSQL databases: **source** and **target**.

## Features

1. **Data Synchronization**
    - The application reads exam result data stored as JSON strings in the source database.
    - These JSON objects are mapped to Exam entities and stored in the target database.
    - Manual triggering of the synchronization feature is achieved via sending a request to the `POST /api/target/exam/sync` endpoint.
    - To ensure fault tolerance, exceptions are handled and summarized in a data transfer report, which is provided in the response upon completion of the exam synchronization process.

2. **Student Module Averages**
    - Fetches the average results of a student's most recent exams per module.
    - Endpoint: `GET /api/target/codecooler/averages/{studentId}`

3. **Mentor Statistics**
    - Provides statistics on the strictness of mentors, specifically the number of passes and fails they give on exams on each attempt.
    - Endpoint: `GET /api/target/codecooler/mentorstats/{mentorId}`

4. **Data Initialization**
    - The application initializes sample data (sources, exam results, students, mentors) during setup.

## Technology Stack

- Spring Boot for building the application.
- Java as the programming language.
- PostgreSQL for both the source and target databases.
- JPA for interacting with the databases.