# KElf

[简体中文](README.zh-CN.md)

[![Maven Central](https://img.shields.io/maven-central/v/io.github.riraki/kelf?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.riraki/kelf)
[![License: MPL-2.0](https://img.shields.io/badge/License-MPL--2.0-blue.svg)](LICENSE)

**KElf is a Kotlin/JVM library for parsing ELF32 and ELF64 binaries.** It reads ELF metadata and exposes the parsed structures through a small, programmatic API. KElf does not load or execute ELF files.

## Features

- **ELF identification and headers** — reads the ELF class, byte order, file type, version, entry point, and machine identifier.
- **Program headers** — parses program-header entries and segment attributes.
- **Section headers and sections** — parses section metadata and resolves section names.
- **String tables** — reads section-name, symbol-name, and dynamic string tables.
- **Symbol tables** — parses `.symtab` and `.dynsym` entries.
- **Dynamic section** — parses dynamic entries, including dependency names and selected flag descriptions.
- **Relocations** — parses `REL` and `RELA` entries and exposes their symbol indices and relocation types.
- **Hash tables** — parses SysV ELF hash and GNU hash tables, with symbol lookup helpers.
- **NOTE sections** — parses NOTE records and exposes GNU Build IDs when present.
- **ELF32 and ELF64** — selects the field widths from the ELF file's identification bytes.
- **Little-endian and big-endian data** — reads multi-byte fields according to `EI_DATA`.

The currently recognized `e_machine` values are `EM_386(X86)`, `EM_ARM(ARM)`, `EM_X86_64(X86-64)`, and `EM_AARCH64(AARCH64)`. Recognition of a machine identifier does not imply complete support for every architecture-specific ELF extension or relocation type.

## Requirements

- JDK 17 or newer to build and run KElf.
- Gradle is not required to be installed separately. The repository includes the Gradle Wrapper.

## Installation

KElf `0.1.0` is published on Maven Central.

### Gradle Kotlin DSL

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.riraki:kelf:0.1.0")
}
```

### Gradle Groovy DSL

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation 'io.github.riraki:kelf:0.1.0'
}
```

### Maven

```xml
<dependency>
    <groupId>io.github.riraki</groupId>
    <artifactId>kelf</artifactId>
    <version>0.1.0</version>
</dependency>
```

The Maven coordinates are independent of the Kotlin package name. The public Kotlin package is `com.riraki.kelf`.

## Quick start

The main entry point is `ElfFile.from(File)`. Pass it an ELF file; KElf reads the ELF class and byte order from the file and parses the corresponding structures internally.

```kotlin
import com.riraki.kelf.ElfFile
import java.io.File

fun main() {
    val elf = ElfFile.from(File("libnative.so"))
    val header = elf.elfHeader

    println("Class: ${header.classDescription}")
    println("Byte order: ${header.dataDescription}")
    println("Machine: ${header.machineDescription}")
    println("Entry point: 0x${header.e_entry.toString(16)}")

    println("\nProgram headers:")
    elf.programHeaders.forEach(::println)

    println("\nSections:")
    elf.sectionHeaders.forEach { section ->
        println("${section.name}: ${section.typeDescription}")
    }

    val jniOnLoad = elf.getSymbol("JNI_OnLoad")
    println("\nJNI_OnLoad: ${jniOnLoad ?: "not found"}")
}
```

`ElfFile` also exposes parsed sections and common tables directly:

```kotlin
import com.riraki.kelf.ElfFile
import com.riraki.kelf.ElfNoteSection
import com.riraki.kelf.ElfRelocationAppendSection
import com.riraki.kelf.ElfRelocationSection
import java.io.File

val elf = ElfFile.from(File("libnative.so"))
val dynamicSymbols = elf.dynamicSymbolTableSection?.symbols.orEmpty()
val staticSymbols = elf.symbolTableSection?.symbols.orEmpty()

elf.dynamicSection?.entries?.forEach { entry ->
    println("${entry.tagDescription}: ${entry.unDescription}")
}

val relSections = elf.sections.filterIsInstance<ElfRelocationSection>()
val relaSections = elf.sections.filterIsInstance<ElfRelocationAppendSection>()
val noteSections = elf.sections.filterIsInstance<ElfNoteSection>()

println("Dynamic symbols: ${dynamicSymbols.size}")
println("Static symbols: ${staticSymbols.size}")
println("REL entries: ${relSections.sumOf { it.entries.size }}")
println("RELA entries: ${relaSections.sumOf { it.entries.size }}")

noteSections.flatMap { it.entries.asIterable() }
    .filter { it.buildIdHex.isNotEmpty() }
    .forEach { println("GNU Build ID: ${it.buildIdHex}") }
```

All examples above use the current API in the `com.riraki.kelf` package.

## Command-line example

The repository contains a small `examples` module with an ELF inspection command-line application. From the repository root, run:

```bash
./gradlew :examples:run --args="/absolute/path/to/libnative.so"
```

On Windows, use `gradlew.bat` instead of `./gradlew`:

```bat
gradlew.bat :examples:run --args="C:\\path\\to\\libnative.so"
```

The example prints the ELF header, program headers, section headers, parsed sections, symbol tables, and dynamic section.

## Build and test

```bash
# Compile and run the automated tests
./gradlew clean test

# Build the library and the examples module
./gradlew build
```

The Wrapper currently uses Gradle 9.3.0. The Kotlin Gradle Plugin version is defined in `settings.gradle.kts`.

## Test fixtures

Prebuilt ELF files used by the automated tests are stored in `src/test/resources/elf/`. They are test inputs and are not included in the KElf main library JAR.

The Android NDK/CMake project used to regenerate the small ARM and AArch64 shared-library fixtures is in `test-fixtures/native-android/`. Regenerating those files is optional: the checked-in fixtures are sufficient to run the existing tests.

See:

- [Test fixture overview](test-fixtures/README.md)
- [Android native fixture builder](test-fixtures/native-android/README.md)
- [ELF test files](src/test/resources/elf/README.md)

## Project layout

```text
KElf/
├── examples/                         # Standalone command-line example module
├── references/                       # ELF reference headers and notes
├── src/
│   ├── main/kotlin/com/riraki/kelf/   # KElf library source
│   └── test/
│       ├── kotlin/                   # Automated tests
│       └── resources/elf/            # ELF test inputs
├── test-fixtures/                    # Tools for creating test ELF files
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
└── gradle/wrapper/
```

## Scope and limitations

KElf is an **ELF parser**, not an ELF loader, dynamic linker, disassembler, or emulator. It reads file structures and metadata. It does not map segments into a process, apply relocations, resolve symbols at runtime, or execute machine code.

The set of machine identifiers and architecture-specific relocation names is intentionally limited to the implementations currently present in the library.

## License

KElf is distributed under the [Mozilla Public License 2.0](LICENSE). Third-party reference files and binary test fixtures may retain separate license or redistribution terms; see the README files in their respective directories.
