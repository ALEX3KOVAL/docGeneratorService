plugins {
    kotlin("jvm")
}

group = "ru.alex3koval.docGenerator"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    loadEventingContractGithubPackage()
}

dependencies {
    implementation(libs.bundles.apache)

    implementation("alex3koval:eventing-contract:latest.release")

    implementation("io.projectreactor:reactor-core:3.4.40")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
