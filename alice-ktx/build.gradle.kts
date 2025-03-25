import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("io.ktor.plugin")
    id("com.vanniktech.maven.publish") version "0.28.0"
}

group = "com.github.alice.ktx"
version = "1.0.0"

project.setProperty("mainClassName", "com.github.alice.ktx.Skill")

dependencies {
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")

    implementation("io.ktor:ktor-client-core-jvm")
    implementation("io.ktor:ktor-client-cio-jvm")
    implementation("io.ktor:ktor-client-content-negotiation-jvm")

    implementation("ch.qos.logback:logback-classic:1.5.6")

    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")

    implementation("io.lettuce:lettuce-core:6.5.0.RELEASE")

    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.17")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
}

kotlin {
    jvmToolchain(11)
}

mavenPublishing {
    coordinates(
        groupId = "io.github.danbeldev",
        artifactId = "alice-ktx",
        version = "1.0.0"
    )

    pom {
        name.set("alice-ktx")
        description.set("Асинхронный фреймворк для разработки навыков Алисы из Яндекс.Диалогов")
        inceptionYear.set("2024")
        url.set("https://github.com/danbeldev/alice-ktx")

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        developers {
            developer {
                id.set("danbel")
                name.set("DanBel")
                email.set("dan.bel.89@bk.ru")
            }
        }

        scm {
            url.set("https://github.com/danbeldev/alice-ktx")
        }
    }

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()
}