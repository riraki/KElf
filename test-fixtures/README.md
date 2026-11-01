# ELF test fixtures

[简体中文](README.zh-CN.md)

This directory contains auxiliary tools used to create ELF files for KElf's tests. Nothing in this directory belongs to the library's production source set or is required by applications that depend on KElf.

## Contents

- `native-android/` — an Android NDK/CMake project that builds small ARM and AArch64 shared-library fixtures.
- `native-linux/` — a host-Linux builder that creates x86-64 shared libraries with SysV and GNU hash tables.
- `../src/test/resources/elf/` — checked-in ELF binaries consumed by automated tests.

## Regenerate native fixtures

See [native-android/README.md](native-android/README.md) for the NDK, CMake, and Ninja requirements. See [native-linux/README.md](native-linux/README.md) for the hash-table fixture builder.

The prebuilt ELF files are already committed under `src/test/resources/elf/`, so neither Android NDK nor a host C compiler is needed to build KElf or run the current tests.
