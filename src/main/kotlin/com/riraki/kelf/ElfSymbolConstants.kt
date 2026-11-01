package com.riraki.kelf

object ElfSymbolConstants {
    /* Symbol visibility specification encoded in the st_other field.  */
    const val STV_DEFAULT = 0		/* Default symbol visibility rules */
    const val STV_INTERNAL = 1		/* Processor specific hidden class */
    const val STV_HIDDEN = 2		/* Sym unavailable in other modules */
    const val STV_PROTECTED = 3		/* Not preemptible, not exported */



    /* Legal values for ST_BIND subfield of st_info (symbol binding).  */
    const val STB_LOCAL = 0             /* Local symbol */
    const val STB_GLOBAL = 1            /* Global symbol */
    const val STB_WEAK = 2              /* Weak symbol */
    const val STB_NUM = 3               /* Number of defined types.  */
    const val STB_LOOS = 10             /* Start of OS-specific */
    const val STB_GNU_UNIQUE = 10       /* Unique symbol.  */
    const val STB_HIOS = 12             /* End of OS-specific */
    const val STB_LOPROC = 13           /* Start of processor-specific */
    const val STB_HIPROC = 15           /* End of processor-specific */



    /* Legal values for ST_TYPE subfield of st_info (symbol type).  */
    const val STT_NOTYPE = 0            /* Symbol type is unspecified */
    const val STT_OBJECT = 1            /* Symbol is a data object */
    const val STT_FUNC = 2              /* Symbol is a code object */
    const val STT_SECTION = 3           /* Symbol associated with a section */
    const val STT_FILE = 4              /* Symbol's name is file name */
    const val STT_COMMON = 5            /* Symbol is a common data object */
    const val STT_TLS = 6               /* Symbol is thread-local data object*/
    const val STT_NUM = 7               /* Number of defined types.  */
    const val STT_LOOS = 10             /* Start of OS-specific */
    const val STT_GNU_IFUNC = 10        /* Symbol is indirect code object */
    const val STT_HIOS = 12             /* End of OS-specific */
    const val STT_LOPROC = 13           /* Start of processor-specific */
    const val STT_HIPROC = 15           /* End of processor-specific */
}
