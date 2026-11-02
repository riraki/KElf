# KElf

[English](README.md)

[![Maven Central](https://img.shields.io/maven-central/v/io.github.riraki/kelf?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.riraki/kelf)
[![License: MPL-2.0](https://img.shields.io/badge/License-MPL--2.0-blue.svg)](LICENSE)

**KElf 是一个使用 Kotlin/JVM 编写的 ELF32 和 ELF64 文件解析库。** 它读取 ELF 文件中的元数据，并通过简洁的程序接口提供解析结果。KElf 不负责加载或执行 ELF 文件。

## 功能特性

- **ELF 标识与文件头**：读取 ELF Class、字节序、文件类型、版本、入口地址和机器架构标识。
- **Program Header**：解析程序头表及 Segment 属性。
- **Section Header 与 Section**：解析节元数据并解析节名称。
- **字符串表**：读取节名称、符号名称和动态字符串表。
- **符号表**：解析 `.symtab` 和 `.dynsym`。
- **Dynamic Section**：解析动态条目、依赖库名称及部分标志位描述。
- **重定位**：解析 `REL` 和 `RELA` 条目，并提供符号索引和重定位类型信息。
- **哈希表**：解析 SysV ELF Hash 和 GNU Hash，并提供符号查找辅助方法。
- **NOTE 节**：解析 NOTE 记录，并在存在时提取 GNU Build ID。
- **ELF32 和 ELF64**：根据 ELF 文件的标识字节自动选择字段宽度。
- **小端和大端数据**：根据 `EI_DATA` 指定的字节序读取多字节字段。

目前识别的 `e_machine` 值包括 `EM_386(X86)`、`EM_ARM(ARM)`、`EM_X86_64(X86-64)` 和 `EM_AARCH64(AARCH64)`。能够识别机器架构标识，并不意味着已经完整支持该架构的所有 ELF 扩展或重定位类型。

## 环境要求

- 构建和运行 KElf 需要 JDK 17 或更高版本。
- 无需单独安装 Gradle。仓库已经包含 Gradle Wrapper。

## 安装

KElf `0.1.0` 已发布到 Maven Central。

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

Maven 坐标与 Kotlin 包名互相独立。当前公开 Kotlin 包名为 `com.riraki.kelf`。

## 快速开始

主要入口是 `ElfFile.from(File)`。将 ELF 文件传入即可，KElf 会读取文件中的 ELF Class 和字节序，并在库内部完成对应结构的解析。

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

`ElfFile` 还提供了对已解析 Section 和常见表结构的直接访问：

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

以上示例使用当前 `com.riraki.kelf` 包中的 API。

## 命令行示例

仓库包含一个独立的 `examples` 模块，提供 ELF 信息查看命令行程序。在仓库根目录执行：

```bash
./gradlew :examples:run --args="/absolute/path/to/libnative.so"
```

Windows 下使用 `gradlew.bat`：

```bat
gradlew.bat :examples:run --args="C:\\path\\to\\libnative.so"
```

示例程序会输出 ELF Header、Program Header、Section Header、已解析的 Section、符号表和 Dynamic Section。

## 构建与测试

```bash
# 编译并运行自动化测试
./gradlew clean test

# 构建库和 examples 模块
./gradlew build
```

当前 Wrapper 使用 Gradle 9.3.0。Kotlin Gradle Plugin 版本由 `settings.gradle.kts` 管理。

## 测试样本

自动化测试使用的预编译 ELF 文件位于 `src/test/resources/elf/`。这些文件仅作为测试输入，不会被打包进 KElf 的主库 JAR。

用于重新生成 ARM 和 AArch64 小型共享库样本的 Android NDK/CMake 工程位于 `test-fixtures/native-android/`。重新生成样本不是运行现有测试的必要条件，仓库中已经包含可用的预编译样本。

相关说明：

- [测试样本总览](test-fixtures/README.md)
- [Android Native 测试样本构建工程](test-fixtures/native-android/README.md)
- [ELF 测试文件说明](src/test/resources/elf/README.md)

## 项目结构

```text
KElf/
├── examples/                         # 独立的命令行示例模块
├── references/                       # ELF 参考头文件及说明
├── src/
│   ├── main/kotlin/com/riraki/kelf/   # KElf 正式库源码
│   └── test/
│       ├── kotlin/                   # 自动化测试代码
│       └── resources/elf/            # ELF 测试输入文件
├── test-fixtures/                    # 用于生成测试 ELF 文件的工具
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
└── gradle/wrapper/
```

## 项目范围与限制

KElf 是一个 **ELF 解析库**，不是 ELF Loader、动态链接器、反汇编器或模拟器。它读取 ELF 文件结构和元数据，但不会将 Segment 映射到进程地址空间、执行重定位、在运行时解析符号或执行机器码。

目前支持的机器架构标识和架构专用重定位名称仅限于库中已经实现的部分。

## 许可证

KElf 使用 [Mozilla Public License 2.0](LICENSE) 发布。第三方参考文件和二进制测试样本可能适用独立的许可证或再分发条款，请查看各自目录中的 README。
