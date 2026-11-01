plugins {
    id("org.jetbrains.kotlin.jvm")
    application
}

dependencies {
    implementation(project(":"))
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("com.riraki.kelf.examples.ElfDump")
}
