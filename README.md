# Description

A Spring Boot REST application for managing operational incident such as situation reports, monitoring dashboards, log snippets, and root cause analyses.

The system supports polymorphic incident types, pagination, filtering, and secure access via HTTP Basic Authentication.

---

## Requirements

- **Java:** JDK 21
- **Build Tool:** Maven
- **Database:** H2 (embedded, in-memory)

---

## Running the Application

Clone the reposity using:

```bash
    -  git clone https://github.com/Asad0786/opsSRE.git 
    -  cd opsSRE
```

From the project root directory:

```bash
mvn spring-boot:run
```

Application starts on:

```
http://localhost:8084
```

---

## Running Tests

```bash
mvn test
```

---

## Database & Sample Data

### Embedded H2 Database

- In-memory database
- Resets on every restart

**H2 Console:**

```
http://localhost:8084/h2-console
```

Typical configuration:

- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `root`
- Password: _(empty)_

### Loading Initial Sample Data

Initial data is loaded automatically at startup via:

- a Spring `CommandLineRunner`

No manual intervention is required.

---

## Security

- **Authentication:** HTTP Basic Auth

### In-Memory Users

| Username | Password | Role  |
| -------- | -------- | ----- |
| admin    | admin    | ADMIN |
| user     | user     | USER  |

---

## API Base Path

```
/api
```

---

## API Endpoints

| Method | Endpoint         | Description                        |
| ------ | ---------------- | ---------------------------------- |
| POST   | `/incident`      | Create a new incident artifact     |
| PUT    | `/incident/{id}` | Update an existing incident        |
| GET    | `/incident/{id}` | Get incident by ID                 |
| GET    | `/incidents`     | Get paginated & filtered incidents |
| DELETE | `/incident/{id}` | Delete incident                    |

All endpoints are **secured** except GET METHODS.

---

## Incident Types

Incident creation uses **polymorphic JSON payloads** via `incidentType`.

Supported types:

- `SITUATION_REPORT`
- `MONITORING_DASHBOARD`
- `LOG_SNIPPET`
- `ROOT_CAUSE_ANALYSIS`

---

## Sample cURL Calls

### 1. Create Situation Report (Secured)

```bash
curl  -X POST \
  'http://localhost:8084/api/incident' \
  --header 'Accept: */*' \
  --header 'User-Agent: Thunder Client (https://www.thunderclient.com)' \
  --header 'Authorization: Basic dXNlcjp1c2Vy' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  "referenceCode": "1",
  "incidentId": "INC-1-15-12-2025",
  "incidentType": "SITUATION_REPORT",
  "statusMessage": "The current situation of this incident is very critical",
  "severity": "CRITICAL"
}'
```

---

### 2. Get All Incidents (Paginated)

```bash
curl  -X GET \
  'http://localhost:8084/api/incidents' \
  --header 'Accept: */*' \
  --header 'User-Agent: Thunder Client (https://www.thunderclient.com)' \
  --header 'Authorization: Basic dXNlcjp1c2Vy'
```

---

### 3. Get Incident by ID

```bash

curl  -X GET \
  'http://localhost:8084/api/incident/1' \
  --header 'Accept: */*' \
  --header 'User-Agent: Thunder Client (https://www.thunderclient.com)' \
  --header 'Authorization: Basic dXNlcjp1c2Vy'
```

---

### 4. Delete Incident (Admin)

```bash
curl  -X DELETE \
  'http://localhost:8084/api/incident/1' \
  --header 'Accept: */*' \
  --header 'User-Agent: Thunder Client (https://www.thunderclient.com)' \
  --header 'Authorization: Basic dXNlcjp1c2Vy'
```

---

## Sample Response

> #### 1.Create Incident:

```json
{
  "message": "Incident created successfully",
  "uri": "/api/incidents",
  "data": {
    "id": 3,
    "referenceCode": "1",
    "incidentId": "INC-1-15-12-2025",
    "createdDate": "2025-12-15",
    "incidentType": "SITUATION_REPORT",
    "statusMessage": "The current situation of this incident is very critical",
    "severity": "CRITICAL"
  },
  "errors": null,
  "statusCode": 201,
  "errorCode": 0
}
```

