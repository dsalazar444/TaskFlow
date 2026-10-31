# TaskFlow – Task Management API

A RESTful API for task management developed with Java and Spring Boot. The project demonstrates backend development fundamentals, layered architecture, relational database integration, CRUD operations, and unit testing.

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 | Programming language |
| Spring Boot 4.1.2 | Backend framework |
| Spring Web MVC | REST API development |
| Spring Data JPA | Data persistence and repository abstraction |
| Hibernate | ORM (Object-Relational Mapping) |
| PostgreSQL | Relational database |
| Maven | Dependency management and build automation |
| JUnit 5 | Unit testing framework |
| Mockito | Mocking dependencies and verifying interactions |
| AssertJ | Fluent assertions |

## Features

- Create tasks.
- Retrieve all tasks.
- Retrieve a task by its ID.
- Update existing tasks.
- Delete tasks.
- Validate incoming request data.
- Handle resource-not-found exceptions.

### Task Model

Each task contains the following attributes:

| Attribute | Type | Description |
|---|---|---|
| id | Long | Unique identifier, automatically generated. |
| title | String | Task title. |
| description | String | Task description. |
| status | TaskStatus | Current task status. |
| dueDate | LocalDate | Task deadline. |

Available task statuses:

- `PENDING`
- `IN_PROGRESS`
- `COMPLETED`

## Architecture

The project follows a layered architecture to separate responsibilities and improve maintainability.

```text
Controller
    |
    v
Service
    |
    v
Repository
    |
    v
PostgreSQL
```

- **Controller:** Handles HTTP requests and responses.
- **Service:** Contains business logic and coordinates operations.
- **Repository:** Handles data access through Spring Data JPA.
- **Model:** Defines the Task entity and its persistence mapping.

## API Endpoints

Base URL: `/api/tasks`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/tasks` | Create a task. |
| GET | `/api/tasks` | Retrieve all tasks. |
| GET | `/api/tasks/{id}` | Retrieve a task by ID. |
| PUT | `/api/tasks/{id}` | Update an existing task. |
| DELETE | `/api/tasks/{id}` | Delete a task. |

## Getting Started

### Prerequisites

- Java 17 or higher.
- PostgreSQL.
- Maven (or use the included Maven Wrapper).

### 1. Clone the repository

```bash
git clone <repository-url>
cd TaskFlow
```

### 2. Configure environment variables

Copy the `.env.example` into an `.env` file:

```bash
cp .env.example .env
```

Configure the following environment variables:

```env
DB_NAME=your_db_name
DB_USERNAME=your_username
DB_PASSWORD=your_password
```

and then export them (in Ubuntu):

```bash
set -a
source .env
set +a

```
Make sure PostgreSQL is running and the database exists.

### 3. Run the application

Using the Maven Wrapper:

```bash
./mvnw spring-boot:run
```

The application will start on:

```text
http://localhost:8080
```

#### Using the Task API

The following examples demonstrate how to interact with the Task API using `curl`.

**1. Create a task**

```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Learn Spring Boot",
    "description": "Study REST APIs and unit testing with JUnit and Mockito",
    "dueDate": "2026-11-15"
  }'
```

**2. Get all tasks**

```bash
curl -X GET http://localhost:8080/api/tasks
```

**3. Get a task by ID**

```bash
curl -X GET http://localhost:8080/api/tasks/1
```

**4. Update a task**

```bash
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Learn Spring Boot and Mockito",
    "description": "Practice unit testing with JUnit 5 and Mockito",
    "dueDate": "2026-12-01",
    "status": "IN_PROGRESS"
  }'
```

Available task statuses:

- `PENDING`
- `IN_PROGRESS`
- `COMPLETED`

**5. Delete a task**

```bash
curl -X DELETE http://localhost:8080/api/tasks/1
```

The API returns `204 No Content` when the delete request is processed successfully. Deleting a task that does not exist is also supported and does not raise an error.

## Testing

The project uses **JUnit 5 and Mockito** to implement unit tests for the service layer.

The tests follow the Arrange-Act-Assert (AAA) pattern:

- **Arrange:** Set up test data and mock behavior.
- **Act:** Execute the method being tested.
- **Assert:** Verify the expected result.

### Testing Tools

| Tool | Usage |
|---|---|
| JUnit 5 | Organizes and executes unit tests. |
| Mockito | Mocks the repository dependency and controls its behavior. |
| MockitoExtension | Integrates Mockito with JUnit 5. |
| ArgumentCaptor | Captures arguments passed to mocked methods. |
| AssertJ | Provides readable assertions. |
| verify() | Verifies interactions with mocked dependencies. |

### Unit Test Cases

| Método | Escenario | Resultado esperado |
|---|---|---|
| `createTask()` | Create a task with valid data. | The task is saved and returned with its expected attributes. |
| `getAllTasks()` | Tasks exist in the database. | Returns the list of existing tasks. |
| `getAllTasks()` | No tasks exist. | Returns an empty list. |
| `getTaskById()` | A task with the requested ID exists. | Returns the corresponding task. |
| `getTaskById()` | No task exists with the requested ID. | Throws `ResourceNotFoundException`. |
| `updateTask()` | Update an existing task with valid data. | Updates the task and returns the modified entity. |
| `updateTask()` | Attempt to update a nonexistent task. | Throws `ResourceNotFoundException`. |
| `deleteTask()` | Delete an existing task. | The repository's delete operation is executed. |
| `deleteTask()` | Attempt to delete a nonexistent task. | No exception is thrown; no deletion is performed. |

**Testing approach:** Repository dependencies are mocked using Mockito, allowing service-layer tests to execute without connecting to PostgreSQL. This isolates business logic and verifies interactions independently of the database.

### Run tests

Execute all tests:

```bash
./mvnw test
```

Execute only the service tests:

```bash
./mvnw -Dtest=TaskServiceTest test
```

## Project Goals

This project was developed to strengthen practical knowledge of:

- Object-Oriented Programming with Java.
- Spring Boot application development.
- RESTful API design.
- Relational database integration with PostgreSQL.
- ORM and persistence using Hibernate and Spring Data JPA.
- Layered architecture and separation of concerns.
- Unit testing with JUnit 5 and Mockito.
- Dependency mocking and interaction verification.