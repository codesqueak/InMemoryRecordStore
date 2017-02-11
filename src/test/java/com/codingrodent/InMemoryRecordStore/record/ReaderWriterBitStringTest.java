/*
* MIT License
*
*         Copyright (c) 2017
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
import com.codingrodent.InMemoryRecordStore.record.records.TestRecordBitString;
import org.junit.*;

import static com.codingrodent.InMemoryRecordStore.record.records.TestRecordBitString.*;
import static org.junit.Assert.assertEquals;

public class ReaderWriterBitStringTest {
    private IMemoryStore memory;

    @Before
    public void setUp() throws Exception {
        memory = new ArrayMemoryStore(5000);
    }

    @Test
    public void basicPacking() throws Exception {
        RecordDescriptor<TestRecordBitString> recordDescriptor = new RecordDescriptor<>(TestRecordBitString.class);
        int bits = (3 * 4 * 8) + SIZE_A * 8 + SIZE_B * 7 + SIZE_C * 9;
        assertEquals(recordDescriptor.getByteLength(), ((bits - 1) >> 3) + 1);
        Writer<TestRecordBitString> writer = new Writer<>(memory, recordDescriptor);
        Reader<TestRecordBitString> reader = new Reader<>(memory, recordDescriptor);
        //
        TestRecordBitString write = new TestRecordBitString("A", "BB", "CCC");
        writer.putRecord(0, write);
        byte[] packed = new byte[]{//
                0x00, 0x00, 0x00, 0x01, // header
                0x41, 0x00, 0x00, 0x00, 0x00, // String
                0x00, 0x00, 0x00, 0x02, // header
                //                0x42, 0x42, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // String
                //                0x00, 0x00, 0x00, 0x03, // header
                //                0x00, 0x43, 0x00, 0x43, 0x00, 0x43, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // String
                //                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // String
                //                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // String
                //                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // String
                //                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00// String
        };
        // Did record pack correctly ?
        for (int i = 0; i < packed.length; i++) {
            assertEquals(packed[i], memory.getByte(i));
        }
        //
        // Ok, see if we can get it back
        TestRecordBitString read = reader.getRecord(0);
        assertEquals(read.a, write.a);
        assertEquals(read.b, write.b);
        assertEquals(read.c, write.c);
    }

}