package com.riraki.kelf

import java.io.BufferedInputStream

/**
 * Reference from:
 *     https://www.sco.com/developers/gabi/latest/ch5.dynamic.html#hash
 *     https://gabi.xinuos.com/elf.pdf
 */
class ElfHashSection : ElfSection {

    private val nbucket: UInt
    private val nchain: UInt
    private val buckets: IntArray
    private val chains: IntArray

    constructor(elfFile: ElfFile, hashSectionHeader: ElfSectionHeader) : super(elfFile, hashSectionHeader) {
        BufferedInputStream(elfFile.file.inputStream()).use {
            InputStreamHelpers.skipFully(it, hashSectionHeader.sh_offset.toLong())

            nbucket = InputStreamHelpers.readUInt(it, elfFile.isLittleEndian)
            nchain = InputStreamHelpers.readUInt(it, elfFile.isLittleEndian)

            require(nbucket > 0u) {
                "Invalid SysV Hash nbucket: $nbucket"
            }
            require(nbucket <= Int.MAX_VALUE.toUInt() && nchain <= Int.MAX_VALUE.toUInt()) {
                "SysV Hash table is too large"
            }

            val requiredSize = 8UL + (nbucket.toULong() + nchain.toULong()) * 4UL
            require(requiredSize <= hashSectionHeader.sh_size) {
                "SysV Hash table exceeds its section size"
            }

            buckets = InputStreamHelpers.readIntArrayFully(it, nbucket.toInt(), elfFile.isLittleEndian)
            chains = InputStreamHelpers.readIntArrayFully(it, nchain.toInt(), elfFile.isLittleEndian)
        }
    }

    fun lookupSymbols(name: String, symbols: Array<ElfSymbol>): ElfSymbol? {
        if (nbucket == 0u || nchain == 0u) {
            return null
        }

        val bucketIndex = (elfHash(name) % nbucket).toInt()
        var symbolIndex = buckets[bucketIndex].toUInt()
        var remaining = chains.size

        while (symbolIndex != 0u && remaining > 0) {
            if (symbolIndex >= nchain || symbolIndex >= symbols.size.toUInt()) {
                return null
            }

            val index = symbolIndex.toInt()
            val symbol = symbols[index]

            if (symbol.name == name) {
                return symbol
            }

            symbolIndex = chains[index].toUInt()
            remaining--
        }

        return null
    }

    companion object {
        /**
         * 因为 elf_hash 本身就是一个 32 位环境的函数，所以这里的 long 本身就是 4 字节
         */
        fun elfHash(name: String): UInt {
            var h = 0u
            for (byte in name.toByteArray(Charsets.UTF_8)) {
                h = (h shl 4) + (byte.toInt() and 0xff).toUInt()
                val g = h and 0xf0000000u
                if (g != 0u) {
                    h = h xor (g shr 24)
                }
                h = h and g.inv()
            }
            return h
        }
    }

    override fun toString(): String {
        return "ElfHashSection(" +
                "nbucket=$nbucket" +
                ", nchain=$nchain" +
                ", buckets=${buckets.contentToString()}" +
                ", chains=${chains.contentToString()}" +
                ")"
    }
}
