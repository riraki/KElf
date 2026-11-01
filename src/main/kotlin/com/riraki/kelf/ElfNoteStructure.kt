package com.riraki.kelf

class ElfNoteStructure(
    val noteHeader: ElfNoteHeader,
    val name: String,
    val descriptor: ByteArray
) {
    /**
     * 如果这是一个 Build ID 类型的 Note，将其描述符转为可读的 16 进制字符串
     */
    val buildIdHex: String
        get() {
            if (noteHeader.n_type == ElfNoteConstants.NT_GNU_BUILD_ID && name == "GNU") {
                return descriptor.joinToString("") { byte ->
                    (byte.toInt() and 0xFF)
                        .toString(16)
                        .padStart(2, '0')
                }
            }
            return ""
        }

    val typeDescription: String
        get() {
            return when (name) {
                "GNU" -> when (noteHeader.n_type) {
                    ElfNoteConstants.NT_GNU_ABI_TAG -> "NT_GNU_ABI_TAG"
                    ElfNoteConstants.NT_GNU_HWCAP -> "NT_GNU_HWCAP"
                    ElfNoteConstants.NT_GNU_BUILD_ID -> "NT_GNU_BUILD_ID"
                    ElfNoteConstants.NT_GNU_GOLD_VERSION -> "NT_GNU_GOLD_VERSION"
                    ElfNoteConstants.NT_GNU_PROPERTY_TYPE_0 -> "NT_GNU_PROPERTY_TYPE_0"
                    else -> "NT_GNU_UNKNOWN(${noteHeader.n_type})"
                }

                "Android" -> when (noteHeader.n_type) {
                    ElfNoteConstants.NT_VERSION -> "NT_VERSION"
                    else -> "NT_ANDROID_UNKNOWN(${noteHeader.n_type})"
                }

                else -> "NT_UNKNOWN(${noteHeader.n_type})"
            }
        }

    override fun toString(): String {
        val detail = if (noteHeader.n_type == ElfNoteConstants.NT_GNU_BUILD_ID) {
            "buildId=$buildIdHex"
        }
        else {
            "descSize=${descriptor.size} bytes"
        }
        return "ElfNoteStructure(" +
                "noteHeader=$noteHeader" +
                ", name=\"$name\"" +
                ", type=\"$typeDescription\"" +
                ", $detail" +
                ")"
    }
}
