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
            nbucket = InputStreamHelpers.readUInt(it, elfFile.isLittleEndian)
            nchain = InputStreamHelpers.readUInt(it, elfFile.isLittleEndian)
            buckets = InputStreamHelpers.readIntArrayFully(it, nbucket.toInt(), elfFile.isLittleEndian)
            chains = InputStreamHelpers.readIntArrayFully(it, nchain.toInt(), elfFile.isLittleEndian)
        }
    }

    fun lookupSymbols(name: String, symbols: Array<ElfSymbol>): ElfSymbol? {
        val bucketIndex = (elfHash(name) % nbucket).toInt()
        var symbolIndex = buckets[bucketIndex]

        while (symbolIndex != 0) {
            val symbol = symbols[symbolIndex]

            if (symbol.name == name) {
                return symbol
            }

            symbolIndex = chains[symbolIndex]
        }

        return null
    }

    companion object {
        /**
         * 因为 elf_hash 本身就是一个 32 位环境的函数，所以这里的 long 本身就是 4 字节
         */
        fun elfHash(name: String): UInt {
            var h = 0u
            name.forEach { ch ->
                h = (h shl 4) + ch.code.toUInt()
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
