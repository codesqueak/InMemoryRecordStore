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

import com.codingrodent.InMemoryRecordStore.record.*;

public class RecordManager<T> {

    private final static long STORAGE_LIMIT = 0x7FFF_FFFC;

    private final int lengthInBytes;
    private final int lengthInWords;
    private final int records;
    private final Reader<T> reader;
    private final Writer<T> writer;

    /**
     * Create a new In Memory component descriptor
     *
     * @param memoryStore      Data storage structure
     * @param records          The records of the memory core in records
     * @param recordDescriptor Field type information
     */
    public RecordManager(final IMemoryStore memoryStore, final int records, final RecordDescriptor<T> recordDescriptor) {
        if (records < 8) {
            throw new IllegalArgumentException("The component must have at least eight records");
        }
        long lengthInBytes = ((long) recordDescriptor.getByteLength()) * records;
        if (lengthInBytes > STORAGE_LIMIT) {
            throw new IllegalArgumentException("Maximum storage limit exceeded - " + STORAGE_LIMIT + " bytes");
        }
        this.lengthInBytes = (int) lengthInBytes;
        this.lengthInWords = (int) (((lengthInBytes - 1) >> 2) + 1);
        this.records = records;
        this.reader = new Reader<>(memoryStore, recordDescriptor);
        this.writer = new Writer<>(memoryStore, recordDescriptor);
        memoryStore.build(lengthInWords);
    }

    /**
     * Read a record at the specified location
     *
     * @param location Location
     * @return Record
     */
    public T getRecord(final int location) throws IllegalArgumentException {
        if ((location < 0) || (location >= records)) {
            throw new IllegalArgumentException("Record location out of bounds");
        }
        return reader.getRecord(location);
    }

    /**
     * Write a record at the specified location
     *
     * @param location Location
     * @param record   Record
     */
    public void putRecord(final int location, final T record) throws IllegalArgumentException {
        if ((location < 0) || (location >= records)) {
            throw new IllegalArgumentException("Record location out of bounds");
        }
        writer.putRecord(location, record);
    }

    /**
     * Return the length of a record in bytes
     *
     * @return Byte length
     */

    public int getLengthInBytes() {
        return lengthInBytes;
    }

    /**
     * Return the length of a record in words. Note 1 word = 4 bytes.  A byte length of 5 for example, would equal two words.
     *
     * @return Word length
     */
    public int getLengthInWords() {
        return lengthInWords;
    }

    /**
     * Get the number of records allocated for storage
     *
     * @return Allocated storage
     */
    public int getRecords() {
        return records;
    }

}
