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

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.0")
    implementation(libs.bundles.apache)
    implementation("org.springframework:spring-webflux")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.19.2")

    implementation("io.projectreactor:reactor-core:3.4.40")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}