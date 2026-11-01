#ifndef SHA512_H
#define SHA512_H

#include <string>
#include <cstdint>
#include <array>

class SHA512 {
public:
	SHA512();

public:
	void update(const std::string& data);
	void update(const uint8_t* data, size_t len);
	std::string hexdigest();
	std::array<uint8_t, 64> digest();

private:
	void final();

	// SHA512 核心算法函数
	void process_block(const uint8_t* block);

private:
	// 64 位循环右移
	static uint64_t rotr(uint64_t x, uint64_t n);
	static uint64_t Ch(uint64_t x, uint64_t y, uint64_t z);
	static uint64_t Maj(uint64_t x, uint64_t y, uint64_t z);
	static uint64_t Sigma0(uint64_t x);
	static uint64_t Sigma1(uint64_t x);
	static uint64_t sigma0(uint64_t x);
	static uint64_t sigma1(uint64_t x);

	static std::string hex_digit(uint64_t x);

private:
	uint64_t H[8];

	uint8_t buffer[128];
	size_t buffer_size;

	uint64_t bit_len_low;
	uint64_t bit_len_high;

	bool finished;

	// SHA512 用于核心 80 轮计算的 80 个常量
	static const uint64_t K[80];
};

#endif // SHA512_H
