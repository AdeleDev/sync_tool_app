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
}


group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
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
        moduleName = project.name
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
//                devServer = KotlinWebpackConfig.DevServer(
//                    static = mutableListOf("$buildDir/distributions"),
//                    // proxy api calls to springboot running on 3000 configured in application.yml
//                    proxy = hashMapOf("/" to "http://localhost:3000")
//                )
                outputFileName = "test-task-multiplatform.js"
                //outputPath = File(buildDir, "processedResources/spring/main/static")
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
//                implementation("com.benasher44:uuid:0.3.0")
                implementation("org.jetbrains.kotlinx:kotlinx-html:0.7.3")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.2.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependsOn(commonMain)
            dependencies {
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
                implementation("org.openapitools:jackson-databind-nullable:$jacksonNullableVersion")

                implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
//                implementation("jakarta.validation:jakarta.validation-api")
                implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
                implementation("org.springframework.boot:spring-boot-starter-validation:$springBootVersion")
                implementation("org.springframework.boot:spring-boot-starter-data-mongodb:$springBootVersion")
                implementation("commons-fileupload:commons-fileupload:1.5")

            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("org.springframework.boot:spring-boot-starter-test")
            }
        }
        val jsMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.7.2")

                fun kotlinw(target: String): String =
                    "org.jetbrains.kotlin-wrappers:kotlin-$target"

                dependencies {
                    implementation(project.dependencies.enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:$kotlinWrappersVersion"))
                    implementation(kotlinw("react"))
                    implementation(kotlinw("react-dom"))
                    implementation(kotlinw("react-router-dom"))
                    implementation(kotlinw("emotion"))
                    implementation(kotlinw("mui"))
                    implementation(kotlinw("mui-icons"))
                }

                implementation(npm("date-fns", "2.30.0"))
                implementation(npm("@date-io/date-fns", "2.16.0"))
            }
        }
        val jsTest by getting
    }
//    sourceSets["jvmMain"].apply {
//        kotlin.srcDir("$buildDir/openapi-generated-src")
//    }
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
    generatorName.set("spring")
    inputSpec.set("$rootDir/src/jvmMain/resources/api/SyncService_OpenApi_1.0.0.yaml")
    outputDir.set("$rootDir/src/jvmMain/java")
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

//todo fix fail in build
tasks.named<Copy>("jvmProcessResources") {
    val jsBrowserProductionWebpack = tasks.named("jsBrowserDevelopmentWebpack")
    dependsOn(jsBrowserProductionWebpack)
    val jsBrowserDistribution = tasks.named("jsBrowserDistribution")
    from(jsBrowserDistribution) {
        into("static")
    }
}

//todo fix failin build include JS artifacts in any JAR we generate
tasks.getByName<Jar>("jvmJar") {
    val taskName = "jsBrowserDevelopmentWebpack"
    val webpackTask = tasks.getByName<KotlinWebpack>(taskName)
    dependsOn(webpackTask) // make sure JS gets compiled first
    from(File(webpackTask.destinationDirectory, webpackTask.outputFileName)) // bring output file along into the JAR
}

tasks.getByName("jsBrowserDevelopmentWebpack") {
    dependsOn(tasks.named("jsDevelopmentExecutableCompileSync"))
    dependsOn(tasks.named("jsProductionExecutableCompileSync"))
}

tasks.getByName("jsBrowserProductionWebpack") {
    dependsOn(tasks.named("jsDevelopmentExecutableCompileSync"))
    dependsOn(tasks.named("jsProductionExecutableCompileSync"))
}


tasks.getByName<JavaExec>("run") {
    //dependsOn(tasks.named<Jar>("jvmJar"))
    classpath(tasks.getByName<Jar>("jvmJar")) // so that the JS artifacts generated by `jvmJar` can be found and served
}


