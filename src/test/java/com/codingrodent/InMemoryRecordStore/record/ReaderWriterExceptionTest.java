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

import com.codingrodent.InMemoryRecordStore.core.*;
import com.codingrodent.InMemoryRecordStore.record.records.*;
import org.junit.*;

import java.util.UUID;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

public class ReaderWriterExceptionTest {
    private IMemoryStore memory;
    private final Boolean[] booleanArray = {true, false, true, true, false};

    @Before
    public void setUp() throws Exception {
        memory = new ArrayMemoryStore(1024);
    }

    @Test
    public void writeReadExceptions() throws Exception {
        RecordDescriptor<TestRecordBytePack> descriptor = new RecordDescriptor<>(TestRecordBytePack.class);
        Writer wrongWriter = new Writer<>(memory, descriptor);
        //
        // Record type
        try {
            wrongWriter.putRecord(0, new TestRecordLong());
            fail("Expecting RecordStoreException to be thrown");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Object supplied to writer is of the wrong type");
        }
        //
        // Storage limits
        Writer<TestRecordBytePack> writer = new Writer<>(memory, descriptor);
        TestRecordBytePack testRecordBytePack = new TestRecordBytePack(1, -1, -32768, true, 0x0000_1234_5678_9ABCL, false, UUID.randomUUID(), new boolean[10], booleanArray);
        writer.putRecord(0, testRecordBytePack);
        int maxRecords = 1024 * 4 / descriptor.getByteLength();
        writer.putRecord(maxRecords - 1, testRecordBytePack);
        try {
            writer.putRecord(maxRecords, testRecordBytePack);
            fail("Expecting RecordStoreException to be thrown");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Write location beyond end of storage");
        }
    }

}