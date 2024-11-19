pluginManagement {
    plugins {
        kotlin("jvm") version "2.0.21"
        kotlin("plugin.serialization") version "2.0.21"
        id("org.jetbrains.dokka") version "1.9.20"
        id("io.ktor.plugin") version "3.0.1"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "alice-skill"
include("alice-ktx", "examples")