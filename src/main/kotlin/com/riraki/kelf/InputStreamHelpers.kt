package com.riraki.kelf

import java.io.EOFException
import java.io.InputStream

object InputStreamHelpers {
    fun readByteArrayFully(inputStream: InputStream, n: Int): ByteArray {
        if (n < 0) {
            throw IllegalArgumentException("Negative read length: $n")
        }

        val buffer = ByteArray(n)
        var bytesRead = 0
        while (bytesRead < n) {
            val count = inputStream.read(buffer, bytesRead, n - bytesRead)
            if (count == -1) {
                throw EOFException("Unexpected end of file: expected $n bytes, read $bytesRead")
            }
            if (count == 0) {
                val value = inputStream.read()
                if (value == -1) throw EOFException("Unexpected end of file")
                buffer[bytesRead++] = value.toByte()
            }
            else {
                bytesRead += count
            }
        }
        return buffer
    }

    fun readIntArrayFully(inputStream: InputStream, n: Int, isLittleEndian: Boolean): IntArray {
        return IntArray(n) {
            readInt(inputStream, isLittleEndian)
        }
    }

    fun readCheckedByte(inputStream: InputStream): Int {
        val value = inputStream.read()
        if (value == -1) throw EOFException("Unexpected end of file while reading byte")
        return value
    }

    fun readShort(inputStream: InputStream, isLittleEndian: Boolean): Short {
        return readUShort(inputStream, isLittleEndian).toShort()
    }

    fun readUShort(inputStream: InputStream, isLittleEndian: Boolean): UShort {
        val b1 = inputStream.read()
        val b2 = inputStream.read()
        if (b1 == -1 || b2 == -1) {
            throw EOFException("Reached end of stream while reading UShort")
        }

        return if (isLittleEndian) {
            ((b1 and 0xff) or ((b2 and 0xff) shl 8)).toUShort()
        }
        else {
            ((b2 and 0xff) or ((b1 and 0xff) shl 8)).toUShort()
        }
    }

    fun readInt(inputStream: InputStream, isLittleEndian: Boolean): Int {
        return readUInt(inputStream, isLittleEndian).toInt()
    }

    fun readUInt(inputStream: InputStream, isLittleEndian: Boolean): UInt {
        val b1 = inputStream.read()
        val b2 = inputStream.read()
        val b3 = inputStream.read()
        val b4 = inputStream.read()
        if (b1 == -1 || b2 == -1 || b3 == -1 || b4 == -1) {
            throw EOFException("Reached end of stream while reading UInt")
        }
        return if (isLittleEndian) {
            ((b1 and 0xff)
                    or ((b2 and 0xff) shl 8)
                    or ((b3 and 0xff) shl 16)
                    or ((b4 and 0xff) shl 24)
                    ).toUInt()
        }
        else {
            ((b4 and 0xff)
                    or ((b3 and 0xff) shl 8)
                    or ((b2 and 0xff) shl 16)
                    or ((b1 and 0xff) shl 24)
                    ).toUInt()
        }
    }

    fun readLong(inputStream: InputStream, isLittleEndian: Boolean): Long {
        return readULong(inputStream, isLittleEndian).toLong()
    }

    fun readULong(inputStream: InputStream, isLittleEndian: Boolean): ULong {
        val b1 = inputStream.read()
        val b2 = inputStream.read()
        val b3 = inputStream.read()
        val b4 = inputStream.read()
        val b5 = inputStream.read()
        val b6 = inputStream.read()
        val b7 = inputStream.read()
        val b8 = inputStream.read()
        if (b1 == -1 || b2 == -1 || b3 == -1 || b4 == -1 ||
            b5 == -1 || b6 == -1 || b7 == -1 || b8 == -1) {
            throw EOFException("Reached end of stream while reading ULong")
        }
        return if (isLittleEndian) {
            ((b1.toLong() and 0xff)
                    or ((b2.toLong() and 0xff) shl 8)
                    or ((b3.toLong() and 0xff) shl 16)
                    or ((b4.toLong() and 0xff) shl 24)
                    or ((b5.toLong() and 0xff) shl 32)
                    or ((b6.toLong() and 0xff) shl 40)
                    or ((b7.toLong() and 0xff) shl 48)
                    or ((b8.toLong() and 0xff) shl 56)
                    ).toULong()
        }
        else {
            ((b8.toLong() and 0xff)
                    or ((b7.toLong() and 0xff) shl 8)
                    or ((b6.toLong() and 0xff) shl 16)
                    or ((b5.toLong() and 0xff) shl 24)
                    or ((b4.toLong() and 0xff) shl 32)
                    or ((b3.toLong() and 0xff) shl 40)
                    or ((b2.toLong() and 0xff) shl 48)
                    or ((b1.toLong() and 0xff) shl 56)
                    ).toULong()
        }
    }

    fun skipFully(inputStream: InputStream, n: Long) {
        if (n <= 0) {
            return
        }
        var remaining = n
        while (remaining > 0) {
            val skipped = inputStream.skip(remaining)
            if (skipped > 0L) {
                remaining -= skipped
            }
            else {
                if (inputStream.read() == -1) {
                    throw EOFException("Unexpected EOF while skipping $n bytes")
                }
                remaining--
            }
        }
    }
}
