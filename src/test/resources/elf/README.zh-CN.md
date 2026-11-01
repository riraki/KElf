# ELF 测试文件

[English](README.md)

本目录存放 KElf 自动化测试和示例程序使用的 ELF 二进制文件。这些文件仅作为测试输入，不是 KElf 的运行时依赖，也不会被打包进 KElf 主 JAR。

## 文件说明

- `generated/libnative-lib-armeabi-v7a.so`：使用 Android NDK 测试样本工程构建的 32 位 ARM 共享库。
- `generated/libnative-lib-arm64-v8a.so`：使用 Android NDK 测试样本工程构建的 64 位 AArch64 共享库。
- `android/arm/libart.so`：32 位 ARM Android Runtime 库样本，项目记录的来源版本为 Android 13 / API Level 33。
- `android/arm64/libart64.so`：64 位 AArch64 Android Runtime 库样本，项目记录的来源版本为 Android 13 / API Level 33。

前两个生成样本可以使用 `test-fixtures/native-android/build.sh` 重新构建。Android Runtime 库样本是直接提交到仓库的测试输入，不由该脚本生成。

## 再分发提示

Android Runtime 库二进制文件属于第三方测试材料。它们保存在本仓库中，并不意味着这些文件自动适用仓库顶层的 MPL-2.0 许可证。若要在其他软件包或发行物中再分发这些二进制文件，请先核实其来源及适用的许可证声明，并随文件保留相应的上游声明和源码/构建信息。
