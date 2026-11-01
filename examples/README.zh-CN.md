# KElf 示例

[English](README.md)

本模块包含一些小型程序，用于演示如何在 Kotlin/JVM 项目中使用 KElf。它与库的正式源码集相互独立，不会被打包进 KElf 库产物。

## 运行 ELF 信息输出示例

`ElfDump` 会输出 `ElfFile` 提供的主要 ELF 结构。

在仓库根目录执行：

```bash
./gradlew :examples:run --args="/absolute/path/to/libnative.so"
```

程序要求传入且只传入一个参数，即 ELF 文件路径。它会输出：

- ELF Header 信息，包括 Class、字节序和机器架构类型
- Program Header 和 Section Header
- KElf 已识别的 Section
- 存在时的动态符号表和静态符号表
- 存在时的 Dynamic Section 条目

## Kotlin 基础用法

```kotlin
import com.riraki.kelf.ElfFile
import java.io.File

fun inspect(path: String) {
    val elf = ElfFile.from(File(path))

    println(elf.elfHeader.classDescription)
    println(elf.elfHeader.dataDescription)
    println(elf.elfHeader.machineDescription)

    elf.getSymbol("JNI_OnLoad")?.let { symbol ->
        println("JNI_OnLoad = 0x${symbol.st_value.toString(16)}")
    }
}
```

## Java 互操作

KElf 是一个 Kotlin/JVM 库，也可以从 Java 调用。`from` 工厂方法通过伴生对象暴露给 Java：

```java
import com.riraki.kelf.ElfFile;
import com.riraki.kelf.ElfSymbol;
import java.io.File;

public final class KElfJavaExample {
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: KElfJavaExample <elf-file>");
        }

        ElfFile elf = ElfFile.from(new File(args[0]));
        System.out.println(elf.getElfHeader());

        ElfSymbol symbol = elf.getSymbol("JNI_OnLoad");
        System.out.println(symbol == null ? "JNI_OnLoad not found" : symbol);
    }
}
```

## 说明

- 示例使用的公开包名为 `com.riraki.kelf`。
- 示例模块通过 `implementation(project(":"))` 依赖根目录中的 KElf 项目。
- 示例只演示 ELF 解析，不会将 ELF 加载到进程中，也不会执行重定位。
