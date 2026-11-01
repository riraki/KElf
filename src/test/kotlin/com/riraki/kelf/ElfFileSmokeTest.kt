package com.riraki.kelf

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ElfFileSmokeTest {
    @Test
    fun parsesArm32SharedObject() {
        val elfFile = loadFixture("/elf/generated/libnative-lib-armeabi-v7a.so")

        assertTrue(elfFile.is32Bits)
        assertEquals(ElfMachine.EM_ARM, elfFile.elfHeader.elfMachine)
        assertTrue(elfFile.programHeaders.isNotEmpty())
    }

    @Test
    fun parsesArm64SharedObject() {
        val elfFile = loadFixture("/elf/generated/libnative-lib-arm64-v8a.so")

        assertTrue(elfFile.is64Bits)
        assertEquals(ElfMachine.EM_AARCH64, elfFile.elfHeader.elfMachine)
        assertTrue(elfFile.programHeaders.isNotEmpty())
    }

    @Test
    fun parsesAndroidArm32ArtLibrary() {
        val elfFile = loadFixture("/elf/android/arm/libart.so")

        assertTrue(elfFile.is32Bits)
        assertEquals(ElfMachine.EM_ARM, elfFile.elfHeader.elfMachine)
        assertTrue(elfFile.programHeaders.isNotEmpty())
    }

    @Test
    fun parsesAndroidArm64ArtLibrary() {
        val elfFile = loadFixture("/elf/android/arm64/libart64.so")

        assertTrue(elfFile.is64Bits)
        assertEquals(ElfMachine.EM_AARCH64, elfFile.elfHeader.elfMachine)
        assertTrue(elfFile.programHeaders.isNotEmpty())
    }

    private fun loadFixture(resourcePath: String): ElfFile {
        val resource = requireNotNull(javaClass.getResource(resourcePath)) {
            "Missing ELF test fixture: $resourcePath"
        }
        return ElfFile.from(File(resource.toURI()))
    }
}
