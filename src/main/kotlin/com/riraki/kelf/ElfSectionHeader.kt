package com.riraki.kelf

import java.io.BufferedInputStream
import java.io.File

class ElfSectionHeader {
    val sh_name: UInt           // 节名称在字符串表中的偏移量
    val sh_type: UInt           // 节的类型
    val sh_flags: ULong         // 一组描述 Section 属性的位标志 UInt/ULong
    val sh_addr: ULong          // 节的虚拟地址 UInt/ULong
    val sh_offset: ULong        // 节在文件中的偏移 UInt/ULong
    val sh_size: ULong          // 节的大小 UInt/ULong
    val sh_link: UInt           // 与其他节关联的索引
    val sh_info: UInt           // 与节类型相关的附加信息
    val sh_addralign: ULong     // 地址对齐要求 UInt/ULong
    val sh_entsize: ULong       // 固定大小表项的大小 UInt/ULong

    private val mStringTable: ElfStringTableSection?

    constructor(elfFile: ElfFile, offset: ULong, stringTable: ElfStringTableSection?) {
        val file: File = elfFile.file
        val isLittleEndian = elfFile.isLittleEndian
        val is32Bits = elfFile.is32Bits
        BufferedInputStream(file.inputStream()).use {
            InputStreamHelpers.skipFully(it, offset.toLong())
            sh_name = InputStreamHelpers.readUInt(it, isLittleEndian)
            sh_type = InputStreamHelpers.readUInt(it, isLittleEndian)
            sh_flags = ElfParserHelpers.readUIntOrULong(it, is32Bits, isLittleEndian)
            sh_addr = ElfParserHelpers.readUIntOrULong(it, is32Bits, isLittleEndian)
            sh_offset = ElfParserHelpers.readUIntOrULong(it, is32Bits, isLittleEndian)
            sh_size = ElfParserHelpers.readUIntOrULong(it, is32Bits, isLittleEndian)
            sh_link = InputStreamHelpers.readUInt(it, isLittleEndian)
            sh_info = InputStreamHelpers.readUInt(it, isLittleEndian)
            sh_addralign = ElfParserHelpers.readUIntOrULong(it, is32Bits, isLittleEndian)
            sh_entsize = ElfParserHelpers.readUIntOrULong(it, is32Bits, isLittleEndian)
        }

        this.mStringTable = stringTable
    }

    val name: String
        get() = nameDescription

    val nameDescription: String
        get() {
            return mStringTable?.getString(sh_name.toInt()) ?: FormatHelpers.toHexString(sh_name)
        }

    val typeDescription: String
        get() {
            return when (sh_type) {
                ElfSectionConstants.SHT_NULL -> "SHT_NULL"
                ElfSectionConstants.SHT_PROGBITS -> "SHT_PROGBITS"
                ElfSectionConstants.SHT_SYMTAB -> "SHT_SYMTAB"
                ElfSectionConstants.SHT_STRTAB -> "SHT_STRTAB"
                ElfSectionConstants.SHT_RELA -> "SHT_RELA"
                ElfSectionConstants.SHT_HASH -> "SHT_HASH"
                ElfSectionConstants.SHT_DYNAMIC -> "SHT_DYNAMIC"
                ElfSectionConstants.SHT_NOTE -> "SHT_NOTE"
                ElfSectionConstants.SHT_NOBITS -> "SHT_NOBITS"
                ElfSectionConstants.SHT_REL -> "SHT_REL"
                ElfSectionConstants.SHT_SHLIB -> "SHT_SHLIB"
                ElfSectionConstants.SHT_DYNSYM -> "SHT_DYNSYM"
                ElfSectionConstants.SHT_INIT_ARRAY -> "SHT_INIT_ARRAY"
                ElfSectionConstants.SHT_FINI_ARRAY -> "SHT_FINI_ARRAY"
                ElfSectionConstants.SHT_PREINIT_ARRAY -> "SHT_PREINIT_ARRAY"
                ElfSectionConstants.SHT_GROUP -> "SHT_GROUP"
                ElfSectionConstants.SHT_SYMTAB_SHNDX -> "SHT_SYMTAB_SHNDX"
                ElfSectionConstants.SHT_RELR -> "SHT_RELR"
                ElfSectionConstants.SHT_NUM -> "SHT_NUM"
                ElfSectionConstants.SHT_LOOS -> "SHT_LOOS"
                ElfSectionConstants.SHT_GNU_ATTRIBUTES -> "SHT_GNU_ATTRIBUTES"
                ElfSectionConstants.SHT_GNU_HASH -> "SHT_GNU_HASH"
                ElfSectionConstants.SHT_GNU_LIBLIST -> "SHT_GNU_LIBLIST"
                ElfSectionConstants.SHT_CHECKSUM -> "SHT_CHECKSUM"
                ElfSectionConstants.SHT_LOSUNW -> "SHT_LOSUNW"
                ElfSectionConstants.SHT_GNU_verdef -> "SHT_GNU_verdef"
                ElfSectionConstants.SHT_GNU_verneed -> "SHT_GNU_verneed"
                ElfSectionConstants.SHT_GNU_versym -> "SHT_GNU_versym"
                ElfSectionConstants.SHT_HISUNW -> "SHT_HISUNW"
                ElfSectionConstants.SHT_HIOS -> "SHT_HIOS"
                ElfSectionConstants.SHT_LOPROC -> "SHT_LOPROC"
                ElfSectionConstants.SHT_HIPROC -> "SHT_HIPROC"
                ElfSectionConstants.SHT_LOUSER -> "SHT_LOUSER"
                ElfSectionConstants.SHT_HIUSER -> "SHT_HIUSER"
                ElfSectionConstants.SHT_ANDROID_RELR -> "SHT_ANDROID_RELR"
                ElfSectionConstants.SHT_ANDROID_REL -> "SHT_ANDROID_REL"
                ElfSectionConstants.SHT_ANDROID_RELA -> "SHT_ANDROID_RELA"
                ElfSectionConstants.SHT_RISCV_ATTRIBUTES -> "SHT_RISCV_ATTRIBUTES"
                else -> "SHT_UNKNOWN($sh_type)"
            }
        }

