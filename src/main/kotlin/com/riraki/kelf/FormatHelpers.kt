package com.riraki.kelf

object FormatHelpers {
    @OptIn(ExperimentalUnsignedTypes::class)
    fun toHexSpaceString(ubytes: UByteArray): String {
        return ubytes.joinToString(", ", "[", "]",
            transform = { ubyte ->
                (ubyte.toInt() and 0xff).toString(16).padStart(2, '0').uppercase()
            })
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    fun toHexSpaceString(bytes: ByteArray): String {
        return toHexSpaceString(ElfParserHelpers.convertBytesToUBytes(bytes))
    }

    fun toHexString(value: Byte): String {
        return "0x${value.toString(16).uppercase()}"
    }

    fun toHexString(value: UByte): String {
        return "0x${value.toString(16).uppercase()}"
    }

    fun toHexString(value: Short): String {
        return "0x${value.toString(16).uppercase()}"
    }

    fun toHexString(value: UShort): String {
        return "0x${value.toString(16).uppercase()}"
    }

    fun toHexString(value: Int): String {
        return "0x${value.toString(16).uppercase()}"
    }

    fun toHexString(value: UInt): String {
        return "0x${value.toString(16).uppercase()}"
    }

    fun toHexString(value: Long): String {
        return "0x${value.toString(16).uppercase()}"
    }

    fun toHexString(value: ULong): String {
        return "0x${value.toString(16).uppercase()}"
    }
}
