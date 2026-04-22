# Smart Campus Sensor & Room Management API

**Module:** 5COSC022W Client-Server Architectures  
**University:** University of Westminster  
**Assignment:** Coursework – REST API Design, Development, and Implementation  

---

## Table of Contents

1. [API Overview](#api-overview)
2. [Project Structure](#project-structure)
3. [How to Build and Run](#how-to-build-and-run)
4. [API Endpoints Reference](#api-endpoints-reference)
5. [Sample curl Commands](#sample-curl-commands)
6. [Report – Conceptual Questions](#report--conceptual-questions)

---

## API Overview

This project implements a RESTful API for the University's **Smart Campus** initiative.
The system manages physical **Rooms** and the **Sensors** deployed within them. It also maintains a
historical log of **Sensor Readings**.

The API is built using:
- **JAX-RS** (Java API for RESTful Web Services)
- **Jersey 2.32** (JAX-RS implementation)
- **Jackson** (JSON serialisation/deserialisation)
- **Apache Tomcat 9** (Servlet container)
- **Maven** (Dependency and build management)
- **NetBeans** (IDE)

All data is stored **in-memory** using Java `HashMap` and `List` structures.
The API base path is `/api/v1`.

### Core Resources

| Resource | Description |
|---|---|
| Room | A physical campus room with an ID, name, capacity and list of assigned sensor IDs |
| Sensor | An IoT sensor assigned to a room with a type, status and current value |
| SensorReading | A historical reading record with a UUID, epoch timestamp and measured value |

---

## Project Structure

    SmartCampusAPI/
    |   ├── src/main/java/com/smartcampus/
    |   ├── ApplicationConfig.java     Entry point — @ApplicationPath("/api/v1")
    │   ├── model/
    │   │   ├── Room.java
    │   │   ├── Sensor.java
    │   │   ├── SensorReading.java
    │   │   └── ErrorResponse.java          Standardised JSON error body
    │   ├── resource/
    │   │   ├── DiscoveryResource.java      GET /api/v1
    │   │   ├── RoomResource.java           GET, POST, DELETE /api/v1/rooms
    │   │   ├── SensorResource.java         GET, POST /api/v1/sensors + sub-resource locator
    │   │   └── SensorReadingResource.java  GET, POST /api/v1/sensors/{id}/readings
    │   ├── storage/
    │   │   └── DataStore.java              In-memory data store (static HashMaps)
    │   └── exception/
    │       ├── RoomNotEmptyException.java
    │       ├── RoomNotEmptyExceptionMapper.java        409 Conflict
    │       ├── RoomNotFoundException.java
    │       ├── RoomNotFoundExceptionMapper.java        404 Not Found
    │       ├── LinkedResourceNotFoundException.java
    │       ├── LinkedResourceNotFoundExceptionMapper.java  422 Unprocessable Entity
    │       ├── SensorNotFoundException.java
    │       ├── SensorNotFoundExceptionMapper.java      404 Not Found
    │       ├── SensorUnavailableException.java
    │       ├── SensorUnavailableExceptionMapper.java   403 Forbidden
    │       └── GlobalExceptionMapper.java              500 Internal Server Error
    └── pom.xml

---

## How to Build and Run

### Prerequisites

- Java JDK 8 or above
- Apache Maven 3.x
- Apache Tomcat 9.x
- NetBeans IDE 18

### Step 1 — Clone the Repository

```bash
git clone https://github.com/OshadhiRanawaka/SmartCampusAPI.git
```

### Step 2 — Open in NetBeans

1. Open NetBeans
2. Go to **File → Open Project**
3. Navigate to the cloned `SmartCampusAPI` folder and click **Open Project**

### Step 3 — Set Up Apache Tomcat

If you have not already added Tomcat to NetBeans:

1. Go to the **Services** tab
2. Right-click **Servers → Add Server**
3. Select **Apache Tomcat or TomEE**
4. Browse to your extracted Tomcat folder and click **Finish**

### Step 4 — Build the Project

Right-click the project in NetBeans → **Clean and Build**

You should see `BUILD SUCCESS` in the Output window.

### Step 5 — Run the Project

Right-click the project → **Run**

Tomcat will start and deploy the application.

### Step 6 — Access the API

Open Postman or a browser and navigate to:

    http://localhost:8080/SmartCampusAPI/api/v1

You should receive a JSON discovery response confirming the API is running.

---

## API Endpoints Reference

### Discovery

| Method | Endpoint | Description | Success Code |
|---|---|---|---|
| GET | `/api/v1` | API metadata and resource links | 200 |

### Rooms

| Method | Endpoint | Description | Success Code |
|---|---|---|---|
| GET | `/api/v1/rooms` | Get all rooms | 200 |
| POST | `/api/v1/rooms` | Create a new room | 201 |
| GET | `/api/v1/rooms/{roomId}` | Get a specific room | 200 |
| DELETE | `/api/v1/rooms/{roomId}` | Delete a room (blocked if sensors present) | 200 |

### Sensors

| Method | Endpoint | Description | Success Code |
|---|---|---|---|
| GET | `/api/v1/sensors` | Get all sensors | 200 |
| GET | `/api/v1/sensors?type=CO2` | Get sensors filtered by type | 200 |
| POST | `/api/v1/sensors` | Register a new sensor (validates roomId) | 201 |
| GET | `/api/v1/sensors/{sensorId}` | Get a specific sensor | 200 |

### Sensor Readings (Sub-Resource)

| Method | Endpoint | Description | Success Code |
|---|---|---|---|
| GET | `/api/v1/sensors/{sensorId}/readings` | Get full reading history | 200 |
| POST | `/api/v1/sensors/{sensorId}/readings` | Add a reading + update currentValue | 201 |

### Error Responses

| HTTP Code | Scenario |
|---|---|
| 400 | Missing or invalid request fields |
| 403 | Posting a reading to a MAINTENANCE or OFFLINE sensor |
| 404 | Room or sensor ID not found |
| 409 | Deleting a room that still has sensors |
| 422 | Creating a sensor with a roomId that does not exist |
| 500 | Any unexpected server error |

---

## Sample curl Commands

> **Note:** Run these in order as some depend on data created by earlier commands.
> Make sure the server is running before executing.

### curl 1 — GET API Discovery

```bash
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1 \
  -H "Accept: application/json"
```

**Expected:** `200 OK` with API name, version, contact, and resource links.

---

### curl 2 — POST Create a New Room

```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "id": "ENG-101",
    "name": "Engineering Workshop",
    "capacity": 40
  }'
```

**Expected:** `201 Created` with the new room object echoed back.

---

### curl 3 — POST Create a New Sensor (linked to ENG-101)

```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "id": "TEMP-002",
    "type": "Temperature",
    "status": "ACTIVE",
    "currentValue": 0.0,
    "roomId": "ENG-101"
  }'
```

**Expected:** `201 Created` with the new sensor object.

---

### curl 4 — POST Add a Reading and Update currentValue

```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors/TEMP-002/readings \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "value": 23.8
  }'
```

**Expected:** `201 Created` with reading details and `updatedCurrentValue: 23.8`.

---

### curl 5 — DELETE Room That Has Sensors — 409 Error

```bash
curl -X DELETE http://localhost:8080/SmartCampusAPI/api/v1/rooms/LIB-301 \
  -H "Accept: application/json"
```

**Expected:** `409 Conflict` with JSON error explaining sensors are still assigned.

---

### curl 6 — POST Reading to MAINTENANCE Sensor — 403 Error

```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors/OCC-001/readings \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "value": 12.0
  }'
```

**Expected:** `403 Forbidden` with JSON error explaining the sensor is in MAINTENANCE.

---

### curl 7 — GET Room That Does Not Exist — 404 Error

```bash
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/rooms/FAKE-999 \
  -H "Accept: application/json"
```

**Expected:** `404 Not Found` with JSON error message.

---

## Report – Answers to Conceptual Questions in each Part

---

### Part 1 — Q1: JAX-RS Resource Class Lifecycle

By default, JAX-RS creates a **new instance** of a resource class for each incoming HTTP request. 
This is known as **per-request scope**. Each request receives its own new object, which is discarded 
once the response is sent.

This has an important impact on in-memory data storage. If data is kept as instance variables in a 
resource class, each request will encounter an empty dataset because every request creates a new object. 
To fix this, shared data should be stored in **static fields**. This way, the data belongs to the class itself, 
allowing all requests to access the same single copy.

In this project, `DataStore.java` uses `public static final Map<>` fields for
rooms, sensors, and readings. Because these are static, they persist across all
requests and all resource instances throughout the server's lifetime.

For thread safety in a real concurrent environment, these maps should be
`ConcurrentHashMap` instead of `HashMap` to prevent race conditions where two
simultaneous requests could corrupt the data structure. For this coursework, the
in-memory approach with static maps is appropriate.

---

### Part 1 — Q2: HATEOAS and Hypermedia

**HATEOAS** (Hypermedia as the Engine of Application State) means that API responses contain links
to related resources and possible actions, not just raw data. 
This helps make RESTful APIs easier to use and understand, as they explain what you can do next.

This approach helps client developers in a few key ways. They don't need to hardcode URLs—if the 
server changes a path, clients get the new links automatically. New developers can learn the API by 
following links in responses, instead of using outdated documentation. It also makes the client and server 
less dependent on each other, since the client uses provided links rather than fixed URL patterns. 

In this project, the `GET /api/v1` discovery endpoint shows a basic example of HATEOAS by returning a 
map of resource names and their URLs.

---

### Part 2 — Q1: Returning Full Objects vs IDs in a Room List

Returning only IDs  produces a very lightweight response and minimises bandwidth — useful when there are hundreds
of rooms. However, it forces the client to make a separate `GET /rooms/{id}` request for every room it wants details about. 
This is known as the **N+1 problem** and results in a large number of round-trip requests, which increases total latency and server load.

Returning complete room objects in the list is more convenient for clients, reduces the number of API calls, and simplifies implementation on the client side. 
The downside is that it results in a larger response size. However, since rooms have only a few fields in the Smart Campus system, the payload isn't too big, 
making full objects the better option. If the dataset were larger, we could use pagination to limit the size of each response while still providing full objects for each page.

---

### Part 2 — Q2: Is DELETE Idempotent?

DELETE is **not fully idempotent** in this implementation. Calling
`DELETE /api/v1/rooms/{roomId}` the first time successfully removes the room
and returns `200 OK`. Calling the exact same request a second time returns
`404 Not Found` because the room no longer exists.

In theory, a truly idempotent DELETE request would give the same response every time, meaning a second delete wouldn't
result in an error. However, it's common in RESTful APIs to return a `404` error when trying to delete something that no longer exists.
This approach helps the client understand the current status of the resource.

---

### Part 3 — Q1: Technical Consequences of @Consumes Mismatch

When a client sends data with `Content-Type: text/plain` or `Content-Type: application/xml`, the JAX-RS runtime automatically rejects the request
with a **415 Unsupported Media Type** error. This means the Java method won't be executed. This is a significant benefit of using annotations 
because the framework prevents invalid content types, eliminating the need for extra validation code in each method.

---

### Part 3 — Q2: @QueryParam vs Path-Based Filtering

Using `@QueryParam` for filtering (e.g. `/api/v1/sensors?type=CO2`) is
superior to embedding the filter in the path (e.g. `/api/v1/sensors/type/CO2`)
for several reasons.

Query parameters are used to **filter, search, and sort** a collection of items. They don’t suggest that there is a permanent, identifiable resource at that URL. 
On the other hand, path parameters indicate that something like `/type/CO2` represents a specific resource with its own identity, which is not appropriate for filtering purposes. 

Query parameters are easy to combine. For example, you can use multiple filters like this: `/api/v1/sensors?type=CO2&status=ACTIVE`. 
In contrast, using path segments leads to a confusing structure: `/api/v1/sensors/type/CO2/status/ACTIVE`.

---

### Part 4 — Q1: Benefits of the Sub-Resource Locator Pattern

The Sub-Resource Locator pattern separates concerns by delegating nested
resource handling to dedicated classes rather than cramming all logic into
one controller.

In this project, `SensorResource` takes care of sensor operations, while `SensorReadingResource` manages all aspects related to readings separately. 
Each class has a clear, specific role, making the code easier to read, test, and maintain. If changes are needed for reading logic, like adding pagination
or authentication, only `SensorReadingResource` needs to be updated, ensuring that the sensor operations in `SensorResource` remain unaffected.

The pattern represents the domain hierarchy clearly. A reading is linked to a sensor, as shown in the URL `/sensors/{id}/readings`. 
This makes the API easy to understand and self-explanatory. As the API grows to include more nested paths, the locator pattern helps keep complexity 
low and the code modular, which would be difficult to manage in a single large resource class.

---

### Part 5 — Q1: Why HTTP 422 Over 404 for a Missing Referenced Resource

A `404 Not Found` error means the **requested URL** doesn't exist on the server. For example, if a client tries to POST a new sensor using a `roomId` that isn't valid, 
the endpoint `POST /api/v1/sensors` is still valid and reachable. The issue lies in the **request body**—specifically, the `roomId` points to a room that doesn't exist. 
In this case, the server should respond with a `422 Unprocessable Entity` error. This indicates that while the server understood the request and the JSON format is correct, 
the content doesn't meet business rules, so the server can't process it.

Using a 422 response provides the client with a clearer diagnostic signal: the URL and format are correct, but the data refers to something non-existent. This is more helpful than a 404 error, 
which might suggest the client used the wrong URL.

---

### Part 5 — Q2: Security Risks of Exposing Java Stack Traces

Exposing raw Java stack traces to external API consumers creates serious
security vulnerabilities.

**Internal architecture exposure:** Stack traces show complete package and class names, like `com.smartcampus.storage.DataStore`. This information can help attackers understand the app's structure and 
pinpoint vulnerable components.

**Exact failure points:** Line numbers in error messages indicate where the code has failed, making it easier for attackers to find specific inputs that can exploit vulnerabilities.

**Database schema leakage:** If a database error occurs, it may reveal sensitive information like connection strings, table names, column names, or parts of SQL queries, which can expose the database structure to attackers.

**Business logic inference:** Exception messages from service layers can reveal internal business rules, validation constraints or data flow that attackers
can be exploited to bypass security checks.

