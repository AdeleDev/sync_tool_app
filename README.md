# Sync Service App 

CRUD Application on Kotlin / React JS multiplatform. Working with mongo DB via Hibernate.

### Built With

* [![Kotlin][Kotlin.io]][Kotlin-url]
* [![SpringBoot][SpringBoot.io]][SpringBoot-url]
* [![Hibernate][Hibernate.io]][Hibernate-url]
* [![OpenApi][OpenApi.io]][OpenApi-url]
* [![React][React.io]][React-url]


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

