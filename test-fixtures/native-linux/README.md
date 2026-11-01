# Linux ELF hash fixtures

[简体中文](README.zh-CN.md)

This small host-Linux build script creates two shared libraries from the same C source:
one with the SysV ELF hash table and one with the GNU hash table. The generated files are
used by KElf regression tests and are not runtime dependencies of the library.

## Requirements

- Linux
- GCC or a compatible C compiler/linker that supports `--hash-style`

## Build

Run from the repository root or this directory:

```bash
./test-fixtures/native-linux/build.sh
```

The script writes the generated files to `src/test/resources/elf/generated/`.

The checked-in binaries are sufficient to run the tests; this builder is optional.
