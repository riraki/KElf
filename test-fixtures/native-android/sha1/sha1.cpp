#include "sha1.h"

#include <vector>
#include <cstring>
#include <sstream>
#include <iomanip>

SHA1::SHA1() {
    // SHA1 的5个初始状态
    h0 = 0x67452301;
    h1 = 0xEFCDAB89;
    h2 = 0x98BADCFE;
    h3 = 0x10325476;
    h4 = 0xC3D2E1F0;

    total_bits = 0;
    finish = false;
}

void SHA1::update(const string& data) {
    update(reinterpret_cast<const uint8_t*>(data.data()), data.size());
}

void SHA1::update(const uint8_t* data, size_t len) {
    // 获取 buffer 使用的字节数
    uint8_t buffer_size = total_bits >> 3 & 0x3f;

    // SHA1长度按bit计算
    uint64_t bit_len = len * 8;

    // 更新 total_bits
    total_bits += bit_len;

    // 用于统计已经处理的 data 的长度
    size_t process_len = 0;

    // 如果缓冲区的加上传入的 data 构成一次 sha1 处理块，那么运行一次算法
    if (buffer_size + len >= 64) {
        memcpy(buffer, data, 64 - buffer_size);
        process_block(buffer);
        process_len += 64 - buffer_size;
        buffer_size = 0;
    }

    // 有可能这个 data 很大，除了上面的处理，自己还可以处理
    for (; process_len + 63 < len; process_len += 64) {
        process_block(data + process_len);
    }

    // 刚刚又处理了 n * 64 个字节，那么还剩下 (len - 64 + buffer_size)) % 64 字节，也就是 len - process_len
    memcpy(buffer + buffer_size, data + process_len, len - process_len);
}

string SHA1::hexdigest() {
    if (!finish) {
        finish = true;
        final();
    }
    // SHA1输出20字节
    string result;
    result += hex_digit(h0);
    result += hex_digit(h1);
    result += hex_digit(h2);
    result += hex_digit(h3);
    result += hex_digit(h4);
    return result;
}

array<uint8_t, 20> SHA1::digest() {
    if (!finish) {
        finish = true;
        final();
    }

    array<uint8_t, 20> result;
    // 大端字节序，高位存高地址，低位存低地址
    for (int i = 0; i < 20; ++i) {
        if (i < 4) {
            result[i] = ((h0 >> ((3 - i) * 8)) & 0xff);
        }
        else if (i < 8) {
            result[i] = ((h1 >> ((7 - i) * 8)) & 0xff);
        }
        else if (i < 12) {
            result[i] = ((h2 >> ((11 - i) * 8)) & 0xff);
        }
        else if (i < 16) {
            result[i] = ((h3 >> ((15 - i) * 8)) & 0xff);
        }
        else {
            result[i] = ((h4 >> ((19 - i) * 8)) & 0xff);
        }
    }
    return result;
}

void SHA1::final() {
    // 计算 buffer 的使用字节数
    uint8_t buffer_size = total_bits >> 3 & 0x3f;

    vector<uint8_t> msg(buffer, buffer + buffer_size);

    // padding开始
    msg.push_back(0x80);

    // 填充到 %64 == 56
    while ((msg.size() % 64) != 56) {
        msg.push_back(0x00);
    }

    // SHA1是大端存长度
    for (int i = 7; i >= 0; --i) {
        msg.push_back(static_cast<uint8_t>(total_bits >> (i * 8)));
    }

    // 每次处理64字节
    for (size_t offset = 0; offset < msg.size(); offset += 64) {
        process_block(msg.data() + offset);
    }
}

void SHA1::process_block(const uint8_t* block) {
    // SHA1的消息调度表
    uint32_t W[80];

    // 前16个来自block
    // 注意：SHA1是大端
    for (int i = 0; i < 16; ++i) {
        W[i] =
            (static_cast<uint32_t>(block[i * 4]) << 24) |
            (static_cast<uint32_t>(block[i * 4 + 1]) << 16) |
            (static_cast<uint32_t>(block[i * 4 + 2]) << 8) |
            static_cast<uint32_t>(block[i * 4 + 3]);
    }

    // SHA1巨大特征：
    // 16扩展到80
    for (int i = 16; i < 80; ++i) {
        W[i] = left_rotate(W[i - 3] ^ W[i - 8] ^ W[i - 14] ^ W[i - 16], 1);
    }
    // 初始化工作变量
    uint32_t a = h0;
    uint32_t b = h1;
    uint32_t c = h2;
    uint32_t d = h3;
    uint32_t e = h4;

    // SHA1核心80轮
    for (int i = 0; i < 80; ++i) {
        uint32_t f;
        uint32_t k;
        // 0~19
        if (i < 20) {
            f = (b & c) | ((~b) & d);
            k = 0x5A827999;
        }
        // 20~39
        else if (i < 40) {
            f = b ^ c ^ d;
            k = 0x6ED9EBA1;
        }
        // 40~59
        else if (i < 60) {
            f = (b & c) | (b & d) | (c & d);
            k = 0x8F1BBCDC;
        }
        // 60~79
        else {
            f = b ^ c ^ d;
            k = 0xCA62C1D6;
        }

        // SHA1核心公式
        uint32_t temp = left_rotate(a, 5) + f + e + k + W[i];
        e = d;
        d = c;

        // 这里是 rotate 30
        c = left_rotate(b, 30);
        b = a;
        a = temp;
    }

    // 累加回state
    h0 += a;
    h1 += b;
    h2 += c;
    h3 += d;
    h4 += e;
}

uint32_t SHA1::left_rotate(uint32_t x, uint32_t n) {
    return (x << n) | (x >> (32 - n));
}

string SHA1::hex_digit(uint32_t x) {
    stringstream ss;
    for (int i = 3; i >= 0; --i) {
        ss << hex << setw(2) << setfill('0') << ((x >> (i * 8)) & 0xff);
    }
    return ss.str();
}
