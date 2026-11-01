# Android Native ELF 测试样本构建工具

[English](README.md)

本 CMake 工程用于构建 KElf 测试所需的 ARM（`armeabi-v7a`）和 AArch64（`arm64-v8a`）小型共享库。它只是测试样本生成工具，并不是 KElf 库的依赖。

## 环境要求

- 带有 CMake Toolchain 文件的 Android NDK
- CMake 3.22.1 或更高版本
- Ninja

将 `ANDROID_NDK_ROOT` 或 `ANDROID_NDK_HOME` 设置为本机 NDK 安装目录，然后在仓库根目录或本目录运行构建脚本：

```bash
export ANDROID_NDK_ROOT="$HOME/Android/Sdk/ndk/<ndk-version>"
./test-fixtures/native-android/build.sh
```

也可以在本目录设置好环境变量后直接运行 `./build.sh`。

## 输出文件

脚本会分别构建 `armeabi-v7a` 和 `arm64-v8a` 两个版本。生成的共享库写入：

```text
src/test/resources/elf/generated/
```

CMake/Ninja 中间文件存放在本目录下的 `.build/` 中，不应提交到 Git。默认 Android 平台目标为 `android-31`；可以通过 `ANDROID_PLATFORM` 环境变量覆盖，例如设置为 `ANDROID_PLATFORM=android-33`。

生成的文件仅作为测试样本，不会被打包进 KElf 主 JAR。
