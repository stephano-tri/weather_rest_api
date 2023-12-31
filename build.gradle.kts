import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.1"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
}

group="eom.tri"
version="0.0.1-SNAPSHOT"

java {
    sourceCompatibility=JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    //to support kotlin coroutines and functional programming
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.boot:spring-boot-starter-webflux:3.1.1")

    //logging
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    modules {
        module("org.springframework.boot:spring-boot-starter-logging") {
            replacedBy("org.springframework.boot:spring-boot-starter-log4j2", "Use Log4j2 instead of Logback")
        }
    }

    //R2DBC(whole db connections)
    implementation("org.springframework.data:spring-data-r2dbc:3.1.1")
    implementation("com.github.jasync-sql:jasync-r2dbc-mysql:2.1.23")
    implementation("io.r2dbc:r2dbc-pool:1.0.0.RELEASE")

    //for serialization
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
}


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs+="-Xjsr305=strict"
        jvmTarget="17"
    }
}



tasks.withType<Test> {
    useJUnitPlatform()
}
