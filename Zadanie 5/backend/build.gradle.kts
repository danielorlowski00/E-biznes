val ktorversion: String by project
val kotlinversion: String by project
val logbackversion: String by project
val exposedversion: String by project
val h2version: String by project

plugins {
    kotlin("jvm") version "1.8.22"
    id("io.ktor.plugin") version "2.3.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.22"
    id("org.jlleitschuh.gradle.ktlint") version "11.4.0"
}

group = "com.example"
version = "0.0.1"
application {
    mainClass.set("com.example.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-cors:$ktorversion")
    implementation("io.ktor:ktor-server-core-jvm:$ktorversion")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorversion")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorversion")
    implementation("org.jetbrains.exposed:exposed-core:$exposedversion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedversion")
    implementation("com.h2database:h2:$h2version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorversion")
    implementation("ch.qos.logback:logback-classic:$logbackversion")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorversion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinversion")
}
