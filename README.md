## HMCTS Dev Challenge Backend Submission

This is the backend API for the simple task listing app. It provides the frontend access to database CRUD operations.

## Running the application

Requirements:
- Java 17+
- Maven

Run the application with:
```bash
mvn spring-boot:run
```

It will be available at http://localhost:8080/

Run the test suite with:
```bash
mvn test
```

API Endpoints:

Base URL: /api/tasks

POST 
    - creates a new task entry in the database

GET /{id}
    - gets the task from the database with the corresponding ID

GET 
    - returns a List of all tasks

PATCH /{id}/status
    - updates the status property of the task with corresponding ID

PUT /{id}
    - updates all task content

DELETE /{id}
    - removes the corresponding task from the DB

## Error handling

404 Not Found is returned when a task does not exist

400 Bad Request is returned for invalid request data

