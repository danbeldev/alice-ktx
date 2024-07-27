import org.gradle.api.publish.maven.MavenPublication

plugins {
    `java-library`
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("io.ktor.plugin")
    id("maven-publish")
    id("org.jetbrains.dokka")
}

group = "com.github.alice.ktx"
version = "0.0.2"

dependencies {
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:1.4.12")
}

kotlin {
    jvmToolchain(11)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = "com.github.danbeldev"
            artifactId = "alice-ktx"
            version = "0.0.2"
        }
    }
}
