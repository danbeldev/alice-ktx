pluginManagement {
    plugins {
        kotlin("jvm") version "1.9.23"
        kotlin("plugin.serialization") version "1.9.23"
        id("org.jetbrains.dokka") version "1.9.20"
        id("io.ktor.plugin") version "2.3.12"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "alice-skill"
include("alice-ktx", "examples")