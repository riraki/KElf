#include "md5.h"

#include <cstring>
#include <iomanip>
#include <vector>
#include <sstream>

MD5::MD5() {
	// MD5 的4个初始状态值
	A = 0x67452301;
	B = 0xEFCDAB89;
	C = 0x98BADCFE;
	D = 0x10325476;

	total_bits = 0;
	finish = false;
}

void MD5::update(const string& data) {
	update(reinterpret_cast<const uint8_t*>(data.data()), data.size());
}

void MD5::update(const uint8_t* data, size_t len) {
	// 保存原始长度（bit单位）
	uint64_t bit_len = len * 8;

	// 先获取当前 buffer 使用的空间（字节）
	uint8_t buffer_size = total_bits >> 3 & 0x3f;

	// 统计总数据的 bit 长度
	total_bits += bit_len;

	// 记录传入的 data 的数据被处理的长度
	size_t process_len = 0;

	// 可以处理一次
	if (len + buffer_size >= 64) {
		memcpy(&buffer[buffer_size], data, 64 - buffer_size);
		process_block(buffer);
		process_len += 64 - buffer_size;
		buffer_size = 0;
	}

	// 这个时候已经处理了 64 - buffer_size 了
	// 还剩下 len - 64 + buffer_size（真的处理了）或 len + buffer_size（没处理）
	for (; process_len + 63 < len; process_len += 64) {
		process_block(data + process_len);
	}

	// 现在又处理了 64 的整数倍，还剩下  (len - 64 * n + buffer_size) % 64 字节 （n = 0, 1, 2, 3 ...）
	// 存放到 buffer 里面就可以了
	memcpy(buffer + buffer_size, data + process_len, len - process_len);
}

string MD5::hexdigest() {
	if (!finish) {
		finish = true;
		final();
	}

	// MD5输出是小端序
	string result;
	result += hex_digit(A);
	result += hex_digit(B);
	result += hex_digit(C);
	result += hex_digit(D);
	return result;
}

array<uint8_t, 16> MD5::digest() {
	if (!finish) {
		finish = true;
		final();
	}

	array<uint8_t, 16> result;
	for (int i = 0; i < 16; ++i) {
		if (i < 4) {
			result[i] = ((A >> (8 * i)) & 0xff);
		}
		else if (i < 8) {
			result[i] = ((B >> (8 * (i - 4))) & 0xff);
		}
		else if (i < 12) {
			result[i] = ((C >> (8 * (i - 8))) & 0xff);
		}
		else {
			result[i] = ((D >> (8 * (i - 12))) & 0xff);
		}
	}
	return result;
}

uint32_t MD5::F(uint32_t x, uint32_t y, uint32_t z) {
	return (x & y) | (~x & z);
}

uint32_t MD5::G(uint32_t x, uint32_t y, uint32_t z) {
	return (x & z) | (y & ~z);
}

uint32_t MD5::H(uint32_t x, uint32_t y, uint32_t z) {
	return x ^ y ^ z;
}

uint32_t MD5::I(uint32_t x, uint32_t y, uint32_t z) {
	return y ^ (x | ~z);
}

// 循环左移
uint32_t MD5::left_rotate(uint32_t x, uint32_t n) {
	return (x << n) | (x >> (32 - n));
}

string MD5::hex_digit(uint32_t x) {
	// MD5输出是小端序
	stringstream ss;
	for (int i = 0; i < 4; ++i) {
		ss << hex << setw(2) << setfill('0')
			<< ((x >> (8 * i)) & 0xff);
	}
	return ss.str();
}

void MD5::final() {
	// 拷贝缓冲区数据
	uint8_t buffer_size = total_bits >> 3 & 0x3f;
	vector<uint8_t> msg(buffer, buffer + buffer_size);

	// MD5 padding 开始：
	// 先补一个 0x80
	msg.push_back(0x80);

	// 补0直到长度 % 64 == 56
	while ((msg.size() % 64) != 56) {
		msg.push_back(0x00);
	}

	// 最后补 64bit 原始长度（小端）
	for (int i = 0; i < 8; ++i) {
		msg.push_back(static_cast<uint8_t>(total_bits >> (8 * i)));
	}

	// 上面的补 0 有可能让 msg.size() == 2 * 64
	// 那么我们可能需要处理两次
	for (size_t offset = 0; offset < msg.size(); offset += 64) {
		process_block(msg.data() + offset);
	}
}

