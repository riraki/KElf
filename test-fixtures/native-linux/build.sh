#!/usr/bin/env bash
set -euo pipefail

script_dir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
repo_root="$(cd -- "$script_dir/../.." && pwd)"
output_dir="$repo_root/src/test/resources/elf/generated"

mkdir -p "$output_dir"

cc="${CC:-gcc}"
"$cc" -shared -fPIC -Wl,--hash-style=sysv \
    "$script_dir/hash-fixture.c" \
    -o "$output_dir/libhash-sysv-x86_64.so"

"$cc" -shared -fPIC -Wl,--hash-style=gnu \
    "$script_dir/hash-fixture.c" \
    -o "$output_dir/libhash-gnu-x86_64.so"

echo "ELF hash fixtures generated in: $output_dir"

# Shared-library fixtures are data files in this repository, not executable programs.
chmod 644 "$output_dir"/*.so
