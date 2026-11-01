# ELF test fixtures

[简体中文](README.zh-CN.md)

This directory contains auxiliary tools used to create ELF files for KElf's tests. Nothing in this directory belongs to the library's production source set or is required by applications that depend on KElf.

## Contents

- `native-android/` — an Android NDK/CMake project that builds small ARM and AArch64 shared-library fixtures.
- `../src/test/resources/elf/` — checked-in ELF binaries consumed by automated tests.
- `../references/` — reference headers retained for implementation research; they are not compiled into KElf.

## Regenerate native fixtures

See [native-android/README.md](native-android/README.md) for the NDK, CMake, and Ninja requirements and the build command.

The prebuilt ELF files are already committed under `src/test/resources/elf/`, so an Android NDK is not needed to build KElf or run the current tests.
