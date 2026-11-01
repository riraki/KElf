#ifndef MD5_H
#define MD5_H

#include <string>
#include <array>
#include <cstdint>

using namespace std;

class MD5 {
public:
    MD5();

public:
    void update(const string& data);
    void update(const uint8_t* data, size_t len);
    string hexdigest();
    array<uint8_t, 16> digest();

private:
    // 四个核心逻辑函数
    static uint32_t F(uint32_t x, uint32_t y, uint32_t z);
    static uint32_t G(uint32_t x, uint32_t y, uint32_t z);
    static uint32_t H(uint32_t x, uint32_t y, uint32_t z);
    static uint32_t I(uint32_t x, uint32_t y, uint32_t z);

    // 循环左移
    static uint32_t left_rotate(uint32_t x, uint32_t n);
    static string hex_digit(uint32_t x);

private:
    void final();
    void process_block(const uint8_t* block);

private:
    uint32_t A, B, C, D;
    uint64_t total_bits;
    uint8_t buffer[64];
    bool finish;
};

#endif // MD5_H
