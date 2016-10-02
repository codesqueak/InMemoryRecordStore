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
package com.codingrodent.InMemoryRecordStore.core;

import com.codingrodent.InMemoryRecordStore.utility.Utilities;

/**
 * This class simulates a block of RAM via the use of an array of int's. Facilities are supplied to
 * read and write the 'RAM' via methods using all supported types
 * <p>
 * Important: All addresses supplied are byte based and not word based as per the underlying
 * storage. This can lead to inefficiencies in non aligned data. For maximum performance it is
 * recommended that the aligned word read / write methods are used where appropriate
 */
public class ArrayMemoryStore implements IMemoryStore {
    private int[] core = null;
    private int bytes = 0;
    private int words = 0;

    /**
     * Create an empty store structure.
     */
    public ArrayMemoryStore() {
    }

    /**
     * Create a new memory core for storage.
     *
     * @param words Size of the memory block requested. The range is 1 to 2**28 32 bit words (1GB)
     * @throws IllegalArgumentException Thrown if the requested storage size if out of range
     */
    public ArrayMemoryStore(final int words) {
        build(words);
    }

    /**
     * Build the storage
     *
     * @param words Size in 32 bit words
     */
    @Override
    public void build(int words) {
        if ((words < 1) || (words > MAX_WORDS)) {
            throw new IllegalArgumentException("The number of words requested was out of range");
        }
        bytes = words * BYTES_PER_WORD;
        this.words = words;
        core = new int[words];
    }

    /**
     * Clear down all memory cells to zero
     */
    @Override
    public void reset() {
        final int length = core.length;
        for (int i = 0; i < length; i++) {
            core[i] = 0;
        }
    }

    /**
     * Get the number of words of memory allocated
     *
     * @return Memory size allocated in 32 bit words
     */
    @Override
    public int getWords() {
        return words;
    }

    /**
     * Get the number of bytes of memory allocated
     *
     * @return Memory size allocated in 8 bit bytes
     */
    @Override
    public int getBytes() {
        return bytes;
    }

    /**
     * Return a word of memory from any address. This operation is potentially slower than a word
     * aligned fetch
     *
     * @param address Byte address to be fetched from (Will wrap if to large)
     * @return The word of memory requested
     */
    @Override
    public int getWord(final int address) {
        int validAddress = validateAndWrapAddress(address);
        switch (address & 0x03) {
            case 0x00: {
                return core[validAddress];
            }
            case 0x01: {
                int part1 = core[validAddress];
                validAddress = ((++validAddress) % words);
                int part2 = core[validAddress];
                return (part1 << 8) | (part2 >>> 24);
            }
            case 0x02: {
                int part1 = core[validAddress];
                validAddress = ((++validAddress) % words);
                int part2 = core[validAddress];
                return (part1 << 16) | (part2 >>> 16);
            }
            default: {
                int part1 = core[validAddress];
                validAddress = ((++validAddress) % words);
                int part2 = core[validAddress];
                return (part1 << 24) | (part2 >>> 8);
            }
        }
    }

    /**
     * Return a long word of memory from any address. This operation is potentially slower than a
     * long word aligned fetch. Note that long word alignment is 32 bit based, not 64 bit.
     *
     * @param address Address to be fetched from (Will wrap if too large)
     * @return The long word of memory requested
     */
    @Override
    public long getLongWord(final int address) {
        long v1 = ((long) getWord(address)) << 32;
        long v2 = (((long) getWord(address + 4)) & LSLW);
        return v1 | v2;
    }

    /**
     * Return a 24 bit word of memory from any address. This operation is always treated as
     * unaligned
     *
     * @param address Byte address to be fetched from (Will wrap if too large)
     * @return The word of memory requested
     */
    @Override
    public int getWord24(final int address) {
        int validAddress = validateAndWrapAddress(address);
        switch (address & 0x03) {
            case 0x00: {
                return (core[validAddress] & 0xFFFFFF00) >>> 8;
            }
            case 0x01: {
                return (core[validAddress] & 0x00FFFFFF);
            }
            case 0x02: {
                int part1 = core[validAddress];
                validAddress = ((++validAddress) % words);
                int part2 = core[validAddress];
                return ((part1 & 0x0000FFFF) << 8) | (part2 >>> 24);
            }
            default: {
                int part1 = core[validAddress];
                validAddress = ((++validAddress) % words);
                int part2 = core[validAddress];
                return ((part1 & 0x000000FF) << 16) | (part2 >>> 16);
            }
        }
    }

