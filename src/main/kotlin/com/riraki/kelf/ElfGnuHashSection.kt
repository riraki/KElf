package com.riraki.kelf

import java.io.BufferedInputStream

/**
 * Reference from:
 *     https://flapenguin.me/elf-dt-gnu-hash
 *     https://blogs.oracle.com/solaris/gnu-hash-elf-sections
 */
class ElfGnuHashSection : ElfSection {

    private val nbucket: UInt
    private val symoffset: UInt
    private val bloom_size: UInt
    private val bloom_shift: UInt
    private val bloom: LongArray    // LongArray/IntArray
    private val buckets: IntArray
    private val chain: IntArray

    private val mSymbols: Array<ElfSymbol>

    private val ELFCLASS_BITS: UInt

    private val chainSize: UInt

    constructor(elfFile: ElfFile, gnuHashSectionHeader: ElfSectionHeader) : super(elfFile, gnuHashSectionHeader) {
        require(gnuHashSectionHeader.sh_link < elfFile.sectionHeaders.size.toUInt()) {
            "Invalid GNU Hash sh_link: ${gnuHashSectionHeader.sh_link}"
        }

        val symbolTableSectionHeader = elfFile.sectionHeaders[gnuHashSectionHeader.sh_link.toInt()]
        require(symbolTableSectionHeader.sh_type == ElfSectionConstants.SHT_DYNSYM) {
            "GNU Hash sh_link must refer to a dynamic symbol table"
        }

        val symbolTableSection = ElfSymbolTableSection(elfFile, symbolTableSectionHeader)
        mSymbols = symbolTableSection.symbols
        val symbolCount = mSymbols.size.toUInt()

        ELFCLASS_BITS = if (elfFile.is32Bits) 32u else 64u

        BufferedInputStream(elfFile.file.inputStream()).use {
            InputStreamHelpers.skipFully(it, gnuHashSectionHeader.sh_offset.toLong())

            nbucket = InputStreamHelpers.readUInt(it, elfFile.isLittleEndian)
            symoffset = InputStreamHelpers.readUInt(it, elfFile.isLittleEndian)
            bloom_size = InputStreamHelpers.readUInt(it, elfFile.isLittleEndian)
            bloom_shift = InputStreamHelpers.readUInt(it, elfFile.isLittleEndian)

            require(nbucket > 0u) { "Invalid GNU Hash nbucket: $nbucket" }
            require(bloom_size > 0u) { "Invalid GNU Hash bloom_size: $bloom_size" }
            require((bloom_size and (bloom_size - 1u)) == 0u) {
                "GNU Hash bloom_size must be a power of two"
            }
            require(bloom_shift < 32u) { "Invalid GNU Hash bloom_shift: $bloom_shift" }
            require(nbucket <= Int.MAX_VALUE.toUInt() && bloom_size <= Int.MAX_VALUE.toUInt()) {
                "GNU Hash table is too large"
            }
            require(symoffset <= symbolCount) {
                "Invalid GNU Hash symoffset: $symoffset"
            }

            chainSize = symbolCount - symoffset
            val bloomWordSize = if (elfFile.is32Bits) 4UL else 8UL
            val requiredSize = 16UL +
                    bloom_size.toULong() * bloomWordSize +
                    nbucket.toULong() * 4UL +
                    chainSize.toULong() * 4UL
            require(requiredSize <= gnuHashSectionHeader.sh_size) {
                "GNU Hash table exceeds its section size"
            }

            bloom = ElfParserHelpers.readIntOrLongArray(
                it,
                bloom_size.toInt(),
                elfFile.is32Bits,
                elfFile.isLittleEndian
            )
            buckets = InputStreamHelpers.readIntArrayFully(
                it,
                nbucket.toInt(),
                elfFile.isLittleEndian
            )
            // Chain indices are relative to symoffset, not to symbol-table index zero.
            chain = InputStreamHelpers.readIntArrayFully(
                it,
                chainSize.toInt(),
                elfFile.isLittleEndian
            )
        }
    }

    fun lookupSymbol(name: String): ElfSymbol? {
        val nameHash: UInt = gnuHash(name)

        val bloomIndex = ((nameHash / ELFCLASS_BITS) % bloom_size).toInt()
        val word: ULong = bloom[bloomIndex].toULong()
        val bit1 = 1UL shl (nameHash % ELFCLASS_BITS).toInt()
        val bit2 = 1UL shl ((nameHash shr bloom_shift.toInt()) % ELFCLASS_BITS).toInt()
        val mask = bit1 or bit2

        // If at least one bit is not set, the symbol is definitely absent.
        if ((word and mask) != mask) {
            return null
        }

        val bucketIndex = (nameHash % nbucket).toInt()
        var symix = buckets[bucketIndex].toUInt()
        if (symix < symoffset || symix >= mSymbols.size.toUInt()) {
            return null
        }

        // Chain indices are relative to symoffset.
        var visited = 0
        while (symix < mSymbols.size.toUInt()) {
            val chainIndex = (symix - symoffset).toInt()
            if (chainIndex !in chain.indices || visited >= chain.size) {
                return null
            }

            val symbol = mSymbols[symix.toInt()]
            val hash = chain[chainIndex].toUInt()

            if ((nameHash or 1u) == (hash or 1u) && name == symbol.name) {
                return symbol
            }

            // The low bit marks the final hash in this bucket's chain.
            if ((hash and 1u) != 0u) {
                break
            }

            ++symix
            ++visited
        }

        return null
    }

    companion object {
        fun gnuHash(name: String): UInt {
            var h = 5381u

            for (byte in name.toByteArray(Charsets.UTF_8)) {
                h = (h shl 5) + h + (byte.toInt() and 0xff).toUInt()
            }

            return h
        }
    }

    override fun toString(): String {
        return "ElfGnuHashSection(" +
                "nbucket=$nbucket" +
                ", symoffset=$symoffset" +
                ", bloom_size=$bloom_size" +
                ", bloom_shift=$bloom_shift" +
                ", bloom=${bloom.contentToString()}" +
                ", buckets=${buckets.contentToString()}" +
                ", chainSize=$chainSize" +
                ", chain=${chain.contentToString()}" +
                ")"
    }
}