---

## Validation

> #### 1.Create Incident:
>
> request:

```json
{
  "referenceCode": "2",
  "incidentId": "INC-2-15-12-2025",
  "incidentType": "MONITORING_DASHBOARD",
  "graphUrl":"as1@#io/randomURL",
  "toolName":"Prometheis",
  "severity": "INFO"

}
```

response (400):

```json
{
  "message": "The request cannot be processed because of incorrect request data",
  "uri": "/api/incident",
  "data": null,
  "errors": "must be a valid URL",
  "statusCode": 400,
  "errorCode": -99999
}

```

> ###### Duplicate unique data -(referenceCode) (409)

```json
request body:
{
  "referenceCode": "2",
  "incidentId": "INC-2-15-12-2025",
  "incidentType": "MONITORING_DASHBOARD",
  "graphUrl":"https://www.graph.io/MonitorDahboard",
  "toolName":"Prometheis",
  "severity": "INFO"

}

response(409):
{
  "message": "An incident  with same reference codealready exists",
  "uri": "/api/incident",
  "data": null,
  "errors": null,
  "statusCode": 409,
  "errorCode": -9990
}
```

> ###### Forbidden (403)
>
> thrown only when an incident is created by admin and user trys to delete

```bash
curl  -X DELETE \
  'http://localhost:8084/api/incident/5' \
  --header 'Accept: */*' \
  --header 'User-Agent: Thunder Client (https://www.thunderclient.com)' \
  --header 'Authorization: Basic dXNlcjp1c2Vy'
```

```json
{
  "message": "You dont have enough permission to delete this incident",
  "uri": "/api/incident/5",
  "data": null,
  "errors": null,
  "statusCode": 403,
  "errorCode": -1
}
```

---

## Request Body

### 1. Create

The request body structure depends on the value of `incidentType`.

> ###### 1.1 Situation Report (SITUATION_REPORT)

```json
{
  "incidentType": "SITUATION_REPORT",
  "referenceCode": "SR-1001",
  "incidentId": "INC-2025-001",
  "statusMessage": "System outage observed in production cluster",
  "severity": "HIGH"
}
```

> ###### 1.2 MONITORING DASHBOARD (MONITORING_DASHBOARD)

```json
{
  "incidentType": "MONITORING_DASHBOARD",
  "referenceCode": "MD-2001",
  "incidentId": "INC-2025-002",
  "graphUrl": "https://monitoring.example.com/dashboard/cpu-usage",
  "toolName": "Grafana"
}

```

> ###### 1.3 LOG SNIPPET (LOG_SNIPPET)

```json
{
  "incidentType": "LOG_SNIPPET",
  "referenceCode": "LOG-3001",
  "incidentId": "INC-2025-003",
  "rawLogContent": "ERROR: Database connection timeout after 30 seconds",
  "serverIp": "192.168.1.10"
}


```

> ###### 1.4 ROOT CAUSE ANALYSIS (ROOT_CAUSE_ANALYSIS)

```json
{
  "incidentType": "ROOT_CAUSE_ANALYSIS",
  "referenceCode": "RCA-4001",
  "incidentId": "INC-2025-004",
  "summary": "Root cause identified as misconfigured database connection pool",
  "linkedIncidentIds": [101, 102, 103]
}

```

---

## Notes

### Key Design Choices

- Polymorphic DTO design using Jackson annotations for extensibility
- Separation of request/response models to protect entities
- Centralized API response wrapper
- Pagination & filtering via model binding
- Validation at DTO level using Jakarta Validation

### Improvements With 2 More Hours (Might take more )

- Add OpenAPI / Swagger documentation
- Represents progress of a RESOLVING an incident
- Add CORELLATION ID for logging requestID per request
- Add GITHUB repository and open issue with linking to Commits/Code
- Add Error Codes mapped from a common repository(custom jar/mvn) or shared ENUMS