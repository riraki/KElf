/*
 * Tiny shared-library fixture used to exercise ELF dynamic hash parsing.
 * The fixture is intentionally independent of Android/JNI.
 */
__attribute__((visibility("default")))
int kelf_exported_add(int left, int right) {
    return left + right;
}

__attribute__((visibility("default")))
int kelf_exported_value = 42;
