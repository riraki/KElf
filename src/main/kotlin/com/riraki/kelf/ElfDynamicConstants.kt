package com.riraki.kelf

object ElfDynamicConstants {
    /* Legal values for d_tag (dynamic entry type). */
    const val DT_NULL = 0L                 // Marks end of dynamic section
    const val DT_NEEDED = 1L               // Name of needed library
    const val DT_PLTRELSZ = 2L             // Size in bytes of PLT relocs
    const val DT_PLTGOT = 3L               // Processor defined value
    const val DT_HASH = 4L                 // Address of symbol hash table
    const val DT_STRTAB = 5L               // Address of string table
    const val DT_SYMTAB = 6L               // Address of symbol table
    const val DT_RELA = 7L                 // Address of Rela relocs
    const val DT_RELASZ = 8L               // Total size of Rela relocs
    const val DT_RELAENT = 9L              // Size of one Rela reloc
    const val DT_STRSZ = 10L               // Size of string table
    const val DT_SYMENT = 11L              // Size of one symbol table entry
    const val DT_INIT = 12L                // Address of init function
    const val DT_FINI = 13L                // Address of termination function
    const val DT_SONAME = 14L              // Name of shared object
    const val DT_RPATH = 15L               // Library search path (deprecated)
    const val DT_SYMBOLIC = 16L            // Start symbol search here
    const val DT_REL = 17L                 // Address of Rel relocs
    const val DT_RELSZ = 18L               // Total size of Rel relocs
    const val DT_RELENT = 19L              // Size of one Rel reloc
    const val DT_PLTREL = 20L              // Type of reloc in PLT
    const val DT_DEBUG = 21L               // For debugging; unspecified
    const val DT_TEXTREL = 22L             // Reloc might modify .text
    const val DT_JMPREL = 23L              // Address of PLT relocs
    const val DT_BIND_NOW = 24L            // Process relocations of object
    const val DT_INIT_ARRAY = 25L          // Array with addresses of init fct
    const val DT_FINI_ARRAY = 26L          // Array with addresses of fini fct
    const val DT_INIT_ARRAYSZ = 27L        // Size in bytes of DT_INIT_ARRAY
    const val DT_FINI_ARRAYSZ = 28L        // Size in bytes of DT_FINI_ARRAY
    const val DT_RUNPATH = 29L             // Library search path
    const val DT_FLAGS = 30L               // Flags for the object being loaded
    const val DT_ENCODING = 32L            // Start of encoded range
    const val DT_PREINIT_ARRAY = 32L       // Array with addresses of preinit fct
    const val DT_PREINIT_ARRAYSZ = 33L     // Size in bytes of DT_PREINIT_ARRAY
    const val DT_SYMTAB_SHNDX = 34L        // Address of SYMTAB_SHNDX section
    const val DT_RELRSZ = 35L              // Total size of RELR relative relocations
    const val DT_RELR = 36L                // Address of RELR relative relocations
    const val DT_RELRENT = 37L             // Size of one RELR relative relocaction
    const val DT_NUM = 38L                 // Number used
    const val DT_LOOS = 0x6000000dL        // Start of OS-specific
    const val DT_HIOS = 0x6ffff000L        // End of OS-specific
    const val DT_LOPROC = 0x70000000L      // Start of processor-specific
    const val DT_HIPROC = 0x7fffffffL      // End of processor-specific

    // DT_* entries which fall between DT_VALRNGHI & DT_VALRNGLO
    const val DT_VALRNGLO = 0x6ffffd00L
    const val DT_GNU_PRELINKED = 0x6ffffdf5L   // Prelinking timestamp
    const val DT_GNU_CONFLICTSZ = 0x6ffffdf6L  // Size of conflict section
    const val DT_GNU_LIBLISTSZ = 0x6ffffdf7L   // Size of library list
    const val DT_CHECKSUM = 0x6ffffdf8L
    const val DT_PLTPADSZ = 0x6ffffdf9L
    const val DT_MOVEENT = 0x6ffffdfaL
    const val DT_MOVESZ = 0x6ffffdfbL
    const val DT_FEATURE_1 = 0x6ffffdfcL     // Feature selection (DTF_*)
    const val DT_POSFLAG_1 = 0x6ffffdfdL     // Flags for DT_* entries
    const val DT_SYMINSZ = 0x6ffffdfeL     // Size of syminfo table (in bytes)
    const val DT_SYMINENT = 0x6ffffdffL    // Entry size of syminfo
    const val DT_VALRNGHI = 0x6ffffdffL

    // DT_* entries which fall between DT_ADDRRNGHI & DT_ADDRRNGLO
    const val DT_ADDRRNGLO = 0x6ffffe00L
    const val DT_GNU_HASH = 0x6ffffef5L    // GNU-style hash table
    const val DT_TLSDESC_PLT = 0x6ffffef6L
    const val DT_TLSDESC_GOT = 0x6ffffef7L
    const val DT_GNU_CONFLICT = 0x6ffffef8L  // Start of conflict section
    const val DT_GNU_LIBLIST = 0x6ffffef9L   // Library list
    const val DT_CONFIG = 0x6ffffefaL      // Configuration information
    const val DT_DEPAUDIT = 0x6ffffefbL    // Dependency auditing
    const val DT_AUDIT = 0x6ffffefcL       // Object auditing
    const val DT_PLTPAD = 0x6ffffefdL      // PLT padding
    const val DT_MOVETAB = 0x6ffffefeL     // Move table
    const val DT_SYMINFO = 0x6ffffeffL     // Syminfo table
    const val DT_ADDRRNGHI = 0x6ffffeffL

