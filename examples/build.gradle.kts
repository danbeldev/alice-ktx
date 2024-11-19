plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "2.0.0-RC1"
    id("io.ktor.plugin")
}

group = "com.github.examples"
version = "unspecified"

dependencies {
    implementation(project(":alice-ktx"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")

    implementation("io.ktor:ktor-client-core-jvm")
    implementation("io.ktor:ktor-client-cio-jvm")
    implementation("io.ktor:ktor-client-content-negotiation-jvm")

    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")

    implementation("io.lettuce:lettuce-core:6.5.0.RELEASE")
}

kotlin {
    jvmToolchain(11)
}