package com.riraki.kelf

import java.io.File

class ElfFile {
    val file: File

    val elfHeader: ElfHeader by lazy {
        ElfHeader(this)
    }

    val programHeaders: Array<ElfProgramHeader> by lazy {
        Array(elfHeader.e_phnum.toInt()) {
            val programHeaderOffset = elfHeader.e_phoff + it.toULong() * elfHeader.e_phentsize
            ElfProgramHeader(this, programHeaderOffset)
        }
    }

    val sectionHeaders: Array<ElfSectionHeader> by lazy {
        Array(elfHeader.e_shnum.toInt()) {
            val sectionHeaderOffset = elfHeader.e_shoff + it.toULong() * elfHeader.e_shentsize
            ElfSectionHeader(this, sectionHeaderOffset, mStringTable)
        }
    }

    val sections: Array<ElfSection> by lazy {
        Array(sectionHeaders.size) {
            when (sectionHeaders[it].sh_type) {
                ElfSectionConstants.SHT_STRTAB -> ElfStringTableSection(this, sectionHeaders[it])
                ElfSectionConstants.SHT_SYMTAB
                    , ElfSectionConstants.SHT_DYNSYM -> ElfSymbolTableSection(this, sectionHeaders[it])
                ElfSectionConstants.SHT_DYNAMIC -> ElfDynamicSection(this, sectionHeaders[it])
                ElfSectionConstants.SHT_RELA -> ElfRelocationAppendSection(this, sectionHeaders[it])
                ElfSectionConstants.SHT_REL -> ElfRelocationSection(this, sectionHeaders[it])
                ElfSectionConstants.SHT_HASH -> ElfHashSection(this, sectionHeaders[it])
                ElfSectionConstants.SHT_GNU_HASH -> ElfGnuHashSection(this, sectionHeaders[it])
                ElfSectionConstants.SHT_NOTE -> ElfNoteSection(this, sectionHeaders[it])
                else -> ElfSection(this, sectionHeaders[it])
            }
        }
    }

    private val mStringTable: ElfStringTableSection by lazy {
        val stringTableSectionHeaderOffset = elfHeader.e_shoff + elfHeader.e_shstrndx * elfHeader.e_shentsize
        ElfStringTableSection(
            this,
            ElfSectionHeader(
                this,
                stringTableSectionHeaderOffset,
                null
            )
        )
    }

    val stringTable: ElfStringTableSection
        get() {
            return sections[elfHeader.e_shstrndx.toInt()] as? ElfStringTableSection
                ?: throw IllegalArgumentException("Index ${elfHeader.e_shstrndx} is not an ElfStringTable")
        }

    val dynamicStringTable: ElfStringTableSection?
        get() = sections.firstOrNull { it.type == ElfSectionConstants.SHT_STRTAB
                && it.name == ElfSectionConstants.NAME_DYNSTR }
            ?.let { ElfStringTableSection(this, it.sectionHeader) }

    val dynamicSymbolTableSection: ElfSymbolTableSection?
        get() = sections.firstOrNull { it.type == ElfSectionConstants.SHT_DYNSYM }
            ?.let { ElfSymbolTableSection(this, it.sectionHeader) }

    val symbolTableSection: ElfSymbolTableSection?
        get() = sections.firstOrNull { it.type == ElfSectionConstants.SHT_SYMTAB }
            ?.let { ElfSymbolTableSection(this, it.sectionHeader) }

    val dynamicSection: ElfDynamicSection?
        get() = sections.firstOrNull { it.type == ElfSectionConstants.SHT_DYNAMIC }
            ?.let { ElfDynamicSection(this, it.sectionHeader) }

    val allSymbols: Array<ElfSymbol>
        get() = (dynamicSymbolTableSection?.symbols ?: emptyArray()) +
                (symbolTableSection?.symbols ?: emptyArray())

    val isLittleEndian: Boolean
        get() = elfHeader.isLittleEndian

    val isBigEndian: Boolean
        get() = elfHeader.isBigEndian

    val is32Bits: Boolean
        get() = elfHeader.is32Bits

    val is64Bits: Boolean
        get() = elfHeader.is64Bits

    fun getSectionHeader(index: Int): ElfSectionHeader = sectionHeaders[index]

    fun getSectionHeader(name: String): ElfSectionHeader? =
        sectionHeaders.firstOrNull { it.name == name }

    fun getSectionHeaders(sectionHeaderType: UInt): Array<ElfSectionHeader> =
        sectionHeaders.filter { it.sh_type == sectionHeaderType }.toTypedArray()

    fun getSection(index: Int): ElfSection = sections[index]

    fun getSections(sectionType: UInt): Array<ElfSection> =
        sections.filter { it.type == sectionType }.toTypedArray()

    fun getSections(type: Class<*>): Array<ElfSection> {
        return buildList {
            for (section in sections) {
                if (type.isInstance(section)) {
                    add(section)
                }
            }
        }.toTypedArray()
    }

    fun getSymbol(symbolName: String): ElfSymbol? = allSymbols.firstOrNull { it.name == symbolName }

    fun getProgramHeader(index: Int): ElfProgramHeader = programHeaders[index]

    fun getProgramHeader(programHeaderType: UInt): Array<ElfProgramHeader> =
        programHeaders.filter { it.p_type == programHeaderType }.toTypedArray()

    private constructor(file: File) {
        this.file = file
    }

    companion object {
        @JvmStatic
        fun from(elfFile: File): ElfFile {
            return ElfFile(elfFile)
        }
    }
}
