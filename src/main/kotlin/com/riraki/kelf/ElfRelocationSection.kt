package com.riraki.kelf

import java.io.BufferedInputStream

class ElfRelocationSection(
    elfFile: ElfFile,
    relocationSectionHeader: ElfSectionHeader
) : ElfSection(elfFile, relocationSectionHeader) {

    val entries: Array<ElfRelocation> by lazy {
        val symbolTableSectionHeader: ElfSectionHeader = elfFile.sectionHeaders[relocationSectionHeader.sh_link.toInt()]
        val symbolTable = ElfSymbolTableSection(elfFile, symbolTableSectionHeader)
        BufferedInputStream(elfFile.file.inputStream()).use { inputStream ->
            InputStreamHelpers.skipFully(inputStream, relocationSectionHeader.sh_offset.toLong())
            Array((relocationSectionHeader.sh_size / relocationSectionHeader.sh_entsize).toInt()) {
                val relocationAppendOffset = relocationSectionHeader.sh_offset + it.toULong() * relocationSectionHeader.sh_entsize
                ElfRelocation(elfFile, relocationAppendOffset, symbolTable)
            }
        }
    }

    override fun toString(): String {
        return "ElfRelocationSection(" +
                "entryCount=${entries.size}" +
                ", entries=${entries.contentToString()}" +
                ")"
    }
}
