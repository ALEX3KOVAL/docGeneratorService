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
    loadEventingContractGithubPackage()
}

dependencies {
    implementation(project(":domain"))

    implementation("alex3koval:eventing-contract:latest.release")

    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.postgresql:r2dbc-postgresql:1.0.7.RELEASE")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.19.2")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("io.projectreactor:reactor-core")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}