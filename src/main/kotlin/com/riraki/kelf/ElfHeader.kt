package com.riraki.kelf

import java.io.BufferedInputStream
import java.io.File

/**
 * reference from https://www.sco.com/developers/gabi/latest/ch4.eheader.html
 * 虽然说，Elf32_Ehdr 和 Elf64_Ehdr 的部分数据成员，占有的字节数是不一样的，但是直接分配最大的那个，并判断 ei_data 便能解决这个问题
 * 所以 Elf 文件头类统一定义为 ElfHeader 类
 */
class ElfHeader {
    val ei_mag: ByteArray           // ELF 魔数，用于识别文件格式
    val ei_class: UByte             // 用于识别64位或32位
    val ei_data: UByte              // 字节序
    val ei_version: UByte           // ELF 标识版本
    val ei_osabi: UByte             // System V ABI 标识
    val ei_abiversion: UByte        // ABI 版本字段

    val ei_pad: ByteArray           // 保留填充字节

    val ei_nident: UByte            // 填充字节的长度

    val e_type: UShort              // 文件类型
    val e_machine: UShort           // 目标架构

    val e_version: UInt             // 文件版本

    val e_entry: ULong              // 程序入口地址 ULong/UInt
    val e_phoff: ULong              // 程序头表起始偏移 ULong/UInt
    val e_shoff: ULong              // 节头表起始偏移 ULong/UInt

    val e_flags: UInt               // 处理器相关标志

    val e_ehsize: UShort            // ELF Header 自身的大小
    val e_phentsize: UShort         // 每个程序头条目的大小
    val e_phnum: UShort             // 程序头条目数量
    val e_shentsize: UShort         // 每个节头条目的大小
    val e_shnum: UShort             // 节头条目数量
    val e_shstrndx: UShort          // 节名称字符串表的索引

    val elfMachine: ElfMachine
        get() = ElfMachine.fromId(e_machine.toInt())

    constructor(elfFile: ElfFile) {
        val file: File = elfFile.file
        BufferedInputStream(file.inputStream()).use {
            ei_mag = InputStreamHelpers.readByteArrayFully(it, 4)
            ei_class = InputStreamHelpers.readCheckedByte(it).toUByte()
            // 当 ei_class 非法时，抛出异常
            validateClass()
            ei_data = InputStreamHelpers.readCheckedByte(it).toUByte()
            // 当 ei_data 非法时，抛出异常
            validateEndianness()
            ei_version = InputStreamHelpers.readCheckedByte(it).toUByte()
            ei_osabi = InputStreamHelpers.readCheckedByte(it).toUByte()
            ei_abiversion = InputStreamHelpers.readCheckedByte(it).toUByte()
            ei_pad = InputStreamHelpers.readByteArrayFully(it, 6)
            ei_nident = InputStreamHelpers.readCheckedByte(it).toUByte()
            e_type = InputStreamHelpers.readUShort(it, isLittleEndian)
            e_machine = InputStreamHelpers.readUShort(it, isLittleEndian)
            e_version = InputStreamHelpers.readUInt(it, isLittleEndian)
            // 如果是 32 位文件就读取 UInt 长度的数据，否则读取 ULong 长度的数据
            e_entry = ElfParserHelpers.readUIntOrULong(it, is32Bits, isLittleEndian)
            e_phoff = ElfParserHelpers.readUIntOrULong(it, is32Bits, isLittleEndian)
            e_shoff = ElfParserHelpers.readUIntOrULong(it, is32Bits, isLittleEndian)
            e_flags = InputStreamHelpers.readUInt(it, isLittleEndian)
            e_ehsize = InputStreamHelpers.readUShort(it, isLittleEndian)
            e_phentsize = InputStreamHelpers.readUShort(it, isLittleEndian)
            e_phnum = InputStreamHelpers.readUShort(it, isLittleEndian)
            e_shentsize = InputStreamHelpers.readUShort(it, isLittleEndian)
            e_shnum = InputStreamHelpers.readUShort(it, isLittleEndian)
            e_shstrndx = InputStreamHelpers.readUShort(it, isLittleEndian)
        }
    }

    /**
     * 判断当前 ELF 文件是否为小端序 (Little-Endian)
     */
    val isLittleEndian: Boolean
        get() = ei_data.toInt() == ELFDATA2LSB

    /**
     * 判断当前 ELF 文件是否为大端序 (Big-Endian)
     */
    val isBigEndian: Boolean
        get() = ei_data.toInt() == ELFDATA2MSB

    /**
     * 在解析 ELF Header 的初期调用，用来提前拦截非法字节序的文件
     */
    fun validateEndianness() {
        val data = ei_data.toInt()
        if (data != ELFDATA2LSB && data != ELFDATA2MSB) {
            throw IllegalArgumentException("Unsupported or invalid ELF data encoding: $data (Expected LSB or MSB)")
        }
    }

    /**
     * 判断是否为 32 位文件
     */
    val is32Bits: Boolean
        get() = ei_class.toInt() == ELFCLASS32

    /**
     * 判断是否为 64 位文件
     */
    val is64Bits: Boolean
        get() = ei_class.toInt() == ELFCLASS64

    /**
     * 在解析 ELF Header 的初期调用，用来提前拦截非法的文件
     * 校验 ELF Class 是否合法
     */
    fun validateClass() {
        val clazz = ei_class.toInt()
        if (clazz != ELFCLASS32 && clazz != ELFCLASS64) {
            throw IllegalArgumentException("Unsupported or invalid ELF class: $clazz (Expected 32-bit or 64-bit)")
        }
    }

    val classDescription: String
        get() = when (ei_class.toInt()) {
            ELFCLASS32 -> "ELFCLASS32"
            ELFCLASS64 -> "ELFCLASS64"
            else -> "ELFCLASS_UNKNOWN($ei_class)"
        }

    val dataDescription: String
        get() = when (ei_data.toInt()) {
            ELFDATA2LSB -> "ELFDATA2LSB (Little-Endian)"
            ELFDATA2MSB -> "ELFDATA2MSB (Big-Endian)"
            else -> "ELFDATA_UNKNOWN($ei_data)"
        }

    val machineDescription: String
        get() = elfMachine.description

    override fun toString(): String {
        return "ElfHeader(" +
                "ei_mag=${FormatHelpers.toHexSpaceString(ei_mag)}" +
                ", ei_class=\"$classDescription\"" +
                ", ei_data=\"$dataDescription\"" +
                ", ei_version=$ei_version" +
                ", ei_osabi=$ei_osabi" +
                ", ei_abiversion=$ei_abiversion" +
                ", ei_pad=${FormatHelpers.toHexSpaceString(ei_pad)}" +
                ", ei_nident=$ei_nident" +
                ", e_type=$e_type" +
                ", e_machine=\"$machineDescription\"" +
                ", e_version=$e_version, e_entry=$e_entry" +
                ", e_phoff=$e_phoff" +
                ", e_shoff=$e_shoff" +
                ", e_flags=$e_flags" +
                ", e_ehsize=$e_ehsize" +
                ", e_phentsize=$e_phentsize" +
                ", e_phnum=$e_phnum" +
                ", e_shentsize=$e_shentsize" +
                ", e_shnum=$e_shnum" +
                ", e_shstrndx=$e_shstrndx" +
                ")"
    }

    companion object {
        private const val ELFDATA2LSB = 1
        private const val ELFDATA2MSB = 2

        private const val ELFCLASS32 = 1
        private const val ELFCLASS64 = 2
    }
}
