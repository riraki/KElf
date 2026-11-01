package com.riraki.kelf

import java.io.BufferedInputStream
import java.io.File

class ElfSymbol {
    val st_name: UInt           // 符号名称在字符串表中的偏移
    val st_info: UByte          // 符号绑定属性和符号类型
    val st_other: UByte         // 符号可见性等信息
    val st_shndx: UShort        // 符号所属节的索引或特殊索引
    val st_value: ULong         // 符号值，通常用于表示地址或其他符号相关数值 UInt/ULong
    val st_size: ULong          // 符号对应实体的大小 UInt/ULong

    private val mElfFile: ElfFile
    private val mSymbolSectionHeader: ElfSectionHeader

    constructor(elfFile: ElfFile, symbolSectionHeader: ElfSectionHeader, symbolOffset: ULong) {
        val file: File = elfFile.file
        val is32Bits = elfFile.is32Bits
        val isLittleEndian = elfFile.isLittleEndian

        BufferedInputStream(file.inputStream()).use {
            InputStreamHelpers.skipFully(it, symbolOffset.toLong())
            if (is32Bits) {
                st_name = InputStreamHelpers.readUInt(it, isLittleEndian)
                st_value = InputStreamHelpers.readUInt(it, isLittleEndian).toULong()
                st_size = InputStreamHelpers.readUInt(it, isLittleEndian).toULong()
                st_info = InputStreamHelpers.readCheckedByte(it).toUByte()
                st_other = InputStreamHelpers.readCheckedByte(it).toUByte()
                st_shndx = InputStreamHelpers.readUShort(it, isLittleEndian)
            }
            else {
                st_name = InputStreamHelpers.readUInt(it, isLittleEndian)
                st_info = InputStreamHelpers.readCheckedByte(it).toUByte()
                st_other = InputStreamHelpers.readCheckedByte(it).toUByte()
                st_shndx = InputStreamHelpers.readUShort(it, isLittleEndian)
                st_value = InputStreamHelpers.readULong(it, isLittleEndian)
                st_size = InputStreamHelpers.readULong(it, isLittleEndian)
            }
        }

        this.mElfFile = elfFile
        this.mSymbolSectionHeader = symbolSectionHeader
    }

    val name: String
        get() = nameDescription

    val nameDescription: String
        get() {
            val stringTable = mElfFile.sections[mSymbolSectionHeader.sh_link.toInt()] as? ElfStringTableSection
                ?: throw IllegalArgumentException("Index ${mSymbolSectionHeader.sh_link} is not an ElfStringTable")
            return stringTable.getString(st_name.toInt())
        }

    val infoDescription: String
        get() {
            val info = st_info.toInt()
            val binding = info ushr 4
            val type = info and 0x0f

            val bindingStr = when (binding) {
                ElfSymbolConstants.STB_LOCAL -> "STB_LOCAL"
                ElfSymbolConstants.STB_GLOBAL -> "STB_GLOBAL"
                ElfSymbolConstants.STB_WEAK -> "STB_WEAK"
                ElfSymbolConstants.STB_NUM -> "STB_NUM"
                ElfSymbolConstants.STB_GNU_UNIQUE -> "STB_GNU_UNIQUE"
                ElfSymbolConstants.STB_HIOS -> "STB_HIOS"
                ElfSymbolConstants.STB_LOPROC -> "STB_LOPROC"
                ElfSymbolConstants.STB_HIPROC -> "STB_HIPROC"
                else -> "STB_UNKNOWN($binding)"
            }

            val typeStr = when (type) {
                ElfSymbolConstants.STT_NOTYPE -> "STT_NOTYPE"
                ElfSymbolConstants.STT_OBJECT -> "STT_OBJECT"
                ElfSymbolConstants.STT_FUNC -> "STT_FUNC"
                ElfSymbolConstants.STT_SECTION -> "STT_SECTION"
                ElfSymbolConstants.STT_FILE -> "STT_FILE"
                ElfSymbolConstants.STT_COMMON -> "STT_COMMON"
                ElfSymbolConstants.STT_TLS -> "STT_TLS"
                ElfSymbolConstants.STT_NUM -> "STT_NUM"
                ElfSymbolConstants.STT_GNU_IFUNC -> "STT_GNU_IFUNC"
                ElfSymbolConstants.STT_HIOS -> "STT_HIOS"
                ElfSymbolConstants.STT_LOPROC -> "STT_LOPROC"
                ElfSymbolConstants.STT_HIPROC -> "STT_HIPROC"
                else -> "STT_UNKNOWN($type)"
            }
            return "$bindingStr|$typeStr"
        }

    val otherDescription: String
        get() {
            val otherLow2Bits = st_other.toInt() and 0x03
            return when (otherLow2Bits) {
                ElfSymbolConstants.STV_DEFAULT -> "STV_DEFAULT"
                ElfSymbolConstants.STV_INTERNAL -> "STV_INTERNAL"
                ElfSymbolConstants.STV_HIDDEN -> "STV_HIDDEN"
                ElfSymbolConstants.STV_PROTECTED -> "STV_PROTECTED"
                else -> "STV_UNKNOWN($otherLow2Bits)"
            }
        }

    override fun toString(): String {
        return "ElfSymbol(" +
                "st_name=\"$nameDescription\"" +
                ", st_info=\"$infoDescription\"" +
                ", st_other=\"$otherDescription\"" +
                ", st_shndx=$st_shndx" +
                ", st_value=$st_value" +
                ", st_size=$st_size" +
                ")"
    }
}
