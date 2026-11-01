package com.riraki.kelf

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ElfFileSmokeTest {
    @Test
    fun parsesArm32SharedObject() {
        val elfFile = loadFixture("/elf/generated/libnative-lib-armeabi-v7a.so")

        assertTrue(elfFile.is32Bits)
        assertEquals(ElfMachine.EM_ARM, elfFile.elfHeader.elfMachine)
        assertTrue(elfFile.programHeaders.isNotEmpty())
        assertEquals(7, elfFile.elfHeader.ei_pad.size)
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


    @Test
    fun looksUpSymbolThroughGnuHash() {
        val elfFile = loadFixture("/elf/generated/libnative-lib-arm64-v8a.so")
        val gnuHash = elfFile.getSections(ElfGnuHashSection::class.java)
            .single() as ElfGnuHashSection

        val symbolName = "Java_com_riraki_nativetest_MainActivity_00024Companion_md5"
        assertEquals(symbolName, gnuHash.lookupSymbol(symbolName)?.name)
        assertNull(gnuHash.lookupSymbol("kelf_symbol_that_does_not_exist"))
    }

    @Test
    fun looksUpSymbolThroughGnuHashOnArm32() {
        val elfFile = loadFixture("/elf/generated/libnative-lib-armeabi-v7a.so")
        val gnuHash = elfFile.getSections(ElfGnuHashSection::class.java)
            .single() as ElfGnuHashSection

        val symbolName = "Java_com_riraki_nativetest_MainActivity_00024Companion_md5"
        assertEquals(symbolName, gnuHash.lookupSymbol(symbolName)?.name)
        assertNull(gnuHash.lookupSymbol("kelf_symbol_that_does_not_exist"))
    }

    @Test
    fun looksUpSymbolThroughGnuHashOnX8664() {
        val elfFile = loadFixture("/elf/generated/libhash-gnu-x86_64.so")
        val gnuHash = elfFile.getSections(ElfGnuHashSection::class.java)
            .single() as ElfGnuHashSection

        assertEquals("kelf_exported_add", gnuHash.lookupSymbol("kelf_exported_add")?.name)
        assertNull(gnuHash.lookupSymbol("kelf_symbol_that_does_not_exist"))
    }

    @Test
    fun looksUpSymbolThroughSysvHash() {
        val elfFile = loadFixture("/elf/generated/libhash-sysv-x86_64.so")
        val hash = elfFile.getSections(ElfHashSection::class.java)
            .single() as ElfHashSection
        val dynamicSymbols = requireNotNull(elfFile.dynamicSymbolTableSection).symbols

        assertEquals("kelf_exported_add", hash.lookupSymbols("kelf_exported_add", dynamicSymbols)?.name)
        assertNull(hash.lookupSymbols("kelf_symbol_that_does_not_exist", dynamicSymbols))
    }

    @Test
    fun rejectsFilesWithoutElfMagic() {
        val file = File.createTempFile("kelf-invalid-", ".bin")
        try {
            file.writeBytes("not an ELF file".toByteArray())
            assertFailsWith<IllegalArgumentException> {
                ElfFile.from(file).elfHeader
            }
        } finally {
            file.delete()
        }
    }

    private fun loadFixture(resourcePath: String): ElfFile {
        val resource = requireNotNull(javaClass.getResource(resourcePath)) {
            "Missing ELF test fixture: $resourcePath"
        }
        return ElfFile.from(File(resource.toURI()))
    }
}
