plugins {
    id("org.jetbrains.kotlin.jvm")
    id("com.vanniktech.maven.publish") version "0.37.0"
    id("org.jetbrains.dokka") version "2.2.0"
    `java-library`
}

group = "io.github.riraki"
version = "0.1.0"
description = "A Kotlin/JVM library for parsing ELF32 and ELF64 binaries."

kotlin {
    jvmToolchain(17)
}

base {
    archivesName.set("kelf")
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    testImplementation(kotlin("test-junit5"))
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.13.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.13.4")
}

mavenPublishing {
    // Keep release publication manual: upload first, then publish from Central Portal.
    coordinates("io.github.riraki", "kelf", version.toString())
    publishToMavenCentral()
    signAllPublications()

    pom {
        name.set("KElf")
        description.set(
            "A Kotlin/JVM library for parsing ELF32 and ELF64 binaries."
        )
        url.set("https://github.com/riraki/KElf")

        licenses {
            license {
                name.set("Mozilla Public License, Version 2.0")
                url.set("https://www.mozilla.org/en-US/MPL/2.0/")
                distribution.set("repo")
            }
        }

        developers {
            developer {
                id.set("riraki")
                name.set("riraki")
                url.set("https://github.com/riraki")
            }
        }

        scm {
            url.set("https://github.com/riraki/KElf")
            connection.set("scm:git:https://github.com/riraki/KElf.git")
            developerConnection.set("scm:git:ssh://git@github.com/riraki/KElf.git")
        }

        issueManagement {
            system.set("GitHub Issues")
            url.set("https://github.com/riraki/KElf/issues")
        }
    }
}
