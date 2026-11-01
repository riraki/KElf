#include <cstring>
#include <vector>
#include <sstream>
#include <iomanip>

#include "sha512.h"

SHA512::SHA512() {
	// SHA512 的 8 个初始状态
	H[0] = 0x6a09e667f3bcc908ULL;
	H[1] = 0xbb67ae8584caa73bULL;
	H[2] = 0x3c6ef372fe94f82bULL;
	H[3] = 0xa54ff53a5f1d36f1ULL;
	H[4] = 0x510e527fade682d1ULL;
	H[5] = 0x9b05688c2b3e6c1fULL;
	H[6] = 0x1f83d9abfb41bd6bULL;
	H[7] = 0x5be0cd19137e2179ULL;

	buffer_size = 0;

	bit_len_low = 0;
	bit_len_high = 0;

	finished = false;
}

void SHA512::update(const std::string& data) {
	update(reinterpret_cast<const uint8_t*>(data.data()), data.size());
}

void SHA512::update(const uint8_t* data, size_t len) {
	// 计算总数据 bit 长度
	bit_len_low += static_cast<uint64_t>(len * 8);
	if (bit_len_low < static_cast<uint64_t>(len * 8)) {
		++bit_len_high;
	}
	bit_len_high += static_cast<uint64_t>(len) >> 61;

	size_t offset = 0;
	// 填满buffer
	if (buffer_size > 0) {
		size_t need = 128 - buffer_size;
		size_t take = (len < need) ? len : need;

		memcpy(buffer + buffer_size, data, need);
		buffer_size += take;
		offset += take;
		len -= take;

		if (buffer_size == 128) {
			process_block(buffer);
			buffer_size = 0;
		}
	}

	// 直接处理完整的 128 字节块
	while (len >= 128) {
		process_block(data + offset);
		offset += 128;
		len -= 128;
	}

	// 剩余不足 128 字节的数据，留给 buffer
	memcpy(buffer + buffer_size, data + offset, len);
	buffer_size += len;
}

std::string SHA512::hexdigest() {
	if (!finished) {
		finished = true;
		final();
	}

	// SHA512 是大端序
	// 高位存低地址
	std::string result;
	for (int i = 0; i < 8; ++i) {
		result += hex_digit(H[i]);
	}
	return result;
}

std::array<uint8_t, 64> SHA512::digest() {
	if (!finished) {
		finished = true;
		final();
	}

	// SHA512 是大端序
	// 高位存低地址
	std::array<uint8_t, 64> result;
	for (int i = 0; i < 8; ++i) {
		for (int j = 0; j < 8; ++j) {
			result[i * 8 + j] = ((H[i] >> ((7 - j) * 8)) & 0xff);
		}
	}

	return result;
}

void SHA512::final() {
	std::vector<uint8_t> msg(buffer, buffer + buffer_size);

	// 先补 0x80
	msg.push_back(0x80);

	// SHA512 要求末尾的数据长度应该是 16 字节（这也就是为什么要用两个 uint64_t 来计算数据长度的原因）
	// (msg.size() + 16) % 128 = 0
	// msg.size() % 128 != 112
	while (msg.size() % 128 != 112) {
		msg.push_back(0x00);
	}

	// SHA512 是大端序，高位存低地址
	for (int i = 7; i >= 0; --i) {
		msg.push_back(static_cast<uint64_t>(bit_len_high >> (i * 8)));
	}
	for (int i = 7; i >= 0; --i) {
		msg.push_back(static_cast<uint64_t>(bit_len_low >> (i * 8)));
	}

	// 填充和加上长度段后，可能是 1 个块，也可能是 2 个块
	for (size_t offset = 0; offset < msg.size(); offset += 128) {
		process_block(msg.data() + offset);
	}
}

// SHA512 核心算法函数
void SHA512::process_block(const uint8_t* block) {
	uint64_t W[80];

	// 将 128 个 uint8_t 转化成 16 个 uint64_t
	// SHA512 是大端，应该高位存低地址
	for (int i = 0; i < 16; ++i) {
		W[i] = (static_cast<uint64_t>(block[i * 8])) << 56 |
			(static_cast<uint64_t>(block[i * 8 + 1])) << 48 |
			(static_cast<uint64_t>(block[i * 8 + 2])) << 40 |
			(static_cast<uint64_t>(block[i * 8 + 3])) << 32 |
			(static_cast<uint64_t>(block[i * 8 + 4])) << 24 |
			(static_cast<uint64_t>(block[i * 8 + 5])) << 16 |
			(static_cast<uint64_t>(block[i * 8 + 6])) << 8 |
			(static_cast<uint64_t>(block[i * 8 + 7]));
	}

	// 将 16 个 uint64_t 扩展为 80 个
	for (int i = 16; i < 80; ++i) {
		W[i] = sigma1(W[i - 2]) + W[i - 7] + sigma0(W[i - 15]) + W[i - 16];
	}

	uint64_t a = H[0];
	uint64_t b = H[1];
	uint64_t c = H[2];
	uint64_t d = H[3];
	uint64_t e = H[4];
	uint64_t f = H[5];
	uint64_t g = H[6];
	uint64_t h = H[7];

	// SHA512 的核心 80 轮计算
	for (int i = 0; i < 80; ++i) {
		uint64_t T1 = h + Sigma1(e) + Ch(e, f, g) + K[i] + W[i];
		uint64_t T2 = Sigma0(a) + Maj(a, b, c);

		h = g;
		g = f;
		f = e;
		e = d + T1;
		d = c;
		c = b;
		b = a;
		a = T1 + T2;
	}

	// 累加回 state
	H[0] += a;
	H[1] += b;
	H[2] += c;
	H[3] += d;
	H[4] += e;
	H[5] += f;
	H[6] += g;
	H[7] += h;
}

