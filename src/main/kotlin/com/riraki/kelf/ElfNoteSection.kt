package com.riraki.kelf

import java.io.BufferedInputStream
import java.io.EOFException

class ElfNoteSection(
    elfFile: ElfFile,
    noteSectionHeader: ElfSectionHeader
) : ElfSection(elfFile, noteSectionHeader) {

    val entries: Array<ElfNoteStructure> by lazy {
        parseEntries(elfFile, noteSectionHeader)
    }

    companion object {
        private fun parseEntries(elfFile: ElfFile, noteSectionHeader: ElfSectionHeader): Array<ElfNoteStructure> {
            val noteStructures = mutableListOf<ElfNoteStructure>()
            BufferedInputStream(elfFile.file.inputStream()).use {
                InputStreamHelpers.skipFully(it, noteSectionHeader.sh_offset.toLong())

                var bytesRead = 0L
                val sectionSize: Long = noteSectionHeader.sh_size.toLong()

                // 循环解析 Note 节区内的所有 Note 条目（可能包含多个）
                while (bytesRead < sectionSize) {
                    if (sectionSize - bytesRead < 12L) {
                        throw EOFException("Incomplete ElfNoteStructure at end of NOTE section")
                    }
                    // 读取 12 字节的 ElfNoteHeader 头部 (3 个 UInt)
                    val n_namesz = InputStreamHelpers.readUInt(it, elfFile.isLittleEndian)
                    val n_descsz = InputStreamHelpers.readUInt(it, elfFile.isLittleEndian)
                    val n_type = InputStreamHelpers.readUInt(it, elfFile.isLittleEndian)
                    bytesRead += 12

                    val noteHeader = ElfNoteHeader(n_namesz, n_descsz, n_type)

                    // 读取 name 字段（必须向上对齐到 4 字节的倍数）
                    val paddedNamesz = (n_namesz + 3u) and 3u.inv()
                    val nameBytes: ByteArray = InputStreamHelpers.readByteArrayFully(it, paddedNamesz.toInt())
                    bytesRead += paddedNamesz.toLong()
                    val name = String(nameBytes, Charsets.UTF_8).trimEnd('\u0000')

                    // 读取 descriptor 描述符字段（必须向上对齐到 4 字节的倍数）
                    val paddedDescsz = (n_descsz + 3u) and 3u.inv()
                    val descBytes: ByteArray = InputStreamHelpers.readByteArrayFully(it, paddedDescsz.toInt())
                    bytesRead += paddedDescsz.toLong()

                    // 截取真实的 descsz 长度的描述符内容
                    val actualDesc = descBytes.copyOfRange(0, n_descsz.toInt())

                    noteStructures.add(ElfNoteStructure(noteHeader, name, actualDesc))
                }
            }
            return noteStructures.toTypedArray()
        }
    }

    override fun toString(): String {
        return "ElfNoteSection(" +
                "entryCount=${entries.size}" +
                ", entries=${entries.contentToString()}" +
                ")"
    }
}
