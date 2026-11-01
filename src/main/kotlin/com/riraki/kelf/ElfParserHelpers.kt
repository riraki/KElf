package com.riraki.kelf

import java.io.InputStream

object ElfParserHelpers {
    /**
     * 如果该 ELF 文件是 32 位的，那么读取 UInt(4字节) 数据转化为 ULong 返回，
     * 如果该 ELF 文件是 64 位的，那么读取 ULong(8字节) 数据返回
     * @param inputStream 用来读取数据的输入流
     * @return 读取到的 UInt 数据或者 ULong，统一存入 ULong 返回
     */
    fun readUIntOrULong(inputStream: InputStream, is32Bits: Boolean, isLittleEndian: Boolean): ULong {
        return if (is32Bits) {
            InputStreamHelpers.readUInt(inputStream, isLittleEndian).toULong()
        }
        else {
            InputStreamHelpers.readULong(inputStream, isLittleEndian)
        }
    }

    /**
     * 如果该 ELF 文件是 32 位的，那么读取 Int(4字节) 数据转化为 Long 返回，
     * 如果该 ELF 文件是 64 位的，那么读取 Long(8字节) 数据返回
     * @param inputStream 用来读取数据的输入流
     * @return 读取到的 Int 数据或者 Long，统一存入 Long 返回
     */
    fun readIntOrLong(inputStream: InputStream, is32Bits: Boolean, isLittleEndian: Boolean): Long {
        return if (is32Bits) {
            InputStreamHelpers.readInt(inputStream, isLittleEndian).toLong()
        }
        else {
            InputStreamHelpers.readLong(inputStream, isLittleEndian)
        }
    }

    /**
     * 如果该 ELF 文件是 32 位的，那么读取 Int(4字节) 数据转化为 Long，放入待返回的数组
     * 如果该 ELF 文件是 64 位的，那么读取 Long(8字节) 数据，放入待返回的数组
     * @param inputStream 用来读取数据的输入流
     * @return 读取到的 Int 数据或者 Long 数组，统一存入 LongArray 返回
     */
    fun readIntOrLongArray(inputStream: InputStream, n: Int, is32Bits: Boolean, isLittleEndian: Boolean): LongArray {
        return LongArray(n) {
            readIntOrLong(inputStream, is32Bits, isLittleEndian)
        }
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    fun convertBytesToUBytes(sbytes: ByteArray, sOff: Int, ubytes: UByteArray, uOff: Int, size: Int) {
        for (i in 0..<size) {
            ubytes[uOff + i] = sbytes[sOff + i].toUByte()
        }
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    fun convertBytesToUBytes(sbytes: ByteArray, ubytes: UByteArray) {
        if (sbytes.size == ubytes.size) {
            convertBytesToUBytes(sbytes, 0, ubytes, 0, sbytes.size)
        }
        else {
            throw IllegalArgumentException("ByteArray size (${sbytes.size}) must match UByteArray size (${ubytes.size})")
        }
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    fun convertBytesToUBytes(sbytes: ByteArray): UByteArray {
        val ubytes = UByteArray(sbytes.size)
        convertBytesToUBytes(sbytes, ubytes)
        return ubytes
    }
}
