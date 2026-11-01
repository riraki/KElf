package com.riraki.kelf.examples

import com.riraki.kelf.ElfFile
import java.io.File

/**
 * A small command-line example that prints the main structures parsed from an ELF file.
 *
 * Usage:
 *   ./gradlew :examples:run --args="/path/to/library.so"
 */
object ElfDump {
    @JvmStatic
    fun main(args: Array<String>) {
        require(args.size == 1) {
            "Usage: ElfDump <path-to-elf-file>"
        }

        val file = File(args[0])
        require(file.isFile) {
            "ELF file does not exist: ${file.absolutePath}"
        }

        val elfFile = ElfFile.from(file)

        println("===== ELF Header =====")
        println(elfFile.elfHeader)

        println("\n===== Program Headers =====")
        elfFile.programHeaders.forEach(::println)

        println("\n===== Section Headers =====")
        elfFile.sectionHeaders.forEach(::println)

        println("\n===== Sections =====")
        elfFile.sections.forEach(::println)

        println("\n===== Dynamic Symbols =====")
        elfFile.dynamicSymbolTableSection?.symbols?.forEach(::println)

        println("\n===== Static Symbols =====")
        elfFile.symbolTableSection?.symbols?.forEach(::println)

        println("\n===== Dynamic Section =====")
        println(elfFile.dynamicSection)
    }
}
