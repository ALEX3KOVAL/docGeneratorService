plugins {
    kotlin("jvm")
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.4"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "ru.alex3koval.docGenerator"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    loadEventingGithubPackages()
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":appImpl"))
    implementation(project(":storage"))

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.0")
    implementation("alex3koval:eventing-impl:latest.release")
    implementation("alex3koval:eventing-contract:latest.release")
    implementation("org.springframework:spring-webflux")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.19.2")
    implementation("org.springframework.kafka:spring-kafka")

    implementation("org.springframework.ws:spring-ws-core:3.1.1")
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j:3.3.0")

    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")

    runtimeOnly("io.r2dbc:r2dbc-postgresql:0.8.13.RELEASE")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}