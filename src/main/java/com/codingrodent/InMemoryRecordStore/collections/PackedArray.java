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
package com.codingrodent.InMemoryRecordStore.collections;

import com.codingrodent.InMemoryRecordStore.core.*;
import com.codingrodent.InMemoryRecordStore.record.RecordDescriptor;

public class PackedArray<E> {

    private final RecordManager<E> recordManager;

    /**
     * Simple constructor using default storage
     *
     * @param clazz   Class of record type
     * @param records Maximum records to store
     */
    public PackedArray(final Class<E> clazz, final int records) {
        RecordDescriptor<E> descriptor = new RecordDescriptor<>(clazz);
        this.recordManager = new RecordManager<>(new ArrayMemoryStore(), records, descriptor);
    }

    /**
     * Read a record at the specified location
     *
     * @param location Location
     * @return Record
     */
    @SuppressWarnings("unchecked")
    public E getRecord(final int location) {
        return recordManager.getRecord(location);
    }

    /**
     * Write a record at the specified location
     *
     * @param location Location
     * @param record   Record
     */
    public void putRecord(final int location, final E record) {
        recordManager.putRecord(location, record);
    }
}
