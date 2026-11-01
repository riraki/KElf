# Android native ELF fixture builder

[简体中文](README.zh-CN.md)

This CMake project builds small ARM (`armeabi-v7a`) and AArch64 (`arm64-v8a`) shared libraries used as KElf test inputs. It is a fixture-generation tool, not a dependency of the KElf library.

## Requirements

- Android NDK with the CMake toolchain file
- CMake 3.22.1 or newer
- Ninja

Set `ANDROID_NDK_ROOT` or `ANDROID_NDK_HOME` to the installed NDK directory, then run the build script from the repository root or this directory:

```bash
export ANDROID_NDK_ROOT="$HOME/Android/Sdk/ndk/<ndk-version>"
./test-fixtures/native-android/build.sh
```

Alternatively, run `./build.sh` from this directory after exporting the variable.

## Output

The script builds both `armeabi-v7a` and `arm64-v8a` variants. The generated shared libraries are written to:

```text
src/test/resources/elf/generated/
```

CMake/Ninja intermediate files are stored in `.build/` under this directory and should not be committed. The default Android platform target is `android-31`; override it by setting `ANDROID_PLATFORM`, for example `ANDROID_PLATFORM=android-33`.

The generated files are test fixtures. They are not included in the KElf main JAR.
