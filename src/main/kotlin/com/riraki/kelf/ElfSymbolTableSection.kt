package com.riraki.kelf

class ElfSymbolTableSection(
    elfFile: ElfFile,
    symbolTableSectionHeader: ElfSectionHeader
) : ElfSection(elfFile, symbolTableSectionHeader) {

    val symbols: Array<ElfSymbol> by lazy {
        Array((symbolTableSectionHeader.sh_size / symbolTableSectionHeader.sh_entsize).toInt()) {
            ElfSymbol(
                elfFile,
                symbolTableSectionHeader,
                symbolTableSectionHeader.sh_offset + it.toULong() * symbolTableSectionHeader.sh_entsize
            )
        }
    }

    fun getString(index: Int): String {
        return symbols[index].name
    }

    override fun toString(): String {
        return "ElfSymbolTableSection(" +
                "symbolCount=${symbols.size}" +
                ", symbols=${symbols.contentToString()}" +
                ")"
    }
}
