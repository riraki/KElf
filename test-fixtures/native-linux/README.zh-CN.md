# Linux ELF 哈希表测试样本构建工具

[English](README.md)

本目录中的脚本使用同一份 C 源码构建两个 Linux 共享库：一个包含 SysV ELF Hash，另一个包含 GNU Hash。这些文件用于 KElf 的哈希解析回归测试，不是 KElf 的运行时依赖。

## 环境要求

- Linux
- 支持 `--hash-style` 参数的 GCC 或兼容 C 编译器/链接器

## 构建

可以在仓库根目录或本目录运行：

```bash
./test-fixtures/native-linux/build.sh
```

生成文件写入 `src/test/resources/elf/generated/`。

仓库已经提交预编译样本，因此运行测试不需要重新构建；此工具仅用于重新生成样本。
