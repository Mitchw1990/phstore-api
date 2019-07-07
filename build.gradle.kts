import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("idea")
    id("org.asciidoctor.convert") version "1.5.3"
    id("org.springframework.boot") version "2.1.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.7.RELEASE"
    kotlin("jvm") version "1.2.71"
    kotlin("plugin.spring") version "1.2.71"
}

version = "1.0"
group = "com.bitprobe.phstore"
java.sourceCompatibility = JavaVersion.VERSION_1_8

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    jcenter()
    mavenCentral()
    maven("https://repo.spring.io/libs-milestone")
    maven("https://maven-central.storage.googleapis.com")
}

extra["springCloudVersion"] = "Greenwich.SR1"
extra["snippetsDir"] = file("build/generated-snippets")

dependencies {

    implementation("com.github.kilianB:JImageHash:3.0.0")
    implementation("com.google.firebase:firebase-admin:6.8.1")
    implementation("com.google.api-client:google-api-client:1.25.0")

    //google cloud dependencies
    implementation("org.springframework.cloud:spring-cloud-gcp-starter")
    implementation("org.springframework.cloud:spring-cloud-gcp-starter-data-datastore")

    //kotlin dependencies
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    //boot dependencies
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    //test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}
