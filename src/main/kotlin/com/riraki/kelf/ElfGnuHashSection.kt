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

    private val mStringTable: ElfStringTableSection
    private val mSymbols: Array<ElfSymbol>

    private val ELFCLASS_BITS: UInt

    private val chainSize: UInt

    constructor(elfFile: ElfFile, gnuHashSectionHeader: ElfSectionHeader, stringTable: ElfStringTableSection) : super(elfFile, gnuHashSectionHeader) {
        val symbolTableSectionHeader: ElfSectionHeader = elfFile.sectionHeaders[gnuHashSectionHeader.sh_link.toInt()]
        val symbolTableSection = ElfSymbolTableSection(elfFile, symbolTableSectionHeader)
        mSymbols = symbolTableSection.symbols
        val symbolCount = mSymbols.size.toUInt()
        BufferedInputStream(elfFile.file.inputStream()).use {
            InputStreamHelpers.skipFully(it, gnuHashSectionHeader.sh_offset.toLong())

            nbucket = InputStreamHelpers.readUInt(it, elfFile.isLittleEndian)
            symoffset = InputStreamHelpers.readUInt(it, elfFile.isLittleEndian)
            bloom_size = InputStreamHelpers.readUInt(it, elfFile.isLittleEndian)
            bloom_shift = InputStreamHelpers.readUInt(it, elfFile.isLittleEndian)
            bloom = ElfParserHelpers.readIntOrLongArray(it, bloom_size.toInt(), elfFile.is32Bits, elfFile.isLittleEndian)
            buckets = InputStreamHelpers.readIntArrayFully(it, nbucket.toInt(), elfFile.isLittleEndian)
            // remember that chains' indexes are shifted by symoffset relatively to the symbol table
            // chainSize = symbols.size - symoffset
            if (symoffset > symbolCount) {
                throw IllegalStateException("Invalid GNU Hash symoffset: $symoffset!")
            }
            chainSize = symbolCount - symoffset
            chain = InputStreamHelpers.readIntArrayFully(it, chainSize.toInt(), elfFile.isLittleEndian)
        }

        ELFCLASS_BITS = if (elfFile.is32Bits) {
            32u
        }
        else {
            64u
        }

        mStringTable = stringTable
    }

    fun lookupSymbol(name: String): ElfSymbol? {
        val nameHash: UInt = gnuHash(name)

        val word: ULong = bloom[((nameHash / ELFCLASS_BITS) % bloom_size).toInt()].toULong()
        val bit1: ULong = 1UL shl (nameHash % ELFCLASS_BITS).toInt()
        val bit2: ULong = 1UL shl ((nameHash shr bloom_shift.toInt()) % ELFCLASS_BITS).toInt()
        val mask: ULong = bit1 or bit2

        /* If at least one bit is not set, a symbol is surely missing. */
        if ((word and mask) != mask) {
            return null
        }

        var symix: UInt = buckets[(nameHash % nbucket).toInt()].toUInt()
        if (symix < symoffset) {
            return null
        }

        /* Loop through the chain. */
        while (true) {
            val symname: String = mStringTable.getString(mSymbols[symix.toInt()].st_name.toInt())
            val hash: UInt = chain[(symix - symoffset).toInt()].toUInt()

            if ((nameHash or 1u) == (hash or 1u) && name == symname) {
                return mSymbols[symix.toInt()]
            }

            /* Chain ends with an element with the lowest bit set to 1. */
            if ((hash and 1u) != 0u) {
                break
            }

            ++symix
        }

        return null
    }

    companion object {
        fun gnuHash(name: String): UInt {
            var h = 5381u

            for (i in name.indices) {
                h = (h shl 5) + h + name[i].code.toUInt()
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
