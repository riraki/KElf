package com.riraki.kelf

class ElfDynamicStructure(
    val d_tag: Long,    // 用来控制 d_un 的解释， Long/Int
    val d_un: ULong,    // d_val 或 d_ptr， ULong/Int
    private val stringTable: ElfStringTableSection?
) {

    val tagDescription: String
        get() {
            return when (d_tag) {
                ElfDynamicConstants.DT_NULL -> "DT_NULL"
                ElfDynamicConstants.DT_NEEDED -> "DT_NEEDED"
                ElfDynamicConstants.DT_PLTRELSZ -> "DT_PLTRELSZ"
                ElfDynamicConstants.DT_PLTGOT -> "DT_PLTGOT"
                ElfDynamicConstants.DT_HASH -> "DT_HASH"
                ElfDynamicConstants.DT_STRTAB -> "DT_STRTAB"
                ElfDynamicConstants.DT_SYMTAB -> "DT_SYMTAB"
                ElfDynamicConstants.DT_RELA -> "DT_RELA"
                ElfDynamicConstants.DT_RELASZ -> "DT_RELASZ"
                ElfDynamicConstants.DT_RELAENT -> "DT_RELAENT"
                ElfDynamicConstants.DT_STRSZ -> "DT_STRSZ"
                ElfDynamicConstants.DT_SYMENT -> "DT_SYMENT"
                ElfDynamicConstants.DT_INIT -> "DT_INIT"
                ElfDynamicConstants.DT_FINI -> "DT_FINI"
                ElfDynamicConstants.DT_SONAME -> "DT_SONAME"
                ElfDynamicConstants.DT_RPATH -> "DT_RPATH"
                ElfDynamicConstants.DT_SYMBOLIC -> "DT_SYMBOLIC"
                ElfDynamicConstants.DT_REL -> "DT_REL"
                ElfDynamicConstants.DT_RELSZ -> "DT_RELSZ"
                ElfDynamicConstants.DT_RELENT -> "DT_RELENT"
                ElfDynamicConstants.DT_PLTREL -> "DT_PLTREL"
                ElfDynamicConstants.DT_DEBUG -> "DT_DEBUG"
                ElfDynamicConstants.DT_TEXTREL -> "DT_TEXTREL"
                ElfDynamicConstants.DT_JMPREL -> "DT_JMPREL"
                ElfDynamicConstants.DT_BIND_NOW -> "DT_BIND_NOW"
                ElfDynamicConstants.DT_INIT_ARRAY -> "DT_INIT_ARRAY"
                ElfDynamicConstants.DT_FINI_ARRAY -> "DT_FINI_ARRAY"
                ElfDynamicConstants.DT_INIT_ARRAYSZ -> "DT_INIT_ARRAYSZ"
                ElfDynamicConstants.DT_FINI_ARRAYSZ -> "DT_FINI_ARRAYSZ"
                ElfDynamicConstants.DT_RUNPATH -> "DT_RUNPATH"
                ElfDynamicConstants.DT_FLAGS -> "DT_FLAGS"
                ElfDynamicConstants.DT_ENCODING -> "DT_ENCODING"
                ElfDynamicConstants.DT_PREINIT_ARRAY -> "DT_PREINIT_ARRAY"
                ElfDynamicConstants.DT_PREINIT_ARRAYSZ -> "DT_PREINIT_ARRAYSZ"
                ElfDynamicConstants.DT_SYMTAB_SHNDX -> "DT_SYMTAB_SHNDX"
                ElfDynamicConstants.DT_RELRSZ -> "DT_RELRSZ"
                ElfDynamicConstants.DT_RELR -> "DT_RELR"
                ElfDynamicConstants.DT_RELRENT -> "DT_RELRENT"
                ElfDynamicConstants.DT_NUM -> "DT_NUM"
                ElfDynamicConstants.DT_LOOS -> "DT_LOOS"
                ElfDynamicConstants.DT_HIOS -> "DT_HIOS"
                ElfDynamicConstants.DT_LOPROC -> "DT_LOPROC"
                ElfDynamicConstants.DT_HIPROC -> "DT_HIPROC"
                ElfDynamicConstants.DT_VALRNGLO -> "DT_VALRNGLO"
                ElfDynamicConstants.DT_GNU_PRELINKED -> "DT_GNU_PRELINKED"
                ElfDynamicConstants.DT_GNU_CONFLICTSZ -> "DT_GNU_CONFLICTSZ"
                ElfDynamicConstants.DT_GNU_LIBLISTSZ -> "DT_GNU_LIBLISTSZ"
                ElfDynamicConstants.DT_CHECKSUM -> "DT_CHECKSUM"
                ElfDynamicConstants.DT_PLTPADSZ -> "DT_PLTPADSZ"
                ElfDynamicConstants.DT_MOVEENT -> "DT_MOVEENT"
                ElfDynamicConstants.DT_MOVESZ -> "DT_MOVESZ"
                ElfDynamicConstants.DT_FEATURE_1 -> "DT_FEATURE_1"
                ElfDynamicConstants.DT_POSFLAG_1 -> "DT_POSFLAG_1"
                ElfDynamicConstants.DT_SYMINSZ -> "DT_SYMINSZ"
                ElfDynamicConstants.DT_SYMINENT -> "DT_SYMINENT"
                ElfDynamicConstants.DT_VALRNGHI -> "DT_VALRNGHI"
                ElfDynamicConstants.DT_ADDRRNGLO -> "DT_ADDRRNGLO"
                ElfDynamicConstants.DT_GNU_HASH -> "DT_GNU_HASH"
                ElfDynamicConstants.DT_TLSDESC_PLT -> "DT_TLSDESC_PLT"
                ElfDynamicConstants.DT_TLSDESC_GOT -> "DT_TLSDESC_GOT"
                ElfDynamicConstants.DT_GNU_CONFLICT -> "DT_GNU_CONFLICT"
                ElfDynamicConstants.DT_GNU_LIBLIST -> "DT_GNU_LIBLIST"
                ElfDynamicConstants.DT_CONFIG -> "DT_CONFIG"
                ElfDynamicConstants.DT_DEPAUDIT -> "DT_DEPAUDIT"
                ElfDynamicConstants.DT_AUDIT -> "DT_AUDIT"
                ElfDynamicConstants.DT_PLTPAD -> "DT_PLTPAD"
                ElfDynamicConstants.DT_MOVETAB -> "DT_MOVETAB"
                ElfDynamicConstants.DT_SYMINFO -> "DT_SYMINFO"
                ElfDynamicConstants.DT_ADDRRNGHI -> "DT_ADDRRNGHI"
                ElfDynamicConstants.DT_VERSYM -> "DT_VERSYM"
                ElfDynamicConstants.DT_RELACOUNT -> "DT_RELACOUNT"
                ElfDynamicConstants.DT_RELCOUNT -> "DT_RELCOUNT"
                ElfDynamicConstants.DT_FLAGS_1 -> "DT_FLAGS_1"
                ElfDynamicConstants.DT_VERDEF -> "DT_VERDEF"
                ElfDynamicConstants.DT_VERDEFNUM -> "DT_VERDEFNUM"
                ElfDynamicConstants.DT_VERNEED -> "DT_VERNEED"
                ElfDynamicConstants.DT_VERNEEDNUM -> "DT_VERNEEDNUM"
                ElfDynamicConstants.DT_AUXILIARY -> "DT_AUXILIARY"
                ElfDynamicConstants.DT_FILTER -> "DT_FILTER"
                ElfDynamicConstants.DT_ANDROID_RELR -> "DT_ANDROID_RELR"
                ElfDynamicConstants.DT_ANDROID_RELRSZ -> "DT_ANDROID_RELRSZ"
                ElfDynamicConstants.DT_ANDROID_RELRENT -> "DT_ANDROID_RELRENT"
                ElfDynamicConstants.DT_ANDROID_RELRCOUNT -> "DT_ANDROID_RELRCOUNT"
                ElfDynamicConstants.DT_ANDROID_REL -> "DT_ANDROID_REL"
                ElfDynamicConstants.DT_ANDROID_RELSZ -> "DT_ANDROID_RELSZ"
                ElfDynamicConstants.DT_ANDROID_RELA -> "DT_ANDROID_RELA"
                ElfDynamicConstants.DT_ANDROID_RELASZ -> "DT_ANDROID_RELASZ"
                ElfDynamicConstants.DT_AARCH64_MEMTAG_MODE -> "DT_AARCH64_MEMTAG_MODE"
                ElfDynamicConstants.DT_AARCH64_MEMTAG_HEAP -> "DT_AARCH64_MEMTAG_HEAP"
                ElfDynamicConstants.DT_AARCH64_MEMTAG_STACK -> "DT_AARCH64_MEMTAG_STACK"
                ElfDynamicConstants.DT_AARCH64_MEMTAG_GLOBALS -> "DT_AARCH64_MEMTAG_GLOBALS"
                ElfDynamicConstants.DT_AARCH64_MEMTAG_GLOBALSSZ -> "DT_AARCH64_MEMTAG_GLOBALSSZ"
                else -> "DT_UNKNOWN(${FormatHelpers.toHexString(d_tag)})"
            }
        }

    val unDescription: String
        get() {
            return if (isPointer) {
                FormatHelpers.toHexString(d_un)
            }
            else {
                /**
                 * 当 d_value 为数值时，其具体应该解释成什么还与 d_tag 有关
                 *     当 d_tag 是 DT_NEEDED 时：这个 d_val 代表的是一个字符串在 .dynstr（动态字符串表）中的字节偏移量
                 *     当 d_tag 是 DT_STRSZ 时：这个 d_val 代表的是整个字符串表的总大小（字节数）
                 *     当 d_tag 是 DT_SYMENT 时：这个 d_val 代表的是单个符号表条目（Symbol Entry）的大小
                 *     当 d_tag 是 DT_FLAGS 时：这个 d_val 代表的是一组位掩码标志（Bitmask Flags）
                 *     其余情况认为是通用数值
                 */
                val retStr = when (d_tag) {
                    ElfDynamicConstants.DT_NEEDED
                        , ElfDynamicConstants.DT_SONAME -> getDependencyLibraryName()
                    ElfDynamicConstants.DT_STRSZ -> FormatHelpers.toHexString(d_un)
                    ElfDynamicConstants.DT_SYMENT -> FormatHelpers.toHexString(d_un)
                    ElfDynamicConstants.DT_FLAGS -> getUnStringWithFlags()
                    ElfDynamicConstants.DT_FLAGS_1 -> getUnStringWithFlags_1()
                    else -> FormatHelpers.toHexString(d_un)
                }
                "\"$retStr\""
            }
        }

    /**
     * The d_tag member controls the interpretation of d_un.
     * 这个 union 到底是 d_val 还是 d_ptr 是按照 d_tag 的来决定的
     * 这个方法就是用来判断这个 union 到底是应该解释 d_val 还是 d_ptr
     *
     * 我解释的规则为：
     * 规则一：看它是否落在“大范围区间内” （标准化自动归类）
     *     解释为指针（d_ptr）：只要 d_tag 的值落在 DT_ADDRRNGLO (0x6ffffe00) 到 DT_ADDRRNGHI (0x6ffffeff) 之间，一律解释为内存地址
     *     解释为数值（d_val）：只要 d_tag 的值落在 DT_VALRNGLO (0x6ffffd00) 到 DT_VALRNGHI (0x6ffffdff) 之间，一律解释为数值/大小
     * 规则二：看它是否属于“常见的显式地址标签”（特例枚举）
     *     必须解释为指针（d_ptr）：代表它存的是某个表、段或函数在内存中的虚拟地址：
     *         DT_STRTAB（字符串表地址）
     *         DT_SYMTAB（符号表地址）
     *         DT_PLTGOT（GOT/PLT 表地址）
     *         DT_HASH / DT_GNU_HASH（哈希表地址）
     *         DT_RELA / DT_REL（重定位表地址）
     *         DT_INIT / DT_FINI（初始化/终止函数地址）
     *         DT_INIT_ARRAY / DT_FINI_ARRAY（初始化/终止函数指针数组地址）
     *         DT_VERSYM / DT_VERDEF / DT_VERNEED（版本控制表地址）
     *     必须解释为数值（d_val）：代表它存的是大小、字节数、数量、索引或标志：
     *         DT_NEEDED（依赖库名称在字符串表中的偏移索引）
     *         所有带 SZ（如 DT_STRSZ, DT_RELSZ —— 表示表的大小）的标签
     *         所有带 ENT（如 DT_SYMENT, DT_RELENT —— 表示单项结构的大小）的标签
     *         DT_FLAGS / DT_FLAGS_1（配置标志位）
     * 规则三：兜底默认规则（其余所有）
     *     如果一个标签不在上述任何地址区间内，也不在已知的地址特例名单中：统一默认解释为数值（d_val）
     *     因为在 ELF 设计中，“地址”是需要明确授权的特权属性，绝大多数未知的、扩展的、计数类的标签本质上全都是无符号整数
     */
    val isPointer: Boolean
        get() {
            return when (d_tag) {
                // 规则一：
                // DT_ADDRRNGLO (0x6ffffe00) 到 DT_ADDRRNGHI (0x6ffffeff) 之间解释为指针
                ElfDynamicConstants.DT_ADDRRNGLO, ElfDynamicConstants.DT_GNU_HASH, ElfDynamicConstants.DT_TLSDESC_PLT
                    , ElfDynamicConstants.DT_TLSDESC_GOT, ElfDynamicConstants.DT_GNU_CONFLICT
                    , ElfDynamicConstants.DT_GNU_LIBLIST, ElfDynamicConstants.DT_CONFIG, ElfDynamicConstants.DT_DEPAUDIT
                    , ElfDynamicConstants.DT_AUDIT, ElfDynamicConstants.DT_PLTPAD, ElfDynamicConstants.DT_MOVETAB
                    , ElfDynamicConstants.DT_SYMINFO, ElfDynamicConstants.DT_ADDRRNGHI -> true
                // DT_VALRNGLO (0x6ffffd00) 到 DT_VALRNGHI (0x6ffffdff) 之间解释为数值
                ElfDynamicConstants.DT_VALRNGLO, ElfDynamicConstants.DT_GNU_PRELINKED, ElfDynamicConstants.DT_GNU_CONFLICTSZ
                    , ElfDynamicConstants.DT_GNU_LIBLISTSZ, ElfDynamicConstants.DT_CHECKSUM, ElfDynamicConstants.DT_PLTPADSZ
                    , ElfDynamicConstants.DT_MOVEENT, ElfDynamicConstants.DT_MOVESZ, ElfDynamicConstants.DT_FEATURE_1
                    , ElfDynamicConstants.DT_POSFLAG_1, ElfDynamicConstants.DT_SYMINSZ, ElfDynamicConstants.DT_SYMINENT
                    , ElfDynamicConstants.DT_VALRNGHI -> false

                // 规则二（删除了与规则一中重复的选项）：
                // 常见地址标签应该解释为指针
                ElfDynamicConstants.DT_STRTAB, ElfDynamicConstants.DT_SYMTAB, ElfDynamicConstants.DT_PLTGOT
                    , ElfDynamicConstants.DT_HASH, ElfDynamicConstants.DT_RELA, ElfDynamicConstants.DT_REL
                    , ElfDynamicConstants.DT_INIT, ElfDynamicConstants.DT_FINI, ElfDynamicConstants.DT_INIT_ARRAY
                    , ElfDynamicConstants.DT_FINI_ARRAY, ElfDynamicConstants.DT_VERSYM, ElfDynamicConstants.DT_VERDEF
                    , ElfDynamicConstants.DT_VERNEED -> true
                // 其余标签中，带有 SZ 的
                ElfDynamicConstants.DT_NEEDED, ElfDynamicConstants.DT_PLTRELSZ, ElfDynamicConstants.DT_RELASZ
                    , ElfDynamicConstants.DT_STRSZ, ElfDynamicConstants.DT_RELSZ, ElfDynamicConstants.DT_INIT_ARRAYSZ
                    , ElfDynamicConstants.DT_FINI_ARRAYSZ, ElfDynamicConstants.DT_PREINIT_ARRAYSZ
                    , ElfDynamicConstants.DT_RELRSZ, ElfDynamicConstants.DT_ANDROID_RELRSZ
                    , ElfDynamicConstants.DT_ANDROID_RELSZ, ElfDynamicConstants.DT_ANDROID_RELASZ
                    , ElfDynamicConstants.DT_AARCH64_MEMTAG_GLOBALSSZ -> false
                // 其余标签中，带有 ENT 的
                ElfDynamicConstants.DT_RELAENT, ElfDynamicConstants.DT_SYMENT, ElfDynamicConstants.DT_RELENT
                    , ElfDynamicConstants.DT_RELRENT, ElfDynamicConstants.DT_ANDROID_RELRENT -> false

                // 规则三（其余所有）：
                else -> false
            }
        }

    /**
     * 用来判断这个 union 到底是应该解释 d_val 还是 d_ptr
     */
    val isValue: Boolean
        get() {
            return isPointer.not()
        }


    /**
     * 从 DynamicString 也就是 .dynstr 的字符串的 d_un 偏移处，获取字符串，直到遇到 \0（\u0000）为止
     * 这个获取到的字符串就是依赖库名称
     * 如果 dynamicString 为 null，代表不需要获取名称，那么这个时候就直接把 d_un 转化成 16 进制字符串返回
     * @return 返回获取到的字符串
     */
    private fun getDependencyLibraryName(): String {
        return stringTable?.getString(d_un.toInt()) ?: FormatHelpers.toHexString(d_un)
    }

    /**
     * 当 d_un 被解释为数值的时候，并且 d_tag 为 DT_FLAGS 时，用来把 d_un 也就是 d_val 转化成其值对应的字符串
     * @return 转化之后的字符串
     */
    private fun getUnStringWithFlags(): String {
        if (isValue.not()) {
            throw UnsupportedOperationException("Cannot execute this operation When d_un is interpreted d_pointer!")
        }
        if (d_tag != ElfDynamicConstants.DT_FLAGS) {
            throw UnsupportedOperationException("Cannot execute this operation When d_tag is not DT_FLAGS!")
        }

        val flags = d_un
        return buildList {
            if ((flags and ElfDynamicConstants.DF_ORIGIN) != 0UL) {
                add("DF_ORIGIN")
            }
            if ((flags and ElfDynamicConstants.DF_SYMBOLIC) != 0UL) {
                add("DF_SYMBOLIC")
            }
            if ((flags and ElfDynamicConstants.DF_TEXTREL) != 0UL) {
                add("DF_TEXTREL")
            }
            if ((flags and ElfDynamicConstants.DF_BIND_NOW) != 0UL) {
                add("DF_BIND_NOW")
            }
            if ((flags and ElfDynamicConstants.DF_STATIC_TLS) != 0UL) {
                add("DF_STATIC_TLS")
            }
        }.ifEmpty { listOf("DF_NONE($d_un)") }.joinToString("|")
    }

    /**
     * 当 d_un 被解释为数值的时候，并且 d_tag 为 DT_FLAGS_1 时，用来把 d_un 也就是 d_val 转化成其值对应的字符串
     * @return 转化之后的字符串
     */
    private fun getUnStringWithFlags_1(): String {
        if (isValue.not()) {
            throw UnsupportedOperationException("Cannot execute this operation When d_un is interpreted d_pointer!")
        }
        if (d_tag != ElfDynamicConstants.DT_FLAGS_1) {
            throw UnsupportedOperationException("Cannot execute this operation When d_tag is not DT_FLAGS_1!")
        }

        val flags = d_un
        return buildList {
            if ((flags and ElfDynamicConstants.DF_1_NOW) != 0UL) {
                add("DF_1_NOW")
            }
            if ((flags and ElfDynamicConstants.DF_1_GLOBAL) != 0UL) {
                add("DF_1_GLOBAL")
            }
            if ((flags and ElfDynamicConstants.DF_1_GROUP) != 0UL) {
                add("DF_1_GROUP")
            }
            if ((flags and ElfDynamicConstants.DF_1_NODELETE) != 0UL) {
                add("DF_1_NODELETE")
            }
            if ((flags and ElfDynamicConstants.DF_1_LOADFLTR) != 0UL) {
                add("DF_1_LOADFLTR")
            }
            if ((flags and ElfDynamicConstants.DF_1_INITFIRST) != 0UL) {
                add("DF_1_INITFIRST")
            }
            if ((flags and ElfDynamicConstants.DF_1_NOOPEN) != 0UL) {
                add("DF_1_NOOPEN")
            }
            if ((flags and ElfDynamicConstants.DF_1_ORIGIN) != 0UL) {
                add("DF_1_ORIGIN")
            }
            if ((flags and ElfDynamicConstants.DF_1_DIRECT) != 0UL) {
                add("DF_1_DIRECT")
            }
            if ((flags and ElfDynamicConstants.DF_1_TRANS) != 0UL) {
                add("DF_1_TRANS")
            }
            if ((flags and ElfDynamicConstants.DF_1_INTERPOSE) != 0UL) {
                add("DF_1_INTERPOSE")
            }
            if ((flags and ElfDynamicConstants.DF_1_NODEFLIB) != 0UL) {
                add("DF_1_NODEFLIB")
            }
            if ((flags and ElfDynamicConstants.DF_1_NODUMP) != 0UL) {
                add("DF_1_NODUMP")
            }
            if ((flags and ElfDynamicConstants.DF_1_CONFALT) != 0UL) {
                add("DF_1_CONFALT")
            }
            if ((flags and ElfDynamicConstants.DF_1_ENDFILTEE) != 0UL) {
                add("DF_1_ENDFILTEE")
            }
            if ((flags and ElfDynamicConstants.DF_1_DISPRELDNE) != 0UL) {
                add("DF_1_DISPRELDNE")
            }
            if ((flags and ElfDynamicConstants.DF_1_DISPRELPND) != 0UL) {
                add("DF_1_DISPRELPND")
            }
            if ((flags and ElfDynamicConstants.DF_1_NODIRECT) != 0UL) {
                add("DF_1_NODIRECT")
            }
            if ((flags and ElfDynamicConstants.DF_1_IGNMULDEF) != 0UL) {
                // 对应常量子项
                add("DF_1_IGNMULDEF")
            }
            if ((flags and ElfDynamicConstants.DF_1_NOKSYMS) != 0UL) {
                add("DF_1_NOKSYMS")
            }
            if ((flags and ElfDynamicConstants.DF_1_NOHDR) != 0UL) {
                add("DF_1_NOHDR")
            }
            if ((flags and ElfDynamicConstants.DF_1_EDITED) != 0UL) {
                add("DF_1_EDITED")
            }
            if ((flags and ElfDynamicConstants.DF_1_NORELOC) != 0UL) {
                add("DF_1_NORELOC")
            }
            if ((flags and ElfDynamicConstants.DF_1_SYMINTPOSE) != 0UL) {
                add("DF_1_SYMINTPOSE")
            }
            if ((flags and ElfDynamicConstants.DF_1_GLOBAUDIT) != 0UL) {
                add("DF_1_GLOBAUDIT")
            }
            if ((flags and ElfDynamicConstants.DF_1_SINGLETON) != 0UL) {
                add("DF_1_SINGLETON")
            }
            if ((flags and ElfDynamicConstants.DF_1_STUB) != 0UL) {
                add("DF_1_STUB")
            }
            if ((flags and ElfDynamicConstants.DF_1_PIE) != 0UL) {
                add("DF_1_PIE")
            }
            if ((flags and ElfDynamicConstants.DF_1_KMOD) != 0UL) {
                add("DF_1_KMOD")
            }
            if ((flags and ElfDynamicConstants.DF_1_WEAKFILTER) != 0UL) {
                add("DF_1_WEAKFILTER")
            }
            if ((flags and ElfDynamicConstants.DF_1_NOCOMMON) != 0UL) {
                add("DF_1_NOCOMMON")
            }
        }.ifEmpty { listOf("DF_1_NONE($d_un)") }.joinToString("|")
    }

    override fun toString(): String {
        return "ElfDynamicStructure(" +
                "d_tag=\"$tagDescription\"" +
                ", d_un=$unDescription" +
                ")"
    }
}
