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
    loadEventerGitHubPackage(EventerType.KAFKA)
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":configuration"))
    implementation(project(":appImpl"))
    implementation(project(":common"))

    implementation("alex3koval:eventing-contract:latest.release")
    implementation("alex3koval:eventing-impl:latest.release")
    implementation("alex3koval:kafka-eventer:latest.release")

    implementation("io.projectreactor:reactor-core:3.4.40")

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-validation")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