    /**
     * Return a short word of memory from any address. This operation is always treated as unaligned
     *
     * @param address Byte address to be fetched from (Will wrap if too large)
     * @return The short word of memory requested
     */
    @Override
    public short getShortWord(final int address) {
        int validAddress = validateAndWrapAddress(address);
        switch (address & 0x03) {
            case 0x00: {
                return (short) ((core[validAddress] & 0xFFFF0000) >> 16);
            }
            case 0x01: {
                return (short) ((core[validAddress] & 0x00FFFF00) >> 8);
            }
            case 0x02: {
                return (short) (core[validAddress] & 0x0000FFFF);
            }
            default: {
                int part1 = core[validAddress];
                validAddress = ((++validAddress) % words);
                int part2 = core[validAddress];
                return (short) (((part1 & 0x000000FF) << 8) | ((part2 & 0xFF000000) >>> 24));
            }
        }
    }

    /**
     * Return a byte of memory from any address. This operation is always treated as unaligned
     *
     * @param address The byte address to be fetched from (Will wrap if too large)
     * @return The byte of memory requested
     */
    @Override
    public byte getByte(final int address) {
        final int validAddress = validateAndWrapAddress(address);
        switch (address & 0x03) {
            case 0x00: {
                return (byte) ((core[validAddress] & 0xFF000000) >>> 24);
            }
            case 0x01: {
                return (byte) ((core[validAddress] & 0x00FF0000) >> 16);
            }
            case 0x02: {
                return (byte) ((core[validAddress] & 0x0000FF00) >> 8);
            }
            default: {
                return (byte) (core[validAddress] & 0x000000FF);
            }
        }
    }

    /**
     * Write a word of memory to a any address. his operation is always treated as unaligned
     *
     * @param address Address to be written to (Will wrap if too large)
     * @param value   Word to be written to memory
     */
    @Override
    public void setWord(final int address, final int value) {
        int validAddress1 = validateAndWrapAddress(address);
        int word1 = core[validAddress1];
        int validAddress2 = (validAddress1 + 1) % words;
        int word2 = core[validAddress2];
        //
        switch (address & 0x03) {
            case 0x00: {
                word1 = value;
                break;
            }
            case 0x01: {
                final int part1 = value >>> 8;
                word1 = (word1 & 0xFF000000) | part1;
                final int part2 = (value & 0x000000FF) << 24;
                word2 = (word2 & 0x00FFFFFF) | part2;
                break;
            }
            case 0x02: {
                final int part1 = value >>> 16;
                word1 = (word1 & 0xFFFF0000) | part1;
                final int part2 = (value & 0x0000FFFF) << 16;
                word2 = (word2 & 0x0000FFFF) | part2;
                break;
            }
            default: {
                final int part1 = value >>> 24;
                word1 = (word1 & 0xFFFFFF00) | part1;
                final int part2 = (value & 0x00FFFFFF) << 8;
                word2 = (word2 & 0x000000FF) | part2;
            }
        }
        core[validAddress1] = word1;
        core[validAddress2] = word2;
    }

    /**
     * Write a long word of memory to a any address. his operation is always treated as unaligned
     *
     * @param address Address to be written to (Will wrap if too large)
     * @param value   Long word to be written to memory
     */
    @Override
    public void setLongWord(final int address, final long value) {
        setWord(address, (int) (value >>> 32));
        setWord(address + 4, (int) (value & LSLW));

    }