    // Versioning entry types and GNU extensions
    const val DT_VERSYM = 0x6ffffff0L
    const val DT_RELACOUNT = 0x6ffffff9L
    const val DT_RELCOUNT = 0x6ffffffaL
    const val DT_FLAGS_1 = 0x6ffffffbL     // State flags
    const val DT_VERDEF = 0x6ffffffcL      // Address of version definition table
    const val DT_VERDEFNUM = 0x6ffffffdL   // Number of version definitions
    const val DT_VERNEED = 0x6ffffffeL     // Address of table with needed versions
    const val DT_VERNEEDNUM = 0x6fffffffL  // Number of needed versions

    // Sun machine-independent extensions in processor-specific range
    const val DT_AUXILIARY = 0x7ffffffdL   // Shared object to load before self
    const val DT_FILTER = 0x7fffffffL      // Shared object to get values from

    const val DT_ANDROID_RELR = 0x6fffe000L
    const val DT_ANDROID_RELRSZ = 0x6fffe001L
    const val DT_ANDROID_RELRENT = 0x6fffe003L
    const val DT_ANDROID_RELRCOUNT = 0x6fffe005L
    const val DT_ANDROID_REL = 0x6000000fL // DT_LOOS + 2
    const val DT_ANDROID_RELSZ = 0x60000010L // DT_LOOS + 3
    const val DT_ANDROID_RELA = 0x60000011L // DT_LOOS + 4
    const val DT_ANDROID_RELASZ = 0x60000012L // DT_LOOS + 5

    const val DT_AARCH64_MEMTAG_MODE = 0x70000009L
    const val DT_AARCH64_MEMTAG_HEAP = 0x7000000bL
    const val DT_AARCH64_MEMTAG_STACK = 0x7000000cL
    const val DT_AARCH64_MEMTAG_GLOBALS = 0x7000000dL
    const val DT_AARCH64_MEMTAG_GLOBALSSZ = 0x7000000fL



    /* Values of `d_un.d_val' in the DT_FLAGS entry.  */
    const val DF_ORIGIN = 0x00000001UL      /* Object may use DF_ORIGIN */
    const val DF_SYMBOLIC = 0x00000002UL    /* Symbol resolutions starts here */
    const val DF_TEXTREL = 0x00000004UL     /* Object contains text relocations */
    const val DF_BIND_NOW = 0x00000008UL    /* No lazy binding for this object */
    const val DF_STATIC_TLS = 0x00000010UL  /* Module uses the static TLS model */



    /* State flags selectable in the `d_un.d_val' element of the DT_FLAGS_1
       entry in the dynamic section.  */
    const val DF_1_NOW = 0x00000001UL           /* Set RTLD_NOW for this object.  */
    const val DF_1_GLOBAL = 0x00000002UL        /* Set RTLD_GLOBAL for this object.  */
    const val DF_1_GROUP = 0x00000004UL         /* Set RTLD_GROUP for this object.  */
    const val DF_1_NODELETE = 0x00000008UL      /* Set RTLD_NODELETE for this object.*/
    const val DF_1_LOADFLTR = 0x00000010UL      /* Trigger filtee loading at runtime.*/
    const val DF_1_INITFIRST = 0x00000020UL     /* Set RTLD_INITFIRST for this object*/
    const val DF_1_NOOPEN = 0x00000040UL        /* Set RTLD_NOOPEN for this object.  */
    const val DF_1_ORIGIN = 0x00000080UL        /* $ORIGIN must be handled.  */
    const val DF_1_DIRECT = 0x00000100UL        /* Direct binding enabled.  */
    const val DF_1_TRANS = 0x00000200UL
    const val DF_1_INTERPOSE = 0x00000400UL     /* Object is used to interpose.  */
    const val DF_1_NODEFLIB = 0x00000800UL      /* Ignore default lib search path.  */
    const val DF_1_NODUMP = 0x00001000UL        /* Object can't be dldump'ed.  */
    const val DF_1_CONFALT = 0x00002000UL       /* Configuration alternative created.*/
    const val DF_1_ENDFILTEE = 0x00004000UL     /* Filtee terminates filters search. */
    const val DF_1_DISPRELDNE = 0x00008000UL    /* Disp reloc applied at build time. */
    const val DF_1_DISPRELPND = 0x00010000UL    /* Disp reloc applied at run-time.  */
    const val DF_1_NODIRECT = 0x00020000UL      /* Object has no-direct binding. */
    const val DF_1_IGNMULDEF = 0x00040000UL
    const val DF_1_NOKSYMS = 0x00080000UL
    const val DF_1_NOHDR = 0x00100000UL
    const val DF_1_EDITED = 0x00200000UL        /* Object is modified after built.  */
    const val DF_1_NORELOC = 0x00400000UL
    const val DF_1_SYMINTPOSE = 0x00800000UL    /* Object has individual interposers.  */
    const val DF_1_GLOBAUDIT = 0x01000000UL     /* Global auditing required.  */
    const val DF_1_SINGLETON = 0x02000000UL     /* Singleton symbols are used.  */
    const val DF_1_STUB = 0x04000000UL
    const val DF_1_PIE = 0x08000000UL
    const val DF_1_KMOD = 0x10000000UL
    const val DF_1_WEAKFILTER = 0x20000000UL
    const val DF_1_NOCOMMON = 0x40000000UL
}
