# Demo service
## Description
This is a university project in which I and a team of my classmates created the server part of an online store.  
The main idea is to create a fault-tolerant system that uses event search patterns to store and process information.  
We used a library created by our teacher, which allowed us to process information even for asynchronous requests using
Event sourcing patterns.  
https://github.com/andrsuh/tiny-event-sourcing  

The correctness was checked on a different number of products and orders that can be received using an external module bombardier.
https://github.com/andrsuh/bombardier  

## API

### Authentication module

- **POST /authentication**: Authenticate
- **POST /authentication/refresh**: Refresh authentication

### User module

- **POST /users**: Register
- **POST /authentication**: Authenticate
- **POST /authentication/refresh**: Refresh authentication
- **GET /users/me**: Get information about current user
- **DELETE /users/me**: Delete current user

### Task module

- **POST /tasks**: Create task
- **GET /tasks**: Get all tasks
- **GET /tasks/{id}**: Get task
- **POST /tasks/{id}/assignee/{username}**: Assign task
- **DELETE /tasks/{id}**: Delete task

### OpenAPI
* **GET /swagger-ui.html**: Swagger UI
  * Paste bearer token to Authorize window to have access to secured endpoints

## Authentication

**Authenticate**

Request:

POST /authentication

```json
{
    "username": "<username>",
    "password": "<password>"
}
```

Response:

```json
{
    "accessToken": "<token>",
    "refreshToken": "<token>"
}
```

Access token should be used in all other requests as header:
`Authorization: Bearer <access token>`

**Refresh authentication**

Request:

POST /authentication/refresh

As an auth token refresh token should be used in request header: `Authorization: Bearer <refresh token>`


## Messaging

### User module

Output events:

- **USER_CREATED**
- **USER_DELETED**


### Task module

Input events:

- **USER_DELETED** - Remove all tasks created by deleted user,
remove assignee from tasks assigned to him

Output events:

- **TASK_CREATED**
- **TASK_DELETED**
- **TASK_ASSIGNED**

### Notification module

Input events:

- **USER_CREATED** - send greeting e-mail
- **TASK_ASSIGNED** - Notify user

```plantuml:messaging
@startuml
node UserModule
node TaskModule
node NotificationModule

'UserModule outputs
UserModule --> USER_CREATED
UserModule --> USER_DELETED

'TaskModule inputs
USER_DELETED --> TaskModule: Remove all tasks created by deleted user,\n \
remove assignee from tasks assigned to him
'TaskModule outputs
TaskModule --> TASK_CREATED
TaskModule --> TASK_DELETED
TaskModule --> TASK_ASSIGNED

'NotificationModule inputs
USER_CREATED --> NotificationModule: Send greeting e-mail
TASK_ASSIGNED --> NotificationModule: Notify user


@enduml
```

![messaging](./docs/messaging.svg)


## Entity-relationship model

```plantuml:entities
@startuml entities
entity User {
    username: text
    --
    * name: text
    * surname: text
    * email: string
}

entity Task {
    id: UUID (generated)
    --
    *author: User
    assignee: User
    *title: text
    description: text
    *status: Status
}

enum Status {
    TODO
    IN_PROCESS
    DONE
}

Task::author }o..|| User::username
Task::assignee }o..o| User::username
Task::status }o--|| Status
@enduml
```

![entities](./docs/entities.svg)
