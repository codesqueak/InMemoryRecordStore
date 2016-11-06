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
import com.codingrodent.InMemoryRecordStore.core.IMemoryStore.AlignmentMode;
import com.codingrodent.InMemoryRecordStore.exception.RecordStoreException;
import com.codingrodent.InMemoryRecordStore.utility.BitTwiddling;

import java.lang.reflect.Field;

public class Reader {
    private final RecordDescriptor recordDescriptor;
    private final IMemoryStore memoryStore;

    /**
     * Create a new record reader
     *
     * @param memoryStore      Data storage structure
     * @param recordDescriptor Field type information
     * @param mode             Alignment mode (bit/byte)
     */
    public Reader(final IMemoryStore memoryStore, final RecordDescriptor recordDescriptor, final AlignmentMode mode) {
        this.recordDescriptor = recordDescriptor;
        this.memoryStore = memoryStore;
    }

    /**
     * Read a record at the specified location
     *
     * @param location Location of stored object in memory
     * @return Record object with fields populated
     * @throws RecordStoreException General error when reading record
     */
    public Object getRecord(final int location) throws RecordStoreException {
        final int byteLength = recordDescriptor.getByteLength();
        int pos = 0;
        int address = location * byteLength;
        byte[] buffer = memoryStore.getByteArray(address, byteLength);
        Class clazz = recordDescriptor.getClazz();
        //
        // Populate each field
        try {
            Object target = clazz.newInstance();
            for (String fieldName : recordDescriptor.getFieldNames()) {
                RecordDescriptor.FieldDetails fieldDetails = recordDescriptor.getFieldDetails(fieldName);
                pos = unpackFieldIntoObject(target, target.getClass().getDeclaredField(fieldName), pos, buffer, fieldDetails);
            }
            return target;
        } catch (IllegalAccessException | InstantiationException | NoSuchFieldException e) {
            throw new RecordStoreException("Unable to populate record", e);
        }
    }

    /**
     * Unpack a field in a byte buffer back into a source object
     *
     * @param target       Object being constructed
     * @param field        Field to be written to
     * @param pos          Position in byte buffer
     * @param buffer       Byte buffer
     * @param fieldDetails Description of the field
     * @return Next position in byte buffer
     * @throws IllegalAccessException If introspection write fails
     */
    private int unpackFieldIntoObject(Object target, final Field field, int pos, final byte[] buffer, final RecordDescriptor.FieldDetails fieldDetails) throws IllegalAccessException {
        IMemoryStore.Type type = fieldDetails.getType();
        int byteLength = fieldDetails.getByteLength();
        switch (type) {
            case Bit: {
                field.set(target, 0x01 == getUnsignedByte(buffer, pos++));
                break;
            }
            case Byte8: {
                field.set(target, buffer[pos++]);
                break;
            }
            case Char16: {
                char c;
                if (1 == byteLength) {
                    c = (char) (buffer[pos++] & 0x00FF);
                } else {
                    c = (char) ((getUnsignedByte(buffer, pos++) << 8) | getUnsignedByte(buffer, pos++));
                }
                field.set(target, c);
                break;
            }
            case Short16: {
                if (1 == byteLength) {
                    field.set(target, (short) buffer[pos++]);
                } else {
                    field.set(target, (short) ((getUnsignedByte(buffer, pos++) << 8) | getUnsignedByte(buffer, pos++)));
                }
                break;
            }
            case Word32: {
                int v = 0;
                for (int i = 0; i < byteLength; i++) {
                    v = (v << 8) | getUnsignedByte(buffer, pos++);
                }
                if (byteLength < 4) {
                    v = BitTwiddling.extend(v, 8 * byteLength);
                }
                field.set(target, v);
                break;
            }
            case Word64: {
                long v = 0;
                for (int i = 0; i < byteLength; i++) {
                    v = (v << 8) | getUnsignedByte(buffer, pos++);
                }
                if (byteLength < 8) {
                    v = BitTwiddling.extend(v, 8 * byteLength);
                }
                field.set(target, v);
                break;
            }
            case Void: {
                pos = pos + byteLength;
                break;
            }
        }
        return pos;
    }

    /**
     * Expand a byte from the buffer without sign extending it
     *
     * @param buffer Byte buffer
     * @param pos    Position in buffer
     * @return Unsigned byte value into integer
     */
    private int getUnsignedByte(byte[] buffer, int pos) {
        return buffer[pos] & 0x00FF;
    }
}
