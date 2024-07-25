plugins {
    kotlin("jvm")
}

group = "com.github.alice.echo"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":alice"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}