package com.riraki.kelf

import java.io.BufferedInputStream
import java.io.File

class ElfStringTableSection(
    elfFile: ElfFile,
    stringTableSectionHeader: ElfSectionHeader
): ElfSection(elfFile, stringTableSectionHeader) {

    private val tableBytes: ByteArray by lazy {
        val file: File = elfFile.file
        BufferedInputStream(file.inputStream()).use {
            InputStreamHelpers.skipFully(it, stringTableSectionHeader.sh_offset.toLong())
            InputStreamHelpers.readByteArrayFully(it, stringTableSectionHeader.sh_size.toInt())
        }
    }

    fun getString(offset: Int): String {
        if (offset >= tableBytes.size || offset < 0) {
            throw IllegalArgumentException("Get string from string table by offset: $offset is illegal!")
        }

        val startIndex = offset
        var endIndex = offset
        while (endIndex < tableBytes.size && tableBytes[endIndex] != 0.toByte()) {
            ++endIndex
        }

        if (endIndex >= tableBytes.size) {
            throw IllegalStateException("Invalid string table: missing null terminator at offset $startIndex")
        }
        return String(tableBytes, startIndex, endIndex - startIndex)
    }

    val tableBytesDescription: String
        get() {
            return String(tableBytes)
        }

    override fun toString(): String {
        return "ElfStringTable(" +
                "tableBytes=$tableBytesDescription" +
                ")"
    }
}
