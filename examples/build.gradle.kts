plugins {
    kotlin("jvm")
}

group = "com.github.examples"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":alice-ktx"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}