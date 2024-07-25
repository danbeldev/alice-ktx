pluginManagement {
    plugins {
        kotlin("jvm") version "1.9.23"
        kotlin("plugin.serialization") version "1.9.23"
    }
}
rootProject.name = "alice-skill"
include("alice")
include("samples")
include("samples:echo")
findProject(":samples:echo")?.name = "echo"
