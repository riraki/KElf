package com.riraki.kelf

import java.io.BufferedInputStream

class ElfRelocationAppend {
    val r_offset: ULong     // 需要进行重定位的位置 UInt/ULong
    val r_info: ULong       // 重定位类型（低32位）与符号索引（高32位） UInt/ULong
    val r_addend: Long      // 参与计算的附加常量 Int/Long

    private val mElfFile: ElfFile
    private val mSymbolTable: ElfSymbolTableSection?

    constructor(elfFile: ElfFile, relocationAppendOffset: ULong, symbolTable: ElfSymbolTableSection?) {
        BufferedInputStream(elfFile.file.inputStream()).use {
            InputStreamHelpers.skipFully(it, relocationAppendOffset.toLong())
            r_offset = ElfParserHelpers.readUIntOrULong(it, elfFile.is32Bits, elfFile.isLittleEndian)
            r_info = ElfParserHelpers.readUIntOrULong(it, elfFile.is32Bits, elfFile.isLittleEndian)
            r_addend = ElfParserHelpers.readIntOrLong(it, elfFile.is32Bits, elfFile.isLittleEndian)
        }

        mElfFile = elfFile
        mSymbolTable = symbolTable
    }

    /**
     * 获取重定位类型
     */
    val relocationType: UInt
        get() {
            return if (mElfFile.is32Bits) {
                (r_info and 0xFFu).toUInt()
            }
            else {
                (r_info and 0xffffffffUL).toUInt()
            }
        }

    /**
     * 获取符号索引
     */
    val symbolIndex: UInt
        get() {
            return if (mElfFile.is32Bits) {
                (r_info shr 8).toUInt()
            }
            else {
                (r_info shr 32).toUInt()
            }
        }

    val relocationTypeDescription: String
        get() {
            return when (mElfFile.elfHeader.elfMachine) {
                ElfMachine.EM_ARM -> ElfRelocationNameResolver.getArmName(relocationType)
                ElfMachine.EM_AARCH64 -> ElfRelocationNameResolver.getAArchName(relocationType)
                ElfMachine.EM_386 -> ElfRelocationNameResolver.getX86Name(relocationType)
                ElfMachine.EM_X86_64 -> ElfRelocationNameResolver.getX86_64Name(relocationType)
            }
        }

    /**
     * 如果 mSymbolTable 不为 null，那么根据 symbolIndex 获取 symbol table 中对应的字符串
     * 如果 mSymbolTable 为 null，那么获取到的将是 symbolIndex 转化成 16 进制数的字符串
     */
    val symbolDescription: String
        get() {
            return mSymbolTable?.getString(symbolIndex.toInt())
                ?: FormatHelpers.toHexString(symbolIndex)
        }

    override fun toString(): String {
        return "ElfRelocationAppend(" +
                "r_offset=${FormatHelpers.toHexString(r_offset)}" +
                ", r_info=${FormatHelpers.toHexString(r_info)}" +
                ", relocationType=\"$relocationTypeDescription\"" +
                ", symbolIndex=${FormatHelpers.toHexString(symbolIndex)}" +
                ", symbol=\"$symbolDescription\"" +
                ", r_addend=$r_addend" +
                ")"
    }
}
