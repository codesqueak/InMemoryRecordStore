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
package net.codingrodent.InMemoryRecordStore.utility;

public class Utilities {
    private final static String hexChar = "0123456789ABCDEF";

    /**
     * Turn a 4 bit nibble into its equivalent hex digit
     *
     * @param value Generate a character from a 4 bit value
     * @return A hex character 0..F
     */
    private static char getHexCharacter(final int value) {
        return hexChar.charAt(value);
    }

    /**
     * Turn a byte into its equivalent hex digits
     *
     * @param value Generate a character from a value
     * @return A hex value in the range 00..FF
     */
    public static String getByte(int value) {
        final char[] byteText = new char[2];
        value = value & 0x000000FF;
        byteText[0] = getHexCharacter(value >>> 4);
        byteText[1] = getHexCharacter(value & 0x0F);
        return new String(byteText);
    }

    /**
     * Turn a short into its equivalent hex digits
     *
     * @param value Generate a character from a value
     * @return A hex value in the range 0000..FFFF
     */
    public static String getShort(final int value) {
        return getByte(value >>> 8) + getByte(value & 0x00FF);
    }

    /**
     * Turn a word into its equivalent hex digits
     *
     * @param value Generate a character from a value
     * @return A hex value in the range 00000000..FFFFFFFF
     */
    public static String getWord(final int value) {
        return getShort(value >>> 16) + getShort(value & 0x0000FFFF);
    }

    /**
     * Convert a hex character into its equivalent value
     *
     * @param hex Character 0..F
     * @return Value in the range 0 ..15
     */
    public static int getHexDigit(final char hex) {
        int i;
        for (i = 0; i < hexChar.length(); i++) {
            if (hexChar.charAt(i) == hex) {
                return i;
            }
        }
        return -1;
    }

}