void MD5::process_block(const uint8_t* block) {
	// 64字节拆成16个uint32_t
	// 注意：小端序
	uint32_t M[16];
	for (int i = 0; i < 16; ++i) {
		M[i] =
			static_cast<uint32_t>(block[i * 4]) |
			(static_cast<uint32_t>(block[i * 4 + 1]) << 8) |
			(static_cast<uint32_t>(block[i * 4 + 2]) << 16) |
			(static_cast<uint32_t>(block[i * 4 + 3]) << 24);
	}
	// 保存当前状态
	uint32_t a = A;
	uint32_t b = B;
	uint32_t c = C;
	uint32_t d = D;

	// MD5 的64个常量
	static const uint32_t T[64] = {
		0xd76aa478, 0xe8c7b756, 0x242070db, 0xc1bdceee,
		0xf57c0faf, 0x4787c62a, 0xa8304613, 0xfd469501,
		0x698098d8, 0x8b44f7af, 0xffff5bb1, 0x895cd7be,
		0x6b901122, 0xfd987193, 0xa679438e, 0x49b40821,
		0xf61e2562, 0xc040b340, 0x265e5a51, 0xe9b6c7aa,
		0xd62f105d, 0x02441453, 0xd8a1e681, 0xe7d3fbc8,
		0x21e1cde6, 0xc33707d6, 0xf4d50d87, 0x455a14ed,
		0xa9e3e905, 0xfcefa3f8, 0x676f02d9, 0x8d2a4c8a,
		0xfffa3942, 0x8771f681, 0x6d9d6122, 0xfde5380c,
		0xa4beea44, 0x4bdecfa9, 0xf6bb4b60, 0xbebfbc70,
		0x289b7ec6, 0xeaa127fa, 0xd4ef3085, 0x04881d05,
		0xd9d4d039, 0xe6db99e5, 0x1fa27cf8, 0xc4ac5665,
		0xf4292244, 0x432aff97, 0xab9423a7, 0xfc93a039,
		0x655b59c3, 0x8f0ccc92, 0xffeff47d, 0x85845dd1,
		0x6fa87e4f, 0xfe2ce6e0, 0xa3014314, 0x4e0811a1,
		0xf7537e82, 0xbd3af235, 0x2ad7d2bb, 0xeb86d391
	};

	// 每轮的左移位数
	static const uint32_t S[64] = {
		7,12,17,22, 7,12,17,22, 7,12,17,22, 7,12,17,22,
		5,9,14,20, 5,9,14,20, 5,9,14,20, 5,9,14,20,
		4,11,16,23, 4,11,16,23, 4,11,16,23, 4,11,16,23,
		6,10,15,21, 6,10,15,21, 6,10,15,21, 6,10,15,21
	};

	// 核心64轮
	for (int i = 0; i < 64; ++i) {
		uint32_t f;
		uint32_t g;
		// Round1
		if (i < 16) {
			f = F(b, c, d);
			g = i;
		}
		// Round2
		else if (i < 32) {
			f = G(b, c, d);
			g = (5 * i + 1) % 16;
		}
		// Round3
		else if (i < 48) {
			f = H(b, c, d);
			g = (3 * i + 5) % 16;
		}
		// Round4
		else {
			f = I(b, c, d);
			g = (7 * i) % 16;
		}

		// MD5核心公式
		uint32_t temp = d;
		d = c;
		c = b;
		b = b + left_rotate(
			a + f + T[i] + M[g],
			S[i]
		);
		a = temp;
	}

	// 累加回状态
	A += a;
	B += b;
	C += c;
	D += d;
}