    /**
     * Write a 24 bit word of memory to a any address. his operation is always treated as unaligned.
     * Due to Java not having a 24 bit type, the 32 bit type is used instead with the bits 24..31
     * masked out.
     *
     * @param address Address to be written to (Will wrap if too large)
     * @param value   Word to be written to memory
     */
    @Override
    public void setWord24(final int address, int value) {
        value = value & 0x00FFFFFF;
        int validAddress1 = validateAndWrapAddress(address);
        int word1 = core[validAddress1];
        int validAddress2 = (validAddress1 + 1) % words;
        int word2 = core[validAddress2];
        //
        switch (address & 0x03) {
            case 0x00: {
                word1 = (word1 & 0x000000FF) | (value << 8);
                break;
            }
            case 0x01: {
                word1 = (word1 & 0xFF000000) | value;
                break;
            }
            case 0x02: {
                final int part1 = value >>> 8;
                word1 = (word1 & 0xFFFF0000) | part1;
                final int part2 = (value & 0x000000FF) << 24;
                word2 = (word2 & 0x00FFFFFF) | part2;
                break;
            }
            default: {
                final int part1 = value >>> 16;
                word1 = (word1 & 0xFFFFFF00) | part1;
                final int part2 = (value & 0x0000FFFF) << 16;
                word2 = (word2 & 0x0000FFFF) | part2;
            }
        }
        core[validAddress1] = word1;
        core[validAddress2] = word2;
    }

    /**
     * Write a short word of memory to a any address. his operation is always treated as unaligned
     *
     * @param address    Address to be written to (Will wrap if too large)
     * @param shortValue Short word to be written to memory
     */
    @Override
    public void setShort(final int address, final short shortValue) {
        int value = (shortValue) & 0x0000FFFF;
        int validAddress1 = validateAndWrapAddress(address);
        int word1 = core[validAddress1];
        int validAddress2 = (validAddress1 + 1) % words;
        int word2 = core[validAddress2];
        //
        switch (address & 0x03) {
            case 0x00: {
                word1 = (word1 & 0x0000FFFF) | (value << 16);
                break;
            }
            case 0x01: {
                word1 = (word1 & 0xFF0000FF) | (value << 8);
                break;
            }
            case 0x02: {
                word1 = (word1 & 0xFFFF0000) | value;
                break;
            }
            default: {
                word1 = (word1 & 0xFFFFFF00) | (value >>> 8);
                word2 = (word2 & 0x00FFFFFF) | ((value & 0x000000FF) << 24);
            }
        }
        core[validAddress1] = word1;
        core[validAddress2] = word2;
    }

    /**
     * Write a byte of memory to a any address. This operation is always treated as unaligned
     *
     * @param address   Address to be written to (Will wrap if too large)
     * @param byteValue Byte to be written to memory
     */
    @Override
    public void setByte(final int address, final byte byteValue) {
        int value = (byteValue) & 0x000000FF;
        int validAddress1 = validateAndWrapAddress(address);
        int word1 = core[validAddress1];
        //
        switch (address & 0x03) {
            case 0x00: {
                word1 = (word1 & 0x00FFFFFF) | (value << 24);
                break;
            }
            case 0x01: {
                word1 = (word1 & 0xFF00FFFF) | (value << 16);
                break;
            }
            case 0x02: {
                word1 = (word1 & 0xFFFF00FF) | (value << 8);
                break;
            }
            default: {
                word1 = (word1 & 0xFFFFFF00) | value;
            }
        }
        core[validAddress1] = word1;
    }

    // ******************************************************************************
    // ******************************************************************************
    // ******************************************************************************

    /**
     * Dump memory for debug purposes
     *
     * @param length Number of words of memory to dump from the core store
     */
    public void dump(int length) {
        //		if (length > core.length)
        //		{
        //			length = core.length;
        //		}
        for (int i = 0; i < length; i++) {
            if (0 == (i % 4)) {
                System.out.println();
                System.out.print(Utilities.getShort(i * 4) + "  ");

            }
            System.out.print(Utilities.getByte((core[i] & 0xFF000000) >> 24) + " ");
            System.out.print(Utilities.getByte((core[i] & 0x00FF0000) >> 16) + " ");
            System.out.print(Utilities.getByte((core[i] & 0x0000FF00) >> 8) + " ");
            System.out.print(Utilities.getByte(core[i] & 0x000000FF) + " ");
        }
        System.out.println("------------------------------------");
    }

}
