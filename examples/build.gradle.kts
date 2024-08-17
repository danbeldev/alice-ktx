plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "2.0.0-RC1"
}

group = "com.github.examples"
version = "unspecified"

dependencies {
    implementation(project(":alice-ktx"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
}

kotlin {
    jvmToolchain(11)
}