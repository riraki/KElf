package com.riraki.kelf

object ElfSectionConstants {
    /* Legal values for sh_type (section type).  */
    const val SHT_NULL = 0u                     /* Section header table entry unused */
    const val SHT_PROGBITS = 1u                 /* Program data */
    const val SHT_SYMTAB = 2u                   /* Symbol table */
    const val SHT_STRTAB = 3u                   /* String table */
    const val SHT_RELA = 4u                     /* Relocation entries with addends */
    const val SHT_HASH = 5u                     /* Symbol hash table */
    const val SHT_DYNAMIC = 6u                  /* Dynamic linking information */
    const val SHT_NOTE = 7u                     /* Notes */
    const val SHT_NOBITS = 8u                   /* Program space with no data (bss) */
    const val SHT_REL = 9u                      /* Relocation entries, no addends */
    const val SHT_SHLIB = 10u                   /* Reserved */
    const val SHT_DYNSYM = 11u                  /* Dynamic linker symbol table */
    const val SHT_INIT_ARRAY = 14u              /* Array of constructors */
    const val SHT_FINI_ARRAY = 15u              /* Array of destructors */
    const val SHT_PREINIT_ARRAY = 16u           /* Array of pre-constructors */
    const val SHT_GROUP = 17u                   /* Section group */
    const val SHT_SYMTAB_SHNDX = 18u            /* Extended section indices */
    const val SHT_RELR = 19u                    /* RELR relative relocations */
    const val SHT_NUM = 20u                     /* Number of defined types.  */
    const val SHT_LOOS = 0x60000000u            /* Start OS-specific.  */
    const val SHT_GNU_ATTRIBUTES = 0x6ffffff5u  /* Object attributes.  */
    const val SHT_GNU_HASH = 0x6ffffff6u        /* GNU-style hash table.  */
    const val SHT_GNU_LIBLIST = 0x6ffffff7u     /* Prelink library list */
    const val SHT_CHECKSUM = 0x6ffffff8u        /* Checksum for DSO content.  */
    const val SHT_LOSUNW = 0x6ffffffau          /* Sun-specific low bound.  */
    const val SHT_GNU_verdef = 0x6ffffffdu      /* Version definition section.  */
    const val SHT_GNU_verneed = 0x6ffffffeu     /* Version needs section.  */
    const val SHT_GNU_versym = 0x6fffffffu      /* Version symbol table.  */
    const val SHT_HISUNW = 0x6fffffffu          /* Sun-specific high bound.  */
    const val SHT_HIOS = 0x6fffffffu            /* End OS-specific type */
    const val SHT_LOPROC = 0x70000000u          /* Start of processor-specific */
    const val SHT_HIPROC = 0x7fffffffu          /* End of processor-specific */
    const val SHT_LOUSER = 0x80000000u          /* Start of application-specific */
    const val SHT_HIUSER = 0xffffffffu          /* End of application-specific */

    const val SHT_ANDROID_RELR = 0x6fffff00u
    const val SHT_ANDROID_REL = 0x60000001u
    const val SHT_ANDROID_RELA = 0x60000002u

    const val SHT_RISCV_ATTRIBUTES = 0x70000003u



    /* Legal values for sh_flags (section flags).  */
    const val SHF_WRITE = 0x1UL                 /* Writable */
    const val SHF_ALLOC = 0x2UL                 /* Occupies memory during execution */
    const val SHF_EXECINSTR = 0x4UL             /* Executable */
    const val SHF_MERGE = 0x10UL                /* Might be merged */
    const val SHF_STRINGS = 0x20UL              /* Contains nul-terminated strings */
    const val SHF_INFO_LINK = 0x40UL            /* `sh_info' contains SHT index */
    const val SHF_LINK_ORDER = 0x80UL           /* Preserve order after combining */
    const val SHF_OS_NONCONFORMING = 0x100UL    /* Non-standard OS specific handling required */
    const val SHF_GROUP = 0x200UL               /* Section is member of a group.  */
    const val SHF_TLS = 0x400UL                 /* Section hold thread-local data.  */
    const val SHF_COMPRESSED = 0x800UL          /* Section with compressed data. */
    const val SHF_MASKOS = 0x0ff00000UL         /* OS-specific.  */
    const val SHF_MASKPROC = 0xf0000000UL       /* Processor-specific */
    const val SHF_GNU_RETAIN = 0x200000UL       /* Not to be GCed by linker.  */
    const val SHF_ORDERED = 0x40000000UL        /* Special ordering requirement (Solaris).  */
    const val SHF_EXCLUDE = 0x80000000UL        /* Section is excluded unless referenced or allocated (Solaris).*/



    // Section name string
    const val NAME_NULL: String = '\u0000'.toString()
    const val NAME_NOTE_ANDROID_IDENT: String = ".note.android.ident"
    const val NAME_NOTE_GNU_BUILD_ID: String = ".note.gnu.build-id"
    const val NAME_DYNSYM: String = ".dynsym"
    const val NAME_GNU_VERSION: String = ".gnu.version"
    const val NAME_GNU_VERSION_R: String = ".gnu.version_r"
    const val NAME_HASH: String = ".hash"
    const val NAME_GNU_HASH: String = ".gnu.hash"
    const val NAME_DYNSTR: String = ".dynstr"
    const val NAME_RELA_DYN: String = ".rela.dyn"
    const val NAME_RELA_PLT: String = ".rela.plt"
    const val NAME_RODATA: String = ".rodata"
    const val NAME_EH_FRAME_HDR: String = ".eh_frame_hdr"
    const val NAME_EH_FRAME: String = ".eh_frame"
    const val NAME_TEXT: String = ".text"
    const val NAME_PLT: String = ".plt"
    const val NAME_DATA_REL_RO: String = ".data.rel.ro"
    const val NAME_FINI_ARRAY: String = ".fini_array"
    const val NAME_INIT_ARRAY: String = ".init_array"
    const val NAME_DYNAMIC: String = ".dynamic"
    const val NAME_GOT: String = ".got"
    const val NAME_GOT_PLT: String = ".got.plt"
    const val NAME_DATA: String = ".data"
    const val NAME_COMMENT: String = ".comment"
    const val NAME_SYMTAB: String = ".symtab"
    const val NAME_SHSTRTAB: String = ".shstrtab"
    const val NAME_STRTAB: String = ".strtab"
    const val NAME_GNU_DEBUGLINK: String = ".gnu_debuglink"
}
