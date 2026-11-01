package com.riraki.kelf

import java.io.BufferedInputStream
import java.io.File

class ElfProgramHeader {
    val p_type: UInt            // 段的类型
    val p_flags: UInt           // 读、写、执行权限
    val p_offset: ULong         // 对应内容在文件中的偏移 UInt/ULong
    val p_vaddr: ULong          // 对应内容的虚拟地址 UInt/ULong
    val p_paddr: ULong          // 物理地址字段 UInt/ULong
    val p_filesz: ULong         // 文件中对应内容的大小 UInt/ULong
    val p_memsz: ULong          // 内存中对应区域的大小 UInt/ULong
    val p_align: ULong          // 对齐要求 UInt/ULong

    constructor(elfFile: ElfFile, offset: ULong) {
        val file: File = elfFile.file
        val is32Bits = elfFile.is32Bits
        val isLittleEndian = elfFile.isLittleEndian
        BufferedInputStream(file.inputStream()).use {
            InputStreamHelpers.skipFully(it, offset.toLong())
            if (is32Bits) {
                p_type = InputStreamHelpers.readUInt(it, isLittleEndian)
                p_offset = InputStreamHelpers.readUInt(it, isLittleEndian).toULong()
                p_vaddr = InputStreamHelpers.readUInt(it, isLittleEndian).toULong()
                p_paddr = InputStreamHelpers.readUInt(it, isLittleEndian).toULong()
                p_filesz = InputStreamHelpers.readUInt(it, isLittleEndian).toULong()
                p_memsz = InputStreamHelpers.readUInt(it, isLittleEndian).toULong()
                p_flags = InputStreamHelpers.readUInt(it, isLittleEndian)
                p_align = InputStreamHelpers.readUInt(it, isLittleEndian).toULong()
            }
            else {
                p_type = InputStreamHelpers.readUInt(it, isLittleEndian)
                p_flags = InputStreamHelpers.readUInt(it, isLittleEndian)
                p_offset = InputStreamHelpers.readULong(it, isLittleEndian)
                p_vaddr = InputStreamHelpers.readULong(it, isLittleEndian)
                p_paddr = InputStreamHelpers.readULong(it, isLittleEndian)
                p_filesz = InputStreamHelpers.readULong(it, isLittleEndian)
                p_memsz = InputStreamHelpers.readULong(it, isLittleEndian)
                p_align = InputStreamHelpers.readULong(it, isLittleEndian)
            }
        }
    }

    val typeDescription: String
        get() {
            return when (p_type) {
                ElfProgramConstants.PT_NULL -> "PT_NULL"
                ElfProgramConstants.PT_LOAD -> "PT_LOAD"
                ElfProgramConstants.PT_DYNAMIC -> "PT_DYNAMIC"
                ElfProgramConstants.PT_INTERP -> "PT_INTERP"
                ElfProgramConstants.PT_NOTE -> "PT_NOTE"
                ElfProgramConstants.PT_SHLIB -> "PT_SHLIB"
                ElfProgramConstants.PT_PHDR -> "PT_PHDR"
                ElfProgramConstants.PT_TLS -> "PT_TLS"
                ElfProgramConstants.PT_NUM -> "PT_NUM"
                ElfProgramConstants.PT_LOOS -> "PT_LOOS"
                ElfProgramConstants.PT_GNU_EH_FRAME -> "PT_GNU_EH_FRAME"
                ElfProgramConstants.PT_GNU_STACK -> "PT_GNU_STACK"
                ElfProgramConstants.PT_GNU_RELRO -> "PT_GNU_RELRO"
                ElfProgramConstants.PT_GNU_PROPERTY -> "PT_GNU_PROPERTY"
                ElfProgramConstants.PT_GNU_SFRAME -> "PT_GNU_SFRAME"
                ElfProgramConstants.PT_SUNWBSS -> "PT_SUNWBSS"
                ElfProgramConstants.PT_SUNWSTACK -> "PT_SUNWSTACK"
                ElfProgramConstants.PT_HIOS -> "PT_HIOS"
                ElfProgramConstants.PT_LOPROC -> "PT_LOPROC"
                ElfProgramConstants.PT_HIPROC -> "PT_HIPROC"
                else -> "PT_UNKNOWN($p_type)"
            }
        }

    val flagsDescription: String
        get() {
            return buildList {
                if ((p_flags and ElfProgramConstants.PF_X) != 0u) add("PF_X")
                if ((p_flags and ElfProgramConstants.PF_W) != 0u) add("PF_W")
                if ((p_flags and ElfProgramConstants.PF_R) != 0u) add("PF_R")
                if ((p_flags and ElfProgramConstants.PF_MASKOS) != 0u) add("PF_MASKOS")
                if ((p_flags and ElfProgramConstants.PF_MASKPROC) != 0u) add("PF_MASKPROC")
            }.ifEmpty { listOf("PF_UNKNOWN($p_flags)") }.joinToString("|")
        }

    override fun toString(): String {
        return "ElfProgramHeader(" +
                "p_type=\"$typeDescription\"" +
                ", p_flags=\"$flagsDescription\"" +
                ", p_offset=$p_offset" +
                ", p_vaddr=$p_vaddr" +
                ", p_paddr=$p_paddr" +
                ", p_filesz=$p_filesz" +
                ", p_memsz=$p_memsz" +
                ", p_align=$p_align" +
                ")"
    }
}
