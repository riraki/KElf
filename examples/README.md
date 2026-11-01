# KElf examples

[简体中文](README.zh-CN.md)

This module contains small programs that demonstrate how to consume KElf as a Kotlin/JVM dependency. It is separate from the library's production source set and is not packaged in the KElf library artifact.

## Run the ELF dump example

`ElfDump` prints the main structures exposed by `ElfFile`.

From the repository root:

```bash
./gradlew :examples:run --args="/absolute/path/to/libnative.so"
```

The program requires exactly one argument: the path to an ELF file. It prints:

- ELF header information, including class, byte order, and machine type
- Program headers and section headers
- Sections recognized by KElf
- Dynamic and static symbol tables, when present
- Dynamic-section entries, when present

## Minimal Kotlin usage

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

## Java interoperability

KElf is published as a Kotlin/JVM library and can also be called from Java. The `from` factory is exposed through the companion object:

```java
import com.riraki.kelf.ElfFile;
import com.riraki.kelf.ElfSymbol;
import java.io.File;

public final class KElfJavaExample {
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: KElfJavaExample <elf-file>");
        }

        ElfFile elf = ElfFile.Companion.from(new File(args[0]));
        System.out.println(elf.getElfHeader());

        ElfSymbol symbol = elf.getSymbol("JNI_OnLoad");
        System.out.println(symbol == null ? "JNI_OnLoad not found" : symbol);
    }
}
```

## Notes

- Examples use the public package `com.riraki.kelf`.
- The example module depends on the root KElf project with `implementation(project(":"))`.
- These programs demonstrate parsing only. They do not load the ELF into a process or apply relocations.
