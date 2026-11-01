plugins {
    id("org.jetbrains.kotlin.jvm")
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
