package com.riraki.kelf

open class ElfSection(
    private val elfFile: ElfFile,
    val sectionHeader: ElfSectionHeader
) {

    val type: UInt
        get() = sectionHeader.sh_type

    val name: String
        get() = sectionHeader.nameDescription

    override fun toString(): String {
        return "ElfSection(elfFile=$elfFile, sectionHeader=$sectionHeader)"
    }
}
