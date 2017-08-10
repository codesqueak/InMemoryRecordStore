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

import com.codingrodent.InMemoryRecordStore.core.IMemoryStore;
import com.codingrodent.InMemoryRecordStore.exception.RecordStoreException;
import com.codingrodent.InMemoryRecordStore.utility.BitTwiddling;

import java.lang.reflect.*;
import java.util.UUID;

/**
 *
 */
public class Writer<T> {
    private final RecordDescriptor<T> recordDescriptor;
    private final IMemoryStore memoryStore;
    private final BitWriter bitWriter;

    /**
     * Create a new record writer
     *
     * @param memoryStore      Data storage structure
     * @param recordDescriptor Field type information
     */
    public Writer(final IMemoryStore memoryStore, final RecordDescriptor<T> recordDescriptor) {
        if (recordDescriptor.isFieldByteAligned()) {
            this.bitWriter = null;
        } else {
            this.bitWriter = new BitWriter();
        }
        this.recordDescriptor = recordDescriptor;
        this.memoryStore = memoryStore;
    }

    /**
     * Write a record at the specified location
     *
     * @param loc    Location
     * @param record Record
     * @throws RecordStoreException General error when writing record
     */
    public void putRecord(final int loc, final T record) throws RecordStoreException {
        if (!record.getClass().equals(recordDescriptor.getClazz())) {
            throw new RecordStoreException("Object supplied to writer is of the wrong type");
        }
        // Write buffer to storage
        int byteLength = recordDescriptor.getByteLength();
        int writeLocation = loc * byteLength;
        if ((memoryStore.getBytes() - writeLocation) < byteLength) {
            throw new RecordStoreException("Write location beyond end of storage");
        }
        // Find all fields and build byte buffer
        Class clazz = record.getClass();
        int bufferPosition = 0;
        byte[] buffer = new byte[byteLength];
        for (String fieldName : recordDescriptor.getFieldNames()) {
            try {
                RecordDescriptor.FieldDetails fieldDetails = recordDescriptor.getFieldDetails(fieldName);
                IMemoryStore.Type type = fieldDetails.getType();
                Field field = clazz.getDeclaredField(fieldName);
                Object value = field.get(record);
                if ((null == value) && !(type == IMemoryStore.Type.Void))
                    throw new IllegalArgumentException("Field (" + field.getName() + ") is null. Unable to pack");
                // If the field is an array, check its size
                if (field.getType().isArray() && (Array.getLength(value) != fieldDetails.getElements())) {
                    throw new IllegalArgumentException("Array size does not match. Should be " + fieldDetails.getElements());
                }
                //  Alignment ?
                if (recordDescriptor.isFieldByteAligned()) {
                    // Byte aligned
                    bufferPosition = packFieldIntoBytes(bufferPosition, buffer, value, fieldDetails.getByteLength(), type, fieldDetails.getElements());
                } else {
                    // Bit aligned
                    bufferPosition = packFieldIntoBits(bufferPosition, buffer, value, fieldDetails.getBitLength(), type, fieldDetails.getElements());
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RecordStoreException(e);
            }
        }
        // Write buffer into storage
        memoryStore.setByteArray(writeLocation, buffer);
    }

    /**
     * Pack an annotated field into the byte storage for a record
     *
     * @param pos               Write position (byte)
     * @param buffer            Byte buffer
     * @param value             Object to be written. Actual type from record.
     * @param byteLength        Length of target field in bytes
     * @param type              Object type
     * @param maxVariableLength Maximuj length for varibale length objects, e.g. Strings
     * @return Next free byte in the buffer
     */
    private int packFieldIntoBytes(int pos, final byte[] buffer, final Object value, final int byteLength, final IMemoryStore.Type type, final int maxVariableLength) {
        //
        // Don't forget - you can't make things longer !
        switch (type) {
            case Bit: {
                buffer[pos++] = (byte) ((Boolean) value ? 0x01 : 0x00);
                break;
            }
            case Byte8: {
                buffer[pos++] = (Byte) value;
                break;
            }
            case Short16: {
                short v = (Short) value;
                if (1 == byteLength) {
                    v = BitTwiddling.shrink(v, 8);
                    buffer[pos++] = (byte) (v & 0x00FF);
                } else {
                    buffer[pos++] = (byte) (v >>> 8);
                    buffer[pos++] = (byte) (v & 0x00FF);
                    pos++;
                }
                break;
            }
            case Char16: {
                char v = (Character) value;
                pos = packByteAlignedChar(pos, buffer, byteLength, v);
                break;
            }
            case Word32: {
                int v = (Integer) value;
                if (byteLength < 4)
                    v = BitTwiddling.shrink(v, 8 * byteLength);
                for (int i = byteLength - 1; i >= 0; i--) {
                    buffer[pos + i] = (byte) (v & 0x00FF);
                    v = v >>> 8;
                }
                pos = pos + byteLength;
                break;
            }
            case Word64: {
                long v = (Long) value;
                if (byteLength < 8)
                    v = BitTwiddling.shrink(v, 8 * byteLength);
                for (int i = byteLength - 1; i >= 0; i--) {
                    buffer[pos + i] = (byte) (v & 0x00FF);
                    v = v >>> 8;
                }
                pos = pos + byteLength;
                break;
            }
            case Void: {
                pos = pos + byteLength;
                break;
            }
            case UUID: {
                UUID v = (UUID) value;
                pos = longToBuffer(pos, buffer, v.getMostSignificantBits());
                pos = longToBuffer(pos, buffer, v.getLeastSignificantBits());
                break;
            }
            case booleanArray: {
                boolean[] v = (boolean[]) value;
                for (boolean b : v)
                    buffer[pos++] = (byte) (b ? 1 : 0);
                break;
            }
            case BooleanArray: {
                Boolean[] v = (Boolean[]) value;
                for (boolean b : v)
                    buffer[pos++] = (byte) (b ? 1 : 0);
                break;
            }
            case FixedString: {
                String v = (String) value;
                // Insert length header value
                int length = v.length();
                pos = intToBuffer(pos, buffer, length);
                // Add characters
                for (int i = 0; i < v.length(); i++) {
                    char c = v.charAt(i);
                    pos = packByteAlignedChar(pos, buffer, byteLength, c);
                }
                // Zero out any unused space
                for (int i = v.length(); i < maxVariableLength; i++) {
                    pos = packByteAlignedChar(pos, buffer, byteLength, (char) 0);
                }
                break;
            }
            case Double: {
                long p = Double.doubleToRawLongBits((Double) value);
                pos = longToBuffer(pos, buffer, p);
                break;
            }
            case Float: {
                int p = Float.floatToRawIntBits((Float) value);
                pos = intToBuffer(pos, buffer, p);
                break;
            }
        }
        return pos;
    }

    /**
     * Pack an annotated field into the bit storage for a record
     *
     * @param pos               Write position (bit)
     * @param buffer            Byte buffer
     * @param value             Object to be written. Actual type from record.
     * @param bitLength         Length of target field in bits
     * @param type              Object type
     * @param maxVariableLength Maximuj length for varibale length objects, e.g. Strings
     * @return Next free bit in the buffer
     */

    private int packFieldIntoBits(int pos, final byte[] buffer, final Object value, final int bitLength, final IMemoryStore.Type type, final int maxVariableLength) {
        //
        // Don't forget - you can't make things longer !
        switch (type) {
            case Bit: {
                byte[] booleanValue = {(byte) ((Boolean) value ? 0x01 : 0x00)};
                bitWriter.insertBits(booleanValue, buffer, pos, bitLength);
                break;
            }
            case Byte8: {
                byte[] byteValue = {(Byte) value};
                byteValue[0] = (byte) value;
                bitWriter.insertBits(byteValue, buffer, pos, bitLength);
                break;
            }
            case Short16: {
                short shrunkShort;
                if (bitLength < 16)
                    shrunkShort = BitTwiddling.shrink((Short) value, bitLength);
                else
                    shrunkShort = (Short) value;
                byte[] shortValue;
                if (bitLength <= 8) {
                    shortValue = new byte[1];
                    shortValue[0] = (byte) (shrunkShort & 0x00FF);
                } else {
                    shortValue = new byte[2];
                    shortValue[0] = (byte) ((shrunkShort & 0xFF00) >>> 8);
                    shortValue[1] = (byte) (shrunkShort & 0x00FF);
                }
                bitWriter.insertBits(shortValue, buffer, pos, bitLength);
                break;
            }
            case Char16: {
                char c = (Character) value;
                packBitAlignedChar(pos, buffer, bitLength, c);
                break;
            }
            case Word32: {
                int shrunkInt;
                if (bitLength < 32)
                    shrunkInt = BitTwiddling.shrink((Integer) value, bitLength);
                else
                    shrunkInt = (Integer) value;
                int size = ((bitLength - 1) >> 3) + 1;
                byte[] integerValue = new byte[size];
                for (int i = 1; i <= size; i++) {
                    integerValue[size - i] = (byte) (shrunkInt & 0x00FF);
                    shrunkInt = shrunkInt >>> 8;
                }
                bitWriter.insertBits(integerValue, buffer, pos, bitLength);
                break;
            }
            case Word64: {
                long shrunkLong;
                if (bitLength < 64)
                    shrunkLong = BitTwiddling.shrink((Long) value, bitLength);
                else
                    shrunkLong = (Long) value;
                int size = ((bitLength - 1) >> 3) + 1;
                byte[] longValue = new byte[size];
                for (int i = 1; i <= size; i++) {
                    longValue[size - i] = (byte) (shrunkLong & 0x00FF);
                    shrunkLong = shrunkLong >>> 8;
                }
                bitWriter.insertBits(longValue, buffer, pos, bitLength);
                break;
            }
            case Void: {
                break;
            }
            case UUID: {
                UUID v = (UUID) value;
                byte[] longValue = new byte[8];
                longToBuffer(0, longValue, v.getMostSignificantBits());
                bitWriter.insertBits(longValue, buffer, pos, 64);
                longToBuffer(0, longValue, v.getLeastSignificantBits());
                bitWriter.insertBits(longValue, buffer, pos + 64, 64);
                break;
            }
            case booleanArray: {
                boolean[] v = (boolean[]) value;
                byte[] zero = {0};
                byte[] one = {1};
                for (boolean b : v) {
                    bitWriter.insertBits(b ? one : zero, buffer, pos, bitLength);
                    pos = pos + bitLength;
                }
                pos = pos - bitLength;
                break;
            }
            case BooleanArray: {
                Boolean[] v = (Boolean[]) value;
                byte[] zero = {0};
                byte[] one = {1};
                for (boolean b : v) {
                    bitWriter.insertBits(b ? one : zero, buffer, pos, bitLength);
                    pos = pos + bitLength;
                }
                pos = pos - bitLength;
                break;
            }
            case FixedString: {
                String v = (String) value;
                int length = v.length();
                // Insert length header value
                byte[] integerValue = new byte[4];
                for (int i = 1; i <= 4; i++) {
                    integerValue[4 - i] = (byte) (length & 0x00FF);
                    length = length >>> 8;
                }
                bitWriter.insertBits(integerValue, buffer, pos, 32);
                pos = pos + 32;
                // Add characters
                for (int i = 0; i < v.length(); i++) {
                    char c = v.charAt(i);
                    packBitAlignedChar(pos, buffer, bitLength, c);
                    pos = pos + bitLength;
                }
                // Zero out any unused space
                for (int i = v.length(); i < maxVariableLength; i++) {
                    packBitAlignedChar(pos, buffer, bitLength, (char) 0);
                    pos = pos + bitLength;
                }
                pos = pos - bitLength;
                break;
            }
            case Double: {
                byte[] longValue = new byte[8];
                longToBuffer(0, longValue, Double.doubleToRawLongBits((Double) value));
                bitWriter.insertBits(longValue, buffer, pos, 64);
                break;
            }
            case Float: {
                byte[] integerValue = new byte[4];
                intToBuffer(0, integerValue, Float.floatToRawIntBits((Float) value));
                bitWriter.insertBits(integerValue, buffer, pos, 32);
                break;
            }
        }
        return pos + bitLength;
    }

    /**
     * Pack away a character (bit aligned)
     *
     * @param pos       Write position (byte)
     * @param buffer    Byte buffer
     * @param bitLength Length of target field in bits
     * @param c         Character to pack
     */
    private void packBitAlignedChar(final int pos, final byte[] buffer, final int bitLength, final char c) {
        byte[] charValue;
        if (bitLength <= 8) {
            charValue = new byte[1];
            charValue[0] = (byte) (c & 0x00FF);
        } else {
            charValue = new byte[2];
            charValue[0] = (byte) ((c & 0xFF00) >>> 8);
            charValue[1] = (byte) (c & 0x00FF);
        }
        bitWriter.insertBits(charValue, buffer, pos, bitLength);
    }

    /**
     * Write an int into the record buffer
     *
     * @param pos    Start position in buffer
     * @param buffer Buffer
     * @param p      Value to write
     * @return End position in buffer
     */
    private int intToBuffer(final int pos, final byte[] buffer, int p) {
        for (int i = 3; i >= 0; i--) {
            buffer[pos + i] = (byte) (p & 0x00FF);
            p = p >>> 8;
        }
        return pos + 4;
    }

    /**
     * Write a long into the record buffer
     *
     * @param pos    Start position in buffer
     * @param buffer Buffer
     * @param p      Value to write
     * @return End position in buffer
     */
    private int longToBuffer(final int pos, final byte[] buffer, long p) {
        for (int i = 7; i >= 0; i--) {
            buffer[pos + i] = (byte) (p & 0x00FF);
            p = p >>> 8;
        }
        return pos + 8;
    }

    /**
     * Pack away a character (byte aligned)
     *
     * @param pos        Write position (byte)
     * @param buffer     Byte buffer
     * @param byteLength Length of target field in bytes
     * @param c          Character to pack
     * @return Updated position in byte buffer
     */
    private int packByteAlignedChar(int pos, final byte[] buffer, final int byteLength, final char c) {
        if (1 == byteLength) {
            buffer[pos++] = (byte) (c & 0x00FF);
        } else {
            buffer[pos++] = (byte) (c >>> 8);
            buffer[pos++] = (byte) (c & 0x00FF);
        }
        return pos;
    }

}
