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

/**
 * This class contains functionality to convert a byte packed record into its bit packed equivalent
 */
class BitWriter {

    private final static int[] BIT_SET = {0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80};

    /**
     * Create a new bit  writer
     */
    public BitWriter() {
    }

    /**
     * Pack one byte aligned field into the bit aligned field.  Byte fields are right aligned, bit fields left
     * <p>
     * Very slow implementation  - optimize once test cases have good coverage
     *
     * @param sourceArray      The record array of byte fields data (Data in bits 0..n)
     * @param targetArray      The target bit field array (Data filled from left most bit first)
     * @param writeBitPosition Where to write to in the target array
     * @param bitLength        Size of field in bits
     */
    public void insertBits(final byte[] sourceArray, final byte[] targetArray, int writeBitPosition, final int bitLength) {
        final int byteArrayOffset = sourceArray.length - 1;
        for (int bit = bitLength - 1; bit >= 0; bit--) {
            int readBit = bit & 0x07;
            int readByte = byteArrayOffset - (bit >> 3);
            int readMask = BIT_SET[readBit];
            //
            int val = (sourceArray[readByte] & readMask);
            if (0 != val) {
                // Optimization - All bits start as zero, so only copy one's
                int writeBit = 7 - (writeBitPosition & 0x07);
                int writeByte = writeBitPosition >> 3;
                targetArray[writeByte] = (byte) (targetArray[writeByte] | BIT_SET[writeBit]);
            }
            writeBitPosition++;
        }
    }

}
