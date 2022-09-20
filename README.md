# ktor-todolist
## Introduction
Welcome to `ktor-todolist`, a prototype application built with Ktor framework created as part of my Master Thesis - *API security built with Ktor framework and GraphQL query language*. The thesis has been submitted in support of candidature for a master degree of IT studies in cybersecurity specialty at Adam Mickiewicz University in Poznań. 

The goal of the project is to show how to implement every type of authentication supported by Ktor and use it to secure a REST or GraphQL API, as well as to present some of the good practices, missing in the official Ktor documentation. Also, as part of the project, a custom, easy to use and extend authorization mechanism has been created and used in both APIs. The latter is especially important, as Ktor misses a native authorization mechanizm.

## Most important files:
- common:
  - [Authorization.kt](src/main/kotlin/com/example/common/plugins/authorization/Authorization.kt) - class containing generic authorization mechanism, customised by separate functions like the one in [RoleBasedAuthorization.kt](src/main/kotlin/com/example/common/plugins/authorization/RoleBasedAuthorization.kt) or [AttributeBasedAuthorization.kt](src/main/kotlin/com/example/common/plugins/authorization/AttributeBasedAuthorization.kt), which main purpose is to pass a custom lambda expression with authorization checks
  - [Security.kt](src/main/kotlin/com/example/common/plugins/Security.kt) - configurations for each type of authentication supported by Ktor
  - [AuthorizationRouting.kt](src/main/kotlin/com/example/common/plugins/authorization/AuthorizationRouting.kt) - REST API routing configuration, containing endpoint handlers for different types of login
  - [GraphQL.kt](src/main/kotlin/com/example/common/plugins/GraphQL.kt) - GraphQL API routing configuration, containing endpoint handlers for getting and creating a user
- user:
  - [UserRouting.kt](src/main/kotlin/com/example/user/api/UserRouting.kt) - REST API routing configuration, containing endpoint handlers for user CRUD actions

## How to run the application
The application requires a PostgreSQL database running on JDBC URL `jdbc:postgresql://localhost/postgres` on 5432 port with user `michal.najborowski` and no password. It can be either ran as a [Docker image](https://hub.docker.com/_/postgres) or, if on a MacOS computer system, as a local database set by a [Postgres.app](https://postgresapp.com) application.
It is built with [Gradle Build Tool](https://gradle.org/install/) version at least 7.1 or higher, which also requires [JavaJDK](https://adoptopenjdk.net) version at least 1.8 or higher.

With prerequisities correctly set up, the application is built with command
```
gradle build
```
and ran with command
```
gradle run
```

The basic path is `localhost:8080`. The GraphQL sandbox is available under `/graphql` GET request. All request are available as a [Postman](https://www.postman.com) collection in the [resources/postman](src/main/resources/postman) directory and can be executed with this tool aswell.
