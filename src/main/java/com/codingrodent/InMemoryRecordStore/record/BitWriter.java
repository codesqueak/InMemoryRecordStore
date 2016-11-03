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

import java.util.Arrays;

/**
 * This class contains functionality to  convert a byte packed record into its bit packed equivalent
 */
public class BitWriter {

    private final RecordDescriptor recordDescriptor;

    /**
     * Create a new bit  writer
     *
     * @param recordDescriptor Field type information
     */
    public BitWriter(final RecordDescriptor recordDescriptor) {
        this.recordDescriptor = recordDescriptor;
    }

    /**
     * Convert byte packed to bit packed record
     *
     * @param byteRecord Byte packed record
     * @return Bit packed record
     */
    public byte[] bitPack(final byte[] byteRecord) {
        int packedByteLength = ((recordDescriptor.getBitLength() - 1) >> 3) + 1;
        byte[] targetArray = new byte[packedByteLength];
        int writeBitPosition = 0;
        int sourceBytePosition = 0;
        for (String fieldName : recordDescriptor.getFieldNames()) {
            RecordDescriptor.FieldDetails fieldDetails = recordDescriptor.getFieldDetails(fieldName);
            int bitLength = fieldDetails.getBitLength();
            int byteLength = fieldDetails.getByteLength();
            byte[] field = Arrays.copyOfRange(byteRecord, sourceBytePosition, sourceBytePosition + byteLength);
            targetArray = insertBits(field, targetArray, writeBitPosition, bitLength);
            writeBitPosition = writeBitPosition + bitLength;
            sourceBytePosition = sourceBytePosition + byteLength;
        }
        // all done ...
        return byteRecord;
    }

    /**
     * Pack one byte aligned field into the bit aligned field.  Byte fields are right aligned, bit fields left
     *
     * @param sourceArray      The record array of byte fields data
     * @param targetArray      The target bit field array
     * @param writeBitPosition Where to write to in the target array
     * @param bitLength        Size of field in bits
     * @return Target bit array with field writtent to it
     */
    private byte[] insertBits(byte[] sourceArray, byte[] targetArray, int writeBitPosition, int bitLength) {
        final int byteArrayOffset = sourceArray.length - 1;
        for (int bit = bitLength - 1; bit >= 0; bit--) {
            int readBit = bit & 0x07;
            int readByte = byteArrayOffset - (bit >> 3);
            int writeBit = 7 - (writeBitPosition & 0x07);
            int writeByte = writeBitPosition++ >> 3;
            System.out.println("From " + readBit + " " + readByte + " to  " + writeBit + " " + writeByte);
        }
        System.out.println("-------------");
        return targetArray;
    }

}
