# SAV Management API - TIPSI TT
This is a simple API for managing SAV (Service Après-Vente) for TIPSI TT.

## Running the Project

### Prerequisites

| Tool               | Version  | Link                               |
| :----------------- | :------- | :--------------------------------- |
| **Java JDK**       | 17 or 21 | [Java](https://www.oracle.com/fr/java/technologies/downloads/#java21)  |
| **Docker**         | 20.10+   | [Docker](https://www.docker.com/)  |
| **Docker Compose** | 2.0+     | Included with Docker        |

### Environnment
This is the versions of java, mvn and quarkus used in this project

```
➜ mvn -v
Apache Maven 3.8.7
```
```
➜ java --version
openjdk 21.0.10 2026-01-20
```
```
➜ quarkus --version
3.31.4
```

### 🚀 Installation and Launch

#### 1. Clone the Repository

```bash
git clone https://github.com/imadbourouche/TIPSI-TT
cd TIPSI-TT
```

#### 2. Ways to Start the Project

The project can be started in multiple ways depending on your workflow. You **do not need to run all steps in sequence**; choose the approach that suits you:

* **Full Docker Compose** – use multi-stage build to build the project and start all services (PostgreSQL + api) in one command:

    ```bash
    docker compose up --build -d
    ```


* **Using Docker images from Docker Hub** 
You can directly pull the image of the api and postgres from docker hub:

    ```bash
    docker pull imadpyth/tipsi-tt-api:latest
    docker pull postgres:18
    ```
    and after run 

    ```bash
    docker network create tipsi-net

    docker run -d \
    --name sav_postgres \
    --network tipsi-net \
    -e POSTGRES_DB=sav \
    -e POSTGRES_USER=savuser \
    -e POSTGRES_PASSWORD=savpass \
    -p 5432:5432 \
    postgres:18

    docker run -d \
    --name sav_api \
    --network tipsi-net \
    -p 8080:8080 \
    -e QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://sav_postgres:5432/sav \
    -e QUARKUS_DATASOURCE_USERNAME=savuser \
    -e QUARKUS_DATASOURCE_PASSWORD=savpass \
    -e QUARKUS_FLYWAY_MIGRATE_AT_START=true \
    -e QUARKUS_HIBERNATE_ORM_DATABASE_GENERATION=validate \
    imadpyth/tipsi-tt-api:latest
    ```

* **Development Mode with Maven** – start only the application while using a running PostgreSQL container:

    ```bash
    docker compose up postgres -d

    ./mvnw quarkus:dev
    ```

> Accéder à l'API et à la documentation Swagger UI à l'adresse suivante :
>    - **API REST**: `http://localhost:8080`
>    - **Swagger UI**: `http://localhost:8080/q/swagger-ui`

#### 3. Testing

run automated tests without starting the app manually:

```bash
./mvnw clean verify
```

## ✨ System Requirements

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

#### 3. Filtering

* The system should be able to filter interactions by client.
* The system should be able to filter interactions by type (CALL, EMAIL, MEETING, OTHER).
* The system should be able to filter interactions by commercial.
* The system should be able to filter interactions by date range.
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

## 💾 Data Model

### Table : `clients`
| Column | Type | Constraint | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PK, Auto-incr | Identifiant unique |
| `name` | VARCHAR | NOT NULL | Nom de l'entreprise |
| `sector` | VARCHAR | NOT NULL | Secteur d'activité |
| `created_at` | TIMESTAMP | DEFAULT NOW | Date de création |
| `deleted_at` | TIMESTAMP | NULL | Soft Delete (NULL = actif) |

### Table : `interactions`
| Column | Type | Constraint | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PK, Auto-incr | Identifiant unique |
| `client_id` | BIGINT | FK → clients.id | Lien vers le client |
| `commercial` | VARCHAR | NOT NULL | Commercial responsable |
| `type` | VARCHAR | NOT NULL | CALL, EMAIL, MEETING, OTHER |
| `summary` | TEXT | NOT NULL | Résumé de l'échange |
| `occurred_at` | TIMESTAMP | NOT NULL | Date de l'interaction |
| `duration` | INT | NULL | Durée en minutes |
| `created_at` | TIMESTAMP | DEFAULT NOW | Date d'enregistrement |

## 🔌 API REST - Endpoints

### Client Management

| Method | Endpoint | Description | Status |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/clients` | Create a client | `201 Created` |
| `GET` | `/api/clients` | List clients | `200 OK` |
| `GET` | `/api/clients/{id}` | Get a client | `200 OK` |
| `DELETE` | `/api/clients/{id}` | Delete a client | `204 No Content` |
| `GET` | `/api/clients/{id}/interactions` | client history| `200 OK` |
| `GET` | `/api/clients/{id}/stats` | client statistics | `200 OK` |

### Interaction Management

| Method | Endpoint | Description | Status |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/interactions` | Create an interaction | `201 Created` |
| `GET` | `/api/interactions` | Search (filters) | `200 OK` |
| `GET` | `/api/interactions/{id}` | Get an interaction | `200 OK` |
| `PUT` | `/api/interactions/{id}` | Update an interaction | `200 OK` |
| `DELETE` | `/api/interactions/{id}` | Delete an interaction | `204 No Content` |

### Filter Parameters (`GET /api/interactions`)

| Parameter | Type | Description |
| :--- | :--- | :--- |
| `client_id` | Long | Filter by client_id |
| `type` | String | Filter by type (CALL, EMAIL...) |
| `commercial` | String | Filter by commercial |
| `from` | Date | Start date (ISO 8601) |
| `to` | Date | End date (ISO 8601) |


## Technical Choices

### 1. Architecture

The project uses a classic N-tier architecture (Resource -> Service -> Entity/Repository) for a clear separation of responsibilities, with DTOs managed via Mappers.

### 2. Backend Framework (Quarkus)

Stack used by the team (mentioned by the CTO), its fast startup time and its cloud-native ecosystem (Hibernate ORM with Panache, RestEasy Reactive, etc.), facilitating integration tests with H2 and rapid development.

### 3. Validation

Use of `Hibernate Validator` at the DTO level to ensure that "all incoming data is validated before persistence" in a declarative manner.

### 4. Date Management

The use of `@PastOrPresent` ensures that "future interactions cannot be recorded".

### 5. In-memory database for tests

H2 is used during integration tests (files `*IT.java` or with the Quarkus test profile) to have a fast and reproducible ephemeral environment, without requiring Docker to be started. In local production (dev or prod mode), PostgreSQL is used.

### 6. Migrations BDD

Integration of `Flyway` to manage the evolution of the database structure in a deterministic manner, by preparing scripts for table creation (`V1.0.0`) and the insertion of basic data (`V1.0.1`).

### 7. Global Exception Handling

The mapping of application errors (`BadRequestException`, `NotFoundException`) and `ConstraintViolationException` is done via a `GlobalExceptionMapper` which always returns a consistent JSON format for the consumer of the REST API.

### 8. Soft Deletion

To respect the constraint "The deletion of a client must not lead to the loss of the history", a "Soft Delete" approach has been implemented with the `deleted_at` column. The client is never physically deleted from the database.


## 📈 Scaling Analysis for TIPSI numbers

To validate the sustainability of our solution, we project data volumes based on TIPSI’s current organization:

* **Client Portfolio**: 3500 merchants (B2B companies) to be managed.
* **Users (Internal Staff)**: A dedicated internal team of 5 members.
* **Data Volume**: With regular follow-ups, the `interactions` table will quickly become the heart of the system. If each merchant averages 5 interactions per month (calls, emails, notes), we reach:
    * 17500 interactions per month.
    * 210,000 new interactions per year.
    * Over 1 million rows in less than 5 years.


* **Business Constraint**: The tool must be ultra-responsive to ensure the internal team remains efficient in their daily workflow.

### Technical choices for Scalability

The chosen architecture perfectly meets the needs of a high-growth internal tool:

* **PostgreSQL**: Excellent for guaranteeing data integrity (ACID). With 3500 merchants, the database remains highly performant. The primary challenge will be optimized text searching within interaction notes.
* **Quarkus**: Its lightweight footprint allows the API to be deployed on modest infrastructure while handling connection spikes (e.g., morning login rushes) with zero latency.

### Is our current code suited for internal use?

Yes for current volumes, but adjustments are needed for user experience.

#### 1. Mandatory Pagination (Priority 1)

* Implement pagination for endpoints listing clients or interactions (`listClients` and `listInteractions`).

#### 2. SQL Indexing (Priority 1)

* Add natural SQL indexes on foreign keys, specifically the `client_id` column in the `interactions` table.
* Implement simple indexes on columns frequently used for filtering, such as `commercial`, `type`, `from`, and `to`.

#### 3. Statistical Calculation Optimization (Priority 2)

* Currently, billing statistics (`totalDuration`, `breakdownByType`) are calculated within the API using Java logic (ORM Mapping -> Stream API -> Summation). For large data volumes, it is imperative to delegate these calculations to the database using SQL aggregation queries (`SUM()`, `COUNT()`, `GROUP BY`).

#### 4. Caching (Priority 3)

* Introduce a local or distributed caching system (via the Quarkus Cache extension or Redis) for static routes or analytical dashboards. This allows staff to view daily metrics without triggering new database queries unless the underlying data has changed, significantly reducing the load on PostgreSQL.
