package com.riraki.kelf

class ElfNoteHeader(
    val n_namesz: UInt,
    val n_descsz: UInt,
    val n_type: UInt
) {
    val typeDescription: String
        get() {
            return when (n_type) {
                ElfNoteConstants.NT_GNU_ABI_TAG -> "NT_GNU_ABI_TAG"
                ElfNoteConstants.NT_GNU_HWCAP -> "NT_GNU_HWCAP"
                ElfNoteConstants.NT_GNU_BUILD_ID -> "NT_GNU_BUILD_ID"
                ElfNoteConstants.NT_GNU_GOLD_VERSION -> "NT_GNU_GOLD_VERSION"
                ElfNoteConstants.NT_GNU_PROPERTY_TYPE_0 -> "NT_GNU_PROPERTY_TYPE_0"
                else -> "NT_UNKNOWN(${n_type})"
            }
        }

    override fun toString(): String {
        return "ElfNoteHeader(" +
                "n_namesz=$n_namesz" +
                ", n_descsz=$n_descsz" +
                ", n_type=\"$typeDescription\"" +
                ")"
    }
}
