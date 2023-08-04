# Sync Service App

CRUD Application on Kotlin / React JS multiplatform. Working with mongoDB via Hibernate.

Roles:

* viewer : can only check main page with images
* designer: can add new entities
* drawer: can add images to entities, save local changes and update main images with draft variants

User stories:

* As a game designer, I want to create the entity with
  string  id and allocate a sequence on that entity to drawer.
* As drawer, I want to follow the link that the game designer sent and go to the page of the desired hero/weapon
* As drawer, I want to find new images of entities, not applying changes to the main database and content folder (similar to
  uncommitted changes in git)
* As drawer, I want to represent the changes that are going apply, on separate pages. (similar to git status)
* As drawer, I want to apply my changes in order to process the information the changes got into the database,
and the pictures were found in the shared folder with content. (similar to git commit )
* As drawer, I want to undo my changes. (similar to git revert)
* Like any employee of the company, I want to view all current entities

![2023-08-04 19_36_15-Sync Service.png](..%2F..%2FOneDrive%2FDesktop%2F2023-08-04%2019_36_15-Sync%20Service.png)
![2023-08-04 19_36_43-Sync Service.png](..%2F..%2FOneDrive%2FDesktop%2F2023-08-04%2019_36_43-Sync%20Service.png)
![2023-08-04 19_38_32-Sync Service.png](..%2F..%2FOneDrive%2FDesktop%2F2023-08-04%2019_38_32-Sync%20Service.png)

### Built With

* [![Kotlin][Kotlin.io]][Kotlin-url]
* [![SpringBoot][SpringBoot.io]][SpringBoot-url]
* [![Hibernate][Hibernate.io]][Hibernate-url]
* [![OpenApi][OpenApi.io]][OpenApi-url]
* [![React][React.io]][React-url]
* [![MongoDB][Mongo.io]][Mongo-url]

## Pre-installations

#### Clone the repo:

```sh
git clone https://github.com/AdeleDev/sync_tool_app.git
```

#### To have a look on API:

```
https://github.com/AdeleDev/sync_tool_app/blob/master/src/jvmMain/resources/api/SyncService_OpenApi_1.0.0.yaml
```

#### Build project:

```sh
gradlew clean
```

Start mongo db via Docker:

```sh
docker-compose up -d
```

Run service:

```sh
gradlew run
```

#### API request base URL :

```
http://localhost:8080/v1/elemenet/
```

<!-- MARKDOWN LINKS & IMAGES -->

[Kotlin.io]: https://img.shields.io/badge/-Kotlin-white?style=for-the-badge&logo=kotlin

[Kotlin-url]: https://kotlinlang.org/docs/js-project-setup.html

[SpringBoot.io]: https://img.shields.io/badge/-Springboot-green?style=for-the-badge&logo=springboot

[SpringBoot-url]: https://spring.io/projects/spring-boot

[Hibernate.io]: https://img.shields.io/badge/-Hibernate-gray?style=for-the-badge&logo=hibernate

[Hibernate-url]: https://hibernate.org/

[OpenApi.io]: https://img.shields.io/badge/-OpenApi-blueviolet?style=for-the-badge&logo=openapiinitiative

[OpenApi-url]: https://www.openapis.org/

[React.io]: https://img.shields.io/badge/React-black?style=for-the-badge&logo=react

[React-url]: https://reactjs.org/

[Mongo.io]: https://img.shields.io/badge/MongoDb-black?style=for-the-badge&logo=mongodb

[Mongo-url]: https://www.mongodb.com/
