package com.riraki.kelf

import java.io.BufferedInputStream

class ElfRelocationAppendSection(
    elfFile: ElfFile,
    relocationAppendSectionHeader: ElfSectionHeader
) : ElfSection(elfFile, relocationAppendSectionHeader) {

    val entries: Array<ElfRelocationAppend> by lazy {
        val symbolTableSectionHeader: ElfSectionHeader = elfFile.sectionHeaders[relocationAppendSectionHeader.sh_link.toInt()]
        val symbolTable = ElfSymbolTableSection(elfFile, symbolTableSectionHeader)
        BufferedInputStream(elfFile.file.inputStream()).use { inputStream ->
            InputStreamHelpers.skipFully(inputStream, relocationAppendSectionHeader.sh_offset.toLong())
                Array((relocationAppendSectionHeader.sh_size / relocationAppendSectionHeader.sh_entsize).toInt()) {
                val relocationAppendOffset = relocationAppendSectionHeader.sh_offset + it.toULong() * relocationAppendSectionHeader.sh_entsize
                ElfRelocationAppend(elfFile, relocationAppendOffset, symbolTable)
            }
        }
    }

    override fun toString(): String {
        return "ElfRelocationAppendSection(" +
                "entryCount=${entries.size}" +
                ", entries=${entries.contentToString()}" +
                ")"
    }
}
