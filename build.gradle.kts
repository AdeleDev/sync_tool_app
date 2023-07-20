import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

val kotlinVersion: String by project
val logback_version: String by project
val kotlinWrappersVersion = "1.0.0-pre.354"

val springBootVersion: String by project
val springDependencyManagement: String by project
val openapiGenVersion: String by project
val logstashEncoderVersion: String by project
val sourceEncoding: String by project
val springCoreVersion: String by project
val springDoc: String by project
val jacksonVersion: String by project
val swaggerVersion: String by project
val jacksonNullableVersion: String by project
val hibernateVersion: String by project
val mapStructVersion: String by project
extra["kotlin.version"] = kotlinVersion


plugins {
    kotlin("multiplatform") version "1.8.20"
    application
    id("org.jetbrains.kotlin.plugin.spring") version "1.8.20"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.20"
    id("org.springframework.boot") version "2.7.13"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.openapi.generator") version "6.5.0"
//    kotlin("kapt") version "1.8.20"

//    kotlin("js") version "1.6.10"

}


group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
//    maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers") }
}

kotlin {
    jvm {
        jvmToolchain(17)
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(IR) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
        }
    }
//    useCommonJs()


//    dependencies {
//        implementation("org.jetbrains.kotlin-wrappers:kotlin-react:17.0.2-pre.290-kotlin-1.6.10")
//        implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:17.0.2-pre.290-kotlin-1.6.10")
//        implementation("org.jetbrains.kotlin-wrappers:kotlin-redux:4.1.2-pre.290-kotlin-1.6.10")
//        implementation("org.jetbrains.kotlin-wrappers:kotlin-react-redux:7.2.6-pre.290-kotlin-1.6.10")
//        implementation("org.jetbrains.kotlin-wrappers:kotlin-styled:5.3.3-pre.290-kotlin-1.6.10")
//        implementation("org.jetbrains.kotlin-wrappers:kotlin-react-router-dom:6.2.1-pre.290-kotlin-1.6.10")
//        implementation("org.jetbrains.kotlin-wrappers:kotlin-ring-ui:4.1.5-pre.290-kotlin-1.6.10")
//
//        // for kotlin-ring-ui
//        implementation(npm("core-js", "^3.16.0"))
//    }
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
                implementation("net.logstash.logback:logstash-logback-encoder:${logstashEncoderVersion}") {
                    isTransitive = false
                }
                // Open API
                implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
                implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
                implementation("com.squareup.moshi:moshi-kotlin:1.11.0")
                implementation("com.squareup.moshi:moshi-adapters:1.11.0")
                implementation("com.squareup.okhttp3:okhttp:4.9.0")
                implementation("io.swagger.core.v3:swagger-annotations:$swaggerVersion")

                implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
//                implementation("jakarta.validation:jakarta.validation-api")
                implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
                implementation("org.springframework.boot:spring-boot-starter-validation:$springBootVersion")
//                implementation("org.mapstruct:mapstruct:${mapStructVersion}")
//                kapt("org.mapstruct:mapstruct-processor:${mapStructVersion}")
//                configurations["kapt"].dependencies.add(
//                    project.dependencies.create("org.mapstruct:mapstruct-processor:${mapStructVersion}")
//                )
                implementation("org.springframework.boot:spring-boot-starter-data-mongodb:$springBootVersion")

            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("org.springframework.boot:spring-boot-starter-test")
            }
        }
        val jsMain by getting {
            dependencies {
                fun kotlinw(target: String): String =
                    "org.jetbrains.kotlin-wrappers:kotlin-$target"

                dependencies {
//                    implementation("io.ktor:ktor-client-js:$ktor_version")
//                    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
//                    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
                    implementation(project.dependencies.enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:$kotlinWrappersVersion"))
                    implementation(kotlinw("react"))
                    implementation(kotlinw("react-dom"))
                    implementation(kotlinw("react-router-dom"))
                    implementation(kotlinw("emotion"))
                    implementation(kotlinw("mui"))
                }
            }
        }
        val jsTest by getting
    }
    sourceSets["jvmMain"].apply {
        kotlin.srcDir("$buildDir/openapi-generated-src")
    }
}
//
//dependencies {
//    implementation("ch.qos.logback:logback-classic:$logback_version")
//    implementation(kotlin("stdlib"))
//    "kapt"("org.mapstruct:mapstruct-processor:${mapStructVersion}")
//}


//tasks.named("bootRun") {
//    mainClassName.set("scorewarrior.syncService.SyncApplication")
//}

//kapt {
//    showProcessorStats = true
//}


openApiGenerate {
    generatorName.set("kotlin-spring")
    inputSpec.set("$rootDir/src/jvmMain/resources/api/SyncService_OpenApi_1.0.0.yaml")
    outputDir.set("$buildDir/openapi-generated-src")
    configOptions.set(
        mapOf(

            "dateLibrary" to "java8",
            "sourceFolder" to "",
            "returnResponse" to "true",
            "interfaceOnly" to "true"
        )
    )
    globalProperties.set(
        mapOf(
            "apis" to "",
            "apiDocs" to "false",
            "modelDocs" to "false",
            "models" to "",
            "supportingFiles" to "ApiUtil.java"
        )
    )
    apiPackage.set("scorewarrior.syncservice.api")
    modelPackage.set("scorewarrior.syncservice.model")
    modelNameSuffix.set("Dto")
    generateApiTests.set(false)
    generateModelTests.set(false)
}

tasks.named("compileKotlinJvm", org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask::class.java) {
    dependsOn ("openApiGenerate")
}

application {
    mainClass.set("scorewarrior.syncService.SyncApplicationKt")
}

//bootRun {
//    mainClass.set("scorewarrior.syncService.SyncApplication")
//}

//todo fix fail in build tasks.named<Copy>("jvmProcessResources") {
//    val jsBrowserProductionWebpack = tasks.named("jsBrowserProductionWebpack")
//    dependsOn(jsBrowserProductionWebpack)
//}

//todo fix failin build include JS artifacts in any JAR we generate
//tasks.getByName<Jar>("jvmJar") {
//    val taskName = "jsBrowserDevelopmentWebpack"
//    val webpackTask = tasks.getByName<KotlinWebpack>(taskName)
//    dependsOn(webpackTask) // make sure JS gets compiled first
//    from(File(webpackTask.destinationDirectory, webpackTask.outputFileName)) // bring output file along into the JAR
//}



tasks.getByName<JavaExec>("run") {
    dependsOn(tasks.named<Jar>("jvmJar"))
    classpath(tasks.getByName<Jar>("jvmJar")) // so that the JS artifacts generated by `jvmJar` can be found and served
}


