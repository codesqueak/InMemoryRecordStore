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

/**
 * This interface defines facilities  to read and write the 'RAM' via methods using all supported types
 * <p>
 * Maximum effective size is 2GB
 */
public interface IMemoryStore {

    /**
     * Build the storage
     *
     * @param words Length in 32 bit words
     */
    void build(int words);

    /**
     * Clear down all memory cells to zero
     */
    void reset();

    /**
     * Get the number of words of memory allocated
     *
     * @return Memory length allocated in 32 bit words
     */
    int getWords();

    /**
     * Get the number of bytes of memory allocated
     *
     * @return Memory length allocated in 8 bit bytes
     */
    int getBytes();

    /**
     * Return a word of memory from any address. This operation is potentially slower than a word
     * aligned fetch
     *
     * @param address Byte address to be fetched from (Will wrap if too large)
     * @return The word of memory requested
     */
    int getWord(final int address);

    /**
     * Return a long word of memory from any address. This operation is potentially slower than a
     * long word aligned fetch. Note that long word alignment is 32 bit based, not 64 bit.
     *
     * @param address Address to be fetched from (Will wrap if too large)
     * @return The long word of memory requested
     */
    long getLongWord(final int address);

    /**
     * Return a 24 bit word of memory from any address. This operation is always treated as
     * unaligned
     *
     * @param address Byte address to be fetched from (Will wrap if too large)
     * @return The word of memory requested
     */
    int getWord24(final int address);

    /**
     * Return a short word of memory from any address. This operation is always treated as unaligned
     *
     * @param address Byte address to be fetched from (Will wrap if too large)
     * @return The short word of memory requested
     */
    short getShortWord(final int address);

    /**
     * Return a byte of memory from any address. This operation is always treated as unaligned
     *
     * @param address The byte address to be fetched from (Will wrap if too large)
     * @return The byte of memory requested
     */
    byte getByte(final int address);

    /**
     * Read a  byte array from any address. This operation is always treated as unaligned
     *
     * @param address Address to be written to (Will wrap if too large)
     * @param length  Bytes to be read from memory
     * @return Memory array at address
     */
    byte[] getByteArray(int address, final int length);

    /**
     * Write a word of memory to a any address. his operation is always treated as unaligned
     *
     * @param address Address to be written to (Will wrap if too large)
     * @param value   Word to be written to memory
     */
    void setWord(final int address, final int value);

    /**
     * Write a long word of memory to a any address. his operation is always treated as unaligned
     *
     * @param address Address to be written to (Will wrap if too large)
     * @param value   Long word to be written to memory
     */
    void setLongWord(final int address, final long value);

    /**
     * Write a 24 bit word of memory to a any address. his operation is always treated as unaligned.
     * Due to Java not having a 24 bit type, the 32 bit type is used instead with the bits 24..31
     * masked out.
     *
     * @param address Address to be written to (Will wrap if too large)
     * @param value   Word to be written to memory
     */
    void setWord24(final int address, int value);

    /**
     * Write a short word of memory to a any address. his operation is always treated as unaligned
     *
     * @param address    Address to be written to (Will wrap if too large)
     * @param shortValue Short word to be written to memory
     */
    void setShort(final int address, final short shortValue);

    /**
     * Write a byte of memory to a any address. This operation is always treated as unaligned
     *
     * @param address   Address to be written to (Will wrap if too large)
     * @param byteValue Byte to be written to memory
     */
    void setByte(final int address, final byte byteValue);

    /**
     * Write a byte array to memory to any address. This operation is always treated as unaligned
     *
     * @param address    Address to be written to (Will wrap if too large)
     * @param byteValues Bytes to be written to memory
     */
    void setByteArray(int address, final byte[] byteValues);

    /**
     * Validate an address, and if OK wrap on the length of the memory allocated
     *
     * @param address Address to be checked
     * @return Wrapped address divided by four to give the 32 bit word to be accessed
     * @throws IllegalArgumentException Thrown if the address is illegal, i.e. Not +ve
     */
    default int validateAndWrapAddress(final int address) {
        if (address < 0) {
            throw new IllegalArgumentException("Address out of range");
        }
        return (address % getBytes()) >> 2;
    }

    enum Type {
        Bit, Byte8, Short16, Word32, Word64, Char16, Void, UUID, BooleanArray, booleanArray, FixedString, Double, Float
    }

}
