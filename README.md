# SAV Management API - TIPSI TT

## System Requirements

### Functional Requirements

#### 1. Client Management

* The system should be able to create a client.
* The system should be able to update a client.
* The system should be able to delete a client.
* The system should be able to list all clients.
* The system should be able to get a client by ID.

#### 2. Interaction Management

* The system should be able to create an interaction.
* The system should be able to update an interaction.
* The system should be able to delete an interaction.
* The system should be able to list all interactions.
* The system should be able to get an interaction by ID.

#### 3. Advanced Search

* The system should be able to search interactions by client.
* The system should be able to search interactions by type (CALL, EMAIL, MEETING, OTHER).
* The system should be able to search interactions by commercial.
* The system should be able to search interactions by date range.
* The system should be able to combine filters.

#### 4. Statistics

* The system should be able to get the total number of interactions per client.
* The system should be able to get the total number of interactions per type.
* The system should be able to get the total duration of interactions.

### Non-Functional Requirements

* **Referential Integrity**: An interaction must always be associated with an existing client.
* **Temporal Validation**: Future interactions cannot be recorded (date ≤ now).
* **Persistent History**: Deleting a client does not remove historical interactions.
* **Input Validation**: All incoming data is validated before persistence.
* **REST Client APIs**: The app should be able to use REST Client APIs

## Running the Project

### Prerequisites

| Tool               | Version  | Link                               |
| :----------------- | :------- | :--------------------------------- |
| **Java JDK**       | 17 or 21 | [Adoptium](https://adoptium.net/)  |
| **Maven**          | 3.8+     | [Maven](https://maven.apache.org/) |
| **Docker**         | 20.10+   | [Docker](https://www.docker.com/)  |
| **Docker Compose** | 2.0+     | Included with Docker Desktop       |

---

## 🚀 Installation and Launch

### 1. Clone the Repository

```bash
git clone https://github.com/imadbourouche/TIPSI-TT
cd TIPSI-TT
```

### 2. Ways to Start the Project

The project can be started in multiple ways depending on your workflow. You **do not need to run all steps in sequence**; choose the approach that suits you:

* **Full Docker Compose** – start all services (PostgreSQL + app) in one command:

```bash
docker-compose up --build -d
```

* **Development Mode with Maven** – start only the application while using a running PostgreSQL container:

```bash
docker-compose up postgres -d

./mvnw quarkus:dev
```

> Accéder à l'API et à la documentation Swagger UI à l'adresse suivante :
>    - **API REST**: `http://localhost:8080`
>    - **Swagger UI**: `http://localhost:8080/q/swagger-ui`
### 3. Testing

run automated tests without starting the app manually:

```bash
./mvnw clean verify
```