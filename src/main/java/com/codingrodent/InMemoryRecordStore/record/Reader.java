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

/**
 *
 */
public class Reader {
    private final RecordDescriptor recordDescriptor;
    private final IMemoryStore memoryStore;
    private final AlignmentMode mode;

    /**
     * Create a new record reader
     *
     * @param memoryStore      Data storage structure
     * @param recordDescriptor Field type information
     */
    public Reader(final IMemoryStore memoryStore, final RecordDescriptor recordDescriptor, final AlignmentMode mode) {
        this.recordDescriptor = recordDescriptor;
        this.memoryStore = memoryStore;
        this.mode = mode;
    }

    /**
     * Read a record at the specified location
     *
     * @param location Location
     * @return Record
     */
    public Object getRecord(final int location) {
        int address = location * recordDescriptor.getSizeInBytes();
        //
        for (String fieldName : recordDescriptor.getFieldNames()) {
            RecordDescriptor.FieldDetails fieldDetails = recordDescriptor.getFieldDetails(fieldName);
            unpackFieldIntoObject(address, fieldDetails);
            address = address + fieldDetails.getByteLength();
        }
        //
        return null;
    }

    //
    //
    //

    private Object unpackFieldIntoObject(int address, RecordDescriptor.FieldDetails fieldDetails) {
        System.out.println(address + " : " + fieldDetails.getType() + " : " + fieldDetails.getFieldName() + " : " + fieldDetails.getByteLength());
        return null;
    }
}
