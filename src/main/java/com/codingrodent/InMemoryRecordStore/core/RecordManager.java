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

import com.codingrodent.InMemoryRecordStore.core.IMemoryStore.AlignmentMode;
import com.codingrodent.InMemoryRecordStore.record.*;

/**
 *
 */
public class RecordManager {

    private final AlignmentMode mode;
    private final int sizeInBits;
    private final int sizeInBytes;
    private final int sizeInWords;
    private RecordDescriptor recordDescriptor;
    private final Reader reader;
    private final Writer writer;

    /**
     * Create a new In Memory component descriptor
     *
     * @param memoryStore      Data storage structure
     * @param size             The size of the memory core in records
     * @param recordDescriptor Field type information
     */
    public RecordManager(final IMemoryStore memoryStore, final int size, final RecordDescriptor recordDescriptor) {
        if (size < 1) {
            throw new IllegalArgumentException("The component must have at least one record");
        }
        this.recordDescriptor = recordDescriptor;
        // Determine size based on the four possible alignment strategies
        int sizeInBits;
        int sizeInBytes;
        if (recordDescriptor.isFieldByteAligned()) {
            if (recordDescriptor.isRecordByteAligned()) {
                sizeInBytes = recordDescriptor.getSizeInBytes() * size;
                sizeInBits = sizeInBytes * 8;
                mode = AlignmentMode.BYTE_BYTE;
            } else {
                sizeInBytes = recordDescriptor.getSizeInBytes() * size;
                mode = AlignmentMode.BYTE_BIT;
                sizeInBits = sizeInBytes * 8;
            }
        } else {
            if (recordDescriptor.isRecordByteAligned()) {
                sizeInBytes = recordDescriptor.getSizeInBytes() * size;
                sizeInBits = sizeInBytes * 8;
                mode = AlignmentMode.BIT_BYTE;
            } else {
                sizeInBits = recordDescriptor.getSizeInBits() * size;
                sizeInBytes = (sizeInBits + 7) >> 3;
                mode = AlignmentMode.BIT_BIT;
            }
        }
        if (AlignmentMode.BYTE_BYTE != mode) {
            throw new UnsupportedOperationException("Alignment mode selected not supported at present (" + mode + ")");
        }
        //
        this.sizeInBytes = sizeInBytes;
        this.sizeInBits = sizeInBits;
        this.sizeInWords = ((sizeInBytes - 1) >> 2) + 1;
        this.reader = new Reader(memoryStore, recordDescriptor, mode);
        this.writer = new Writer(memoryStore, recordDescriptor, mode);
        memoryStore.build(sizeInWords);
    }

    /**
     * Read a record at the specified location
     *
     * @param loc Location
     * @return Record
     */
    public Object getRecord(final int loc) {
        Object record = reader.getRecord(loc);
        return recordDescriptor.getClazz().cast(record);
    }

    /**
     * Write a record at the specified location
     *
     * @param loc    Location
     * @param record Record
     */
    public void putRecord(final int loc, final Object record) {
        writer.putRecord(loc, record);
    }

    // ******************************************************************************
    // ******************************************************************************
    // ******************************************************************************

    public int getSizeInBits() {
        return sizeInBits;
    }

    public int getSizeInBytes() {
        return sizeInBytes;
    }

    public int getSizeInWords() {
        return sizeInWords;
    }

    // ******************************************************************************
    // ******************************************************************************
    // ******************************************************************************

}
