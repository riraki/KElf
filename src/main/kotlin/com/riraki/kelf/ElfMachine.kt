package com.riraki.kelf

enum class ElfMachine(val id: Int, val description: String) {
    EM_386(3, "Intel 80386"),
    EM_ARM(40, "ARM 32-bit"),
    EM_X86_64(62, "AMD x86-64"),
    EM_AARCH64(183, "ARM AArch64");

    companion object {
        fun fromId(id: Int): ElfMachine {
            return entries.find { it.id == id }
                ?: throw UnsupportedOperationException("Unsupported ELF machine architecture ID: $id")
        }
    }
}
