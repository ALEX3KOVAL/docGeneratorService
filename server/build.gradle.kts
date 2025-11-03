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
    implementation(project(":storage"))
    implementation(project(":configuration"))
    implementation(project(":appImpl"))

    implementation("alex3koval:eventing-impl:latest.release")
    implementation("alex3koval:eventing-contract:latest.release")
    implementation("io.github.resilience4j:resilience4j-reactor:2.2.0")
    implementation("io.github.resilience4j:resilience4j-spring-boot3:2.2.0")

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation(kotlin("test"))
}

tasks {
    val copyDeps = register("copyDeps", Copy::class) {
        from(configurations.runtimeClasspath.get())
        into(rootProject.layout.buildDirectory.dir("libs"))
        outputs.upToDateWhen { true }
    }

    val copyResources = register("copyResources", Copy::class) {
        from(sourceSets.main.get().resources)
        into(rootProject.layout.buildDirectory.dir("resources").get().asFile.absolutePath)

        outputs.upToDateWhen { true }
    }

    bootJar {
        destinationDirectory.set(rootProject.layout.buildDirectory.get())
        archiveFileName.set("docGenerator.jar")

        manifest {
            attributes.apply {
                put("Class-Path", configurations.runtimeClasspath.get()
                    .filter { it.extension == "jar" }
                    .distinctBy { it.name }
                    .joinToString(separator = " ", transform = { "libs/${it.name}" }))
            }
        }

        dependsOn(copyDeps)
        dependsOn(copyResources)
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}
