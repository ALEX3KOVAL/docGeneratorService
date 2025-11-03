plugins {
    kotlin("jvm")
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.4"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "ru.alex3koval"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    loadEventingImplGithubPackage()
}

dependencies {
    implementation(project(":domain"))
    implementation("alex3koval:eventing-impl:latest.release")

    implementation("org.springframework.boot:spring-boot-starter")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}