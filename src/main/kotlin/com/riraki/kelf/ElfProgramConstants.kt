package com.riraki.kelf

object ElfProgramConstants {
    /* Legal values for p_type (segment type).  */
    const val PT_NULL = 0u                      /* Program header table entry unused */
    const val PT_LOAD = 1u                      /* Loadable program segment */
    const val PT_DYNAMIC = 2u                   /* Dynamic linking information */
    const val PT_INTERP = 3u                    /* Program interpreter */
    const val PT_NOTE = 4u                      /* Auxiliary information */
    const val PT_SHLIB = 5u                     /* Reserved */
    const val PT_PHDR = 6u                      /* Entry for header table itself */
    const val PT_TLS = 7u                       /* Thread-local storage segment */
    const val PT_NUM = 8u                       /* Number of defined types */
    const val PT_LOOS = 0x60000000u             /* Start of OS-specific */
    const val PT_GNU_EH_FRAME = 0x6474e550u     /* GCC .eh_frame_hdr segment */
    const val PT_GNU_STACK = 0x6474e551u        /* Indicates stack executability */
    const val PT_GNU_RELRO = 0x6474e552u        /* Read-only after relocation */
    const val PT_GNU_PROPERTY = 0x6474e553u     /* GNU property */
    const val PT_GNU_SFRAME = 0x6474e554u       /* SFrame segment.  */
    const val PT_SUNWBSS = 0x6ffffffau          /* Sun Specific segment */
    const val PT_SUNWSTACK = 0x6ffffffbu        /* Stack segment */
    const val PT_HIOS = 0x6fffffffu             /* End of OS-specific */
    const val PT_LOPROC = 0x70000000u           /* Start of processor-specific */
    const val PT_HIPROC = 0x7fffffffu           /* End of processor-specific */



    /* Legal values for p_flags (segment flags).  */
    const val PF_X = 1u                         /* Segment is executable */
    const val PF_W = 2u                         /* Segment is writable */
    const val PF_R = 4u                         /* Segment is readable */
    const val PF_MASKOS = 0x0ff00000u           /* OS-specific */
    const val PF_MASKPROC = 0xf0000000u         /* Processor-specific */
}
