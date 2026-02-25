# SAV Management API - TIPSI TT
This is a simple API for managing SAV (Service Après-Vente) for TIPSI TT.

## Running the Project

### Prerequisites

| Tool               | Version  | Link                               |
| :----------------- | :------- | :--------------------------------- |
| **Java JDK**       | 17 or 21 | [Java](https://www.oracle.com/fr/java/technologies/downloads/#java21)  |
| **Docker**         | 20.10+   | [Docker](https://www.docker.com/)  |
| **Docker Compose** | 2.0+     | Included with Docker        |

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

## Modélisation des Données

### Table : `clients`
| Colonne | Type | Contrainte | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PK, Auto-incr | Identifiant unique |
| `name` | VARCHAR | NOT NULL | Nom de l'entreprise |
| `sector` | VARCHAR | NOT NULL | Secteur d'activité |
| `created_at` | TIMESTAMP | DEFAULT NOW | Date de création |
| `deleted_at` | TIMESTAMP | NULL | Soft Delete (NULL = actif) |

### Table : `interactions`
| Colonne | Type | Contrainte | Description |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PK, Auto-incr | Identifiant unique |
| `client_id` | BIGINT | FK → clients.id | Lien vers le client |
| `user_id` | BIGINT | FK → users.id | Commercial responsable |
| `type` | VARCHAR | NOT NULL | CALL, EMAIL, MEETING, OTHER |
| `summary` | TEXT | NOT NULL | Résumé de l'échange |
| `occurred_at` | TIMESTAMP | NOT NULL | Date de l'interaction |
| `duration` | INT | NULL | Durée en minutes |
| `created_at` | TIMESTAMP | DEFAULT NOW | Date d'enregistrement |

## API REST - Endpoints

### Gestion des Clients

| Méthode | Endpoint | Description | Status |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/clients` | Créer un client | `201 Created` |
| `GET` | `/api/clients` | Lister les clients | `200 OK` |
| `GET` | `/api/clients/{id}` | Consulter un client | `200 OK` |
| `DELETE` | `/api/clients/{id}` | Supprimer (Soft) | `204 No Content` |
| `GET` | `/api/clients/{id}/interactions` | client history| `200 OK` |
| `GET` | `/api/clients/{id}/stats` | Statistiques client | `200 OK` |

### Gestion des Interactions

| Méthode | Endpoint | Description | Status |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/interactions` | Créer une interaction | `201 Created` |
| `GET` | `/api/interactions` | Rechercher (filtres) | `200 OK` |
| `GET` | `/api/interactions/{id}` | Consulter une interaction | `200 OK` |
| `PUT` | `/api/interactions/{id}` | Modifier une interaction | `200 OK` |
| `DELETE` | `/api/interactions/{id}` | Supprimer une interaction | `204 No Content` |

### Paramètres de Recherche (`GET /api/interactions`)

| Paramètre | Type | Description |
| :--- | :--- | :--- |
| `client_id` | Long | Filtrer par client_id |
| `type` | String | Filtrer par type (CALL, EMAIL...) |
| `user_id` | Long | Filtrer par commercial |
| `from` | Date | Date de début (ISO 8601) |
| `to` | Date | Date de fin (ISO 8601) |



## Choix Techniques

### 1. Architecture

Le projet adopte une architecture classique en N-tiers (Resource -> Service -> Entity/Repository) pour une séparation claire des responsabilités, avec les DTOs gérés via des Mappers.

### 2. Framework Backend (Quarkus)

Stack utilisée par l'équipe (mentionnée par le CTO), sa rapidité de démarrage et son écosystème cloud-native (Hibernate ORM with Panache, RestEasy Reactive, etc.), facilitant l'intégration des tests avec H2 et un développement rapide.

### 3. Contrôle et Validation

Utilisation intensive de `Hibernate Validator` au niveau des DTOs pour garantir que "*toutes les données entrantes soient validées avant persistance*" de manière déclarative.

### 4. Gestion des Dates

L'utilisation de `@PastOrPresent` garantit qu'une "*interaction future ne peut pas être enregistrée*".

### 5. Base de données in-memory pour les tests

H2 est utilisé lors des tests d'intégration (fichiers `*IT.java` ou avec le profil test Quarkus) pour disposer d'un environnement éphémère reproductible et rapide, ne nécessitant pas le démarrage de Docker. En production locale (mode dev ou prod), PostgreSQL est utilisé.

### 6. Migrations BDD

Intégration de `Flyway` pour gérer l'évolution de la structure de base de données de manière déterministe, en préparant les scripts pour la création de tables (`V1.0.0`) et l'insertion des données de base (`V1.0.1`).

### 7. Gestion globale des Exceptions

Le mappage des erreurs d'application (`BadRequestException`, `NotFoundException`) et `ConstraintViolationException` se fait via un `GlobalExceptionMapper` qui renvoie toujours un format JSON cohérent pour le consommateur de l'API REST.

### 8. Soft Deletion

Pour respecter la contrainte "*La suppression d’un client ne doit pas entraîner la perte de l’historique*", une approche de "Soft Delete" a été implémentée avec la colonne `deleted_at`. Le client n'est jamais supprimé physiquement de la base de données.