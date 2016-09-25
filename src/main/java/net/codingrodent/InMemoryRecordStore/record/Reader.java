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
package net.codingrodent.InMemoryRecordStore.record;

import net.codingrodent.InMemoryRecordStore.core.IMemoryStore;

/**
 *
 */
public class Reader {
    private RecordDescriptor recordDescriptor;
    private IMemoryStore memoryStore;

    /**
     * Create a new record reader
     *
     * @param memoryStore      Data storage structure
     * @param recordDescriptor Field type information
     */
    public Reader(final IMemoryStore memoryStore, final RecordDescriptor recordDescriptor) {
        this.recordDescriptor = recordDescriptor;
        this.memoryStore = memoryStore;
    }

    /**
     * Read a record at the specified location
     *
     * @param loc Location
     * @return Record
     */
    public Object getRecord(final int loc) {
        return null;
    }

}