    val flagsDescription: String
        get() {
            return buildList {
                if ((sh_flags and ElfSectionConstants.SHF_WRITE) != 0UL) {
                    add("SHF_WRITE")
                }
                if ((sh_flags and ElfSectionConstants.SHF_ALLOC) != 0UL) {
                    add("SHF_ALLOC")
                }
                if ((sh_flags and ElfSectionConstants.SHF_EXECINSTR) != 0UL) {
                    add("SHF_EXECINSTR")
                }
                if ((sh_flags and ElfSectionConstants.SHF_MERGE) != 0UL) {
                    add("SHF_MERGE")
                }
                if ((sh_flags and ElfSectionConstants.SHF_STRINGS) != 0UL) {
                    add("SHF_STRINGS")
                }
                if ((sh_flags and ElfSectionConstants.SHF_INFO_LINK) != 0UL) {
                    add("SHF_INFO_LINK")
                }
                if ((sh_flags and ElfSectionConstants.SHF_LINK_ORDER) != 0UL) {
                    add("SHF_LINK_ORDER")
                }
                if ((sh_flags and ElfSectionConstants.SHF_OS_NONCONFORMING) != 0UL) {
                    add("SHF_OS_NONCONFORMING")
                }
                if ((sh_flags and ElfSectionConstants.SHF_GROUP) != 0UL) {
                    add("SHF_GROUP")
                }
                if ((sh_flags and ElfSectionConstants.SHF_TLS) != 0UL) {
                    add("SHF_TLS")
                }
                if ((sh_flags and ElfSectionConstants.SHF_COMPRESSED) != 0UL) {
                    add("SHF_COMPRESSED")
                }
                if ((sh_flags and ElfSectionConstants.SHF_MASKOS) != 0UL) {
                    add("SHF_MASKOS")
                }
                if ((sh_flags and ElfSectionConstants.SHF_MASKPROC) != 0UL) {
                    add("SHF_MASKPROC")
                }
                if ((sh_flags and ElfSectionConstants.SHF_GNU_RETAIN) != 0UL) {
                    add("SHF_GNU_RETAIN")
                }
                if ((sh_flags and ElfSectionConstants.SHF_ORDERED) != 0UL) {
                    add("SHF_ORDERED")
                }
                if ((sh_flags and ElfSectionConstants.SHF_EXCLUDE) != 0UL) {
                    add("SHF_EXCLUDE")
                }
            }.ifEmpty { listOf("SHF_UNKNOWN($sh_flags)") }.joinToString("|")
        }

    override fun toString(): String {
        return "ElfSectionHeader(" +
                "sh_name=\"$nameDescription\"" +
                ", sh_type=\"$typeDescription\"" +
                ", sh_flags=\"$flagsDescription\"" +
                ", sh_addr=$sh_addr" +
                ", sh_offset=$sh_offset" +
                ", sh_size=$sh_size" +
                ", sh_link=$sh_link" +
                ", sh_info=$sh_info" +
                ", sh_addralign=$sh_addralign" +
                ", sh_entsize=$sh_entsize" +
                ")"
    }
}
