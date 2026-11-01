# ELF 测试样本工具

[English](README.md)

本目录存放用于生成 KElf 测试 ELF 文件的辅助工具。本目录中的内容不属于库的正式源码，也不是其他应用依赖 KElf 时所需的运行时组件。

## 目录内容

- `native-android/`：Android NDK/CMake 工程，用于构建 ARM 和 AArch64 小型共享库测试样本。
- `native-linux/`：主机 Linux 构建工具，用于生成带 SysV Hash 和 GNU Hash 的 x86-64 共享库样本。
- `../src/test/resources/elf/`：提交到仓库、供自动化测试使用的 ELF 二进制文件。

## 重新生成 Native 测试样本

NDK、CMake、Ninja 的要求和构建命令请查看 [native-android/README.md](native-android/README.md)。哈希表测试样本的构建方法请查看 [native-linux/README.md](native-linux/README.md)。

预编译 ELF 文件已经保存在 `src/test/resources/elf/`，因此构建 KElf 或运行当前测试不需要安装 Android NDK，也不需要主机 C 编译器。
