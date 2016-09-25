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
package net.codingrodent.InMemoryRecordStore.core;

import net.codingrodent.InMemoryRecordStore.record.*;

/**
 *
 */
public class RecordManager {
    private int sizeInBits = 0;
    private int sizeInBytes = 0;
    private int sizeInWords = 0;
    private RecordDescriptor recordDescriptor;
    private Reader reader;
    private Writer writer;

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
        if (recordDescriptor.isFieldByteAligned()) {
            sizeInBytes = recordDescriptor.getSizeInBytes() * size;
        } else {
            if (recordDescriptor.isRecordByteAligned()) {
                sizeInBytes = recordDescriptor.getSizeInBytes() * size;
            } else {
                sizeInBytes = (((recordDescriptor.getSizeInBits() * size) - 1) >> 3) + 1;
            }
        }
        sizeInWords = ((sizeInBytes - 1) >> 2) + 1;
        memoryStore.build(sizeInWords);
        this.reader = new Reader(memoryStore, recordDescriptor);
        this.writer = new Writer(memoryStore, recordDescriptor);
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
