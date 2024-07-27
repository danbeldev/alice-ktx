plugins {
    kotlin("jvm")
}

group = "com.github.examples"
version = "unspecified"

dependencies {
    implementation(project(":alice-ktx"))
}

kotlin {
    jvmToolchain(11)
}