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
import com.codingrodent.InMemoryRecordStore.util.BitTwiddling;
import com.codingrodent.InMemoryRecordStore.utility.Utilities;

/**
 *
 */
public class Writer {

    private final static byte ZERO = (byte) 0;
    private final static byte ONE = (byte) 1;

    private RecordDescriptor recordDescriptor;
    private IMemoryStore memoryStore;
    private IMemoryStore.AlignmentMode mode;

    /**
     * Create a new record writer
     *
     * @param memoryStore      Data storage structure
     * @param recordDescriptor Field type information
     * @param mode             Alignment mode
     */
    public Writer(final IMemoryStore memoryStore, final RecordDescriptor recordDescriptor, final IMemoryStore.AlignmentMode mode) {
        this.recordDescriptor = recordDescriptor;
        this.memoryStore = memoryStore;
        this.mode = mode;
    }

    /**
     * Write a record at the specified location
     *
     * @param loc    Location
     * @param record Record
     */
    public void putRecord(final int loc, final Object record) {
        if (!record.getClass().equals(recordDescriptor.getClazz())) {
            throw new IllegalArgumentException("Object supplied to writer is of the wrong type");
        }
        Class<?> c = record.getClass();
        int pos = 0;
        byte[] data = new byte[recordDescriptor.getSizeInBytes()];
        for (String fieldName : recordDescriptor.getFieldNames()) {
            try {
                System.out.println(c.getDeclaredField(fieldName).get(record));
                //
                RecordDescriptor.FieldDetails fieldDetails = recordDescriptor.getFieldDetails(fieldName);
                pos = packBytes(pos, data, c.getDeclaredField(fieldName).get(record), fieldDetails.getByteLength(), fieldDetails.getType());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (byte b : data) {
            System.out.print(Utilities.getByte(b) + " ");
        }
        System.out.println();
    }

    //
    //
    //

    private int packBytes(int pos, byte[] data, Object value, int length, IMemoryStore.Type type) {

        //
        // Don't forget - you can't make things longer !
        switch (type) {
            case Bit:
                data[pos++] = (Boolean) value ? ONE : ZERO;
                break;
            case Word64: {
                long v = (Long) value;
                if (length < 8)
                    v = BitTwiddling.shrink(v, 8 * length);
                for (int i = length - 1; i >= 0; i--) {
                    data[pos + i] = (byte) (v & 0xFF);
                    v = v >>> 8;
                }
                pos = pos + length;
            }
            break;
            case Byte8:
                data[pos++] = (Byte) value;
                break;
            case Short16: {
                short v = (short) value;
                if (1 == length)
                    v = BitTwiddling.shrink(v, 8);
                data[pos + 1] = (byte) (v & 0xFF);
                data[pos++] = (byte) (v >>> 8);
                pos++;
                break;
            }
            case Word32: {
                int v = (int) value;
                if (length < 4)
                    v = BitTwiddling.shrink(v, 8 * length);
                for (int i = length - 1; i >= 0; i--) {
                    data[pos + i] = (byte) (v & 0xFF);
                    v = v >>> 8;
                }
                pos = pos + length;
                break;
            }
            case Char16: {
                char v = (char) value;
                data[pos + 1] = (byte) (v & 0xFF);
                data[pos++] = (byte) (v >>> 8);
                pos++;
                break;
            }
            case Void:
                pos = pos + length;
                break;
        }
        //
        return pos;

    }

}
