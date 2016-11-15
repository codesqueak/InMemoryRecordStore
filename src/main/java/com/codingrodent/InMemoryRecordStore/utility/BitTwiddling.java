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
package com.codingrodent.InMemoryRecordStore.utility;

/**
 *
 */
public class BitTwiddling {
    private final static int[] EXTEND32 = new int[33];
    private final static long[] EXTEND64 = new long[65];
    private final static int[] SHRINK32 = new int[33];
    private final static long[] SHRINK64 = new long[65];

    // Predefined bit masks
    static {
        EXTEND32[1] = 1;
        for (int i = 2; i <= 32; i++) {
            EXTEND32[i] = EXTEND32[i - 1] << 1;
        }
        SHRINK32[32] = 0xFFFF_FFFF;
        for (int i = 31; i >= 0; i--) {
            SHRINK32[i] = SHRINK32[i + 1] >>> 1;
        }
        EXTEND64[1] = 1;
        for (int i = 2; i <= 64; i++) {
            EXTEND64[i] = EXTEND64[i - 1] << 1;
        }
        SHRINK64[64] = 0xFFFF_FFFF_FFFF_FFFFL;
        for (int i = 63; i >= 0; i--) {
            SHRINK64[i] = SHRINK64[i + 1] >>> 1;
        }
    }

    private BitTwiddling() {
        // Stop creation
    }

    /**
     * Sign extend a value
     *
     * @param val  Value to be extended
     * @param bits Number of bits in value
     * @return Extended value
     */
    public static byte extend(final byte val, final int bits) {
        final int m = EXTEND32[bits];
        return (byte) ((val ^ m) - m);
    }

    public static byte shrink(final byte val, final int bits) {
        return (byte) (val & SHRINK32[bits]);
    }

    public static short extend(final short val, final int bits) {
        final int m = EXTEND32[bits];
        return (short) ((val ^ m) - m);
    }

    public static short shrink(final short val, final int bits) {
        return (short) (val & SHRINK32[bits]);
    }

    public static int extend(final int val, final int bits) {
        final int m = EXTEND32[bits];
        return (val ^ m) - m;
    }

    public static int shrink(final int val, final int bits) {
        return val & SHRINK32[bits];
    }

    public static long extend(final long val, final int bits) {
        final long m = EXTEND64[bits];
        return (val ^ m) - m;
    }

    public static long shrink(final long val, final int bits) {
        return val & SHRINK64[bits];
    }
}
