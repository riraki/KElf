cmake_minimum_required(VERSION 3.22.1)

project("native-lib")

if(NOT DEFINED ELF_FIXTURE_OUTPUT_DIR)
    set(ELF_FIXTURE_OUTPUT_DIR
        "${CMAKE_CURRENT_LIST_DIR}/../../src/test/resources/elf/generated")
endif()

file(MAKE_DIRECTORY "${ELF_FIXTURE_OUTPUT_DIR}")
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY "${ELF_FIXTURE_OUTPUT_DIR}")
set(CMAKE_BUILD_TYPE "Release")
set(CMAKE_C_FLAGS_RELEASE "${CMAKE_C_FLAGS_RELEASE} -s")
set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -s")

include_directories(.)
include_directories(md5)
include_directories(sha1)
include_directories(sha256)
include_directories(sha512)

set(LIBRARY_NAME "${CMAKE_PROJECT_NAME}-${ANDROID_ABI}")

add_library(
        ${LIBRARY_NAME}
        SHARED
        native-lib.cpp
        md5/md5.cpp
        sha1/sha1.cpp
        sha256/sha256.cpp
        sha512/sha512.cpp)

target_link_libraries(${LIBRARY_NAME}
        android
        log)
