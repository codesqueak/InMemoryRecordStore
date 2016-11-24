/*
* MIT License
*
*         Copyright (c) 2016
*
*         Permission is hereby granted, free of charge, to any person obtaining a copy
*         of this software and associated documentation files (the "Software"), to deal
*         in the Software without restriction, including without limitation the rights
*         to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
*         copies of the Software, and to permit persons to whom the Software is
*         furnished to do so, subject to the following conditions:
*
*         The above copyright notice and this permission notice shall be included in all
*         copies or substantial portions of the Software.
*
*         THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
*         IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
*         FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
*         AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
*         LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
*         OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
*         SOFTWARE.
*/
package com.codingrodent.InMemoryRecordStore.record;

class BitReader {
    private final static int[] BIT_TEST = {0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01};

    /**
     * Create a new bit reader
     */
    public BitReader() {
    }

    /**
     * Unpack byte aligned field into the bit aligned field.  Byte fields are right aligned, bit fields left
     * <p>
     * Very slow implementation  - optimize once test cases have good coverage
     *
     * @param sourceArray     The record array of byte fields data
     * @param readBitPosition Where to read  from in the source array
     * @param bitLength       Size of field in bits
     * @return Integer holding up to 32 bit value
     */
    public int unpack(byte[] sourceArray, int readBitPosition, final int bitLength) {
        int target = 0;
        for (int i = 0; i < bitLength; i++) {
            int readByte = sourceArray[readBitPosition >> 3];
            int readMask = BIT_TEST[readBitPosition & 0x07];
            target = target << 1;
            if (0 != (readByte & readMask)) {
                target = target | 0x01;
            }
            readBitPosition++;
        }
        return target;
    }

    /**
     * Unpack byte aligned field into the bit aligned field.  Byte fields are right aligned, bit fields left
     * <p>
     * Very slow implementation  - optimize once test cases have good coverage
     *
     * @param sourceArray     The record array of byte fields data
     * @param readBitPosition Where to read  from in the source array
     * @return Integer holding up to 32 bit value
     */
    public int unpack32(byte[] sourceArray, int readBitPosition) {
        return unpack(sourceArray, readBitPosition, 32);
    }

    /**
     * Unpack byte aligned field into the bit aligned field.  Byte fields are right aligned, bit fields left
     * <p>
     * Very slow implementation  - optimize once test cases have good coverage
     *
     * @param sourceArray     The record array of byte fields data
     * @param readBitPosition Where to read  from in the source array
     * @return Integer holding up to 32 bit value
     */
    public long unpack64(byte[] sourceArray, int readBitPosition) {

        long p0 = ((long) unpack32(sourceArray, readBitPosition)) & 0x0000_0000_FFFF_FFFF;
        return (((long) unpack32(sourceArray, readBitPosition + 32)) << 32) | p0;

    }

}