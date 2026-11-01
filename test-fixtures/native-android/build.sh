#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd -- "${SCRIPT_DIR}/../.." && pwd)"

ANDROID_NDK="${ANDROID_NDK_ROOT:-${ANDROID_NDK_HOME:-}}"
if [[ -z "${ANDROID_NDK}" ]]; then
    echo "Set ANDROID_NDK_ROOT or ANDROID_NDK_HOME to your Android NDK directory." >&2
    exit 1
fi

TOOLCHAIN_FILE="${ANDROID_NDK}/build/cmake/android.toolchain.cmake"
if [[ ! -f "${TOOLCHAIN_FILE}" ]]; then
    echo "Android NDK toolchain file not found: ${TOOLCHAIN_FILE}" >&2
    exit 1
fi

if ! command -v cmake >/dev/null 2>&1; then
    echo "cmake is required but was not found in PATH." >&2
    exit 1
fi

if ! command -v ninja >/dev/null 2>&1; then
    echo "ninja is required but was not found in PATH." >&2
    exit 1
fi

BUILD_ROOT="${SCRIPT_DIR}/.build"
OUTPUT_DIR="${ROOT_DIR}/src/test/resources/elf/generated"
ANDROID_PLATFORM="${ANDROID_PLATFORM:-android-31}"
mkdir -p "${OUTPUT_DIR}"

for ABI in armeabi-v7a arm64-v8a; do
    cmake \
        -S "${SCRIPT_DIR}" \
        -B "${BUILD_ROOT}/${ABI}" \
        -G Ninja \
        -DANDROID_ABI="${ABI}" \
        -DANDROID_PLATFORM="${ANDROID_PLATFORM}" \
        -DANDROID_NDK="${ANDROID_NDK}" \
        -DCMAKE_TOOLCHAIN_FILE="${TOOLCHAIN_FILE}" \
        -DELF_FIXTURE_OUTPUT_DIR="${OUTPUT_DIR}"

    cmake --build "${BUILD_ROOT}/${ABI}" --parallel
done