// 64 位循环右移
uint64_t SHA512::rotr(uint64_t x, uint64_t n) {
	return (x >> n) | (x << (64 - n));
}

uint64_t SHA512::Ch(uint64_t x, uint64_t y, uint64_t z) {
	return (x & y) ^ (~x & z);
}

uint64_t SHA512::Maj(uint64_t x, uint64_t y, uint64_t z) {
	return (x & y) ^ (x & z) ^ (y & z);
}

uint64_t SHA512::Sigma0(uint64_t x) {
	return rotr(x, 28) ^ rotr(x, 34) ^ rotr(x, 39);
}

uint64_t SHA512::Sigma1(uint64_t x) {
	return rotr(x, 14) ^ rotr(x, 18) ^ rotr(x, 41);
}

uint64_t SHA512::sigma0(uint64_t x) {
	return rotr(x, 1) ^ rotr(x, 8) ^ (x >> 7);
}

uint64_t SHA512::sigma1(uint64_t x) {
	return rotr(x, 19) ^ rotr(x, 61) ^ (x >> 6);
}

std::string SHA512::hex_digit(uint64_t x) {
	std::stringstream ss;
	for (int i = 7; i >= 0; --i) {
		ss << std::hex << std::setw(2) << std::setfill('0') << ((x >> (i * 8)) & 0xff);
	}
	return ss.str();
}

const uint64_t SHA512::K[80] = {
	0x428a2f98d728ae22ULL, 0x7137449123ef65cdULL, 0xb5c0fbcfec4d3b2fULL, 0xe9b5dba58189dbbcULL,
	0x3956c25bf348b538ULL, 0x59f111f1b605d019ULL, 0x923f82a4af194f9bULL, 0xab1c5ed5da6d8118ULL,
	0xd807aa98a3030242ULL, 0x12835b0145706fbeULL, 0x243185be4ee4b28cULL, 0x550c7dc3d5ffb4e2ULL,
	0x72be5d74f27b896fULL, 0x80deb1fe3b1696b1ULL, 0x9bdc06a725c71235ULL, 0xc19bf174cf692694ULL,
	0xe49b69c19ef14ad2ULL, 0xefbe4786384f25e3ULL, 0x0fc19dc68b8cd5b5ULL, 0x240ca1cc77ac9c65ULL,
	0x2de92c6f592b0275ULL, 0x4a7484aa6ea6e483ULL, 0x5cb0a9dcbd41fbd4ULL, 0x76f988da831153b5ULL,
	0x983e5152ee66dfabULL, 0xa831c66d2db43210ULL, 0xb00327c898fb213fULL, 0xbf597fc7beef0ee4ULL,
	0xc6e00bf33da88fc2ULL, 0xd5a79147930aa725ULL, 0x06ca6351e003826fULL, 0x142929670a0e6e70ULL,
	0x27b70a8546d22ffcULL, 0x2e1b21385c26c926ULL, 0x4d2c6dfc5ac42aedULL, 0x53380d139d95b3dfULL,
	0x650a73548baf63deULL, 0x766a0abb3c77b2a8ULL, 0x81c2c92e47edaee6ULL, 0x92722c851482353bULL,
	0xa2bfe8a14cf10364ULL, 0xa81a664bbc423001ULL, 0xc24b8b70d0f89791ULL, 0xc76c51a30654be30ULL,
	0xd192e819d6ef5218ULL, 0xd69906245565a910ULL, 0xf40e35855771202aULL, 0x106aa07032bbd1b8ULL,
	0x19a4c116b8d2d0c8ULL, 0x1e376c085141ab53ULL, 0x2748774cdf8eeb99ULL, 0x34b0bcb5e19b48a8ULL,
	0x391c0cb3c5c95a63ULL, 0x4ed8aa4ae3418acbULL, 0x5b9cca4f7763e373ULL, 0x682e6ff3d6b2b8a3ULL,
	0x748f82ee5defb2fcULL, 0x78a5636f43172f60ULL, 0x84c87814a1f0ab72ULL, 0x8cc702081a6439ecULL,
	0x90befffa23631e28ULL, 0xa4506cebde82bde9ULL, 0xbef9a3f7b2c67915ULL, 0xc67178f2e372532bULL,
	0xca273eceea26619cULL, 0xd186b8c721c0c207ULL, 0xeada7dd6cde0eb1eULL, 0xf57d4f7fee6ed178ULL,
	0x06f067aa72176fbaULL, 0x0a637dc5a2c898a6ULL, 0x113f9804bef90daeULL, 0x1b710b35131c471bULL,
	0x28db77f523047d84ULL, 0x32caab7b40c72493ULL, 0x3c9ebe0a15c9bebcULL, 0x431d67c49c100d4cULL,
	0x4cc5d4becb3e42b6ULL, 0x597f299cfc657e2aULL, 0x5fcb6fab3ad6faecULL, 0x6c44198c4a475817ULL
};
