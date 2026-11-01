#ifndef SHA1_H
#define SHA1_H

#include <cstdint>
#include <array>
#include <string>

using namespace std;

class SHA1 {
public:
	SHA1();

public:
	void update(const string& data);
	void update(const uint8_t* data, size_t len);
	string hexdigest();
	array<uint8_t, 20> digest();
private:
	void final();
	void process_block(const uint8_t* block);

private:
	// SHA1最经典特征：
	// 循环左移
	static uint32_t left_rotate(uint32_t x, uint32_t n);
	static string hex_digit(uint32_t x);

private:
	uint32_t h0, h1, h2, h3, h4;
	uint64_t total_bits;
	uint8_t buffer[64];
	bool finish;
};

#endif // SHA1_H
